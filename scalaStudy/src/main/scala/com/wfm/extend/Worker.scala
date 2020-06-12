package com.wfm.extend

/**
 *
 * @ClassName Worker
 * @Descripyion TODO
 * @author wangfanming
 * @date 2020/2/25 14:53
 * @version 1.0
 */
class Worker(name:String,age:Int,val salary:Double) extends Person1(name,age) {
  println("This is SubClass of Person1")
  override val school: String = "NYIST"

  override def toString: String = "I'm a Worker " + super.sleep
}
