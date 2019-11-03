package com.wfm.rpcClientBasedNetty.client;

import com.wfm.rpcClientBasedNetty.aop.RpcInvokeHook;

import java.lang.reflect.Proxy;

/**
 * @Auther: wangfanming
 * @Date: 2019/7/17 10:28
 * @Description: RpcClentProxyBuilder 是用于产生代理对象的工厂，可以产生同步或异步的代理对象
 */
public class RpcClientProxyBuilder {
    public static class ProxyBuilder<T>{
        private Class<T> clazz;
        private RpcClient rpcClient;

        private long timeoutMills = 0;
        private RpcInvokeHook rpcInvokeHook = null;
        private String host;
        private int port;
        private int threads;


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
         * 功能描述: 设置线程个数
         * @param:
         * @return:
         * @auther: wangfanming
         * @date: 2019/7/23 16:07
         */
        public ProxyBuilder<T> threads(int threadCount)
        {
            this.threads = threadCount;
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
         * 功能描述: 用于创建同步方法下的代理对象
         * @param:
         * @return:
         * @auther: wangfanming
         * @date: 2019/7/17 10:43
         */
        public T build(){
            if(threads <= 0)
                threads = Runtime.getRuntime().availableProcessors();

            rpcClient = new RpcClient(timeoutMills,rpcInvokeHook,host,port,threads);
            //初始化channel
            rpcClient.connect();

            T res = null;
             try {
                res = (T) Proxy.newProxyInstance(clazz.getClassLoader(),new Class<?>[]{clazz},rpcClient);
             }catch (Exception e){
                 e.printStackTrace();
             }

             return res;
        }

        /**
         *
         * 功能描述: 用于创建异步方式下的代理对象。
         * @param:
         * @return:
         * @auther: wangfanming
         * @date: 2019/7/17 10:45
         */
        public RpcClientAsyncProxy buildAsyncProxy(){
            if(threads <= 0)
                threads = Runtime.getRuntime().availableProcessors();

            rpcClient = new RpcClient(timeoutMills,rpcInvokeHook,host,port,threads);
            rpcClient.connect();

            return new RpcClientAsyncProxy(rpcClient);
        }
    }

    public static <T> ProxyBuilder<T> create(Class<T> targetClass){
        return new ProxyBuilder<T>(targetClass);
    }
}
