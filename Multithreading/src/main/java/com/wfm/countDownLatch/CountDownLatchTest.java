package com.wfm.countDownLatch;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Auther: wangfanming
 * @Date: 2019/7/18 16:21
 * @Description:
 */
public class CountDownLatchTest {
    public static void main(String[] args) throws InterruptedException {
        long now = System.currentTimeMillis();
        CountDownLatch countDownLatch = new CountDownLatch(2);
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.execute(new SeeDoctorTask(countDownLatch));
        executorService.execute(new QueueTask(countDownLatch));

        countDownLatch.await();

        System.out.println("over，回家 cost:"+(System.currentTimeMillis()-now));
    }
}
