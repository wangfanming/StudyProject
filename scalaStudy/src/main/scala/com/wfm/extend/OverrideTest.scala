package com.wfm.extend

/**
 *
 * @ClassName OverrideTest
 * @Descripyion TODO
 * @author wangfanming
 * @date 2020/2/25 14:56
 * @version 1.0
 */
object OverrideTest extends App {  //扩展APP特质，就可以省去书写main
  println("Hello APP")
  val worker = new Worker("Duncan", 26, 15000)
  println(worker.toString)
}
