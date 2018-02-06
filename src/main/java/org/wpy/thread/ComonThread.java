package org.wpy.thread;

/**
 * DESC
 *
 * @author
 * @create 2017-07-19 上午10:58
 **/
public class ComonThread extends Thread {
    @Override
    public void run() {
        synchronized (ComonThread.class) {
            for (int i = 0; i < 10; i++) {
                Comon.num += 1;
                System.out.println(Comon.num);
            }
        }
    }
}
