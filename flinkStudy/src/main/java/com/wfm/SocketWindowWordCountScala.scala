package com.wfm

import java.util.concurrent.TimeUnit._

import org.apache.flink.api.java.utils.ParameterTool
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.windowing.time.Time


/**
  * @ClassName: SocketWindowWordCountScala
  * @Auther: wangfanming
  * @Date: 2019/9/9
  * @Description: TODO
  * @Version 1.0
  */
object SocketWindowWordCountScala {

  case class WordWithCount(word:String,count:Int)

  def main(args: Array[String]): Unit = {
    // the port to connect to
    val port: Int = try {
      ParameterTool.fromArgs(args).getInt("port")
    } catch {
      case e: Exception => {
        System.err.println("No port specified. Please run 'SocketWindowWordCount --port <port>'")

        return
      }
    }

    // get the execution environment
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

    // get input data by connecting to the socket
    val text = env.socketTextStream("cluster", 9001, '\n')

    import org.apache.flink.api.scala._

    // parse the data, group it, window it, and aggregate the counts
    val windowCounts = text
      .flatMap { w => w.split("\\s") }
      .map { w => WordWithCount(w, 1) }
      .keyBy("word")
      .timeWindow(Time.seconds(5), Time.seconds(1))
      .sum("count")

    // print the results with a single thread, rather than in parallel
    windowCounts.print().setParallelism(1)

    env.execute("Socket Window WordCount")

  }
}
