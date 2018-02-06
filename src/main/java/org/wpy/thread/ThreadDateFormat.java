package org.wpy.thread;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

/**
 * DESC
 * <p>
 * SimpleDateFormat的线程不安全
 * <p>
 * <p>
 * SimpleDateFormat(下面简称sdf)类内部有一个Calendar对象引用,它用来储存和这个sdf相关的日期信息,
 * 例如sdf.parse(dateStr), sdf.format(date)
 * 诸如此类的方法参数传入的日期相关String, Date等等, 都是交友Calendar引用来储存的.这样就会导致一个问题,如果你的sdf是个static的, 那么多个thread 之间就会共享这个sdf, 同时也是共享这个Calendar引用, 并且, 观察 sdf.parse() 方法,你会发现有如下的调用:
 * <p>
 * <p>
 * Date解析代码的实现。
 * <p>
 * Date parse() {
 * <p>
 * calendar.clear(); // 清理calendar
 * <p>
 * ... // 执行一些操作, 设置 calendar 的日期什么的
 * <p>
 * calendar.getTime(); // 获取calendar的时间
 * <p>
 * }
 *
 * @author
 * @create 2017-06-23 下午1:25
 **/
public class ThreadDateFormat {

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(50);
        ConcurrentSkipListSet<String> set = new ConcurrentSkipListSet<String>();


        for (int j = 0; j < 50; j++) {
            executorService.submit(() -> {
                for (int i = 1; i <= 9; i++) {
                    String var1 = "2017-01-0" + i + " 10:12:12";
                    //System.out.println(var1);
                    set.add(Thread.currentThread().getName() + "\t\t" + var1 + "\t\t" + DateUtil.formatYMDHMS(DateUtil.parseYMDHMS(var1)));
                }
            });
        }


/*

        executorService.submit(() -> {
            for (int i = 1; i <= 9; i++) {
                String var1 ="2017-01-0"+i+" 10:12:12";
                System.out.println(var1);
                set.add(Thread.currentThread().getName()+"\t\t"+var1 +"\t\t"+Tool.parser(var1));
            }
        });

        new Thread(() -> {
            for (int i = 1; i <= 9; i++) {
                String var1 = "2017-01-0" + i + " 10:12:12";
                System.out.println(var1);
                set.add(Thread.currentThread().getName() + "\t\t" + var1 + "\t\t" + Tool.parser(var1));
            }
        }).start();

*/

        Thread.sleep(3000);
        set.forEach(System.out::println);
        executorService.shutdown();
    }


    static class Tool {

        private static ReentrantLock lock = new ReentrantLock(true);
        private static DateFormat df = new SimpleDateFormat("yyyy-MM-hh MM:mm:ss");

        private static DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");


        public static Long LockParser(String arg) {
            lock.lock();
            try {
                return df.parse(arg).getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
            return null;
        }


        public static Long parser(String arg) {
            try {
                return df.parse(arg).getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        }


        public static Long synchronizedParser(String arg) {
            DateTime dateTime = DateTime.parse(arg, format);
            DateTime dateTime1 = new DateTime(dateTime.toDate());
            System.out.println(dateTime1.toString(format));
            return dateTime.toDate().getTime();
        }


    }
}


