package com.dsdata

import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.evaluation.RegressionEvaluator
import org.apache.spark.ml.recommendation.ALS
import org.apache.spark.ml.recommendation.ALS.Rating
import org.apache.spark.sql.{Dataset, SparkSession}
import org.jblas.{DoubleMatrix, FloatMatrix}



/**
  * @Auther: wangfanming
  * @Date: 2019/10/29 15:26
  * @Description:
  */
object SimilarMovies {

  //求余弦相似度
  def consinSim(product1: FloatMatrix, product2:FloatMatrix):Double={
    product1.dot(product2)/(product1.norm2()*product2.norm2())
  }

  def main(args: Array[String]): Unit = {
    val spark: SparkSession = SparkSession.builder()
      .appName("SimilarMovies")
      .master("local[*]")
      .getOrCreate()

    spark.sparkContext.setLogLevel("ERROR")

    val fileDS = spark.read.textFile("D:\\share\\临时\\nf_prize_dataset.tar\\download\\testData")


    import spark.implicits._
    val ratings = fileDS.map(x => {
      val strings = x.split(",")
      Rating(strings(1).toInt, strings(0).toInt, strings(2).toFloat)
    }).cache()

    val Array(training, testing) = ratings.randomSplit(Array(0.8, 0.2), seed = 1234)

    val tuple = ALS.train(training.rdd)

    val itemSimilarMatrix = tuple._2.map{case (movieId,feature) => (movieId,new FloatMatrix(feature))}

    // 计算笛卡尔积并过滤合并
    val movieSimilar = itemSimilarMatrix.cartesian(itemSimilarMatrix)

    val movieSi = movieSimilar.filter(
      //过滤与本身自己的笛卡尔积
      x=>x._1._1 != x._2._1
    ).map(
      t=>{
        val simScore: Double = consinSim(t._1._2,t._2._2)
        (t._1._1,(t._2._1,simScore))

      }
    ).groupByKey().map(x=>{
      (x._1,x._2.toList.sortWith(_._2 > _._2))
    })
    val movieSi1 = movieSi.map{case x=>{
      val movieId = x._1
      val builder = new StringBuilder
      builder.append(movieId).append(":")
      builder.append(x._2.map{case (a,b)=>{a}}.mkString(","))

      builder.toString()
    }}

    val movieSi2 = movieSi.map(x=>{
      val movieId1 = x._1
      movieId1 + ":" +x._2.map(_.toString().replace("(","").replace(")","")).mkString(" ")
    })

    movieSi2.coalesce(1).saveAsTextFile("D:\\share\\临时\\nf_prize_dataset.tar\\download\\train")
//    movieSi1.coalesce(1).saveAsTextFile("D:\\share\\临时\\nf_prize_dataset.tar\\download\\trainSet")

  }


}

