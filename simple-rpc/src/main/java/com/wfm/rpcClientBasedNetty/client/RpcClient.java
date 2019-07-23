package com.wfm.rpcClientBasedNetty.client;

import com.wfm.netFramework.client.RpcClientChannelInactiveListener;
import com.wfm.netFramework.client.RpcClientDispatchHandler;
import com.wfm.netFramework.client.RpcClientResponseHandler;
import com.wfm.netFramework.netty.NettyKryoDecoder;
import com.wfm.netFramework.netty.NettyKryoEncoder;
import com.wfm.rpcClientBasedNetty.aop.RpcInvokeHook;
import com.wfm.rpcClientBasedNetty.future.RpcFuture;
import com.wfm.rpcClientBasedNetty.test.TestInterface;
import com.wfm.rpcServerBasedNetty.context.RpcRequest;
import com.wfm.utils.InfoPrinter;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Auther: wangfanming
 * @Date: 2019/7/17 11:14
 * @Description: RpcClient是实现了InvocaHandler的类，同步方式下的代理对象调用任何方法都会变成RpcClient的invoke()方法，
 *              在invoke()方法中就可以得到调用的方法名和参数，通过Netty将方法名和参数传递至服务器。
 *              异步模式下，最终会直接调用RpcClient的call()方法，返回一个RpcFuture对象。
 */
public class RpcClient implements InvocationHandler {

    private long timeoutMills = 0;
    private RpcInvokeHook rpcInvokerHook = null;
    private String host;
    private int port;
    private Bootstrap bootstrap;

    private RpcClientResponseHandler rpcClientResponseHandler;
    private Channel channel;
    private AtomicInteger invokeIdGenerator = new AtomicInteger(0);
    private RpcClientChannelInactiveListener rpcClientChannelInactiveListener;


    public RpcClient(long timeoutMills, RpcInvokeHook rpcInvokeHook, String host, int port)
    {
        this.timeoutMills = timeoutMills;
        this.rpcInvokerHook = rpcInvokeHook;
        this.host = host;
        this.port = port;
    }

    /**
     *
     * 功能描述: 被代理对象在调用自身方法时，实际上是在调用本方法进行“增强”。
     * @param:
     * @return:
     * @auther: wangfanming
     * @date: 2019/7/18 15:07
     */
    @Override
    public Object invoke(Object proxy,Method method,Object[] args) throws Throwable {
        RpcFuture rpcFuture = call(method.getName(), args);
        Object result;

        //RpcFuture的get方法，在此处都会调用到CountDownLatch的await()方法发生阻塞，直到TestThread的run()方法，等待完成后，
        // rcFuture在调用countDownLatch.countDown()后，阻塞才会打开。
        if(timeoutMills == 0){
            result = rpcFuture.get();
        }else{
            result = rpcFuture.get(timeoutMills);
        }

        if(rpcInvokerHook != null){
            rpcInvokerHook.afterInvoke(method.getName(),args);
        }

        return result;
    }

//    public void connect(){
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        System.out.println("connect to " + host + ":" + port + " success!");
//
//        testInterface = new TestInterface(){
//          public String testMethod01(String string){
//              return string.toUpperCase();
//          }
//        };
//    }

    public void connect()
    {
        bootstrap = new Bootstrap();
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        try
        {
            bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<Channel>()
                    {
                        @Override
                        protected void initChannel(Channel ch) throws Exception
                        {
                            ch.pipeline().addLast(new NettyKryoDecoder(),
                                    new RpcClientDispatchHandler(rpcClientResponseHandler, rpcClientChannelInactiveListener),
                                    new NettyKryoEncoder());
                        }
                    });
            bootstrap.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
            bootstrap.option(ChannelOption.TCP_NODELAY, true);
            bootstrap.option(ChannelOption.SO_KEEPALIVE, true);

            do
            {
                channel = tryConnect();
            }
            while(channel == null);

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    private Channel tryConnect()
    {
        try
        {
            InfoPrinter.println("Try to connect to [" + host + ":" + port + "].");
            ChannelFuture future = bootstrap.connect(host, port).sync();
            if(future.isSuccess())
            {
                InfoPrinter.println("Connect to [" + host + ":" + port + "] successed.");
                return future.channel();
            }
            else
            {
                InfoPrinter.println("Connect to [" + host + ":" + port + "] failed.");
                InfoPrinter.println("Try to reconnect in 10s.");
                Thread.sleep(10000);
                return null;
            }
        }
        catch (Exception exception)
        {
            InfoPrinter.println("Connect to [" + host + ":" + port + "] failed.");
            InfoPrinter.println("Try to reconnect in 10 seconds.");
            try
            {
                Thread.sleep(10000);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            return null;
        }
    }


//    public RpcFuture call(String methodName,Object ... args){
//        if(rpcInvokerHook != null){
//            rpcInvokerHook.beforeInvoke(methodName,args);
//        }
//        System.out.print("invoke method = " + methodName + " args =");
//
//        for(Object argsObject : args){
//            System.out.print(" " + argsObject.toString());
//        }
//        System.out.println("");
//
//        RpcFuture rpcFuture = new RpcFuture();
//        //此处示例程序中将TestThread作为服务端调用，此处的RpcFuture时一个出参，主要用来对结果做包装。
//        TestThread testThread = new TestThread(rpcFuture, methodName, args);
//        testThread.start();
//
//        //此处直接返回RpcFuture对象，不做等待。在TestThread的run()方法执行完成后，RpcFuture对象的result才会被赋值。
//        return rpcFuture;
//    }

    public RpcFuture call(String methodName, Object ... args)
    {
        if(rpcInvokerHook != null)
            rpcInvokerHook.beforeInvoke(methodName, args);

        RpcFuture rpcFuture = new RpcFuture();
        int id = invokeIdGenerator.addAndGet(1);
        rpcClientResponseHandler.register(id, rpcFuture);

        RpcRequest rpcRequest = new RpcRequest(id, methodName, args);
        if(channel != null)
            channel.writeAndFlush(rpcRequest);
        else
            return null;

        return rpcFuture;
    }

//    private TestInterface testInterface;
//    class TestThread extends Thread{
//        String methodName;
//        Object[] args;
//        RpcFuture rpcFuture;
//
//        public TestThread(RpcFuture rpcFuture,String methodName,Object[] args){
//            this.rpcFuture = rpcFuture;
//            this.methodName = methodName;
//            this.args = args;
//        }
//
//        @Override
//        public void run() {
//            try {
//                Thread.sleep(2000);
//                int parameterCount = args.length;
//                Method method;
//
//                if(parameterCount > 0){
//                    Class<?>[] parameterTypes = new Class[args.length];
//                    for(int i = 0;i < parameterCount; i++){
//                        parameterTypes[i] = args[i].getClass();
//                    }
//                    method = testInterface.getClass().getMethod(methodName, parameterTypes);
//                }else{
//                    method = testInterface.getClass().getMethod(methodName);
//                }
//
//                //调用 invoke()方法，完成逻辑 ,此时 rpcFuture的计数器会执行countDown(),如果设置了listener，则会触发listener的方法执行。
//                rpcFuture.setResult(method.invoke(testInterface,args));
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            } catch (IllegalArgumentException e) {
//                e.printStackTrace();
//            } catch (InvocationTargetException e) {
//                e.printStackTrace();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            } catch (NoSuchMethodException e) {
//                e.printStackTrace();
//            } catch (SecurityException e) {
//                e.printStackTrace();
//            }
//
//        }
//    }





}
