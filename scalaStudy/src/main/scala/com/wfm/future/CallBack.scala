package com.wfm.future

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Random, Success}

/**
 *
 * @ClassName CallBack
 * @Descripyion TODO
 * @author wangfanming
 * @date 2020/2/27 16:23
 * @version 1.0
 */
object CallBack{
  def main(args: Array[String]): Unit = {
    val f1 = Future{
      Thread.sleep(2000)
      if (Random.nextInt() < 0.5) throw new Exception
      42
    }

    f1.onComplete {
      case Success(v) => println(s"$v")
      case Failure(exception) => println(s"f1:${exception.getMessage}")
    }

    // todo 组合Futrue
    val future1 = Future{getData1()}
    val future2 = Future{getData2()}
//
//    future1 onComplete{
//      case Success(v1) =>
//        future2 onComplete{
//          case Success(v2) => {
//            val sum = v1 + v2
//            println(sum)
//          }
//          case Failure(ex) => {
//            println(s"${ex.getMessage}")
//          }
//        }
//      case Failure(ex)=>{
//        println(s"${ex.getMessage}")
//      }
//    }

    //todo 使用map\flatMap来组合
    val combined = future1.map(n1 => future2.map(n2 => n1 + n2))
    println(combined)

    //todo 使用for表达式来组合
    val a = for(n1 <- future1;n2 <- future2 if n1 != n2) yield n1 + n2
    println(s"a:$a")

//    //todo zip方式获取计算结果
//    val b = future1.zip(future2)

    Thread.sleep(10000)
  }

  def getData1() = {
    Thread.sleep(2000)
    10
  }
  def getData2() = {
    Thread.sleep(2000)
    20
  }
}
