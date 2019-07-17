package com.wfm.rpcBasedNetty.aop;

/**
 * @Auther: wangfanming
 * @Date: 2019/7/17 11:55
 * @Description:
 */
public interface RpcInvokeHook {
    public void beforeInvoke(String methodName,Object[] args);
    public void afterInvoke(String methodName,Object[] args);
}
