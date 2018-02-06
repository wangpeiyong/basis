package org.wpy.thread;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

/**
 * DESC
 *
 * @author
 * @create 2017-08-30 上午11:18
 **/
public class DateUtil {
    private static final DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");


    public static Date parseYMDHMS(String arg) {
        DateTime dateTime = DateTime.parse(arg, format);
        return dateTime.toDate();
    }

    public static String formatYMDHMS(Date date) {
        DateTime dateTime = new DateTime(date);
        return dateTime.toString(format);
    }


}
