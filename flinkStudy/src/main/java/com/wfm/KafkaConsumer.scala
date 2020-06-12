package com.wfm

import java.util.Properties

import org.apache.flink.streaming.api.scala._
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.api.{CheckpointingMode, TimeCharacteristic}
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.connectors.kafka.{FlinkKafkaConsumer010, FlinkKafkaProducer010}

object KafkaConsumer {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
    env.enableCheckpointing(1000)
    env.getCheckpointConfig.setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE)

    val properties = new Properties()
    properties.setProperty("bootstrap.servers","192.168.10.128:9092,192.168.10.128:9093")
    properties.setProperty("zookeeper.connect","192.168.10.128:2181")
    properties.setProperty("group.id","test")

    val myConsumer = new FlinkKafkaConsumer010[String]("test1", new SimpleStringSchema(), properties)
    myConsumer.setStartFromEarliest()
    val transaction = env.addSource(myConsumer)
    transaction.print()
    val acceptAction = transaction.map(x => {
      val i = Integer.parseInt(x)
      i+1 + ""
    })

    val myProducer = new FlinkKafkaProducer010[String]("test1", new SimpleStringSchema(), properties)
    acceptAction.addSink(myProducer)
    // execute program
    env.execute("Flink Streaming Scala API Skeleton")
  }
}
