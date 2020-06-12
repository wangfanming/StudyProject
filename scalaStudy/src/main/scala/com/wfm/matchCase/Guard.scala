package com.wfm.matchCase

/**
 *
 * @ClassName Guard
 * @Descripyion TODO
 * @author wangfanming
 * @date 2020/2/26 21:25
 * @version 1.0
 */
object Guard {
  def main(args: Array[String]): Unit = {
    //todo 模式匹配中守卫的使用
    (1 to 100) map {x => x match {
      case _ if(x %2 == 0) => println(x)
      case 5 => println(s"Point")
      case _ =>
    }}
    // todo 类型匹配
    val list = Array[Any](1, 1.0f, 2.22222222, 'a', "Hello")
    list map { x => x match {
      case s:String => println(s"String :$s")
      case i:Int => println(s"Int: $i")
      case c:Char => println(s"Char: $c")
      case f:Float => println(s"Float: $f")
      case d:Double => println(s"Double: $d")
      case _ =>
    }}

    // todo 模式匹配还可用于 数组、元组、列表
    //todo 模式匹配与正则表达的结合
    val pattern = "([0-9]+) ([a-z]+)".r
    "99 bootles" match {
      case pattern(num,item) => println(s"$num,$item")
      case _ =>
    }
  }
}
