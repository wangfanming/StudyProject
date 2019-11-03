package com.wfm

import org.apache.flink.api.java.utils.ParameterTool
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.windowing.time.Time

/**
  * @ClassName: AddTest
  * @Auther: wangfanming
  * @Date: 2019/9/10
  * @Description: TODO
  * @Version 1.0
  */
object AddTest {
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

    val windowSum = text.filter(!_.isEmpty).map(_.toInt).timeWindowAll(Time.seconds(5),Time.seconds(1)).sum(0)
    // print the results with a single thread, rather than in parallel
    windowSum.print().setParallelism(1)

    env.execute("Socket Window windowSum")


  }
}
