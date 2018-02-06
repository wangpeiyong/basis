package org.wpy.lock;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by wpy on 2017/3/23.
 * <p>
 * ReentrantLock 重入锁、  ReadWriteLock 读写锁
 * <p>
 * signal:唤醒block时间最长的线程。
 */
public class TestSomeLocks<T> {

    private ReentrantLock lock = new ReentrantLock(Boolean.TRUE);
    private ReadWriteLock readWriteLock = new ReentrantReadWriteLock(Boolean.TRUE);
    private Lock readLock = readWriteLock.readLock();
    private Lock writeLock = readWriteLock.writeLock();
    private AtomicInteger current;
    private List<T> data;

    public TestSomeLocks(int size) {
        this.data = new ArrayList<T>(size);
        this.current = new AtomicInteger(0);
    }

    public static void main(String[] args) throws Exception {
        final TestSomeLocks<Integer> testSomeLocks = new TestSomeLocks<>(5);

        ExecutorService executorService = Executors.newFixedThreadPool(3);

        List<Future<Integer>> futures = new ArrayList<>(5);

        futures.add(executorService.submit(() -> {
            try {
                testSomeLocks.addElement(2);
            } catch (Exception _e) {
                _e.printStackTrace();
            }
            return 2;
        }));

        futures.add(executorService.submit(() -> {
            try {
                testSomeLocks.addElement(1);
            } catch (Exception _e) {
                _e.printStackTrace();
            }
            return 1;
        }));

        futures.add(executorService.submit(() -> {
            try {
                testSomeLocks.addElement(3);
            } catch (Exception _e) {
                _e.printStackTrace();
            }
            return 3;
        }));


        futures.add(executorService.submit(() -> {
            try {
                testSomeLocks.getElement();
            } catch (Exception _e) {
                _e.printStackTrace();
            }
            return 4;
        }));


        futures.add(executorService.submit(() -> {
            try {
                testSomeLocks.getElement();
            } catch (Exception _e) {
                _e.printStackTrace();
            }
            return 5;
        }));

        futures.add(executorService.submit(() -> {
            try {
                testSomeLocks.getElement();
            } catch (Exception _e) {
                _e.printStackTrace();
            }
            return 6;
        }));


        futures.stream().forEach((future) -> {
            try {
                //System.out.printf("  %s",future.get());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });


        futures.add(executorService.submit(() -> {
            try {
                testSomeLocks.addElementByReentrantLock(7);
            } catch (Exception _e) {
                _e.printStackTrace();
            }
            return 7;
        }));


        Thread.sleep(6000);
        executorService.shutdown();
    }

    /**
     * 读锁： 允许多个线程同时得到锁（共享锁）
     *
     * @return
     */
    public T getElement() throws Exception {
        readLock.lock();
        try {
            T o = data.remove(current.decrementAndGet());
            System.out.printf(" getElement %s\n", o);
            Thread.sleep(2000);
            return o;
        } finally {
            readLock.unlock();
        }
    }

    /**
     * 写锁： 只允许一个线程得到锁（排他锁）
     *
     * @param o 加入的元素
     */
    public void addElement(T o) throws Exception {
        writeLock.lock();
        try {
            System.out.printf(" addElement %s \n", o);
            Thread.sleep(2000);
            current.incrementAndGet();
            data.add(o);
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * 重入锁： 只允许一个线程同时得到锁（排他锁）
     *
     * @param o
     * @throws Exception
     */
    public void addElementByReentrantLock(T o) throws Exception {
        lock.lock();
        try {
            System.out.printf(" addElementByReentrantLock %s \n", o);
            Thread.sleep(2000);
            current.incrementAndGet();
            data.add(o);
        } finally {
            lock.unlock();
        }
    }
}
