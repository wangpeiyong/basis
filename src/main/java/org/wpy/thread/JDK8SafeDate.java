package org.wpy.thread;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * DESC
 * jdk8 线程安全的日期类
 *
 * @author
 * @create 2017-07-12 上午9:48
 **/
public class JDK8SafeDate {

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public static void main(String[] args) throws Exception {
        ExecutorService service = Executors.newFixedThreadPool(3);
        AtomicLong atomicLong = new AtomicLong(0);


        for (int i = 0; i < 10; i++) {
            service.submit(() ->
                    System.out.println("jdk8 :" + LocalDate.parse("2014-12-2" + atomicLong.incrementAndGet()))
            );
        }

        atomicLong.set(0);
        //不安全的dataFormat类
        for (int i = 0; i < 10; i++) {
            service.submit(() -> {
                        try {
                            System.out.println("旧的  :" + sdf.format(sdf.parse("2014-12-2" + atomicLong.incrementAndGet())));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
            );
        }

        /* LocalTime: HH:mm:ss */
        System.out.println(LocalTime.now());
        System.out.println(LocalTime.now().withNano(0));
        System.out.println(LocalTime.parse("12:00:01"));
        System.out.println(LocalTime.of(12, 01, 11));

        /* LocalDateTime: yyyy-MM-dd HH:mm:ss */
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        System.out.println(LocalDateTime.now());
        System.out.println(LocalDateTime.parse("2014-11-23 12:00:01", formatter).format(formatter));
        System.out.println(LocalDateTime.of(2015, 12, 30, 12, 23, 23));

        LocalDateTime current = LocalDateTime.now();
        System.out.println(current.withHour(22).withNano(0).withDayOfMonth(2).format(formatter));
        System.out.println("当月最后一天：" + current.with(TemporalAdjusters.lastDayOfMonth()).withNano(0));

        // 取2015年1月第一个周一，这个计算用Calendar要死掉很多脑细胞：
        LocalDate firstMondayOf2015 = LocalDate.parse("2017-01-01").with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY)); // 2015-01-05
        System.out.println(firstMondayOf2015);

        // Future 实现线程的任务的处理。并加入超时自动熔断 poll。
        CompletionService<String> completionService = new ExecutorCompletionService<String>(service);
        for (int i = 0; i < 5; i++) {
            completionService.submit(() -> {
                int num = new Random().nextInt(10000);
                Thread.sleep(num);
                return "wang" + num;
            });
        }
        Future<String> future;
        for (; (future = completionService.poll(9, TimeUnit.SECONDS)) != null; )
            System.out.println(future.get());
        service.shutdown();
    }
}
