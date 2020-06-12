package com.wfm.future

import java.time.{LocalDateTime, LocalTime}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future}

/**
 *
 * @ClassName Test01
 * @Descripyion TODO
 * @author wangfanming
 * @date 2020/2/27 16:08
 * @version 1.0
 */
object Test01 {
  def main(args: Array[String]): Unit = {
    val f = Future{
      Thread.sleep(10000)
      println(s"This is the future at ${LocalTime.now}")
      42
    }
    println(s"This is the present at ${LocalTime.now}")

    import scala.concurrent.duration._

//    Await.result(f,10.seconds)
    Await.ready(f,10.seconds)  //todo 线程在此阻塞，直到 f 执行完成
    println(f.value)




  }
}
