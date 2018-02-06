package org.wpy.concurrent.book;

import org.junit.Test;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;


/**
 * DESC  Semaphore 控制并发数。
 *
 * @author
 * @create 2017-07-18 上午11:12
 **/
public class SemaphoreTest {


    public static void ThreadPool(Consumer<ExecutorService> consumer) {
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        consumer.accept(executorService);
        executorService.shutdown();
    }

    /**
     * Java8 内置的四大核心函数式接口
     * <p>
     * Consumer<T> : 消费型接口       void accept(T t);
     * <p>
     * Supplier<T> : 供给型接口       T get();
     * <p>
     * Function<T, R> : 函数型接口    R apply(T t);
     * <p>
     * Predicate<T> : 断言型接口     boolean test(T t);
     */
    @Test
    public void method1() {
        Semaphore semaphore = new Semaphore(2, true);
        ThreadPool(executorService1 -> {
            for (int i = 0; i < 3; i++) {
                executorService1.submit(() -> {
                    try {
                        semaphore.acquire();
                        System.out.println("----------进入锁----------");
                        Thread.sleep(new Random().nextInt(3000));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        semaphore.release();
                        System.out.println("----------释放锁----------");
                    }
                });
            }
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }


    @Test
    public void method2() {
        Semaphore semaphore = new Semaphore(2, true);
        ThreadPool(executorService -> {
            for (int i = 0; i < 3; i++) {
                executorService.submit(() -> {
                    try {
                        semaphore.acquire(2);
                        System.out.println("----------进入锁----------");
                        Thread.sleep(new Random().nextInt(1000));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        semaphore.release(2);
                        System.out.println("----------释放锁----------");
                    }
                });
            }
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }


    @Test
    public void method3() {
        Semaphore semaphore = new Semaphore(2, true);
        ThreadPool(executorService -> {
            for (int i = 0; i < 3; i++) {
                executorService.submit(() -> {
                    try {
                        if (semaphore.tryAcquire()) {
                            System.out.println("----------进入锁----------");
                            Thread.sleep(new Random().nextInt(1000));
                            semaphore.release();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        System.out.println("----------释放锁----------");
                    }
                });
            }
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    @Test
    public void method4() {
        Semaphore semaphore = new Semaphore(2, true);
        ThreadPool(executorService -> {
            for (int i = 0; i < 3; i++) {
                executorService.submit(() -> {
                    try {
                        if (semaphore.tryAcquire(2, 1, TimeUnit.SECONDS)) {
                            System.out.println("----------进入锁----------");
                            Thread.sleep(new Random().nextInt(1000));
                            semaphore.release(2);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        System.out.println("----------释放锁----------");
                    }
                });
            }
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }


    @Test
    public void method5() {

    }

}

class ResourcePool {

    private ReentrantLock reentrantLock = new ReentrantLock(true);
    private String[] resource = {"resource-1", "resource-2", "resource-3"};
    private Semaphore availableResourceTotal = new Semaphore(resource.length);

    public String getResource() {
        try {
            availableResourceTotal.acquire();
            reentrantLock.lock();
            Thread.sleep(3000);
            System.out.println("---------获得资源----------");
            return "resource";
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            reentrantLock.unlock();
        }
        return null;
    }


    public void releaseResource() {
        try {
            reentrantLock.lock();
            availableResourceTotal.release();
            Thread.sleep(3000);
            System.out.println("---------释放资源----------");
        } catch (Exception e) {
            reentrantLock.unlock();
        }
    }


}