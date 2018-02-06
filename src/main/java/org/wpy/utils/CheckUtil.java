package org.wpy.utils;

public class CheckUtil {
    public static void main(String[] args) throws IllegalAccessException {
        User user = new User();
        long start = System.currentTimeMillis();
        int num = 10000;
        /*user.setName("sdsd");
        user.setAddr("sdsd");




        for (int i = 0; i < num; i++) {
            Asserts.notNull(user.getName(), user.getName());
            Asserts.notEmpty(user.getName(), user.getName());
        }*/
        System.out.println(System.currentTimeMillis() - start);


        start = System.currentTimeMillis();
        for (int i = 0; i < num; i++) {
            user.check();
        }
        System.out.println(System.currentTimeMillis() - start);
    }
}
