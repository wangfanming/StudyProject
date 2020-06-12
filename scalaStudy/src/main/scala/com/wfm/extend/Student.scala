package com.wfm.extend

/**
 *
 * @ClassName Student
 * @Descripyion TODO
 * @author wangfanming
 * @date 2020/2/25 17:18
 * @version 1.0
 */
class Student private (var name:String,var age:Int) {
    //私有主构造器，别的类不能直接使用，必须依赖其他辅助构造器
}
