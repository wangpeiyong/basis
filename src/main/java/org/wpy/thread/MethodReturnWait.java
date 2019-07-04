package org.wpy.thread;

public class MethodReturnWait {

    public static void main(String[] args) throws InterruptedException {

        String fun = fun();
        synchronized (MethodReturnWait.fun()) {
            fun.wait();
        }

    }

    public static String fun() {
        System.out.println("---------fun start--------");
        return "---------fun start--------";
    }
}
