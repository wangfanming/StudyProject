package com.wfm.test;

import com.wfm.cglibproxy.WorkHandlerBaseCglibProxy;
import com.wfm.jdkproxy.Student;

/**
 * @Auther: wangfanming
 * @Date: 2019/7/17 17:40
 * @Description:
 */
public class TestCglibProxy {
    public static void main(String[] args) {
        Student student = new Student();
        Student studentProxy = (Student)new WorkHandlerBaseCglibProxy().createWorkHandler(student);
        System.out.println(studentProxy.work());
    }
}
