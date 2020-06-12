package com.wfm

import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
 *
 * @ClassName WordCount
 * @Descripyion TODO
 * @author wangfanming
 * @date 2020/3/23 17:50
 * @version 1.0
 */
object WordCount {
  def main(args: Array[String]): Unit = {
    val sparkSession: SparkSession = SparkSession.builder().appName("WordCount").master("spark://127.0.0.1:7077").getOrCreate()

    val streamingContext = new StreamingContext(sparkSession.sparkContext, Seconds(5))

    val line: ReceiverInputDStream[String] = streamingContext.socketTextStream("192.168.0.100", 9999)
    val words: DStream[String] = line.flatMap(_.split(" "))
    val pairs: DStream[(String, Int)] = words.map((_,1))

    val wordCounts: DStream[(String, Int)] = pairs.reduceByKey(_ + _)

    wordCounts.print()
    streamingContext.start()
    streamingContext.awaitTermination()
  }

}
