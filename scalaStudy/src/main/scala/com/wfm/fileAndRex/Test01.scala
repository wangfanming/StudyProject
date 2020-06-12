package com.wfm.fileAndRex

import scala.io.Source

/**
 *
 * @ClassName Test01
 * @Descripyion TODO
 * @author wangfanming
 * @date 2020/2/25 17:29
 * @version 1.0
 */
object Test01 {
  def main(args: Array[String]): Unit = {
    val source = Source.fromFile("D:\\Study\\workspace\\StudyProject\\scalaStudy\\src\\main\\resources\\words")
//    val iterator = source.getLines()
//    for(i <- iterator){   // 迭代读取每行数据
//      println(i)
//    }
    val iter = source.buffered   //得到一个 字符数组
    while (iter.hasNext){
      if(iter.head == 'h'){
        println("Successful")
        iter.next()
      }else{
        println(iter.next())
      }
    }

    source.close()
  }
}
