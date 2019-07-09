package com.wfm.traits

/**
  * @Auther: wangfanming
  * @Date: 2019/7/8 09:53
  * @Description:
  */
//带有具体实现的trait
trait logTrait {
  def log(message:String){
    println("Message ==>  " + message)
  }
  def log2(message:String)
}

trait ConsoleTrait extends logTrait{
  override def log2(message: String): Unit = {
    println("ConsoleTrait Message ==>  " + message)
  }
}

class ConsoleTraitTest extends ConsoleTrait{

}

//当作接口的trait
trait Logger{
  def log(message:String)
}

class ConsoleLogger extends Logger {
  override def log(message: String): Unit = {
    println("ConsoleLogger printlog:" + message)
  }
}

abstract class AbstractTest{
  def save()
}

class MyTest extends AbstractTest with Logger {
  override def save(): Unit = {
    println("Saved Successfully!")
  }

  override def log(message: String): Unit = {
    println("MyTest " + message)
  }
}
