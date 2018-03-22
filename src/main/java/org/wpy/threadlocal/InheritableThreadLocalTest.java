package org.wpy.threadlocal;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class InheritableThreadLocalTest {
    private static final InheritableThreadLocal INHERITABLE_THREAD_LOCAL = new InheritableThreadLocal();

    private static final ThreadLocal THREAD_LOCAL = new ThreadLocal();

    private static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(1);

    public static void main(String[] args) {
        INHERITABLE_THREAD_LOCAL.set("wang");
        THREAD_LOCAL.set("wang2");

        new Thread(()->{
            System.err.println(INHERITABLE_THREAD_LOCAL.get());
            System.err.println(THREAD_LOCAL.get());
        }).start();


        EXECUTOR_SERVICE.submit(()->{
            System.err.println(INHERITABLE_THREAD_LOCAL.get());
            System.err.println(THREAD_LOCAL.get());
        });


        INHERITABLE_THREAD_LOCAL.set("wang22");
        EXECUTOR_SERVICE.submit(()->{
            System.err.println(INHERITABLE_THREAD_LOCAL.get());
            System.err.println(THREAD_LOCAL.get());
        });
    }
}
