package org.wpy.lock;

import java.util.concurrent.locks.LockSupport;


/**
 * LockSupport
 * <p>
 * unsafe 实现线程的定时阻塞、手动释放
 * <p>
 * 1、park() 定时线程挂起
 * 2、unpark() 线程在定时休眠中被唤醒。手动唤醒挂起线程。
 * <p>
 * 比sleep强大。以后不要用sleep了。不够灵活。
 */
public class UnSafeLock {

    public static void main(String[] args) throws InterruptedException {

        ThreadPark threadPark = new ThreadPark();
        threadPark.start();
        ThreadUnPark threadUnPark = new ThreadUnPark(threadPark);
        threadUnPark.start();
        //等待threadUnPark执行成功
        threadUnPark.join();
        System.out.println("运行成功....");
    }


    static class ThreadPark extends Thread {

        public void run() {
            System.out.println(Thread.currentThread() + "我将被阻塞在这了60s....");

            //阻塞60s，单位纳秒  1s = 1000000000
            LockSupport.parkNanos(1000000000L * 60);

            System.out.println(Thread.currentThread() + "我被恢复正常了....");
        }
    }

    static class ThreadUnPark extends Thread {

        public Thread thread = null;

        public ThreadUnPark(Thread thread) {
            this.thread = thread;
        }

        public void run() {

            System.out.println("提前恢复阻塞线程ThreadPark");

            //恢复阻塞线程
            LockSupport.unpark(thread);
        }
    }

}
