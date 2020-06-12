package com.wfm

import org.apache.spark.SparkContext
import org.apache.spark.sql.SparkSession

/**
 *
 * @ClassName CollectAsMap
 * @Descripyion TODO
 * @author wangfanming
 * @date 2020/3/23 2:03
 * @version 1.0
 */
object CollectAsMap {
  def main(args: Array[String]): Unit = {
    val session: SparkSession = SparkSession.builder().appName("CollectAsMap").master("local").getOrCreate()
    val sc: SparkContext = session.sparkContext

    val a = sc.parallelize(List(1, 2, 3, 1, 2), 1)
    val b = sc.parallelize(List("a", "b", "c", "b", "d"), 1)
    val c = a.zip(b)
    c.foreach(println)
    println()
    c.collectAsMap().foreach(println)
  }

}
