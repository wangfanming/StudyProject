package com.wfm.netFramework.client;

/**
 * @Auther: wangfanming
 * @Date: 2019/7/23 15:00
 * @Description:
 */
import com.wfm.rpcClientBasedNetty.future.RpcFuture;
import com.wfm.rpcServerBasedNetty.context.RpcResponse;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;


public class RpcClientResponseHandler
{
    private ConcurrentMap<Integer, RpcFuture> invokeIdRpcFutureMap = new ConcurrentHashMap<Integer, RpcFuture>();

    private ExecutorService threadPool;
    private BlockingQueue<RpcResponse> responseQueue = new LinkedBlockingQueue<RpcResponse>();

    public RpcClientResponseHandler(int threads)
    {
        threadPool = Executors.newFixedThreadPool(threads);
        for(int i=0; i<threads; i++)
        {
            threadPool.execute(new RpcClientResponseHandleRunnable(invokeIdRpcFutureMap, responseQueue));
        }
    }

    public void register(int id, RpcFuture rpcFuture)
    {
        invokeIdRpcFutureMap.put(id, rpcFuture);
    }

    public void addResponse(RpcResponse rpcResponse)
    {
        responseQueue.add(rpcResponse);
    }
}
