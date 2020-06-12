package com.wfm

import org.apache.spark.sql.SparkSession

/**
 *
 * @ClassName FlodTest
 * @Descripyion TODO  按照RDD分区顺序，每个分区取zeroValue做为初始值，除了第一个分区会计算2次zeroValue,其余分区各自累加一次zeroValue,最后计算结果= 2 * zeroValue + (numPartitions -1) * zeroValue + 原始序列求和 = (numPartitions + 1) * zeroValue + 原始序列求和
 * @author wangfanming
 * @date 2020/3/23 0:09
 * @version 1.0
 */
object FoldTest {
  def main(args: Array[String]): Unit = {
    val sparkSession = SparkSession.builder().appName("FlodTest").master("local[1]").getOrCreate()
    val sparkContext = sparkSession.sparkContext
    val rdd = sparkContext.parallelize(List(1, 2, 3, 4, 5)).repartition(2)
    rdd.foreach(println(_))

    val fold = rdd.fold(2)((x, y) => {
      println(s"x= $x,y= $y")
      x + y
    })
    println(fold)

    sparkSession.close()
  }
}
