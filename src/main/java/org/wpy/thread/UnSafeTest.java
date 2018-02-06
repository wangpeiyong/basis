package org.wpy.thread;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * 线程可见性、变量CAS操作
 */
public class UnSafeTest {
    private static final Unsafe UNSAFE;

    static {
        try {
            Field theUnsafeInstance = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafeInstance.setAccessible(true);
            UNSAFE = (Unsafe) theUnsafeInstance.get(Unsafe.class);
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    public static void main(String[] args) throws InstantiationException, NoSuchFieldException {
        /**
         * 1、实例化需要unsafe处理的对象。
         */
        Player player = (Player) UNSAFE.allocateInstance(Player.class);
        player.setAge(18);
        player.setName("li lei");
        player.setAddrs(new String[]{"wang", "zhsjs"});
        for (Field field : Player.class.getDeclaredFields()) {
            System.out.println(field.getName() + ":对应的内存偏移地址" + UNSAFE.objectFieldOffset(field));
        }


        /**
         * 2、得到三个属性的直接引用的偏移地址
         */
        int nameOffest = (int) UNSAFE.objectFieldOffset(Player.class.getDeclaredField("name"));
        int ageOffset = (int) UNSAFE.objectFieldOffset(Player.class.getDeclaredField("age"));
        int addrsOffset = (int) UNSAFE.objectFieldOffset(Player.class.getDeclaredField("addrs"));
        System.out.println(String.format("-----addrsOffset:%s------nameOffest:%s----ageOffset:%s----", addrsOffset, nameOffest, ageOffset));
        System.out.println("对象的addrs offest地址属性" + String.join(",", (String[]) UNSAFE.getObjectVolatile(player, addrsOffset)));


        /**
         * 3、得到对象中某个属性的堆中地址
         */
        System.out.println("----------属性addr内部元素的offest地址---------");
        Class<?> ak = String[].class;
        int ABASE = UNSAFE.arrayBaseOffset(ak);
        int scale = UNSAFE.arrayIndexScale(ak);
        int ASHIFT = 31 - Integer.numberOfLeadingZeros(scale);
        System.out.println(String.format("ABASE=%s\tscale=%s\tASHIFT=%s", ABASE, scale, ASHIFT));
        UNSAFE.putObjectVolatile(player.getAddrs(), ((long) 0 << ASHIFT) + ABASE, "wang222");
        System.out.println(UNSAFE.getObjectVolatile(player.getAddrs(), ((long) 0 << ASHIFT) + ABASE));
        System.out.println(player);


        /**
         * 4、unsafe CAS方式修改对象的属性值。     可见性
         */
        //修改内存偏移地址为8的值（age）,返回true,说明通过内存偏移地址修改age的值成功
        System.out.println(UNSAFE.compareAndSwapInt(player, ageOffset, 18, 20));
        System.out.println("age修改后的值：" + player.getAge());
        System.out.println("-------------------");


        /**
         * 5、修改对象的属性值。     线程不一定可见
         */
        //修改内存偏移地址为8的值，但是修改后不保证立马能被其他的线程看到。   线程不一定可见
        UNSAFE.putOrderedInt(player, ageOffset, 33);
        System.out.println("age修改后的值：" + player.getAge());
        System.out.println("-------------------");


        /**
         * 6、修改、得到线程可见的属性
         */
        //修改内存偏移地址为12的值，volatile修饰，修改能立马对其他线程可见    线程可见
        UNSAFE.putObjectVolatile(player, nameOffest, "han mei");
        System.out.println("name修改后的值：" + UNSAFE.getObjectVolatile(player, nameOffest));
    }
}

class Player {

    private String[] addrs;
    private String name;

    private int age;

    private Player() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String[] getAddrs() {
        return addrs;
    }

    public void setAddrs(String[] addrs) {
        this.addrs = addrs;
    }

    @Override
    public String toString() {
        return "Player{" +
                "addrs=" + Arrays.toString(addrs) +
                ", name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}