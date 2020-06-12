package com.wfm.func

/**
 *
 * @ClassName Closure
 * @Descripyion TODO
 * @author wangfanming
 * @date 2020/2/26 16:27
 * @version 1.0
 */
object Closure {
  /**
   * @Author wangfanming
   * @Description TODO
   * @Date 16:30 2020/2/26
   * @param
   * @return scala.Function1<java.lang.Object,java.lang.Object>
   */
  def mulBy(factor:Double)= (x:Double) => factor * x
  def main(args: Array[String]): Unit = {
    val triple = mulBy(3)
    val half = mulBy(0.5)

    println(s"${triple(14)}  ${half(14)}")
  }
}
