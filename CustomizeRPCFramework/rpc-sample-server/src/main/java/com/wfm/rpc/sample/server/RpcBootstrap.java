package com.wfm.rpc.sample.server;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 用户系统服务端的启动入口
 * 其意义是启动 sparingContext,从而构造框架中的RpcServer
 * 同时，系统中标注了RpcService注解的业务会发布到Rpcserver
 */
public class RpcBootstrap {
    public static void main(String[] args) {
        new ClassPathXmlApplicationContext("spring.xml");
    }
}
