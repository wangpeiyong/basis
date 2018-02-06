package org.wpy.queue;


import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

/**
 * DESC
 * <p>
 * 1、offer() （time有无时间内）有无资源插入。无资源则返回false。成功则返回true。
 * 2、add()    直接插入元素，无资源报出异常
 * 3、poll（）  （time时间内），轮训得到队列中元素。推荐设置时间，不然耗尽资源
 * 4、peek()    查询元素，但不删除
 * 5、take（）   得到队列首的元素
 * 6、remove（） 删除队列首的元素
 *
 * @author
 * @create 2017-07-07 上午11:51
 **/
public class MultiQueueTest {

    public static void main(String[] args) throws Exception {
        Map<String, Integer> data = new HashMap<String, Integer>(10);
        data.put("wang", 1);
        data.forEach((K, V) -> System.out.println(K + V));


    }


    /**
     * offer 阻塞增加，无资源则返回false  、                add 直接增加(无资源抛出异常)
     * poll  直接返回，无为null，可设置等待时间 、           take 阻塞等待。
     *
     * @param args
     */
    public static void LinkedBlockingQueueTest(String[] args) {
        LinkedBlockingQueue linkedBlockingQueue = new LinkedBlockingQueue();
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        executorService.submit(() -> {
            for (int i = 0; ; i++) {
                try {
                    Thread.sleep(1000);
                    System.out.println("----------放入---------");
                    // offer 阻塞增加  、 add 直接增加
                    linkedBlockingQueue.add(i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        executorService.submit(() -> {
            for (; ; )
                // poll 直接返回，无为null 、   take 阻塞等待。
                System.out.println(linkedBlockingQueue.take());
        });
    }

    /**
     * ArrayBlockingQueueTest
     * offer \ take 设置等待时间，推荐使用。
     *
     * @throws Exception
     */
    public static void ArrayBlockingQueueTest() throws Exception {
        ArrayBlockingQueue<Integer> queue = new ArrayBlockingQueue<Integer>(10);
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        executorService.submit(() -> {
            for (int i = 0; ; i++) {
                try {
                    Thread.sleep(500);
                    System.out.println(queue.offer(i, 1L, TimeUnit.SECONDS));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        executorService.submit(() -> {
            for (; ; Thread.sleep(2000))
                System.out.println(queue.poll(1L, TimeUnit.SECONDS));
        });
    }


    /**
     * 得到优先队列
     * put 、poll 、take 类poll实现排序Comparable得到最新的第一个数。
     *
     * @throws Exception
     */
    public static void PriorityBlockingQueueTest() throws Exception {
        PriorityBlockingQueue<Student> queue = new PriorityBlockingQueue<Student>();

        for (int i = 0; i < 5; i++) {
            try {
                Thread.sleep(500);
                Student student = new Student("wang", 24 - i);
                queue.add(student);
                System.out.println(student + " 开始排队...");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        while (true) {
            /*System.out.println(queue.take());*/
            System.out.println(queue.poll(1L, TimeUnit.SECONDS));
        }
    }

    /**
     * DelayQueueTest :
     * 延迟队列：delay接口。
     *
     * @throws Exception
     */
    public static void DelayQueueTest() throws Exception {
        DelayQueue<DelayMsg> queue = new DelayQueue<>();
        for (int i = 0; i < 5; i++) {
            try {
                Thread.sleep(500);
                DelayMsg student = new DelayMsg((System.currentTimeMillis() + (1000 * i)), "wang");
                queue.add(student);
                System.out.println(student + " 开始排队...");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        while (true) {
            System.out.println(queue.take());
        }
    }


}

class Student implements Comparable<Student> {
    private String name;
    private Integer age;

    public Student(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public int compareTo(Student o2) {
        return Integer.compare(this.age, o2.age);
    }

    @Override
    public String toString() {
        return String.format("name:%s  age:%s", this.name, this.age);
    }
}

class DelayMsg implements Delayed {

    private long endTime;
    private String msg;

    public DelayMsg(long endTime, String msg) {
        this.endTime = endTime;
        this.msg = msg;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(this.endTime - System.currentTimeMillis(), TimeUnit.MICROSECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        return Long.compare(this.endTime, ((DelayMsg) o).endTime);
    }

    @Override
    public String toString() {
        return "DelayMsg{" + "endTime=" + endTime + ", msg='" + msg + '\'' + '}';
    }
}
