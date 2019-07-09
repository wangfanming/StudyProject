package com.wfm.traits

/**
  * @Auther: wangfanming
  * @Date: 2019/7/8 09:52
  * @Description:
  */
object TraitTest {
  def main(args: Array[String]): Unit = {
    val consoleTraitTest = new ConsoleTraitTest
    consoleTraitTest.log2("consoleTraitTest")

    val consoleLogger = new ConsoleLogger()
    consoleLogger.log("ConsoleLogger")

    val myTest = new MyTest()
    myTest.log("MyTest")

    myTest.save()

  }
}
