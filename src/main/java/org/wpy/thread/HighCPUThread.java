package org.wpy.thread;

import java.util.Random;

/**
 * DESC   查找高耗能的cpu的系统净瓶
 * <p>
 * CPU高负载：
 * 1、cpu的大量的计算
 * 2、cpu的循环。
 * <p>
 * <p>
 * 查找高负载的方式：
 * 1、TOP得到cpu的高负载java进程id。
 * 2、pidstat 得到该进程下面的高耗能线程id。
 * 3、jstack得到java进程的的线程信息。
 * 4、将pidstat中的高耗能线程转为十六进制的线程id（OX....），查找jstack中的线程id。并得到高耗能线程的代码。
 * <p>
 * <p>
 * 异常线程：
 * <p>
 * "Thread-0" #10 prio=5 os_prio=31 tid=0x00007fa5ce0f1800 nid=0x4f03 runnable [0x0000700008f54000]
 * java.lang.Thread.State: RUNNABLE
 * <p>
 * PS: 1、最好所有的线程起名
 * 2、queue不能使用及时返回的循环，应该使用take、put、或者 poll(time)等
 * <p>
 * <p>
 * <p>
 * add        增加一个元索                     如果队列已满，则抛出一个IIIegaISlabEepeplian异常
 * remove   移除并返回队列头部的元素    如果队列为空，则抛出一个NoSuchElementException异常
 * element  返回队列头部的元素             如果队列为空，则抛出一个NoSuchElementException异常
 * offer       添加一个元素并返回true       如果队列已满，则返回false
 * poll         移除并返问队列头部的元素    如果队列为空，则返回null
 * peek       返回队列头部的元素             如果队列为空，则返回null
 * put         添加一个元素                      如果队列满，则阻塞
 * take        移除并返回队列头部的元素     如果队列为空，则阻塞
 *
 * @author
 * @create 2017-07-25 上午10:43
 **/
public class HighCPUThread {

    public static void main(String[] args) {
        new Thread(() -> {
            for (; true; ) {
                long num = new Random().nextInt(1000) * new Random().nextInt(10000);
                //System.out.println(num);
            }
        }).start();

    }
}
