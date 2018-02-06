package org.wpy.gc;

import org.junit.Test;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;

/**
 * DESC
 * <p>
 * -ea -XX:+PrintGCDetails -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/Users/wpy/IdeaProjects/myproject/hw-bigdata/java-basic/
 * <p>
 * OOM异常：(老年代、年轻代都出现OOM )
 * [Full GC (Allocation Failure) [PSYoungGen: 373157K->373157K(415744K)] [ParOldGen: 1398140K->1398084K(1398272K)] 1771298K->1771242K(1814016K), [Metaspace: 5089K->5085K(1056768K)], 0.8810836 secs] [Times: user=0.48 sys=0.22, real=0.88 secs]
 * java.lang.OutOfMemoryError: Java heap space
 * <p>
 * <p>
 * MAT:
 * at java.lang.OutOfMemoryError.<init>()V (OutOfMemoryError.java:48)
 * at org.wpy.gc.OOMTest.test()V (OOMTest.java:21)
 *
 * @author
 * @create 2017-07-25 下午4:53
 **/
public class OOMTest {
    public static void main(String[] args) {
        // get name representing the running Java virtual machine.
        String name = ManagementFactory.getRuntimeMXBean().getName();
        System.out.println(name);
// get pid
        String pid = name.split("@")[0];
        System.out.println("Pid is:" + pid);
        new Thread(() -> {
            for (; ; ) {
                System.out.print("");
            }
        }).start();
        for (; ; ) {
            System.out.print("");
        }
    }

    @Test
    public void test() {
        List list = new ArrayList();
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            byte[] data = new byte[1024 * 1014 * 1]; //1M
            list.add(data);
        }
    }
}
