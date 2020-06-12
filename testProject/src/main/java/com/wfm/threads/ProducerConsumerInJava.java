package com.wfm.threads;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

/**
 * @author wangfanming
 * @version 1.0
 * @ClassName ProducerConsumerInJava
 * @Descripyion TODO
 * @date 2020/3/29 15:21
 */
public class ProducerConsumerInJava {
    public static void main(String[] args) {
        System.out.println("How to use wait and notify method in Java");
        System.out.println("Solving Producer Consumper Problem");
        LinkedList<Integer> buffer = new LinkedList<>();
        int maxSize = 10;
        Producer producer01 = new Producer(maxSize, buffer, "producer01");
        Consumer consumer01 = new Consumer("consumer01", maxSize, buffer);
        new Thread(producer01).start();
        new Thread(consumer01).start();

    }
}

class Producer implements Runnable{
    private String name;
    private int maxSize;
    private Queue<Integer> queue;

    public Producer(int maxSize, Queue<Integer> queue,String name) {
        this.name = name;
        this.maxSize = maxSize;
        this.queue = queue;
    }

    @Override
    public void run() {
        while (true){
            synchronized (queue){
                while (queue.size()==maxSize){  //之所以使用循环去检查队列是否已满，是为了防止在条件成立前，该线程被唤醒，而导致死锁
                    try {
                        System.out.println("Queue is full, " + "Producer thread waiting for " + "consumer to take something from queue");
                        queue.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Random random = new Random();
                int i = random.nextInt();
                System.out.println("Producing value :" + i);
                queue.add(i);
                queue.notifyAll();
            }
        }
    }
}

class Consumer implements Runnable{
    private String name;
    private int maxSize;
    private Queue<Integer> queue;

    public Consumer(String name, int maxSize, Queue<Integer> queue) {
        this.name = name;
        this.maxSize = maxSize;
        this.queue = queue;
    }

    @Override
    public void run() {
        while (true){
            synchronized (queue){
                while (queue.isEmpty()){
                    System.out.println("Queue is empty," + "Consumer thread is waiting" + " for producer thread to put something in queue");
                    try {
                        queue.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("Consuming value : " + queue.remove());
                queue.notifyAll();
            }
        }
    }
}