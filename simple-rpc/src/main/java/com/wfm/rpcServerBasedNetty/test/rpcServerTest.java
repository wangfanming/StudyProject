package com.wfm.rpcServerBasedNetty.test;

import com.wfm.rpcClientBasedNetty.aop.RpcInvokeHook;
import com.wfm.rpcClientBasedNetty.test.TestInterface;
import com.wfm.rpcServerBasedNetty.server.RpcServer;
import com.wfm.rpcServerBasedNetty.server.RpcServerBuilder;

public class rpcServerTest {
    public static void main(String[] args) {

//        (String string) -> { return string.toUpperCase(); };
        TestInterface testInterface = new TestInterface() {
            @Override
            public String testMethod02() {
                return "return from server";
            }
            public String testMethod01(String string)
            {
                return string.toUpperCase();
            }
        };

        RpcInvokeHook hook = new RpcInvokeHook()
        {
            public void beforeInvoke(String methodName, Object[] args)
            {
                System.out.println("beforeInvoke " + methodName);
            }

            public void afterInvoke(String methodName, Object[] args)
            {
                System.out.println("afterInvoke " + methodName);
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
