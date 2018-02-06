package org.wpy.num;

import java.util.HashMap;

/**
 * DESC
 *
 * @author
 * @create 2017-06-16 上午10:39
 **/
public class Start {
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
