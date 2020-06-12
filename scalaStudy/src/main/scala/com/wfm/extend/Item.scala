package com.wfm.extend

/**
 *
 * @ClassName Item
 * @Descripyion TODO
 * @author wangfanming
 * @date 2020/2/25 16:42
 * @version 1.0
 */
class Item(val desc:String,val price:Double) {
  final override def equals(other: Any) = other match {
    case that:Item => desc == that.desc && price == that.price
    case _ => false
  }

  final override def hashCode(): Int = (desc,price).##   // ##方法是hashCode的null值安全的版本，对null值交出0而不是抛出异常
}
