package com.wfm.extend

/**
 *
 * @ClassName Person
 * @Descripyion TODO
 * @author wangfanming
 * @date 2020/2/25 14:41
 * @version 1.0
 */
class Person1(val name:String,var age:Int) {
  println("This primary constructor of Person1")
  val school = "KMUST"
  def sleep = "2 hours"
  override def toString: String = s"${getClass.getName}[name=$name]"
}
