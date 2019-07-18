package com.wfm.jdkproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @Auther: wangfanming
 * @Date: 2019/7/17 16:28
 * @Description:
 */
public class WorkHadlerBaseJDKProxy implements InvocationHandler {

    private Object obj;

    public WorkHadlerBaseJDKProxy(){}

    public WorkHadlerBaseJDKProxy(Object obj){
        this.obj = obj;
    }

    /**
     *
     * 功能描述: 在 被代理对象执行前后，可以对其功能进行加强
     * @param:
     * @return:
     * @auther: wangfanming
     * @date: 2019/7/17 16:31
     */
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //在真实的对象执行之前我们可以添加自己的操作
        System.out.println("before invoke。。。");

        //执行被代理对象的指定运行方法
        Object invokeObj = method.invoke(obj, args);


        //在真实的对象执行之前我们可以添加自己的操作
        System.out.println("after invoke。。。");

        return invokeObj;
    }
}
