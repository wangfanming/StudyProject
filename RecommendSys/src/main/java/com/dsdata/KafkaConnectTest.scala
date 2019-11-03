package com.dsdata

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.kafka010._
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe

import scala.collection.mutable

/**
  * @Auther: wangfanming
  * @Date: 2019/10/30 16:02
  * @Description:
  */
object KafkaConnectTest {
  def main(args: Array[String]): Unit = {

    val kafkaParams = Map[String, Object](
      "bootstrap.servers" -> "10.10.3.211:9092,10.10.3.211:9093,10.10.3.211:9094",
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[StringDeserializer],
      "group.id" -> "use_a_separate_group_id_for_each_stream",
      "auto.offset.reset" -> "latest",
      "enable.auto.commit" -> (false: java.lang.Boolean)
    )

    val sparkSession = SparkSession.builder().appName("KafkaConnectTest").master("local[*]").getOrCreate()
    val sc = sparkSession.sparkContext

    val streamingContext = new StreamingContext(sc, Seconds(5)) //for spark streaming
    val topics = Array("test")
    val stream = KafkaUtils.createDirectStream[String, String](
      streamingContext,
      PreferConsistent,
      Subscribe[String, String](topics, kafkaParams)
    )

    stream.map(record => (record.key, record.value)).foreachRDD(x=>{x.foreach(print(_))})

    streamingContext.start()
    streamingContext.awaitTermination()



  }
}
