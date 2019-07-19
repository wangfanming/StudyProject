package com.wfm.rpcClientBasedNetty.test;

import com.wfm.rpcClientBasedNetty.aop.RpcInvokeHook;
import com.wfm.rpcClientBasedNetty.client.RpcClientAsyncProxy;
import com.wfm.rpcClientBasedNetty.client.RpcClientProxyBuilder;
import com.wfm.rpcClientBasedNetty.future.RpcFuture;
import com.wfm.rpcClientBasedNetty.future.RpcFutureListener;

/**
 * @Auther: wangfanming
 * @Date: 2019/7/17 14:03
 * @Description:
 */
public class AsyncTest {
    public static void main(String[] args) {
        RpcInvokeHook hook = new RpcInvokeHook() {
            public void beforeInvoke(String method, Object[] args) {
                System.out.println("before invoke " + method);
            }

            public void afterInvoke(String method, Object[] args) {
                System.out.println("after invoke " + method);
            }
        };

        RpcClientAsyncProxy rpcClientAsyncProxy = RpcClientProxyBuilder.create(TestInterface.class)
                .timeout(0)
                .hook(hook)
                .connect("127.0.0.1", 3721)
                .buildAsyncProxy();

        RpcFuture rpcFuture = rpcClientAsyncProxy.call("testMethod01", "qwqeceaveras");
        rpcFuture.setRpcFutureListener(new RpcFutureListener() {
            public void onResult(Object result)
            {
                System.out.println("RpcFutureListener result = " + result.toString());
            }

            public void onException(Throwable throwable)
            {
                System.out.println("RpcFutureListener onException");
            }
        });

        System.out.println("RpcFuture isDone = " + rpcFuture.isDone());

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("RpcFuture isDone = " + rpcFuture.isDone());
        try {
            System.out.println("result = " + rpcFuture.get());
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }
}

