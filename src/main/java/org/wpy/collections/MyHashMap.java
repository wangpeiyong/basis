package org.wpy.collections;


import java.util.HashMap;

/**
 * DESC
 * <p>
 * hashMap中的重点：
 * 1、负载因子：   默认是0.75。 标识当map中的数组有0.75的填充率，将会自动扩充。
 * 2、初始化长度： 默认是16.    规范Map中数组的长度，数组的长度为大于初始化长度的2次方的最小值。
 *
 * @author
 * @create 2017-07-14 上午10:55
 **/
public class MyHashMap {
    public static void main(String[] args) {

        HashMap<K, String> data = new HashMap<>();
        data.put(new K("wang"), "wang");
        data.put(new K("wang"), "wang");
        data.forEach((K, V) -> System.out.println(K + "   " + V));

        System.out.println(1 - 0.9);
    }

    static class K {
        private String data;

        public K(String data) {
            this.data = data;
        }

        @Override
        public int hashCode() {
            return data != null ? data.hashCode() : 0;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            K k = (K) o;
            return data != null ? data.equals(k.data) : k.data == null;
        }
    }
}
