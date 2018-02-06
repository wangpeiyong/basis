package org.wpy.genericity;

/**
 * @author wpy
 * @version V1.0
 * @Description: TODO
 * @Package org.wpy.genericity
 * @date 2018/1/3 21:18
 */
public class Main<Integer> {

    public static void main(String[] args) {
        System.out.println((new Main().getClass().getGenericSuperclass()));

    }
}
