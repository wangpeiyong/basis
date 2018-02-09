package org.wpy.atomic;


import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by wpy on 2017/3/24.
 */
public class AtomicElementReferance {

    private static ConcurrentHashMap<Integer, User> map = new ConcurrentHashMap<>(5);

    public static void main(String[] args) throws Exception {

        final CyclicBarrier cyclicBarrier = new CyclicBarrier(2);

        for (int i = 0; i < 5; i++) {
            map.put(i, new User("zhangsan " + i, i));
        }


        ExecutorService executorService = Executors.newFixedThreadPool(4);
        executorService.execute(
                () -> {
                    try {
                        cyclicBarrier.await();
                        System.out.println(Thread.currentThread().getName() + "时间：" + System.nanoTime() + "\t 1 更新元素user 1");
                        User user = map.get(1);
                        user.setName("wang111");
                        map.put(1, user);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
        );

        executorService.execute(
                () -> {
                    try {
                        cyclicBarrier.await();
                        System.out.println(Thread.currentThread().getName() + "时间：" + System.nanoTime() + "\t 2 更新元素user 1");
                        User user = map.get(1);
                        user.setName("wang222");
                        map.put(1, user);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
        );

        Thread.sleep(1000);

        System.out.println(Thread.currentThread().getName() + "时间：" + System.nanoTime() + "\t 2 得到元素user 1");
        System.out.println(map.get(1));


        Thread.sleep(7000);
        executorService.shutdown();


    }
}

class User {
    private String name;
    private Integer id;

    public User(String name, Integer id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "User{" + "name='" + name + '\'' + ", id=" + id + '}';
    }
}
