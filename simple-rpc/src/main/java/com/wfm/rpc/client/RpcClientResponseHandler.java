package com.wfm.rpc.client;

import com.wfm.rpc.future.RpcFuture;
import com.wfm.rpc.context.RpcResponse;

import java.util.concurrent.*;

public class RpcClientResponseHandler {
    private ConcurrentMap<Integer, RpcFuture> invokeIdRpcFutureMap = new ConcurrentHashMap<Integer, RpcFuture>();

    private ExecutorService threadPool;
    private BlockingQueue<RpcResponse> responseQueue = new LinkedBlockingQueue<RpcResponse>();

    public RpcClientResponseHandler(int threads) {
        threadPool = Executors.newFixedThreadPool(threads);
        for (int i = 0; i < threads; i++) {
            threadPool.execute(new RpcClientResponseHandleRunnable(invokeIdRpcFutureMap, responseQueue));
        }
    }

    public void register(int id, RpcFuture rpcFuture) {
        invokeIdRpcFutureMap.put(id, rpcFuture);
    }

    public void addResponse(RpcResponse rpcResponse) {
        responseQueue.add(rpcResponse);
    }
}