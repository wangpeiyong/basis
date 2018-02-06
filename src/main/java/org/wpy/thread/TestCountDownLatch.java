package org.wpy.thread;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * java.util.concurrent.CountDownLatch#await()：(AQS队列阻塞同步实现)
 * <p>
 * 尝试无阻塞CAS status得到锁：
 * java.util.concurrent.CountDownLatch.Sync#tryReleaseShared   nextc == 0
 * <p>
 * AQS得到CAS 线程安全和 locksupport的线程park：
 * java.util.concurrent.locks.AbstractQueuedSynchronizer#doAcquireSharedInterruptibly
 * <p>
 * <p>
 * <p>
 * java.util.concurrent.CountDownLatch.Sync#tryReleaseShared
 * CAS 尝试释放锁：
 * java.util.concurrent.locks.AbstractQueuedSynchronizer#state
 * private volatile int state;
 * <p>
 * <p>
 * AQS队列中拿出一个线程信息释放，并利用status CAS防止并发。
 * java.util.concurrent.locks.AbstractQueuedSynchronizer.Node
 * java.util.concurrent.locks.AbstractQueuedSynchronizer#unparkSuccessor（node）
 */
public class TestCountDownLatch {

    public static void main(String[] args) {
        final CountDownLatch countDownLatch = new CountDownLatch(2);


        ExecutorService executorService = Executors.newFixedThreadPool(4);
        executorService.execute(() -> {
            try {
                countDownLatch.await();
                System.out.println("await over");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });


        executorService.execute(() -> {
            for (int i = 0; i < 4; i++) {
                countDownLatch.countDown();
                System.out.println("countDown");

            }
        });

        executorService.shutdown();

    }
}
