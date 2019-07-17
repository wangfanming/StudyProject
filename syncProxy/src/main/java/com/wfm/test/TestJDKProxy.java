package com.wfm.test;

import com.wfm.jdkproxy.People;
import com.wfm.jdkproxy.Student;
import com.wfm.jdkproxy.WorkHadlerBaseJDKProxy;

import java.lang.reflect.Proxy;

/**
 * @Auther: wangfanming
 * @Date: 2019/7/17 16:34
 * @Description:
 */
public class TestJDKProxy {
    public static void main(String[] args) {
        People people = new Student();

        WorkHadlerBaseJDKProxy workHadler = new WorkHadlerBaseJDKProxy(people);

        People stdent = (People) Proxy.newProxyInstance(people.getClass().getClassLoader(), people.getClass().getInterfaces(), workHadler);

        System.out.println(stdent.work());;

    }
}
