package com.wfm.collection

/**
 *
 * @ClassName WordFrequency
 * @Descripyion TODO
 * @author wangfanming
 * @date 2020/2/26 20:21
 * @version 1.0
 */
object WordFrequency {
  def main(args: Array[String]): Unit = {
    val freq = scala.collection.mutable.Map[Char, Int]()
    for(c <- "Mississippi") freq(c) = freq.getOrElse(c,0) + 1
    println(s"freq1:$freq")

    val a = (Map[Char,Int]() /: "Mississippi"){
      (m,c) => m + (c -> (m.getOrElse(c,0) +1))
    }
    println(a)

  }
}
