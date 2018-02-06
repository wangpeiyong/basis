package org.wpy.value;

import org.junit.Test;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 当写大于读的时候，copyonwrite每次写都要加锁，并且还要复制整个数组。此时，性能不如vector，只加锁，不复制数组。
 * <p>
 * 1、cow 写加锁，复制数组。 写性能弱于vector。
 * 2、vector 读加锁，写加锁。
 */
public class ConcurrentList {
    @Test
    public void test() {
        CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<String>();
        Vector<String> vector = new Vector<>(10);

        LocalTime time = LocalTime.now();
        for (int i = 0; i < 100000; i++) {
            list.add(new String(i + ""));
        }
        System.out.println(time.until(LocalTime.now(), ChronoUnit.SECONDS));  // 3 秒


        time = LocalTime.now();
        for (int i = 0; i < 100000; i++) {
            vector.add(new String(i + ""));
        }
        System.out.println(time.until(LocalTime.now(), ChronoUnit.SECONDS));  // 0 秒
    }
}
