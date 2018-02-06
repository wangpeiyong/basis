package org.wpy.statics;

/**
 * DESC
 *
 * @author
 * @create 2017-08-24 下午4:50
 **/
public class StaticClass {

    public static void test1(String args) {
        System.out.println("String");
    }

    public static void test1(char args) {
        System.out.println("char");
    }

    public static void test1(int args) {
        System.out.println("int");
    }

    public static void main(String[] args) {
        int num = 'a';
        test1(num);
        test1('a');
        test1('a');
    }


}
