package org.wpy.lock;

import java.util.concurrent.locks.ReentrantLock;

/**
 * DESC   AQS + unsafe来实现同步[cas实现]
 * <p>
 * 1、重入锁中的java.util.concurrent.locks.AbstractQueuedSynchronizer，
 * 2、利用unsafe中private volatile int state的CAS来实现非锁的更新。
 * 3、任务队列head、tail（链表）unsafe.compareAndSwapObject(this, headOffset、tailOffset, null, update); head、tail的CAS更新。
 * 4、任务队列中保存线程信息。
 * 5、开始tryLock、未得到线程就去休眠线程park。
 * 6、当有人释放线程时候，处理任务队列中的等待unpark。
 *
 * @author
 * @create 2017-05-04 下午5:36
 **/
public class ReentrantLockTest {
    private static ReentrantLock lock = new ReentrantLock(Boolean.TRUE);   //同一个对象的重入锁，可以防止任何对象冲入     类似： synchronized(obj)

    public static void main(String[] a) {
        new Thread(() ->
                new ReentrantLockTest().printLock()
                , "Thread-1").start();
        new ReentrantLockTest().printLock();

    }

    public void printLock() {
        // TODO: 2017/5/4 异常没有得到锁的话就不需要释放锁
        lock.lock();
        System.out.println(Thread.currentThread().getName() + "得到锁");

        try {
            Thread.sleep(12222);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
            System.out.println(Thread.currentThread().getName() + "释放锁");
        }
    }

}
