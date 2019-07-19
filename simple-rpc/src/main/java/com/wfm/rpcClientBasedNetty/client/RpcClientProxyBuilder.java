package com.wfm.rpcClientBasedNetty.client;

import com.wfm.rpcClientBasedNetty.aop.RpcInvokeHook;

import java.lang.reflect.Proxy;

/**
 * @Auther: wangfanming
 * @Date: 2019/7/17 10:28
 * @Description:
 */
public class RpcClientProxyBuilder {
    public static class ProxyBuilder<T>{
        private Class<T> clazz;
        private RpcClient rpcClient;

        private long timeoutMills = 0;
        private RpcInvokeHook rpcInvokeHook = null;
        private String host;
        private int port;

        private ProxyBuilder(Class<T> clazz){
            this.clazz = clazz;
        }

        /**
         *
         * 功能描述: 设置超时
         * @param:
         * @return:
         * @auther: wangfanming
         * @date: 2019/7/17 10:34
         */
        public ProxyBuilder<T> timeout(long timeoutMills){
            this.timeoutMills = timeoutMills;
            if(timeoutMills < 0){
                throw  new IllegalArgumentException("timeoutMills can not be minus!");
            }
            return this;
        }

        /***
         *    设置回调
         * @param hook
         * @return
         */
        public ProxyBuilder<T> hook(RpcInvokeHook hook){
            this.rpcInvokeHook = hook;
            return this;
        }

        /**
         *
         * 功能描述: 设置连接信息
         * @param:
         * @return:
         * @auther: wangfanming
         * @date: 2019/7/17 10:37
         */
        public ProxyBuilder<T> connect(String host,int port){
            this.host = host;
            this.port = port;

            return  this;
        }

        /**
         *
         * 功能描述: 构建同步代理。以同步方式，线程将被阻塞，直到获得结果或超时。
         * @param:
         * @return:
         * @auther: wangfanming
         * @date: 2019/7/17 10:43
         */
        public T build(){
            rpcClient = new RpcClient(timeoutMills,rpcInvokeHook,host,port);
            rpcClient.connect();

            return (T) Proxy.newProxyInstance(clazz.getClassLoader(),new Class<?>[]{clazz},rpcClient);
        }

        /**
         *
         * 功能描述: 构建异步代理。以异步方式，一个RpcFuture将立即返回。
         * @param:
         * @return:
         * @auther: wangfanming
         * @date: 2019/7/17 10:45
         */
        public RpcClientAsyncProxy buildAsyncProxy(){
            rpcClient = new RpcClient(timeoutMills,rpcInvokeHook,host,port);
            rpcClient.connect();

            return new RpcClientAsyncProxy(rpcClient);
        }
    }

    public static <T> ProxyBuilder<T> create(Class<T> targetClass){
        return new ProxyBuilder<T>(targetClass);
    }
}
