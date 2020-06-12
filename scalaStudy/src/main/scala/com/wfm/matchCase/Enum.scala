package com.wfm.matchCase

/**
 *
 * @ClassName Enum
 * @Descripyion TODO
 * @author wangfanming
 * @date 2020/2/27 10:11
 * @version 1.0
 */
class Enum {
  // todo 利用密封类特性结合样例类实现枚举类型  超类被声明为sealed，则所有子类在编译期可知
  sealed abstract class TrafficLightColor
  case object Red extends TrafficLightColor
  case object Yellow extends TrafficLightColor
  case object Blue extends TrafficLightColor

  def main(args: Array[String]): Unit = {

  }
}
