package com.wfm.volatileTest;

import java.util.concurrent.atomic.AtomicInteger;

public class VolatileTest {
//    public static AtomicInteger num = new AtomicInteger(0);
    public static volatile int num = 0;
    public static void main(String[] args) {
        for (int i = 0; i< 100; i++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for(int i =0;i<1000;i++){
//                        num.getAndIncrement();
                        num++;
                    }
                }
            }).start();
        }
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(num);
    }

}
