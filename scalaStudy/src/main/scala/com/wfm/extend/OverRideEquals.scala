package com.wfm.extend

/**
 *
 * @ClassName OverRideEquals
 * @Descripyion TODO
 * @author wangfanming
 * @date 2020/2/25 16:42
 * @version 1.0
 */
object OverRideEquals extends App {
  val item1 = new Item("苹果", 2.3)
  val item2 = new Item("苹果", 2.2)
  val item3 = new Item("苹果", 2.3)

  println(item1.equals(item2))
  println(item1.equals(item3))
}
