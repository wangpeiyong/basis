package org.wpy.thread;

/**
 * DESC
 *
 * @author
 * @create 2017-07-19 上午11:05
 **/
public class SharingObjects1 {
    private static boolean ready;
    private static int number;

    private static class ReaderThread extends Thread {
        public static void main(String[] args) {
            new ReaderThread().start();
            number = 42;
            ready = true;
            System.out.println("thread " + Thread.currentThread().getId() + " over");
        }

        @Override
        public void run() {
            while (!ready) {
                Thread.yield();
                System.out.println(number);
            }
            System.out.println("thread " + Thread.currentThread().getId() + " over");
        }
    }
}