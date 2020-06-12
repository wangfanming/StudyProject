package com.wfm.traits

/**
 *
 * @ClassName LoggedException
 * @Descripyion TODO  特质 LoggedException 扩展了 Exception类，而且被扩展的这个类会成为所有混入这个特质的子类的超类
 * @author wangfanming
 * @date 2020/2/26 0:10
 * @version 1.0
 */
trait LoggedException extends Exception with ConsoleLogger {
  override def log(message: String): Unit = log(getMessage)
}
class UnhappyException extends LoggedException{
  override def getMessage: String = "arggh !"
}

