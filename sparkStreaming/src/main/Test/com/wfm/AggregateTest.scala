package com.wfm

import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession

import scala.collection.immutable

/**
 *
 * @ClassName AggregateTest
 * @Descripyion TODO
 * @author wangfanming
 * @date 2020/3/23 1:44
 * @version 1.0
 */
object AggregateTest {
  def main(args: Array[String]): Unit = {
    val session: SparkSession = SparkSession.builder().appName("AggregateTest").master("local").getOrCreate()
    val sparkContext: SparkContext = session.sparkContext
    val list: immutable.Seq[Int] = List(1, 2, 3, 4)
    val rdd: RDD[Int] = sparkContext.parallelize(list)
    //计算(序列求和，序列元素个数)
    val tuple: (Int, Int) = rdd.aggregate(0, 0)((x, y) => (x._1 + y, x._2 + 1), (x, y) => (x._1 + y._1, x._2 + y._2))
    println(tuple)

    session.close()
  }
}
