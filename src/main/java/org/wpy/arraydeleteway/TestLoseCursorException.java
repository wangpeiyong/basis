package org.wpy.arraydeleteway;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;

/**
 * DESC 遍历删除数组中元素的正确姿势
 *
 * @author
 * @create 2017-04-17 上午11:21
 **/
public class TestLoseCursorException {

    public static void main(String[] args) {

        List<Object> data = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            data.add(String.format("$%s", i));
        }

        rightArrayElemantDelete(data);

        IteratorDelete();
        StreamDelete();

    }


    /**
     * ERROR
     * 迭代删除丢失光标
     * Exception  java.util.ConcurrentModificationException
     *
     * @param data
     */
    public static void forException(List<Object> data) throws ConcurrentModificationException {
        for (Object o : data) {
            if (String.valueOf(o).contains("2"))
                data.remove(o);
        }
    }

    /**
     * ERROR
     * iterable遍历删除丢失光标异常
     * Exception  java.util.ConcurrentModificationException
     *
     * @param data
     * @throws ConcurrentModificationException
     */
    public static void iteratorException(List<Object> data) throws ConcurrentModificationException {
        Iterator<Object> iterable = data.iterator();
        while (iterable.hasNext()) {
            Object o = iterable.next();
            if (String.valueOf(o).contains("2"))
                data.remove(o);
        }
    }


    /**
     * ERROR
     * 数组遍历删除，如果先得到size的话，会出现下表越界。数组的size应该是动态的。
     * IndexOutOfBoundsException: Index: 3, Size: 3
     *
     * @param data
     * @throws IndexOutOfBoundsException
     */
    public static void outOfIndexException(List<Object> data) throws IndexOutOfBoundsException {
        int size = data.size();
        for (int i = 0; i < size; i++) {
            if (String.valueOf(data.get(i)).contains("2"))
                data.remove(i);
        }
    }


    /**
     * ERROR ，删除向左移动
     * 数组遍历删除，应该动态的获取当前数组的长度。
     *
     * @param data
     */
    public static void leftMoveArrayElemantDelete(List<Object> data) {
        // TODO: 2017/4/17 动态的获取数组的长度，每次循环都要获取。
        for (int i = 0; i < data.size(); i++) {
            if (String.valueOf(data.get(i)).contains("2"))
                data.remove(i);
        }
    }


    /**
     * RIGHT：正确的删除方式，删除左移，但是能够遍历所有元素。
     *
     * @param data
     */
    public static void rightArrayElemantDelete(List<Object> data) {
        for (int i = data.size() - 1; i >= 0; i--) {
            if (String.valueOf(data.get(i)).contains("2"))
                data.remove(i);
        }
    }


    /**
     * RIGHT: 利用迭代器正确的删除元素。
     * <p>
     * Iterator.remove()
     */
    public static void IteratorDelete() {
        List<String> data = new ArrayList<String>(6);
        for (int i = 0; i < 6; i++) {
            data.add("wang" + i % 2);
        }
        Iterator<String> it = data.iterator();
        while (it.hasNext()) {
            String ele = it.next();
            if (ele.equals("wang1")) {
                it.remove();
            }
        }
        data.forEach(System.out::println);
    }


    /**
     * JDK8: Stream delete
     * <p>
     * 通过stream删除元素
     */
    public static void StreamDelete() {
        List<String> data = new ArrayList<String>(6);
        for (int i = 0; i < 6; i++) {
            data.add("wang" + i % 2);
        }
        data.stream().filter(a -> !a.equals("wang1")).forEach(System.out::println);
    }

}
