package com.wfm.operator

/**
 *
 * @ClassName UnapplyTest
 * @Descripyion TODO
 * @author wangfanming
 * @date 2020/2/26 9:38
 * @version 1.0
 */
class Name(val first:String,val last:String){}
object Name{
  def apply(author:String) = {
    val strings = author.split(" ")
    new Name(strings(0),strings(1))
  }

  def unapply(input:Name) ={
    if (input == null) None else Some((input.first,input.last))
  }
}
case class Currency(v1:Double,v2:String)

object UnapplyTest {
  def main(args: Array[String]) = {
    val author = Name("Divad HK")
    author match {
      case Name(first,last) => println(s"first:$first,last:$last" )
      case _ => println(" Error")
    }

    val currency = Currency(12.2, "Duncan")
    currency match {
      case Currency(amount,"Duncan") => println(s"amount:$amount")  //todo 可以在case匹配过程中，自动比较是否等于预期值“Duncan”
      case _ => println("Error")
    }
  }

}
