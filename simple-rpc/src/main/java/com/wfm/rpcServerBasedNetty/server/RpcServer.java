package com.wfm.rpcServerBasedNetty.server;

import com.wfm.rpcClientBasedNetty.aop.RpcInvokeHook;
import com.wfm.rpcServerBasedNetty.context.RpcRequest;

import java.util.concurrent.atomic.AtomicInteger;

public class RpcServer
{
    private Class<?> interfaceClass;
    private Object serviceProvider;

    private int port;
    private int threads;
    private RpcInvokeHook rpcInvokeHook;

    private RpcServerRequestHandler rpcServerRequestHandler;

    protected RpcServer(Class<?> interfaceClass, Object serviceProvider, int port, int threads,
                        RpcInvokeHook rpcInvokeHook)
    {
        this.interfaceClass = interfaceClass;
        this.serviceProvider = serviceProvider;
        this.port = port;
        this.threads = threads;
        this.rpcInvokeHook = rpcInvokeHook;

        rpcServerRequestHandler = new RpcServerRequestHandler(interfaceClass,
                serviceProvider, threads, rpcInvokeHook);
        rpcServerRequestHandler.start();
    }

    public void start()
    {
        System.out.println("bind port:"+port + " success!");

        //simulation for receive RpcRequest
        AtomicInteger idGenerator = new AtomicInteger(0);
        for(int i=0; i<10; i++)
        {
            rpcServerRequestHandler.addRequest(new RpcRequest(idGenerator.addAndGet(1),
                    "testMethod01", new Object[]{"qwerty"}));
        }
    }

    public void stop()
    {
        //TODO add stop codes here
        System.out.println("server stop success!");
    }
}