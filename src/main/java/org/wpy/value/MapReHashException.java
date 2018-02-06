package org.wpy.value;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * java.util.HashMap#resize() 初始化map的大小、负载因子、重新增加数字的长度。
 * <p>
 * 优美的生成数组index的算法。
 * <p>
 * <p>
 * resize():
 * 将该数据中的元素重新分配地址，J索引中的元素可拆分为 newTab[j] 和 newTab[j + oldCap] 中去。
 * <p>
 * <p>
 * 1、(e.hash & oldCap) == 0 既是  e.hash &(newCap -1) == j
 * 得到当前的table数组中不变index的元素。并将他们node连接。
 * newTab[j] = loHead;
 * <p>
 * 2、非 e.hash &(newCap -1) == j
 * 得到将当前table中元素变化到j + oldCap中的元素，并将他们node连接。
 * newTab[j + oldCap] = hiHead;
 * <p>
 * put()方法:
 * 1、hash找数组index
 * 2、链表遍历比较key equals，然后onlyIfAbsent比较更新。
 */
public class MapReHashException {
    public static void main(String[] args) {
        Map<String, String> map = new HashMap<>();

        map.put("33333333", "");

        ExecutorService executorService = Executors.newFixedThreadPool(4);

        List<Future<String>> submits = new ArrayList<Future<String>>(4);


        for (int i = 0; i < 100; i++) {
            submits.add(executorService.submit(() -> {
                String data = String.valueOf(new Random().nextInt(100));
                map.put(data, "");
                return data;
            }));
        }

        executorService.shutdown();

    }
}
