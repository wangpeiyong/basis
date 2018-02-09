package org.wpy.atomic;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CyclicBarrier;

/**
 * Created by wpy on 2017/4/12.
 * <p>
 * ConcurrentHashMap 使用：
 * 1、put()方法（锁数组中的元素）
 * 1.1、先将key做hash，然后hash &（len -1）得到bash。
 * 1.2、利用UnSafe对新建bash的volicate变量（map中数组的元素）。
 * 1.3、map中的元素bash的链表修改。（synchronized(数组中元素) 对bash做同步）
 * 1.4、synchronized(数组中元素) 内实现同步更新，equals遍历比较，更新。
 * <p>
 * 2、get()方法 （得到map中数组的volicate元素）
 * 2.1、先将key做hash，然后hash &（len -1）得到bash。
 * 2.2、利用UnSafe对新建bash的volicate变量（得到map中数组的volicate元素）。
 * 2.3、比较equals，返回。（并没有加锁，也有可能时延得不到值）。
 * <p>
 * 3、size()方法
 * 3.1、利用UnSafe CAS对baseCount统计
 * 3.2、利用UnSafe Cell对cellCount统计.
 * 3.3、baseCount + cellCount实现size统计。
 */
public class TestConcurrentMap {


    /**
     * 实现两个任务的并发执行
     */
    private static final CyclicBarrier cyclicBarrier = new CyclicBarrier(2);


    private static ConcurrentHashMap<Long, Long> concurrentHashMap = new ConcurrentHashMap<>();

    public static void main(String[] args) throws Exception {


        /**
         * 1、remove不存在的不会出现异常
         */
        System.out.println(concurrentHashMap.remove(1L));

        for (int i = 0; i < 5; i++) {
            concurrentHashMap.put((long) i, (long) i);
        }

        new Thread(() -> {
            try {
                cyclicBarrier.await();
                concurrentHashMap.remove(1L);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();


        /**
         * map移除元素，keyset也会改变
         */
        new Thread(() -> {
            try {
                Set<Long> keys = concurrentHashMap.keySet();
                keys.forEach(System.out::print);
                cyclicBarrier.await();
                concurrentHashMap.remove(1L);
                System.out.println();
                keys.forEach(System.out::print);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();


        System.out.println("OVER");
    }
}
