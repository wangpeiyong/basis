package org.wpy.threadlocal;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by wpy on 2017/3/22.
 * <p>
 * <p>
 * threadLocal的实现：（线程数组threadLocal的hash为索引，值为value。）
 * <p>
 * <p>
 * <p>
 * 1、在线程中有一个threadLocal(数组)的内存。保存该线程的threadLocal值。
 * <p>
 * 2、线程中threadLocal为一个16个虚引用key元素 Entry<WeakReference<threadLocal<?>>,value> 数组。
 * <p>
 * 3、Entry<WeakReference<threadLocal<?>>,value> 。当threadLocal不存在时候，entry的key就不存在了。不能得到value。（强引用 + 弱引用 实现同时GC）
 * <p>
 * 4、threadLocal的生成的hash值 & 数组长度 得到数组中的索引solt，然后将value放入该数组。
 * <p>
 * 5、超过localthread负载
 * <p>
 * rehash ：当线程中的threadLocal大于负载因子（2/3 length）。
 * 1、删除entry 失效的threadLocal。
 * 2、threadLocal数组长度 * 2 ,并打开重新拷贝到新数组。设置负载个数。
 */
public class TestTheadLocal<T> {

    private final ThreadLocal<T> threadLocal = new ThreadLocal<T>();

    public static void main(String[] args) {
        TestTheadLocal<String> testTheadLocal = new TestTheadLocal<String>();

        final ThreadLocal<String> threadLocal = testTheadLocal.threadLocal;

        ExecutorService executorService = Executors.newFixedThreadPool(2);

        List<Future<String>> submits = new ArrayList<Future<String>>(2);

        for (int i = 0; i < 2; i++) {
            submits.add(executorService.submit(() -> {
                String data = String.valueOf(new Random().nextInt(100));
                threadLocal.set(data);
                System.out.println(threadLocal.get());
                return data;
            }));
        }

        submits.stream().forEach((a) -> {
            try {
                System.out.println(a.get());
            } catch (Exception _e) {
            }
        });
        executorService.shutdown();

    }

}
