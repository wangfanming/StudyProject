package com.wfm.extend

/**
 *
 * @ClassName AnonymousSubTest
 * @Descripyion TODO
 * @author wangfanming
 * @date 2020/2/25 15:26
 * @version 1.0
 */
object AnonymousSubTest {
  def main(args: Array[String]): Unit = {
    val alien = new Person1("Fred",age = 23){  //定义一个匿名子类
      def greeting = "Greetings,Earthing! My name is Fred"
    }
    meet(alien)
  }

  def meet(p:Person1{def greeting:String})= println(s"${p.name} says: ${p.greeting}")

}
