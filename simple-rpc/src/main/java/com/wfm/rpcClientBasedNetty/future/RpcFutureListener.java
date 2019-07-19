package com.wfm.rpcClientBasedNetty.future;

/**
 * @Auther: wangfanming
 * @Date: 2019/7/17 12:00
 * @Description:
 */
public interface RpcFutureListener {
    public void onResult(Object result);
    public void onException(Throwable throwable);
}
