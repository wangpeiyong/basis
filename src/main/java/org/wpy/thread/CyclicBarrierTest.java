package org.wpy.thread;

import java.util.Random;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by wpy on 2017/7/17.   起跑线
 * <p>
 * PS:  线程数 >= CyclicBarrier数目
 * <p>
 * 阻塞保持n个并发 (循环阻塞)
 * <p>
 * <p>
 * java.util.concurrent.CyclicBarrier#dowait实现：（重入锁线程的阻塞同步）
 * <p>
 * java.util.concurrent.locks.ReentrantLock 重入锁中count减1
 * <p>
 * 线程中放入阻塞Blocker为AQS
 * java.util.concurrent.locks.LockSupport#setBlocker
 * <p>
 * count==0
 * java.util.concurrent.CyclicBarrier#barrierCommand CyclicBarrier运行的函数，实现观察者模式，完成后的任务。
 * java.util.concurrent.CyclicBarrier#breakBarrier   AQS唤醒阻塞线程。count = 初始值
 */
public class CyclicBarrierTest {

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        barrier1(executorService);
        executorService.shutdown();
    }

    public static void barrier1(ExecutorService executorService) {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(2);
        for (int i = 0; i < 5; i++) {
            task(executorService, cyclicBarrier);
        }
    }

    public static void task(ExecutorService executorService, CyclicBarrier cyclicBarrier) {
        executorService.submit(() -> {
            try {
                Thread.sleep(new Random().nextInt(5000));
                cyclicBarrier.await();
                System.out.println(Thread.currentThread().getName() + ": 准备好了！NumberWaiting:" + cyclicBarrier.getNumberWaiting());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }


    public static void barrier2(ExecutorService executorService) {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(2, () -> System.out.println("--------------let us go ！-------------------"));
        for (int i = 0; i < 6; i++) {
            task(executorService, cyclicBarrier);
        }
    }
}
