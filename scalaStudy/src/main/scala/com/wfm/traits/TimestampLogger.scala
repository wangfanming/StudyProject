package com.wfm.traits

/**
 *
 * @ClassName TimestampLogger
 * @Descripyion TODO
 * @author wangfanming
 * @date 2020/2/25 22:07
 * @version 1.0
 */
trait Logger{
  def log(message:String) = {}
}
trait ConsoleLogger extends Logger{
  override def log(message: String) = {
    println(s"ConsoleLogger's log $message")
  }
}
trait TimestampLogger extends Logger {
  override def log(message: String): Unit = super.log(s"TimestampLogger ${java.time.Instant.now()} $message")
}
trait ShortLogger extends Logger {
  val maxLength = 15 // todo 该字段会被直接加载到使用改特质的子类中，成为子类自身的字段，此处如果不给出默认值，也可以在使用该特质的类中给出确定值
  abstract override def log(message: String): Unit = {
    super.log(
      if(message.length < maxLength) message  else message.substring(0,maxLength -3) + "..."
    )

  }
}

class BankAccount(initBalance:Double){
  private var balance = initBalance  // 生成私有 get set 方法
//  var balance = initBalance  // 直接将值暴露出去，不安全
  def deposit(amount:Double)= {
    balance += amount
    balance
  }
  def withdraw(amount:Double) ={
    balance -= amount
    balance
  }

  def query = balance
}

class SavingAccount(initBalance:Double) extends BankAccount(initBalance ){
  private var num :Int = _
  def earnMonthlyInterest()={
    num = 3         //每月3次免手续费 1元
    super.deposit(1) // 月利息1元
  }

  override def deposit(amount: Double): Double = {
    if (num > 0){
      num -= 1
      super.deposit(amount)
    }else super.deposit(amount)
  }

  override def withdraw(amount: Double): Double = {
    if (num > 0){
      num -= 1
      super.withdraw(amount)
    }else super.withdraw(amount)
  }

}

class SavingAccount2(initBalance:Double) extends SavingAccount(initBalance) with Logger{
  override def withdraw(amount: Double): Double = {
//    if(amount > balance){  //这种方式能直接使用balance，是因为 balance没有用private修饰，但是不安全
    if (query < amount) {
      log("余额不足")
      -1
    } else{
      log("扣款成功")
      super.withdraw(amount)
    }

  }
}

/**
 * 以下A 的构造顺序：
 * BankAccount -> Logger -> ShortLogger -> ConsoleLogger -> A
 */
class A extends BankAccount(100) with ShortLogger with ConsoleLogger
/**
 * TODO  类或对象添加多个互相调用的特质时，从 最后一个 开始执行。
 */
object Test02{
  def main(args: Array[String]): Unit = {
    val acct01 = new SavingAccount2(100) with ConsoleLogger with TimestampLogger
    acct01.withdraw(20)

    val acct2 = new SavingAccount2(100) with ConsoleLogger with TimestampLogger with ShortLogger{
      override val maxLength = 20}
    acct2.withdraw(20)

    val acct3 = new SavingAccount2(100) with ConsoleLogger with ShortLogger with TimestampLogger
    acct3.withdraw(10)

    val acct4 = new SavingAccount2(100) with ConsoleLogger with TimestampLogger with ShortLogger
    acct4.withdraw(10)
  }
}