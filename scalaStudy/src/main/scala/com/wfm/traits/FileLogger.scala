package com.wfm.traits

import java.io.PrintWriter

/**
 *
 * @ClassName FileLogger
 * @Descripyion TODO
 * @author wangfanming
 * @date 2020/2/25 23:56
 * @version 1.0
 */
trait FileLogger extends Logger {
  val fileName:String
  val out = new PrintWriter(fileName)

  override def log(message: String): Unit ={
    out.println(message)
    out.flush()
  }
}
class B
object Test{
  def main(args: Array[String]): Unit = {
    /**
     * todo 以下这种使用方法会得到一个NullPointerException。原因在于 FileLogger的构造器先于子类B的构造器执行
     */
//    val b = new B with FileLogger {
//      override val fileName: String = "D:\\Study\\workspace\\StudyProject\\scalaStudy\\src\\main\\resources\\words"
//    }

    /**
     * todo 如下解决方案1： 在此使用 提前定义块，强制子类的字段先于父类初始化   在类中使用，则提前定义块写在extends关键词后边
     * todo 解决方案2： 在FileLogger中使用lazy修饰 fileName
     */
    val b = new {val fileName: String = "D:\\Study\\workspace\\StudyProject\\scalaStudy\\src\\main\\resources\\words"}
      with B with FileLogger
    b.log("abc")



  }
}