package com.wfm.rpcBasedNetty.test;

import com.wfm.rpcBasedNetty.aop.RpcInvokeHook;
import com.wfm.rpcBasedNetty.client.RpcClientProxyBuilder;

/**
 * @Auther: wangfanming
 * @Date: 2019/7/17 13:55
 * @Description:
 */
public class SyncTest01 {
    public static void main(String[] args) {
        RpcInvokeHook hook = new RpcInvokeHook() {
            public void beforeInvoke(String methodName, Object[] args) {
                System.out.println("before invoke " + methodName);
            }

            public void afterInvoke(String methodName, Object[] args) {
                System.out.println("after invoke " + methodName);
            }
        };

        TestInterface testInterface = RpcClientProxyBuilder.create(TestInterface.class)
                .timeout(0)
                .hook(hook)
                .connect("127.0.0.1", 3721)
                .build();

        System.out.println("invoke result = " + testInterface.testMethod01("qwerty"));
    }
}
