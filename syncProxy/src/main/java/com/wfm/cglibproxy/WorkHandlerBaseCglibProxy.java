package com.wfm.cglibproxy;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @Auther: wangfanming
 * @Date: 2019/7/17 17:23
 * @Description:
 */
public class WorkHandlerBaseCglibProxy implements MethodInterceptor {
    private Object obj;

    public WorkHandlerBaseCglibProxy(){}

    public Object createWorkHandler(Object obj){
        this.obj = obj;
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(obj.getClass());
        enhancer.setCallback(this);
        Object proxyObj = enhancer.create();

        return proxyObj;
    }

    public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        Object result = null;
        //可以在调用被代理对象的方法之前，对其进行加强
        if("work".equals(method.getName())){
            System.out.println("代理方法：" + method.getName() + "被调用了。。");
        }
        result = method.invoke(obj, args);
        return result;
    }
}
