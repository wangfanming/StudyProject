package com.wfm.advancedType

import scala.collection.mutable

/**
 *
 * @ClassName Document
 * @Descripyion TODO 在使用this类型将方法串起来的时候，有继承关系，则会导致this混乱，可以在父类中使用this.type强制指定返回类型
 * @author wangfanming
 * @date 2020/2/28 11:00
 * @version 1.0
 */
class Document {
  var title:String = _
  var author:String = _
  def setTitle(title:String):this.type ={this.title = title; this}
  def setAuthor(author:String):this.type = {this.author = author; this}
}
class Book extends Document{
  var chapter:String = _
  def setChapter(chapter:String)={this.chapter = chapter; this}  //
}

//todo 实现 “流利接口” book set Title to "Scala for the Impatient"
object Title
class Document1{
  var title:String = _
  private var useNextArgsAs:Any = null
  def set(obj:Title.type):this.type  = {
    useNextArgsAs = obj;
    this
  }

  def to(arg:String) = {
    if (useNextArgsAs == Title){
      this.title = arg;
    }else{}
  }
}

class Book1 extends Document1

object Document{
  def main(args: Array[String]): Unit = {
    val book = new Book
    book.setTitle("a").setAuthor("D").setChapter("")

    //todo 实现了流利接口
    val book1 = new Book1
    book1 set Title to ""

    //todo 类型别名
    type index = mutable.HashMap[String,Int]

  }
}