package com.wfm.extend

/**
 *
 * @ClassName Teacher
 * @Descripyion TODO
 * @author wangfanming
 * @date 2020/2/25 17:09
 * @version 1.0
 */
class Teacher {  //这里省略了（） 主构造器。  构造器和类定义交织在一起
  var name:String = _  //私有字段， 生成公有getter和setter方法
  private var  age = 27  //私有字段  生成私有的getter和setter方法
  private[this] var gender = "male"  //对象私有字段，不生成getter和setter方法

  def this(name:String){  //重载构造器    任何一个重载构造器都必须调用其他的重载构造器或主构造器
    this
    this.name = name
  }

  def sayHi = println(s"${this.name},${this.age},${this.gender}")

}
object TeacherTest extends App {
  val lili = new Teacher("lili")
  lili.sayHi
  lili.name = "Duncan"
  lili.sayHi
}
