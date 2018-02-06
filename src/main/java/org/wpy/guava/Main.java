package org.wpy.guava;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.RateLimiter;

import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author <a href="py.wang@bkjk.com">py.wang</a>
 * @description:
 * @package org.wpy.guava
 * @date 2017/12/13 14:36
 * @see
 */
public class Main {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        LoadingCache<Long, AtomicInteger> stringAtomicIntegerLoadingCache = CacheBuilder.newBuilder().expireAfterWrite(100, TimeUnit.SECONDS)
                .build(new CacheLoader<Long, AtomicInteger>() {
                    @Override
                    public AtomicInteger load(Long key) {
                        return new AtomicInteger(1);
                    }
                });

        Random random = new Random();
        for (int i = 0; i < 1000; i++) {
            Thread.sleep(random.nextInt(100));
            stringAtomicIntegerLoadingCache.get(System.currentTimeMillis() / 100).incrementAndGet();
        }
        System.out.println("-------------over------------------------");
        stringAtomicIntegerLoadingCache.asMap().forEach((k, v) -> System.out.println(k + "         " + v));
    }


    /**
     * 每秒的请求数目限制
     *
     * @throws ExecutionException
     */
    public static void limitRequestPerSecond() throws ExecutionException {
        LoadingCache<Long, AtomicInteger> data = CacheBuilder.newBuilder()
                .expireAfterWrite(2, TimeUnit.SECONDS)
                .build(new CacheLoader<Long, AtomicInteger>() {
                    @Override
                    public AtomicInteger load(Long aLong) throws Exception {
                        return new AtomicInteger(0);
                    }
                });

        while (Boolean.TRUE) {
            if (data.get(System.currentTimeMillis()).getAndIncrement() > 5) {
                System.err.println("--------------不能得到链接--------------------");
                break;
            }
        }
    }


    /**
     * 令牌桶限流算法
     */
    public static void tokenBucket() {
        //每秒增加10个令牌，桶的容量为10个。
        RateLimiter limiter = RateLimiter.create(10);

        for (int i = 0; i < 20; i++) {
            System.out.println("----------得到一个令牌，并开始处理业务-----------" + (System.currentTimeMillis() / 1000));
            limiter.acquire();
        }
    }


    /**
     * 带热启动的令牌桶限流算法
     */
    public static void coldTokenBucket() {
        //每秒增加10个令牌，桶的容量为10个。并有10秒的预热时间。
        RateLimiter limiter = RateLimiter.create(10, 1, TimeUnit.SECONDS);

        for (int i = 0; i < 200; i++) {
            System.out.println("----------得到一个令牌，并开始处理业务-----------" + (System.currentTimeMillis() / 1000));
            limiter.acquire();
        }
    }

}
