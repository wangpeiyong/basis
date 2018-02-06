package org.wpy.gc;

import org.junit.Test;
import org.wpy.classloader.DiskClassLoader;

import java.lang.reflect.Method;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * DESC
 * <p>
 * Metaspace       used 2425K, capacity 4498K, committed 4864K, reserved 1056768K
 * class space   used 262K, capacity 386K, committed 512K, reserved 1048576K
 * <p>
 * 在开始的行中Metaspace，used值是用于加载类的空间量。该capacity值是当前分配的块中可用于元数据的空间。该committed值是可用于块的空间量。该reserved值是对元数据的空间保留（但不一定提交）的量。
 *
 * @author
 * @create 2017-07-24 下午1:28
 **/
public class FullGCTest {

    AtomicInteger count = new AtomicInteger(0);

    /**
     * -XX:+PrintGCDetails -Xms20M --XX:+PrintGCDetails -Xms20M
     * <p>
     * [Full GC (Ergonomics) [PSYoungGen: 496K->0K(6144K)] [ParOldGen: 9856K->8829K(13824K)] 10352K->8829K(19968K), [Metaspace: 5077K->5077K(1056768K)], 0.0193322 secs] [Times: user=0.03 sys=0.00, real=0.02 secs]
     * <p>
     * 老年代内存不足导致 full gc
     *
     * @throws InterruptedException
     */
    @Test
    public void test1() throws InterruptedException {
        List list = new ArrayList<Object>();
        for (int i = 0; i < 10000; i++) {
            byte[] tmp = new byte[1024];
            list.add(tmp);
        }
    }

    /**
     * 不同的类加载器加载相同的类，导致永久区内存溢出。
     * <p>
     * -XX:+PrintGCDetails  -XX:MetaspaceSize=5M -XX:MaxMetaspaceSize=7M
     * <p>
     * [Full GC (Metadata GC Threshold) [PSYoungGen: 960K->0K(38400K)] [ParOldGen: 1110K->1662K(61440K)] 2070K->1662K(99840K), [Metaspace: 8199K->8183K(1056768K)], 0.0197651 secs] [Times: user=0.03 sys=0.00, real=0.02 secs]
     * <p>
     * Metadata的Full GC内存溢出
     */
    @Test
    public void test2() {
        for (int i = 0; i < 100; i++) {
            //创建自定义classloader对象。
            DiskClassLoader diskLoader = new DiskClassLoader("/Users/wpy/Desktop/org/wpy/classloader");
            try {
                //加载class文件
                Class c = diskLoader.loadClass("org.wpy.classloader.NetClassLoaderSimple");
                if (c != null) {
                    try {
                        Object obj = c.newInstance();
                        Method method = c.getDeclaredMethod("say", null);
                        //通过反射调用Test类的say方法
                        method.invoke(obj, null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * intern在常量区得到内存，导致常量区内存溢出。
     * <p>
     * -XX:+PrintGCDetails  -XX:MetaspaceSize=5M -XX:MaxMetaspaceSize=7M
     * <p>
     * [Full GC (Metadata GC Threshold) [PSYoungGen: 232951K->79264K(313856K)] [ParOldGen: 302606K->439786K(718336K)] 535557K->519050K(1032192K), [Metaspace: 8654K->8654K(1056768K)], 8.4991824 secs] [Times: user=9.48 sys=1.05, real=8.50 secs]
     */
    @Test
    public void test3() {
        List list = new ArrayList<Object>();
        for (int i = 0; i < 10000000; i++) {
            list.add(String.valueOf(i).intern());
        }
    }

    /**
     * 设置线程每个栈大小：-Xss2M
     * 总的线程栈空间：   (MaxProcessMemory - JVMMemory - ReservedOsMemory)   2M * 2444 = 5G
     * <p>
     * <p>
     * 2028个线程，设置每个线程的最小内存为2M.当所有的线程正在运行的时候每个线程占2M。
     * -Xss2M -XX:+PrintGCDetails
     * java.lang.OutOfMemoryError: unable to create new native thread
     * <p>
     * (MaxProcessMemory - JVMMemory - ReservedOsMemory) / (ThreadStackSize) = Number of threads
     * MaxProcessMemory 指的是一个进程的最大内存
     * JVMMemory         JVM内存
     * ReservedOsMemory  保留的操作系统内存
     * ThreadStackSize      线程栈的大小
     * <p>
     * <p>
     * stackOverFlow的解决方案增大线程栈的大小（XSS）:    -Xss600K 增大线程栈的内存
     * 调用连的过长就会栈溢出、或者变量太多。
     */
    @Test
    public void test4() {
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            try {
                new Thread(() -> {
                    try {
                        toString();
                        Thread.sleep(5000);
                    } catch (StackOverflowError | Exception e) {
                        e.printStackTrace();
                        System.out.println("--------------" + count.get());
                        System.exit(0);
                    }
                }).start();
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
                System.out.println(i);
                System.exit(-1);
            }
        }
    }

    /**
     * [GC (System.gc()) [PSYoungGen: 643K->200K(38400K)] 41453K->41010K(125952K), 0.0128114 secs] [Times: user=0.05 sys=0.00, real=0.01 secs]
     * System.gc() 导致full gc的问题。
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void test5() throws ExecutionException, InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(4);
        List list = new ArrayList<Object>(10000000);
        LocalTime time = LocalTime.now();
        Future<Integer> future = service.submit(() -> {
            for (int i = 0; i < 20000000; i++) {
                byte[] tmp = new byte[1024];
                list.add(tmp);
            }
            return list.size();
        });
        service.submit(() -> {
            for (int i = 0; i < Integer.MAX_VALUE; i++) {
                list.clear();
            }
        });
        service.shutdown();
        System.out.println(time.until(LocalTime.now(), ChronoUnit.NANOS) + "   " + future.get());
    }

    /**
     * -XX:+UseParallelOldGC -XX:+PrintGCDetails
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void test6() throws ExecutionException, InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(4);
        List list = new ArrayList<Object>(10000000);
        LocalTime time = LocalTime.now();
        Future<Integer> future = service.submit(() -> {
            for (int i = 0; i < 20000000; i++) {
                byte[] tmp = new byte[1024];
                list.add(tmp);
            }
            return list.size();
        });
        service.submit(() -> {
            for (int i = 0; i < Integer.MAX_VALUE; i++) {
                list.clear();
            }
        });
        service.shutdown();
        System.out.println(time.until(LocalTime.now(), ChronoUnit.NANOS) + "   " + future.get());
    }

    public String toString() {
        if (count.incrementAndGet() >= 2600)
            return "sssss";
        return this.toString();
    }
}
