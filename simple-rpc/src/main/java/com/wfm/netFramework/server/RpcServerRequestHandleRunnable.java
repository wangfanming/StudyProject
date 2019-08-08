package com.wfm.netFramework.server;

/**
 * @Auther: wangfanming
 * @Date: 2019/7/23 15:09
 * @Description:
 */
import com.wfm.rpcClientBasedNetty.aop.RpcInvokeHook;
import com.wfm.rpcServerBasedNetty.context.RpcRequestWrapper;
import com.wfm.rpcServerBasedNetty.context.RpcResponse;
import io.netty.channel.Channel;

import java.util.concurrent.BlockingQueue;

import com.esotericsoftware.reflectasm.MethodAccess;


public class RpcServerRequestHandleRunnable implements Runnable
{
    private Class<?> interfaceClass;
    private Object serviceProvider;
    private RpcInvokeHook rpcInvokeHook;
    private BlockingQueue<RpcRequestWrapper> requestQueue;
    private RpcRequestWrapper rpcRequestWrapper;

    private MethodAccess methodAccess;
    private String lastMethodName = "";
    private int lastMethodIndex;

    public RpcServerRequestHandleRunnable(Class<?> interfaceClass,
                                          Object serviceProvider, RpcInvokeHook rpcInvokeHook,
                                          BlockingQueue<RpcRequestWrapper> requestQueue)
    {
        this.interfaceClass = interfaceClass;
        this.serviceProvider = serviceProvider;
        this.rpcInvokeHook = rpcInvokeHook;
        this.requestQueue = requestQueue;

        methodAccess = MethodAccess.get(interfaceClass);
    }

    public void run()
    {
        while(true)
        {
            try
            {
                rpcRequestWrapper = requestQueue.take();

                String methodName = rpcRequestWrapper.getMethodName();
                Object[] args = rpcRequestWrapper.getArgs();

                if(rpcInvokeHook != null)
                    rpcInvokeHook.beforeInvoke(methodName, args);

                Object result = null;
                if(!methodName.equals(lastMethodName))
                {
                    lastMethodIndex = methodAccess.getIndex(methodName);
                    lastMethodName = methodName;
                }

                result = methodAccess.invoke(serviceProvider, lastMethodIndex, args);

                Channel channel = rpcRequestWrapper.getChannel();
                RpcResponse rpcResponse = new RpcResponse();
                rpcResponse.setId(rpcRequestWrapper.getId());
                rpcResponse.setResult(result);
                rpcResponse.setInvokeSuccess(true);
                channel.writeAndFlush(rpcResponse);

                if(rpcInvokeHook != null)
                    rpcInvokeHook.afterInvoke(methodName, args);
            }
            catch (Exception e)
            {
                Channel channel = rpcRequestWrapper.getChannel();
                RpcResponse rpcResponse = new RpcResponse();
                rpcResponse.setId(rpcRequestWrapper.getId());
                rpcResponse.setThrowable(e);
                rpcResponse.setInvokeSuccess(false);
                channel.writeAndFlush(rpcResponse);
            }
        }
    }
}