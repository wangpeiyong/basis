package org.wpy.threadlocal;

import com.alibaba.ttl.TransmittableThreadLocal;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TransmittableThreadLocalTest {

   private static final TransmittableThreadLocal TRANSMITTABLETHREADLOCAL = new TransmittableThreadLocal();

    private static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(1);

    public static void main(String[] args) {
        TRANSMITTABLETHREADLOCAL.set("sssssss");

        EXECUTOR_SERVICE.submit(()->{
            System.err.println(TRANSMITTABLETHREADLOCAL.get());
        });


        TRANSMITTABLETHREADLOCAL.set("2222");
        EXECUTOR_SERVICE.submit(()->{
            System.err.println(TRANSMITTABLETHREADLOCAL.get());
        });
    }
}
