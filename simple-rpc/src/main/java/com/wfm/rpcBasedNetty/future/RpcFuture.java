package com.wfm.rpcBasedNetty.future;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @Auther: wangfanming
 * @Date: 2019/7/17 10:48
 * @Description:
 */
public class RpcFuture {
    public final static int STATE_AWAIT = 0;
    public final static int STATE_SUCCESS = 1;
    public final static int STATE_EXCEPTION = 2;

    private CountDownLatch countDownLatch;
    private Object result;
    private Throwable throwable;
    private int state;
    private RpcFutureListener rpcFutureListener = null;

    public RpcFuture(){
        countDownLatch = new CountDownLatch(1);
        state = STATE_AWAIT;
    }

    /**
     *
     * 功能描述: 异步调用
     * @param:
     * @return:
     * @auther: wangfanming
     * @date: 2019/7/17 11:52
     */
    public Object get() throws Throwable {
        try {
            countDownLatch.await();
        }catch (InterruptedException e){
            e.printStackTrace();
        }

        if(state == STATE_SUCCESS){
            return result;
        }else if(state == STATE_EXCEPTION){
            throw  throwable;
        }else{
            throw new RuntimeException("RpcFuture Exception!");
        }
    }

    /**
     *
     * 功能描述: 同步调用
     * @param:
     * @return:
     * @auther: wangfanming
     * @date: 2019/7/17 11:51
     */
    public Object get(long timeout) throws Throwable {
        boolean awaitSuccess = true;
        try {
            awaitSuccess = countDownLatch.await(timeout, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if(! awaitSuccess){
            throw new RuntimeException();
        }

        if(state == STATE_SUCCESS){
            return result;
        }else if(state == STATE_EXCEPTION){
            throw  throwable;
        }else {
            throw new RuntimeException("RpcFuture Exception!");
        }

    }

    /**
     *
     * 功能描述: 
     * @param:
     * @return:
     * @auther: wangfanming
     * @date: 2019/7/17 11:03
     */
    public void setResult(Object result){
        this.result = result;
        state = STATE_SUCCESS;

        if(rpcFutureListener != null){
            rpcFutureListener.onException(throwable);
        }

        countDownLatch.countDown();
    }

    /**
     *
     * 功能描述:
     * @param:
     * @return: 
     * @auther: wangfanming
     * @date: 2019/7/17 11:05
     */
    public void setThrowable(Throwable throwable){
        this.throwable = throwable;
        state = STATE_EXCEPTION;
        
        if(rpcFutureListener != null){
            rpcFutureListener.onException(throwable);
        }
        
        countDownLatch.countDown();
    }

    public boolean isDone(){
        return state != STATE_AWAIT;
    }

    public void setRpcFutureListener(RpcFutureListener rpcFutureListener){
        this.rpcFutureListener = rpcFutureListener;
    }



















}
