package org.wpy.threadlocal;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * DESC
 *
 * @author
 * @create 2017-06-15 下午4:31
 **/
public class ThreadLocal2 {
    private static final ThreadLocal<String> threadLocal = new ThreadLocal<String>();

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        List<Future<String>> submits = new ArrayList<Future<String>>(2);
        for (int i = 0; i < 4; i++) {
            submits.add(executorService.submit(() -> {
                String data = String.valueOf(new Random().nextInt(100));
                threadLocal.set(data);
                // System.out.println(threadLocal.get());
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
