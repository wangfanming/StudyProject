package com.wfm.netFramework.client;

/**
 * @Auther: wangfanming
 * @Date: 2019/7/23 15:01
 * @Description:
 */
import com.wfm.rpcClientBasedNetty.future.RpcFuture;
import com.wfm.rpcServerBasedNetty.context.RpcResponse;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentMap;


public class RpcClientResponseHandleRunnable implements Runnable
{
    private ConcurrentMap<Integer, RpcFuture> invokeIdRpcFutureMap;
    private BlockingQueue<RpcResponse> responseQueue;

    public RpcClientResponseHandleRunnable(
            ConcurrentMap<Integer, RpcFuture> invokeIdRpcFutureMap,
            BlockingQueue<RpcResponse> responseQueue)
    {
        this.invokeIdRpcFutureMap = invokeIdRpcFutureMap;
        this.responseQueue = responseQueue;
    }

    public void run()
    {
        while(true)
        {
            try
            {
                RpcResponse rpcResponse = responseQueue.take();

                int id = rpcResponse.getId();
                RpcFuture rpcFuture = invokeIdRpcFutureMap.remove(id);

                if(rpcResponse.isInvokeSuccess())
                    rpcFuture.setResult(rpcResponse.getResult());
                else
                    rpcFuture.setThrowable(rpcResponse.getThrowable());
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }
}
