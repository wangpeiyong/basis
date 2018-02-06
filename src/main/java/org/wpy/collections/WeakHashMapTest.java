package org.wpy.collections;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.WeakHashMap;

/**
 * DESC  WeakHashMap：当空闲内存小于一定比例，就必须
 *
 * @author
 * @create 2017-07-19 下午2:14
 **/
public class WeakHashMapTest {
    public static void main(String[] args) throws InterruptedException {


    }


    @Test
    public void test1() throws InterruptedException {
        WeakHashMap<String, byte[]> map = new WeakHashMap<>();
        for (int i = 0; i < 100000; i++) {
            map.put("sssss" + i, new byte[1024000]);
        }
    }


    @Test
    public void test2() throws InterruptedException {
        HashMap<String, byte[]> map = new HashMap<>();
        for (int i = 0; i < 100000; i++) {
            map.put("sssss" + i, new byte[1024000]);
        }
    }


    // OutOfMemoryError: Requested array size exceeds VM limit
    @Test
    public void testOOM() throws InterruptedException {
        String[] array = new String[Integer.MAX_VALUE];
    }


    @Test
    public void test() throws InterruptedException {
        ArrayList<String> data = new ArrayList<>(100);
        for (int i = 0; i < 100; i++) {
            data.add(String.format("wang:%s", i));
        }
        int length = data.size();
        String s = null;
        for (int i = 0; i < length; i++) {
            if ((s = data.get(i)).indexOf("2") > -1 || s.indexOf("4") > -1)
                System.out.println(s);
        }

    }


}
