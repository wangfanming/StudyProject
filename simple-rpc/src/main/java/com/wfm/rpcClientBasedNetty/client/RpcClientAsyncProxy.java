package com.wfm.rpcClientBasedNetty.client;

import com.wfm.rpcClientBasedNetty.future.RpcFuture;

/**
 * @Auther: wangfanming
 * @Date: 2019/7/17 11:09
 * @Description:
 */
public class RpcClientAsyncProxy {
    private RpcClient rpcClient;
    public  RpcClientAsyncProxy(RpcClient rpcClient){
        this.rpcClient = rpcClient;
    }

    /**
     *
     * 功能描述: 通过call()方法实现对方法的异步调用，实质上是调用了内部持有的RpcClient的call()方法。
     *          无论同步还是异步，最终都会调用到RpcClient。
     * @param:
     * @return:
     * @auther: wangfanming
     * @date: 2019/7/17 11:12
     */
    public RpcFuture call(String methodName,Object ... args){
        return (RpcFuture) rpcClient.call(methodName,args);
    }
}
