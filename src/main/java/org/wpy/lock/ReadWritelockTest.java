package org.wpy.lock;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * DESC
 *
 * @author
 * @create 2017-07-13 下午4:41
 **/
public class ReadWritelockTest {
    private static ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock(false);
    private static Lock readLock = readWriteLock.readLock();
    private static Lock writeLock = readWriteLock.writeLock();

    private static String context = "----------context---------";


    private static void read() {
        readLock.lock();
        try {
            Thread.sleep(3000);
            System.out.println(context);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            readLock.unlock();
        }
    }


    private static void write() {
        writeLock.lock();
        System.out.println("----进入锁------");
        try {
            Thread.sleep(3000);
            System.out.println("----写入------");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("----释放锁------");
            writeLock.unlock();
        }
    }


    private static void tryRead() throws Exception {
        if (readLock.tryLock(10, TimeUnit.SECONDS)) {
            try {
                Thread.sleep(3000);
                System.out.println(context);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                readLock.unlock();
            }
        }

    }


    private static void tryWrite() throws Exception {
        if (writeLock.tryLock(10, TimeUnit.SECONDS)) {
            System.out.println("----进入锁------");
            try {
                Thread.sleep(3000);
                System.out.println("----写入------");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                System.out.println("----释放锁------");
                writeLock.unlock();
            }
        }
    }

    public static void main(String[] args) throws Exception {
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        executorService.submit(() -> read());
        executorService.submit(() -> read());
        executorService.submit(() -> write());
        executorService.submit(() -> write());

        executorService.submit(() -> {
            try {
                tryRead();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        executorService.submit(() -> {
            try {
                tryRead();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        executorService.submit(() -> {
            try {
                tryWrite();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        executorService.submit(() -> {
            try {
                tryWrite();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        executorService.shutdown();
    }

}
