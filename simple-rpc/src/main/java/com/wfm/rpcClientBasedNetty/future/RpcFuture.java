package com.wfm.rpcClientBasedNetty.future;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @Auther: wangfanming
 * @Date: 2019/7/17 10:48
 * @Description: 异步方式下会立即返回一个Future,可以通过调用isDone()方法判断结果是否已经返回，通过get()获取返回结果，也可以通过setRpcFutureListener()
 * 设置结果返回时的回调借口。
 */
public class RpcFuture {
    public final static int STATE_AWAIT = 0;
    public final static int STATE_SUCCESS = 1;
    public final static int STATE_EXCEPTION = 2;

    /*
       RpcFuture内部使用了CountDownLatch,初始值为1。只有在调用setResult()方法(正常返回了远程调用结果)或调用setThrowable()方法(调用过程中有异常发生)后，
       CountLatch才会减到0。在此之前，若设置了Listener的，将获得回调，若对RpcFuture对象调用get()方法将阻塞。
     */
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
     * 功能描述:
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
     * 功能描述:
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
