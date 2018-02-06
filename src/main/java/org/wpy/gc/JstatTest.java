package org.wpy.gc;

import org.junit.Test;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * DESC
 * <p>
 * jinfo -flag +/-PrintGCDetails 2822  设置新增/删除PrintGCDetails
 * jinfo -flag PrintGCDetails 2822     查看PrintGCDetails设置
 *
 * @author
 * @create 2017-07-26 上午10:51
 **/
public class JstatTest {

    AtomicInteger count = new AtomicInteger(0);

    @Test
    public void test() {
        List list = new ArrayList();
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            byte[] data = new byte[1024 * 1024]; //1M
            list.add(new SoftObjectBuilder<byte[]>(data).build());
            System.out.println("-----------------" + count.getAndIncrement());
        }
    }

    @Test
    public void test1() {
        List list = new ArrayList();
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            byte[] data = new byte[1024 * 1024]; //1M
            list.add(data);
            System.out.println("-----------------" + count.getAndIncrement());
        }
    }


}


class SoftObjectBuilder<k> {
    private ReferenceQueue queue = new ReferenceQueue<k>();
    private SoftReference<k> softReference;

    public SoftObjectBuilder() {
    }

    public SoftObjectBuilder(k o) {
        softReference = new SoftReference<k>(o, queue);
    }

    public SoftReference<k> build() {
        return this.softReference;
    }
}
