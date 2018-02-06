package org.wpy.thread;

import org.junit.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.*;

/**
 * 主线程和分支线程的关系：（主线程必须在分支线程开启后才能结束。主线程等待分支线程结束）
 * <p>
 * 1、匿名内部类，当主函线程结束还会运行
 * 2、外部线程，当主线程结束可能分支线程并没有开启。
 */
public class PriorityMultiThread {

    @Test
    public void test1() throws InterruptedException {
        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(2, 4, 20, TimeUnit.SECONDS, new PriorityBlockingQueue<Runnable>());
        for (int i = 0; i < 100; i++)
            poolExecutor.execute(new Task());
        Thread.sleep(2000);
        poolExecutor.shutdown();
    }


    @Test
    public void test2() throws InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(4);
        for (int i = 0; i < 100; i++)
            service.submit(new Task());
        service.shutdown();
    }


    @Test
    public void test3() throws InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(4);
        for (int i = 0; i < 100; i++)
            service.submit(() -> System.out.print("-------------"));
        service.shutdown();
    }


    @Test
    public void test4() throws InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(4);
        for (int i = 0; i < 100; i++)
            service.submit(new Callable() {
                @Override
                public Object call() throws Exception {
                    System.out.print("-------------");
                    return null;
                }
            });
        service.shutdown();
    }


    @Test
    public void test5() throws InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(4);
        for (int i = 0; i < 100; i++)
            service.submit(new Runnable() {
                @Override
                public void run() {
                    System.out.print("-------------");
                }
            });
        service.shutdown();
    }

    @Test
    public void test6() throws InterruptedException {
        System.out.println("ABC" == "ABC");
        System.out.println("ABC" == new String("ABC"));
        System.out.println("ABC" == new String("ABC").intern());
    }


}

class Task implements Runnable, Comparable<Task> {

    @Override
    public int compareTo(Task o) {
        return Integer.compare(this.hashCode(), o.hashCode());
    }

    @Override
    public void run() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        System.out.println(Thread.currentThread().getName() + LocalDateTime.now().format(formatter));
    }
}
