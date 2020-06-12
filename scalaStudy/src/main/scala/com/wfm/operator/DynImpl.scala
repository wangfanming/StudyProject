package com.wfm.operator

import scala.language.dynamics
/**
 *
 * @ClassName DynImpl
 * @Descripyion TODO
 * @author wangfanming
 * @date 2020/2/26 12:11
 * @version 1.0
 */
class DynImpl extends Dynamic {
  var map = Map.empty[String, Any]
  def selectDynamic(name:String) = map.get(name).getOrElse(sys.error("method not found !"))
  def updateDynamic(name:String)(value: Any) = map += name-> value
  def applyDynamic(name:String)(args:Any*)={
    s"method '$name' called with arguments ${args.mkString("'","', '","'")}"
  }
//  def ints(value:Any*):Unit ={
//    value.map(println(_))
//  }
}

object Test{
  def main(args: Array[String]): Unit = {
    val dynImpl = new DynImpl
    dynImpl.ints(1,2,3)  // todo 实际是执行了 dynImpl.applyDynamic("inits")(1,2,3)
//    dynImpl.foo = 12
//    println(dynImpl.foo)
  }
}
