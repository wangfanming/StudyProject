package com.wfm.extend

/**
 *
 * @ClassName Ant
 * @Descripyion TODO
 * @author wangfanming
 * @date 2020/2/25 15:50
 * @version 1.0
 */
class Ant extends {override val range = 2} with Creature {
  // {override val range = 2} with    , todo 这种提前定义的语法可以在超类（父类） 的构造器执行前初始化子类的val字段 ,强调： 子类的该字段会优先于父类字段初始化
}
