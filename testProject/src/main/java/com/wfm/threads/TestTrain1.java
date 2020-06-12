package com.wfm.threads;

import java.util.LinkedList;

/**
 * @author wangfanming
 * @version 1.0
 * @ClassName TestTrain1
 * @Descripyion TODO
 * @date 2020/3/29 11:44
 */
public class TestTrain1 {
    public static void main(String[] args) {
        Station1 station = new Station1();
        for (int i = 1; i < 5; i++) {
            Thread window = station.openWindow("窗口" + i);
            window.start();
        }

    }
}

class Station1 implements Runnable {
    public static volatile boolean isRun = true;
    private static int ticketNumber = 10;
    private static LinkedList<String> persons = new LinkedList<>();

    public Station1() {
        persons.add("赵海");
        persons.add("王瑞");
        persons.add("宋沙");
        persons.add("李伟");
        persons.add("徐晓");
    }

    //打开售票窗口开始卖票
    public Thread openWindow(String win) {
        return new Thread(this, win);
    }

    @Override
    public void run() {
        while (isRun) {  //没有进行同步操作，存在竞争访问共享数据 ticketNumber
            if (ticketNumber < 1) {
                System.out.println("火车票已售完...");
            } else {
                if (!persons.isEmpty()) {
                    String currentPerson = persons.poll();
                    try {
                        Thread.sleep(50);
                        System.out.println(currentPerson + " " + Thread.currentThread().getName() + "买了一张票,当前余票数量："+ --ticketNumber);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("排队的人已买完...");
                    isRun = false;
                }
            }
        }
    }
}
