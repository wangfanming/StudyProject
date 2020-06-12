package com.wfm.extend

/**
 *
 * @ClassName MyTime
 * @Descripyion TODO
 * @author wangfanming
 * @date 2020/2/25 16:57
 * @version 1.0
 */
class MyTime(val time:Int) extends AnyVal {
  def minutes = time % 100
  def hour = time / 100

  override def toString: String = f"$time%4d"
}

object MyTime {
  def apply(time: Int): MyTime =
    if (0 <= time && time < 2400 && time %100 < 60){
      new MyTime(time)
    } else{
      throw new IllegalArgumentException
    }
}
