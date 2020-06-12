package com.wfm.fileAndRex

import java.io.{File, FileInputStream, PrintWriter}

import scala.io.Source

/**
 *
 * @ClassName Test02
 * @Descripyion TODO
 * @author wangfanming
 * @date 2020/2/25 17:39
 * @version 1.0
 */
object Test02 {
  def main(args: Array[String]): Unit = {
    val source01 = Source.fromURL("www.baidu.com", "UTF-8")
    Source.fromString("Hello Word")
    Source.stdin  // 从标准输入读取

    /**
     * Scala并没有直接读取二进制文件的方法，需要依赖Java类库
     */
    val file = new File("D:\\Study\\workspace\\StudyProject\\scalaStudy\\src\\main\\resources\\words")
    val inputStream = new FileInputStream(file)
    val bytes = new Array[Byte](file.length.toInt)
    inputStream.read(bytes)
    inputStream.close()

    /**
     * Scala 写入文件
     */
    val writer = new PrintWriter("D:\\Study\\workspace\\StudyProject\\scalaStudy\\src\\main\\resources\\out")
    for (i <- 1 to 100) writer.println(i)
    writer.close()
  }



}
