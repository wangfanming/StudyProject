package com.wfm.netFramework.test;

import com.wfm.rpcClientBasedNetty.aop.RpcInvokeHook;
import com.wfm.rpcClientBasedNetty.client.RpcClientProxyBuilder;
import com.wfm.rpcClientBasedNetty.test.TestInterface;

/**
 * @Auther: wangfanming
 * @Date: 2019/7/23 16:03
 * @Description:
 */
public class TestClientBuildAndCall
{
    public static void main(String[] args)
    {
        RpcInvokeHook hook = new RpcInvokeHook()
        {
            public void beforeInvoke(String method, Object[] args)
            {
                System.out.println("before invoke in client " + method);
            }

            public void afterInvoke(String method, Object[] args)
            {
                System.out.println("after invoke in client" + method);
            }
        };

        TestInterface testInterface
                = RpcClientProxyBuilder.create(TestInterface.class)
                .timeout(0)
                .threads(4)
                .hook(hook)
                .connect("127.0.0.1", 3721)
                .build();

        for(int i=0; i<10; i++)
        {
            System.out.println("invoke result = " + testInterface.testMethod02());
        }
    }
}
