package org.wpy.thread;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * DESC    很多次重试才能测试出线程同步的问题
 *
 * @author
 * @create 2017-07-19 上午9:31
 **/
public class ThreadVisibilityTest {


    @Test
    public void final1() throws InterruptedException {
        final Data data = new Data("wang", 0);
        CyclicBarrier cyclicBarrier = new CyclicBarrier(10);
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10000; i++) {
            executorService.submit(() -> {
                try {
                    cyclicBarrier.await();
                    data.incAge();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            });
        }
        Thread.sleep(5000);
        executorService.shutdown();
        System.out.println(data.getAge());
    }


    @Test
    public void final2() throws InterruptedException {
        List<IncrementAge> ages = new ArrayList<>(10);
        Data data = new Data("wang", 0);
        CyclicBarrier cyclicBarrier = new CyclicBarrier(10);
        for (int i = 0; i < 10000; i++) {
            IncrementAge age = new IncrementAge();
            age.setData(data);
            age.setCyclicBarrier(cyclicBarrier);
            ages.add(age);
        }
        ages.forEach(a -> a.start());
        Thread.sleep(5000);
        System.out.println(data.getAge());
        System.out.println(Common2.num);
    }


    @Test
    public void final3() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10000; i++) {
            executorService.submit(new ComonThread());   //100000 * 10
        }
        Thread.sleep(5000);
        executorService.shutdown();
        System.out.println(Comon.num);
        //2379697
        //2682942
    }


}

class IncrementAge extends Thread {

    private Data data;
    private CyclicBarrier cyclicBarrier;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public CyclicBarrier getCyclicBarrier() {
        return cyclicBarrier;
    }

    public void setCyclicBarrier(CyclicBarrier cyclicBarrier) {
        this.cyclicBarrier = cyclicBarrier;
    }

    @Override
    public void run() {
        try {
            //cyclicBarrier.await();
            for (int i = 0; i < 10; i++) {
                System.out.println(data.incAge());
                Common2.num += 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class Data {
    private String name;
    private int age;

    public Data(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public Data() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    //synchronized 中代码保证了线程的可见性和原子性
    public int incAge() {
        this.age = ++age;
        return age;
    }
}

class Common2 {
    public static int num = 0;
}