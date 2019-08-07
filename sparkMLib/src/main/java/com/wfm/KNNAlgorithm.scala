package com.wfm

import org.apache.commons.lang.math.NumberUtils
import org.apache.spark.broadcast.Broadcast
import org.apache.spark.sql.{DataFrame, Dataset, Row, SparkSession}
import org.apache.spark.rdd.RDD
import org.apache.spark.mllib.util.{MLUtils}


import scala.collection.mutable
/**
  * @Auther: wangfanming
  * @Date: 2019/7/29 10:11
  * @Description:
  */
object KNNAlgorithm {
  def main(args: Array[String]): Unit = {


    val spark: SparkSession = SparkSession.builder().appName("").master("local[*]").getOrCreate()


    val iris: DataFrame = spark.read
      .option("header", true)
      .option("inferSchema", true)
      .csv("E:\\workspaces\\StudyProject\\sparkMLib\\src\\main\\resources\\input\\iris.csv")

    val Array(testSet, trainSet) = iris.randomSplit(Array(0.3, 0.7), 1234L)

    val knnMode2 = new KNNRunner(spark)
    val res = knnMode2.runKnn(trainSet,testSet,10,"class")


  }
}

class KNNRunner(spark: SparkSession){
  def runKnn(trainSet: DataFrame, testSet: DataFrame, k: Int, cl: String)={

    import spark.implicits._
    val testFetures: RDD[Array[Double]] = testSet.drop(cl).map(row =>{
      val fetuers: Array[Double] = row.mkString(",").split(",").map(_.toDouble)
      fetuers
    }).rdd

    val trainFetures: RDD[(String, Array[Double])] = trainSet.map(row =>{
      val cla: String = row.getAs[String](cl)
      val fetures= row.mkString(",").split(",").filter(NumberUtils.isNumber(_)).map(_.toDouble)
      (cla,fetures)
    }).rdd

//    val trainBroad = spark.sparkContext.broadcast(trainFetures.collect())

    val testNorm: RDD[(Array[Double], Double)] = testFetures.map(testTp =>{
      //定义一个TreeSet之前，先定义一个排序规则
      val orderRules: Ordering[(String, Double)] = Ordering.fromLessThan[(String,Double)](_._2 <= _._2)
      //新建一个空的set 传入排序规则
      val set: mutable.TreeSet[(String, Double)] = mutable.TreeSet.empty(orderRules)

      var sum = 0.0
      for( i <- testTp){
        sum += i*i
      }

      val testTpNorm: Double = math.sqrt(sum)

      printf(testTpNorm+"")
      (testTp,testTpNorm)
    })
    testNorm.foreach(_)

//    testNorm.map(testTP =>{
//
//    })




  }

//  def distance(trainFetures:RDD[(String, Seq[Double])],point : (Array[Double], Double),set:mutable.TreeSet[Double]):Double={
//    trainFetures.values
//  }

}

