package com.wfm.countDownLatch;

import java.util.concurrent.CountDownLatch;

/**
 * @Auther: wangfanming
 * @Date: 2019/7/18 16:17
 * @Description:
 */
public class QueueTask implements Runnable {
    private CountDownLatch countDownLatch;

    public QueueTask(CountDownLatch countDownLatch){
        this.countDownLatch = countDownLatch;
    }

    public void run() {
        try {
            Thread.sleep(5000);
            System.out.println("排队成功，可以开始交费");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if(countDownLatch != null) {
                countDownLatch.countDown();
            }
        }
    }
}
