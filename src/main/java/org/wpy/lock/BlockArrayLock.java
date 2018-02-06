package org.wpy.lock;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 * Created by wpy on 2017/3/22.
 * <p>
 * 阻塞的队列容器
 */
@SuppressWarnings("all")
public class BlockArrayLock<T> {

    /**
     * 线程同步锁
     */
    private Lock lock = new ReentrantLock(Boolean.TRUE);
    private Condition condition = lock.newCondition();


    /**
     * 存放数据
     */
    private List<T> list;
    private AtomicInteger readCurrent;
    private AtomicInteger total;
    private Integer max;
    private AtomicInteger size;


    public BlockArrayLock(Integer max) {
        this.list = new ArrayList<T>(max);
        this.readCurrent = new AtomicInteger(0);
        this.max = max;
        this.total = new AtomicInteger(0);
        this.size = new AtomicInteger(0);
    }

    public static void main(String[] args) throws Exception {
        final BlockArrayLock<Integer> arrayLock = new BlockArrayLock<>(4);

        ExecutorService executorService = Executors.newFixedThreadPool(2);
        /*executorService.execute(()->{
            try {
                for (int i = 0; i < 5 ; i++) {
                    arrayLock.addElement(i);
                }
            }catch (Exception _e){
            }
        });

       executorService.execute(()->{
            try {
                for (int i = 0; i < 5 ; i++) {
                    arrayLock.getElement();
                }
            }catch (Exception _e){
            }
        });*/


        executorService.execute(() -> {
            try {
                arrayLock.longTimeLock(6);
            } catch (Exception _e) {

            }
        });

        executorService.execute(() -> {
            try {
                arrayLock.testTryLock(1);
            } catch (Exception _e) {

            }
        });


        Thread thread = new Thread(() -> {
            try {
                arrayLock.testInterruptLock();
            } catch (Exception _e) {

            }
        });
        thread.start();
        thread.interrupt();


        Thread.sleep(4000);
        executorService.shutdown();
    }

    public Object getElement(long time) throws Exception {
        if (lock.tryLock(time, TimeUnit.SECONDS)) {
            try {
                if (size.get() <= 0)
                    condition.await();
                T t = list.get(readCurrent.get() % max);
                System.out.println(String.format("getElement : %s %s", readCurrent.get() % max, t));
                readCurrent.incrementAndGet();
                size.decrementAndGet();
                condition.signal();
                return t;
            } finally {
                lock.unlock();
            }
        }
        return null;
    }

    public Object getElement() throws Exception {
        lock.lock();
        try {
            if (size.get() <= 0)
                condition.await();
            T t = list.get(readCurrent.get() % max);
            System.out.println(String.format("getElement : %s %s", readCurrent.get() % max, t));
            readCurrent.incrementAndGet();
            size.decrementAndGet();
            condition.signal();
            return t;
        } finally {
            lock.unlock();
        }
    }

    public void addElement(T o) throws Exception {
        lock.lock();
        try {
            if (size.get() >= max)
                condition.await();
            list.add(total.get() % max, o);
            size.incrementAndGet();
            total.incrementAndGet();
            condition.signal();
        } finally {
            lock.unlock();
        }
    }

    public boolean addElement(T o, long time) throws Exception {
        if (lock.tryLock(time, TimeUnit.SECONDS)) {
            try {
                if (size.get() >= max)
                    condition.await();
                list.add(total.get() % max, o);
                size.incrementAndGet();
                total.incrementAndGet();
                condition.signal();
                return true;
            } finally {
                lock.unlock();
            }
        }
        return false;
    }

    /**
     * time时间内不能得到锁就不能执行
     *
     * @param time
     * @return
     * @throws Exception
     */
    public boolean testTryLock(long time) throws Exception {
        if (lock.tryLock(time, TimeUnit.SECONDS)) {
            try {
                System.out.println("testTryLock");
            } finally {
                lock.unlock();
            }
        }
        return false;
    }

    /**
     * 一次尝试能否得到lock，不能得到将不能返回false。
     *
     * @return
     * @throws Exception
     */
    public boolean testTryLock() throws Exception {
        if (lock.tryLock()) {
            try {
                System.out.println("testTryLock");
            } finally {
                lock.unlock();
            }
        }
        return false;
    }

    /**
     * 尝试获取锁，线程在成功获取锁之前被中断，则放弃获取锁，抛出异常.
     * PS: 线程中断将不会等待获取锁。thread.interrupt()
     *
     * @throws Exception
     */
    public void testInterruptLock() throws Exception {
        lock.lockInterruptibly();
        try {
            System.out.println("testInterruptLock");
        } finally {
            lock.unlock();
        }
    }

    /**
     * 线程长时间sleep，获取该锁的线程block状态
     *
     * @param time
     * @throws Exception
     */
    public void longTimeLock(long time) throws Exception {
        lock.lock();
        try {
            Thread.sleep(time * 1000);
            System.out.println("longTimeLock");
        } finally {
            lock.unlock();
        }
    }
}
