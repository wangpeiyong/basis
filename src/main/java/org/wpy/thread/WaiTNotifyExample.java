package org.wpy.thread;

import java.util.concurrent.CountDownLatch;

/**
 * DESC
 * <p>
 * 1、 wait、notify只能利用内部的synchronized同步对象做线程协助。
 * 2、 countDownLatch 使用非线程的同步对象的线程通信。
 *
 * @author
 * @create 2017-07-06 上午9:52
 **/
public class WaiTNotifyExample {

    public static void main(String[] args) {

        Byte lock = 1;

        new Thread(() -> {
            synchronized (lock) {
                try {
                    lock.wait();
                    Thread.sleep(1000);
                    throw new Exception("ZHONGDUANYICHANG");
                } catch (Exception e) {
                    e.printStackTrace();
                    lock.notify();
                }
            }
        }, "t1").start();


        new Thread(() -> {
            synchronized (lock) {
                try {
                    for (int i = 0; i < 10; i++) {
                        Thread.sleep(1000);
                        System.out.println(Thread.currentThread().getName() + i + "执行..........");
                        if (i == 5) {
                            lock.notify();
                            lock.wait();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, "t2").start();


        CountDownLatch countDownLatch = new CountDownLatch(2);


        new Thread(() -> {
            try {
                countDownLatch.await();
                throw new Exception("大家健康的數據反饋了時間地方就是大家立方");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, "t4").start();


        new Thread(() -> {
            try {
                for (int i = 0; i < 10; i++) {
                    Thread.sleep(10);
                    System.out.println(Thread.currentThread().getName() + i + "执行..........");
                    if (i == 5) {
                        System.out.println("countDown");
                        countDownLatch.countDown();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, "t3").start();


    }
}
