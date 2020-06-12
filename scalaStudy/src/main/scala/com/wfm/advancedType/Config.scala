package com.wfm.advancedType

/**
 *
 * @ClassName Config
 * @Descripyion TODO
 * @author wangfanming
 * @date 2020/2/28 16:27
 * @version 1.0
 */
trait Config {
  load
  val text:String
  def load:Unit
}
trait InMemoryConfig extends Config{
  lazy val text = "Hello"
  override def load: Unit = println("load: " + text)
}
trait Context
trait MyContext extends Context{
  this:Config =>
  def welcome = this.text
}
object Env extends MyContext with InMemoryConfig{
  def main(args: Array[String]): Unit = {
    println(Env.text)
  }
}
