package com.wfm.extend

/**
 *
 * @ClassName Worker2
 * @Descripyion TODO
 * @author wangfanming
 * @date 2020/2/25 15:33
 * @version 1.0
 */
class Worker2(name:String) extends Person2(name) {
  override def id: Int = name.hashCode  //override  子类重写父类的抽象方法是，override 可要可不要
}
