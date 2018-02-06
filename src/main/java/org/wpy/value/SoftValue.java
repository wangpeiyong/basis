package org.wpy.value;

import org.junit.Test;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * DESC
 *
 * @author
 * @create 2017-07-19 下午4:54
 **/
public class SoftValue {

    @Test // Soft 软引用是内存不足时候，GC软引用对象。
    public void Soft() {
        byte[] str = new byte[2048];
        ReferenceQueue queue = new ReferenceQueue<String>();
        SoftReference<byte[]> softReference = new SoftReference<byte[]>(str, queue);
        str = null;
        System.gc();
        System.out.println(softReference.get());
        byte[] b = new byte[4 * 102400 * 925];
        System.gc();
        System.out.println(softReference.get());
    }


    @Test //Weak 弱引用只要gc就会删除引用。
    public void Weak() {
        String str = new String("wang");
        ReferenceQueue queue = new ReferenceQueue<String>();
        WeakReference<String> softReference = new WeakReference<String>(str, queue);
        str = null;
        System.out.println(softReference.get());
        System.gc();
        System.out.println(softReference.get());
    }


    @Test
    public void phantom() {
        String str = new String("wang");
        ReferenceQueue queue = new ReferenceQueue<String>();
        PhantomReference<String> softReference = new PhantomReference<String>(str, queue);
        str = null;
        System.out.println(softReference.get());
        System.gc();
        System.out.println(softReference.get());
    }

    @Test
    public void WeakHashMapTest() {
        WeakHashMap<String, byte[]> map = new WeakHashMap<>();
        for (int i = 0; i < 100000; i++) {
            map.put("sssss" + i, new byte[1024000]);
        }
    }


    // 大对象缓存可以考虑放在WeakHashMap中。
    @Test //  WeakHashMap key引用被强引用，所以，该WeakHashMap对象不会GC对象。
    public void WeakHashMapError() {
        List<String> list = new ArrayList<>(100000);
        WeakHashMap<String, byte[]> map = new WeakHashMap<>();
        for (int i = 0; i < 100000; i++) {
            String str = new String("sssss" + i);
            list.add(str);                                      //  WeakHashMap key引用被强引用，所以，该WeakHashMap对象不会GC对象。
            map.put(str, new byte[1024000]);
        }
    }


    // 大对象缓存可以考虑放在WeakHashMap中。
    @Test //  WeakHashMap key引用被强引用，所以，该WeakHashMap对象不会GC对象。
    public void SoftHashMapError() {
        byte[] data = new byte[1024 * 1024 * 10];
        SoftReference<byte[]> sfRefer = new SoftReference(data); // 100m
        System.err.println(sfRefer.get());
        data = null;
        System.gc();
        System.err.println(sfRefer.get());
        byte[] b = new byte[1024 * 1024 * 10];
        System.gc();
        System.err.println(sfRefer.get());
    }


    @Test //  ConcurrentHashMap 中的变量不是线程安全的和原子性的。
    public void ConcurrentSetTest() throws InterruptedException {
        ConcurrentHashMap<String, Set<String>> map = new ConcurrentHashMap<>(10);
        for (int i = 0; i < 10; i++) {
            map.put("key" + i, new HashSet<>());
        }
        ExecutorService service = Executors.newFixedThreadPool(4);
        for (int i = 0; i < 1000; i++) {
            service.submit(() -> {
                map.get("key0").add(new Random().nextInt(10000) + "");
            });
        }
        for (int i = 0; i < 1000; i++) {
            service.submit(() -> {
                map.get("key0").add(new Random().nextInt(10000) + "");
            });
        }
        System.out.println(map.get("key0").size());
        service.shutdown();

    }


    /**
     * integer [-128至127]自动包装的对象地址相同的
     */
    @Test
    public void testIntInteger() {
        int a = 3;
        Integer b = new Integer(3);
        System.out.println(a == b);


        Integer c = new Integer(200);
        Integer d = new Integer(200);
        System.out.println(c == d);


        Integer e = new Integer(3);
        Integer f = new Integer(3);
        System.out.println(e == f);


        Integer g = 3;
        Integer h = 3;
        System.out.println(e.getClass());
        System.out.println(g == h);


        Integer i = 233;
        Integer j = 233;
        System.out.println(i.getClass());
        System.out.println(j == i);

    }


}
