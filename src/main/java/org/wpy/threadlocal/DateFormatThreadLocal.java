package org.wpy.threadlocal;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author peiyong.wang
 * @date 2018/4/20上午11:27
 */
public class DateFormatThreadLocal {
    private final ThreadLocal<DateFormat> t;

    public DateFormatThreadLocal(final String format) {
        this.t = new ThreadLocal<DateFormat>() {
            @Override
            protected DateFormat initialValue() {
                return new SimpleDateFormat(format);
            }
        };
    }

    public Date parse(String str) throws ParseException {
        return this.t.get().parse(str);
    }

    public String format(Date date) {
        return this.t.get().format(date);
    }

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(100);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (int i = 0; i < 100; i++) {
            executorService.submit(() -> {
                try {
                    Date date1 = df.parse("2018-04-20 11:35:03");
                    Date date2 = df.parse("2013-04-20 09:35:18");
                    System.out.println("1:" + date1.toLocaleString());
                    System.out.println("2:" + date2.toLocaleString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
