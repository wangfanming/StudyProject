package com.dsdata

import scala.collection.mutable
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.streaming._
import org.apache.kafka.common.serialization.StringDeserializer
import com.mongodb.casbah.Imports.{$set, MongoClient, MongoCollection, MongoDBObject}
import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.kafka010.KafkaUtils
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent

/**
  * @Auther: wangfanming
  * @Date: 2019/10/25 14:37
  * @Description:
  */
object streamingRecommender {
  private val msgConsumerGroup = "netflix-recommending-system-topic-message-consumer-group"
  private val minSimilarity = 0.7

  def getUserRecentRatings(collection: MongoCollection, K: Int, userId: Int, movieId: Int, rate: Double, timestamp: Long): Array[(Int, Double)] = {
    //function feature: 通过MONGODB中评分的时间戳大小来获取userId的最近K-1次评分，与本次评分组成最近K次评分
    //return type：最近K次评分的数组，每一项是<movieId, rate>
    val query = MongoDBObject("userId" -> userId)
    val orderBy = MongoDBObject("timestamp" -> -1)
    val recentRating = collection.find(query).sort(orderBy).limit(K - 1).toArray.map{ item =>
      (item.get("movieId").toString.toInt, item.get("rate").toString.toDouble)
    }.toBuffer
    recentRating += ((movieId, rate))
    //将本次评分写回到MONGODB，为了测试方便暂时不需要，上线再取消注释
    //collection.insert(MongoDBObject("userId" -> userId, "movieId" -> movieId, "rate" -> rate, "timestamp" -> timestamp))
    recentRating.toArray
  }

  def getSimilarMovies(mostSimilarMovies: collection.Map[Int, Array[Int]], collectionForRatingRecords: MongoCollection, movieId: Int, userId: Int, K: Int): Array[Int] = {
    //function feature: 从广播变量中获取movieId最相似的K个电影，并通过MONGODB来过滤掉已被评分的电影
    //return type：与movieId最相似的电影们，每一项是<other_movieId>
    import com.mongodb.casbah.Imports._
    val similarMoviesBeforeFilter = mostSimilarMovies.getOrElse(movieId, Array[Int]())
    val query = MongoDBObject("userId" -> movieId)
    val condition = "movieId" $in similarMoviesBeforeFilter
    val hasRated = collectionForRatingRecords.find(query ++ condition).toArray.map(_.get("movieId").toString.toInt).toSet
    similarMoviesBeforeFilter.filter(hasRated.contains(_) == false)
  }

  def getSimilarityBetween2Movies(simHash: collection.Map[Int, collection.Map[Int, Double]], movieId1: Int, movieId2: Int): Double = {
    //function feature: 从广播变量中获取movieId1与movieId2的相似度，不存在、或movieId1=movieId2视为毫无相似，相似度为0
    //return type：movieId1与movieId2的相似度
    val (smallerId, biggerId) = if (movieId1 < movieId2) (movieId1, movieId2) else (movieId2, movieId1)
    if (smallerId == biggerId) {
      return 0.0
    }
    simHash.get(smallerId) match {
      case Some(subSimHash) =>
        subSimHash.get(biggerId) match {
          case Some(sim) => sim
          case None => 0.0
        }
      case None => 0.0
    }
  }

  def log(m: Double): Double = math.log(m) / math.log(2)

  def createUpdatedRatings(simiHash: collection.Map[Int, collection.Map[Int, Double]], recentRatings: Array[(Int, Double)], candidateMovies: Array[Int]): Array[(Int, Double)] = {
    //function feature: 核心算法，计算每个备选电影的预期评分
    //return type：备选电影预计评分的数组，每一项是<movieId, maybe_rate>
    val allSimilars = mutable.ArrayBuffer[(Int, Double)]()

    val increaseCounter = mutable.Map[Int, Int]()
    val reduceCounter = mutable.Map[Int, Int]()

    for (cmovieId <- candidateMovies; (rmovieId, rate) <- recentRatings) {
      val sim = getSimilarityBetween2Movies(simiHash, rmovieId, cmovieId)
      if (sim > minSimilarity) {
        allSimilars += ((cmovieId, sim * rate))
        if (rate >= 3.0) {
          increaseCounter(cmovieId) = increaseCounter.getOrElse(cmovieId, 0) + 1
        } else {
          reduceCounter(cmovieId) = reduceCounter.getOrElse(cmovieId, 0) + 1
        }
      }
    }
    allSimilars.toArray.groupBy{case (movieId, value) => movieId}
      .map{ case (movieId, simArray) =>
        (movieId, simArray.map(_._2).sum / simArray.length + log(increaseCounter.getOrElse[Int](movieId, 1)) - log(reduceCounter.getOrElse[Int](movieId, 1)))
      }.toArray
  }

  def updateRecommends2MongoDB(collection: MongoCollection, newRecommends: Array[(Int, Double)], userId: Int, startTimeMillis: Long): Boolean = {
    //function feature: 将备选电影的预期评分回写到MONGODB中
    val endTimeMillis = System.currentTimeMillis()
    /*
    val query = MongoDBObject("userId" -> userId)
    val setter = $set("recommending" -> newRecommends.map(item => item._1.toString + "," + item._2.toString).mkString("|"),
    "timedelay" -> (System.currentTimeMillis() - startTimeMillis).toDouble / 1000)
    collection.update(query, setter, upsert = true, multi = false)
    */
    val toInsert = MongoDBObject("userId" -> userId,
      "recommending" -> newRecommends.map(item => item._1.toString + "," + item._2.toString).mkString("|"),
      "timedelay" -> (endTimeMillis - startTimeMillis).toDouble / 1000)
    collection.insert(toInsert)
    true
  }

  def main(args: Array[String]) {
    if (args.length != 1) {
      println("USAGE: netflix-recommending-system.jar BatchDuration")
      System.exit(1)
    }
    val hdfsDir = "hdfs://10.10.3.210:9000/user/root/netflix/"
    val sparkSession = SparkSession.builder()
      .appName("streamingRecommender")
      .config("spark.io.compression.codec","snappy")
      .master("local[*]").getOrCreate()
    val sc = sparkSession.sparkContext


    val ssc = new StreamingContext(sc, Seconds(5)) //for spark streaming

    //每个电影的最相似的K个电影，HASH[电影Id, 相似的K个电影Ids]
    val topKMostSimilarMovies = sc.textFile(hdfsDir + "simTopK.txt")
      .map{line =>
        val dataArr = line.trim.split(":")
        val movieId = dataArr(0).toInt
        val topK = dataArr(1).split(",").map(_.toInt)
        (movieId, topK)
      }.collectAsMap

    //每个电影与其他电影的相似度，HASH[电影Id, HASH[电影Id2, Id1与Id2相似度]]
    val movie2movieSimilarity = sc.textFile(hdfsDir + "simSimi.txt")
      .map{line =>
        val dataArr = line.trim.split(":")
        val movieId1 = dataArr(0).toInt
        val similarities = dataArr(1).split(" ").map{str => {
          val similarityArray = str.split(",")
          val movieId2 = similarityArray(0).toInt
          val sim = similarityArray(1).toDouble
          (movieId2, sim)
        }}
        (movieId1, similarities.toMap)
      }.collectAsMap



    //最相似电影HASH的广播
    val bTopKMostSimilarMovies = ssc.sparkContext.broadcast(topKMostSimilarMovies)
    //电影间相似度HASH的广播
    val bMovie2movieSimilarity = ssc.sparkContext.broadcast(movie2movieSimilarity)

    //为了在核心任务执行前将广播变量提前pull到各个worker，所以这里做了一堆故意引用了广播的任务
    val firstRDD = ssc.sparkContext.parallelize(1 to 10000, 1000)
    val useless = firstRDD.map{ i =>
      val pullTopK = bTopKMostSimilarMovies.value.contains(i)
      val pullSim = bMovie2movieSimilarity.value.contains(i)
      (i, pullTopK, pullSim)
    }.count()
//    println(useless)

    val zkServers = "10.10.3.211:2181,10.10.3.212:2181,10.10.3.213:2181"
    val K: Int = 5

    //MONGODB连接者，逃避序列化问题，且一个JVM只有一个连接者，提高性能
    object SingleMongoDB extends Serializable {
      lazy val mongoClient = MongoClient("10.10.3.210", 27017)
      def getCollection(collectionName: String): MongoCollection = {
        mongoClient("RecommendingSystem")(collectionName)
      }
    }

    val kafkaParams = Map[String, Object](
      "bootstrap.servers" -> "10.10.3.211:9092,10.10.3.211:9093,10.10.3.211:9094",
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[StringDeserializer],
      "group.id" -> "use_a_separate_group_id_for_each_stream",
      "auto.offset.reset" -> "latest",
      "enable.auto.commit" -> (false: java.lang.Boolean)
    )



    val topics = Array("test")
    val stream = KafkaUtils.createDirectStream[String, String](
      ssc,
      PreferConsistent,
      Subscribe[String, String](topics, kafkaParams)
    )


    val dataDStream = stream.map(x=>{
      x.value()
    }).map{ case msgLine =>
      val dataArr: Array[String] = msgLine.split("\\|")
      val userId = dataArr(0).toInt
      val movieId = dataArr(1).toInt
      val rate = dataArr(2).toDouble
      val startTimeMillis = dataArr(3).toLong
      (userId, movieId, rate, startTimeMillis)
    }.cache

    dataDStream.foreachRDD(rdd => {
      if (!rdd.isEmpty) {
        rdd.map{ case (userId, movieId, rate, startTimeMillis) =>
          //获取近期评分记录
          val recentRatings = getUserRecentRatings(SingleMongoDB.getCollection("ratingRecords"), K, userId, movieId, rate, startTimeMillis)
          //获取备选电影
          val candidateMovies = getSimilarMovies(bTopKMostSimilarMovies.value, SingleMongoDB.getCollection("ratingRecords"), movieId, userId, K)
          //为备选电影推测评分结果
          val updatedRecommends = createUpdatedRatings(bMovie2movieSimilarity.value, recentRatings, candidateMovies)
          //结果回写到MONGODB，注意！！！！！！其实应该返回给客户，与客户当前推荐进行Merge
          updateRecommends2MongoDB(SingleMongoDB.getCollection("recentRecommending"), updatedRecommends, userId, startTimeMillis)
        }.count()
      }
    })

    val updateFunc = (values: Seq[Int], state: Option[Int]) => {
      val currentCount = values.sum
      val previousCount = state.getOrElse(0)
      Some(currentCount + previousCount)
    }

    val movieIdCount = dataDStream.map{case (userId, movieId, rate, startTimeMillis) => (movieId, 1)}


    val stateDStream = movieIdCount.updateStateByKey[Int](updateFunc)
    //选出TOP5的电影
    stateDStream
      .foreachRDD{rdd =>
      val hotMovies = rdd.top(5)(Ordering.by[(Int, Int), Int](_._2))
      for ((movieId, counter) <- hotMovies) {
        println(movieId + ":" + counter)
      }
    }

    ssc.checkpoint(hdfsDir + "checkpoint_dir")
    ssc.start()
    ssc.awaitTermination()
  }
}
