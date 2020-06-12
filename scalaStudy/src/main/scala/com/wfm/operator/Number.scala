package com.wfm.operator

/**
 *
 * @ClassName Number
 * @Descripyion TODO
 * @author wangfanming
 * @date 2020/2/26 10:06
 * @version 1.0
 */
object Number {
  def unapply(arg: String): Option[Int] = {
    try{
      Some(arg.trim.toInt)
    }catch {
      case ex:Exception => None
    }
  }

  def main(args: Array[String]): Unit = {
    val Number(n) = "154"
    println(n)
  }
}
