package com.wfm.countDownLatch;

import java.util.concurrent.CountDownLatch;

/**
 * @Auther: wangfanming
 * @Date: 2019/7/18 16:15
 * @Description:
 */
public class SeeDoctorTask implements Runnable{

    private CountDownLatch countDownLatch ;

    public SeeDoctorTask(CountDownLatch countDownLatch){
        this.countDownLatch = countDownLatch;
    }

    public void run() {
        try {
            Thread.sleep(3000);
            System.out.println("看大夫成功，大夫给开了些药单子");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if(countDownLatch != null) {
                countDownLatch.countDown();
            }
        }
    }
}
