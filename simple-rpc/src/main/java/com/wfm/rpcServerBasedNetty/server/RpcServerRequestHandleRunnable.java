package com.wfm.rpcServerBasedNetty.server;

import com.wfm.rpcClientBasedNetty.aop.RpcInvokeHook;
import com.wfm.rpcServerBasedNetty.context.RpcRequest;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.BlockingQueue;

public class RpcServerRequestHandleRunnable implements Runnable
{
    private Class<?> interfaceClass;
    private Object serviceProvider;
    private RpcInvokeHook rpcInvokeHook;
    private BlockingQueue<RpcRequest> requestQueue;

    public RpcServerRequestHandleRunnable(Class<?> interfaceClass,
                                          Object serviceProvider, RpcInvokeHook rpcInvokeHook,
                                          BlockingQueue<RpcRequest> requestQueue)
    {
        this.interfaceClass = interfaceClass;
        this.serviceProvider = serviceProvider;
        this.rpcInvokeHook = rpcInvokeHook;
        this.requestQueue = requestQueue;
    }

    public void run()
    {
        while(true)
        {
            try
            {
                RpcRequest rpcRequest = requestQueue.take();

                String methodName = rpcRequest.getMethodName();
                Object[] args = rpcRequest.getArgs();

                int parameterCount = args.length;
                Method method = null;
                if(parameterCount > 0)
                {
                    Class<?>[] parameterTypes = new Class[args.length];
                    for(int i=0; i<parameterCount; i++)
                    {
                        parameterTypes[i] = args[i].getClass();
                    }
                    method = interfaceClass.getMethod(methodName, parameterTypes);
                }
                else
                {
                    method = interfaceClass.getMethod(methodName);
                }

                if(rpcInvokeHook != null)
                    rpcInvokeHook.beforeInvoke(methodName, args);

                Object result = method.invoke(serviceProvider, args);
                System.out.println("Send response id = " + rpcRequest.getId() + " result = " + result
                        + " back to client. " + Thread.currentThread());

                if(rpcInvokeHook != null)
                    rpcInvokeHook.afterInvoke(methodName, args);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            catch (NoSuchMethodException e)
            {
                // TODO return NoSuchMethodException to client
                e.printStackTrace();
            }
            catch (SecurityException e)
            {
                e.printStackTrace();
            }
            catch (IllegalAccessException e)
            {
                // TODO return IllegalAccessException to client
                e.printStackTrace();
            }
            catch (IllegalArgumentException e)
            {
                // TODO return IllegalArgumentException to client
                e.printStackTrace();
            }
            catch (InvocationTargetException e)
            {
                // TODO return Exception to client
                e.printStackTrace();
            }
        }
    }
}