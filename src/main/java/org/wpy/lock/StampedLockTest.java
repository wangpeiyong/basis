package org.wpy.lock;

import org.junit.Test;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.StampedLock;

/**
 * DESC    JDK8更优化的读写锁
 *
 * @author
 * @create 2017-07-27 上午10:46
 **/
public class StampedLockTest {

    private final StampedLock stampedLock = new StampedLock();
    private AtomicInteger count = new AtomicInteger(0);

    /**
     * 读操作：
     * 1、先获取乐观锁。当乐观锁有效的时候，直接得到处理计算。
     * 2、当乐观锁无效时候，再获取读锁，处理计算。
     * 3、乐观锁不需要释放结果。
     *
     * @return
     */
    public int read() {
        long stamped = stampedLock.tryOptimisticRead();
        if (!stampedLock.validate(stamped)) {
            stamped = stampedLock.readLock();
            try {
                System.out.println("r");
                return count.get();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                stampedLock.unlockRead(stamped);
            }
        }
        return count.get();
    }


    public void write(int num) {
        long stamped = stampedLock.writeLock();
        try {
            System.out.println("w");
            count.set(num);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            stampedLock.unlockWrite(stamped);
        }
    }


    @Test
    public void testReadWriteLock() throws InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(10);
        StampedLockTest stampedLockTest = new StampedLockTest();
        final CyclicBarrier cyclicBarrier = new CyclicBarrier(10);
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            service.submit(() -> {
                stampedLockTest.write(random.nextInt(100));
            });
        }

        for (int i = 0; i < 10; i++) {
            service.submit(() -> {
                try {
                    //System.out.println(cyclicBarrier.getParties());
                    cyclicBarrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
                System.out.println(stampedLockTest.read());
            });
        }

        service.shutdown();
    }

}
