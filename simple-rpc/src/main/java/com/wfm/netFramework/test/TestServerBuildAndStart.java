package com.wfm.netFramework.test;

import com.wfm.rpcClientBasedNetty.aop.RpcInvokeHook;
import com.wfm.rpcClientBasedNetty.test.TestInterface;
import com.wfm.rpcServerBasedNetty.server.RpcServer;
import com.wfm.rpcServerBasedNetty.server.RpcServerBuilder;

/**
 * @Auther: wangfanming
 * @Date: 2019/7/23 16:13
 * @Description:
 */
public class TestServerBuildAndStart
{
    public static void main(String[] args)
    {
        TestInterface testInterface = new TestInterface()
        {
            @Override
            public String testMethod02() {
                return "return from server";
            }

//            @Override
//            public String testMethod01(String s) {
//                return s.toUpperCase();
//            }
        };

        RpcInvokeHook hook = new RpcInvokeHook()
        {
            public void beforeInvoke(String methodName, Object[] args)
            {
                System.out.println("beforeInvoke in server" + methodName);
            }

            public void afterInvoke(String methodName, Object[] args)
            {
                System.out.println("afterInvoke in server" + methodName);
            }
        };

        RpcServer rpcServer = RpcServerBuilder.create()
                .serviceInterface(TestInterface.class)
                .serviceProvider(testInterface)
                .threads(4)
                .hook(hook)
                .bind(3721)
                .build();

        rpcServer.start();
    }
}
