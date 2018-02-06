package org.wpy.optimization;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.RandomAccess;

/**
 * DESC
 *
 * @author
 * @create 2017-07-17 下午1:15
 **/
public class ListOptimization {
    public static void main(String[] args) {

        // 数组： 扩展 1.5*length
        //        数组的list不能直接使用数组的索引（直接内存 array[index]）
        List<String> data = new ArrayList<>(10);
        for (int i = 0; i < 10; i++) {
            data.add("s" + i);
        }
        print(data);


        //链表：  不存在扩容，不能用index索引遍历。
        List<String> data1 = new LinkedList<>();
        for (int i = 0; i < 10; i++) {
            data1.add("z" + i);
        }
        print(data);

    }


    /**
     * 数组list（内存引用遍历） 和 链表list（迭代器遍历）
     * <p>
     * PS: 链表不能使用get(index)，因为里面每次都是循环得到index，并index++计数。
     *
     * @param args
     */
    public static void print(List args) {
        if (args instanceof RandomAccess) {
            for (int length = args.size(), i = 0; i < length; i++) {
                System.out.println(args.get(i));
            }
        } else {
            for (Object ele : args) {
                System.out.println(ele);
            }
        }
    }
}
