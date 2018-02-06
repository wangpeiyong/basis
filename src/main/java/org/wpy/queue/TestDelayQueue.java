package org.wpy.queue;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * Created by wpy on 2017/3/23.
 */
public class TestDelayQueue {

    public static void main(String[] args) throws Exception {
        DelayQueue<Node> delayQueue = new DelayQueue<>();
        for (int i = 0; i < 10; i++)
            delayQueue.add(new Node(5L + i));


/*
        error:  take()会阻塞等待。

        for (Node node = delayQueue.take() ; node != null ; node = delayQueue.take())
            System.out.println(node.toString());

*/


        for (; !delayQueue.isEmpty(); )
            System.out.println(delayQueue.take().toString());

        System.out.println(" over ");


    }

}

class Node implements Delayed {
    private Long startTime;
    private Long expireTime;

    public Node(Long expireTime) {
        this.expireTime = expireTime * 1000;
        this.startTime = System.currentTimeMillis();
    }

    /**
     * 返回单位是纳秒（ns）
     *
     * @param unit
     * @return
     */
    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(startTime + expireTime - System.currentTimeMillis(), TimeUnit.NANOSECONDS);
    }

    /**
     * 将expireTime大的放在队列的后面
     *
     * @param o
     * @return
     */
    @Override
    public int compareTo(Delayed o) {
        long time = o.getDelay(TimeUnit.MICROSECONDS) - this.getDelay(TimeUnit.MICROSECONDS);

        return time == 0 ? 0 : (time > -1 ? -1 : 1);
    }

    @Override
    public String toString() {
        return String.format("{ startTime:%s , expireTime:%s }", startTime, expireTime);
    }
}

