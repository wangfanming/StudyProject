package com.wfm.rpcClientBasedNetty.client;

import com.wfm.rpcClientBasedNetty.aop.RpcInvokeHook;
import com.wfm.rpcClientBasedNetty.future.RpcFuture;
import com.wfm.rpcClientBasedNetty.test.TestInterface;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @Auther: wangfanming
 * @Date: 2019/7/17 11:14
 * @Description: RpcClient是实现了InvocaHandler的类，同步方式下的代理对象调用任何方法都会变成RpcClient的invoke()方法，
 *              在invoke()方法中就可以得到调用的方法名和参数，通过Netty将方法名和参数传递至服务器。
 *              异步模式下，最终会直接调用RpcClient的call()方法，返回一个RpcFuture对象。
 */
public class RpcClient implements InvocationHandler {

    private long timeoutMills = 0;
    private RpcInvokeHook rpcInvokerHook = null;
    private String host;
    private int port;

    TestInterface testInterface;

    public RpcClient(long timeoutMills, RpcInvokeHook rpcInvokeHook, String host, int port)
    {
        this.timeoutMills = timeoutMills;
        this.rpcInvokerHook = rpcInvokeHook;
        this.host = host;
        this.port = port;
    }

    /**
     *
     * 功能描述: 被代理对象在调用自身方法时，实际上是在调用本方法进行“增强”。
     * @param:
     * @return:
     * @auther: wangfanming
     * @date: 2019/7/18 15:07
     */
    public Object invoke(Object proxy,Method method,Object[] args) throws Throwable {
        RpcFuture rpcFuture = call(method.getName(), args);
        Object result;
        if(timeoutMills == 0){
            result = rpcFuture.get();
        }else{
            result = rpcFuture.get(timeoutMills);
        }

        if(rpcInvokerHook != null){
            rpcInvokerHook.afterInvoke(method.getName(),args);
        }

        return result;
    }

    public void connect(){
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("connect to " + host + ":" + port + " success!");

        testInterface = new TestInterface(){
          public String testMethod01(String string){
              return string.toUpperCase();
          }
        };
    }

    public RpcFuture call(String methodName,Object ... args){
        if(rpcInvokerHook != null){
            rpcInvokerHook.beforeInvoke(methodName,args);
        }
        System.out.print("invoke method = " + methodName + " args =");

        for(Object argsObject : args){
            System.out.print(" " + argsObject.toString());
        }
        System.out.println("");

        RpcFuture rpcFuture = new RpcFuture();
        //此处示例程序中将TestThread作为服务端调用
        TestThread testThread = new TestThread(rpcFuture, methodName, args);
        testThread.start();

        return rpcFuture;
    }

    class TestThread extends Thread{
        String methodName;
        Object[] args;
        RpcFuture rpcFuture;

        public TestThread(RpcFuture rpcFuture,String methodName,Object[] args){
            this.rpcFuture = rpcFuture;
            this.methodName = methodName;
            this.args = args;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(2000);
                int parameterCount = args.length;
                Method method;

                if(parameterCount > 0){
                    Class<?>[] parameterTypes = new Class[args.length];
                    for(int i = 0;i < parameterCount; i++){
                        parameterTypes[i] = args[i].getClass();
                    }
                    method = testInterface.getClass().getMethod(methodName, parameterTypes);
                }else{
                    method = testInterface.getClass().getMethod(methodName);
                }

                //调用 invoke()方法，完成逻辑 ,此时 rpcFuture的计数器会执行countDown()
                rpcFuture.setResult(method.invoke(testInterface,args));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            }

        }
    }





}
