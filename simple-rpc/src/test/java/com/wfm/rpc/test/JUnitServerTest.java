package com.wfm.rpc.test;

import com.wfm.rpc.server.RpcServer;
import com.wfm.rpc.server.RpcServerBuilder;
import org.junit.Test;

/**
 * @ClassName: JUnitServerTest
 * @Auther: wangfanming
 * @Date: 2019/8/31
 * @Description: TODO
 * @Version 1.0
 */
public class JUnitServerTest {
    @Test
    public void testServerStart() {
        JUnitTestInterfaceImpl jUnitTestInterfaceImpl = new JUnitTestInterfaceImpl();
        RpcServer rpcServer = RpcServerBuilder.create()
                .serviceInterface(JUnitTestInterface.class)
                .serviceProvider(jUnitTestInterfaceImpl)
                .threads(4)
                .bind(3721)
                .build();
        rpcServer.start();
    }

    public static void main(String[] args) {
        new JUnitServerTest().testServerStart();
    }
}
