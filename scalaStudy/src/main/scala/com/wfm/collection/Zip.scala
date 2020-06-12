package com.wfm.collection

/**
 *
 * @ClassName Zip
 * @Descripyion TODO
 * @author wangfanming
 * @date 2020/2/26 20:37
 * @version 1.0
 */
object Zip {
  def main(args: Array[String]): Unit = {
    val price = List(5.0, 2.0, 6.0)
    val quantiyies = List(10, 2, 1)
    val tuples = price zip quantiyies
    println(tuples)

    //todo zip 一对一拉链操作
    (price zip quantiyies) map {p => p._1 * p._2} foreach(println(_))

    // todo zipAll 指定较短列表默认值的拉链操作
    val num = List(1,20)
    (price.zipAll(num,0.0,1)) map {p => p._1 * p._2} foreach(print(_))

  }
}
