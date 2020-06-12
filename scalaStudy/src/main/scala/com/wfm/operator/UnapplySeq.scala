package com.wfm.operator

/**
 *
 * @ClassName UnapplySeq
 * @Descripyion TODO
 * @author wangfanming
 * @date 2020/2/26 10:16
 * @version 1.0
 */
object Animal{
  /**
   * @Author wangfanming
   * @Description TODO 提取任意长度值的序列
   * @Date 10:22 2020/2/26
   * @param
   * @return scala.Option<java.lang.String[]>
   */
  def unapplySeq(input:String) ={
    if (input.trim == "") None else Some(input.trim.split("\\s+"))
  }
}


object UnapplySeq {
  def main(args: Array[String]): Unit = {
    "Taylor Swift" match {
      case Animal(first,last) => println(s"first:$first,last:$last")
      case Animal(first,second,last) => println(s"first:$first,second:$second,last:$last")
      case _ => println("No Matched!")
    }
  }
}
