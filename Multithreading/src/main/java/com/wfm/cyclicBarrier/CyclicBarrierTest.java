package com.wfm.cyclicBarrier;

import java.util.concurrent.*;

/**
 * @Auther: wangfanming
 * @Date: 2019/7/22 09:43
 * @Description:
 */
public class CyclicBarrierTest {
    public static void main1(String[] args)
    {
        Runnable runnable = new Runnable()
        {
            public void run()
            {
                System.out.println("CyclicBarrier的所有线程await()结束了，我运行了, 时间为" + System.currentTimeMillis());
            }
        };
        CyclicBarrier cb = new CyclicBarrier(3, runnable);
        CyclicBarrierThread cbt0 = new CyclicBarrierThread(cb, 3);
        CyclicBarrierThread cbt1 = new CyclicBarrierThread(cb, 6);
        CyclicBarrierThread cbt2 = new CyclicBarrierThread(cb, 9);
        cbt0.start();
        cbt1.start();
        cbt2.start();
    }
    /*
    * 从运行结果看，由于是同一个CyclicBarrier，Thread-0先运行到了await()的地方，等着；Thread-2接着运行到了await()的地方，
    * 还等着；Thread-1最后运行到了await()的地方，所有的线程都运行到了await()的地方，所以三个线程以及指定的Runnable"同时"运行后面的代码，
    * 可以看到，await()之后，四个线程运行的时间一模一样，都是1563759863681
    *
    * */

    public static class CallableThread implements Callable<String>
    {
        public String call() throws Exception
        {
            System.out.println("进入CallableThread的call()方法, 开始睡觉, 睡觉时间为" + System.currentTimeMillis());
            Thread.sleep(10000);
            return "123";
        }
    }

    public static void main(String[] args) throws Exception
    {
        ExecutorService es = Executors.newCachedThreadPool();
        CallableThread ct = new CallableThread();
        Future<String> f = es.submit(ct);
        es.shutdown();

        Thread.sleep(5000);
        System.out.println("主线程等待5秒, 当前时间为" + System.currentTimeMillis());

        String str = f.get();
        System.out.println("Future已拿到数据, str = " + str + ", 当前时间为" + System.currentTimeMillis());
    }


}
