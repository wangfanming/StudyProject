package com.wfm.collection

import scala.collection.mutable.ArrayBuffer

/**
 *
 * @ClassName View
 * @Descripyion TODO
 * @author wangfanming
 * @date 2020/2/26 21:06
 * @version 1.0
 */
object View {
  def main(args: Array[String]): Unit = {
    // todo view也是懒计算的，但是view不会缓存值 force和apply都会强制其进行计算
    (1 to 10000000).view.map(x => x * x).filter(x => x.toString == x.toString.reverse)

    // todo 在可变集合中，任何对视图的修改都将影响原集合
    val ints = ArrayBuffer[Int]()
    ints.view(10,20).transform(x => 0)

  }
}
