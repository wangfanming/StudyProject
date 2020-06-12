package com.wfm.collection

/**
 *
 * @ClassName StreamTest
 * @Descripyion TODO
 * @author wangfanming
 * @date 2020/2/26 20:57
 * @version 1.0
 */
object StreamTest {
  def main(args: Array[String]): Unit = {
    // todo 流是一个尾部被懒计算的不可变列表 计算结果会被缓存
    val tenOrMore = numsFrom(10)
    println(tenOrMore.tail)
    // todo force强制计算
    println(tenOrMore.take(5).force)
  }

  def numsFrom(n:BigInt):Stream[BigInt] = n #:: numsFrom(n + 1)

}
