package org.wpy.proxy.cglib;

/**
 * DESC
 *
 * @author
 * @create 2017-07-19 下午3:22
 **/
public class SayHello {
    public void say(String msg) {
        System.out.printf("hello %s !\n", msg.toUpperCase());
    }

    public final void sayFinal(String msg) {
        System.out.printf("hello %s !\n", msg.toUpperCase());
    }
}