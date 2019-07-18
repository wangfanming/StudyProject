package com.wfm.jdkproxy;

/**
 * @Auther: wangfanming
 * @Date: 2019/7/17 16:27
 * @Description:
 */
public class Student implements People {


    public String work() {
        System.out.println("我在学习。。。");
        return "学生要学习";
    }
}
