package org.wpy.classloader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * DESC   不同的类加载器，会实例出很多相同的类。
 *
 * @author
 * @create 2017-07-20 上午11:20
 **/
public class Test {

    @org.junit.Test
    public void test1() {
        ClassLoader cl = Test.class.getClassLoader();
        System.out.println("ClassLoader is:" + cl.toString());

        System.out.println("ClassLoader is:" + cl.getParent());
        System.out.println("ClassLoader is:" + cl.getParent().getParent());
    }


    //结论：从结果中可以看出，虽然是同一份class字节码文件，但是由于被两个不同的ClassLoader实例所加载，所以JVM认为它们就是两个不同的类。
    @org.junit.Test
    public void test2() {
        try {
            //测试加载网络中的class文件
            String rootUrl = "http://localhost:8080/httpweb/classes";
            String className = "org.wpy.classloader.NetClassLoaderSimple";
            NetworkClassLoader ncl1 = new NetworkClassLoader(rootUrl);
            NetworkClassLoader ncl2 = new NetworkClassLoader(rootUrl);
            Class<?> clazz1 = ncl1.loadClass(className);
            Class<?> clazz2 = ncl2.loadClass(className);
            Object obj1 = clazz1.newInstance();
            Object obj2 = clazz2.newInstance();
            clazz1.getMethod("setNetClassLoaderSimple", Object.class).invoke(obj1, obj2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 不同的类加载器中，会加载同一个类引用，每个类加载器中都有一份该类引用，并且每个类加载器都有大小。
     * 不能再父类加载器中找到该类，找到则加载父类加载中的class引用。
     */
    @org.junit.Test
    public void test3() {
        for (int i = 0; i < 100; i++) {
            //创建自定义classloader对象。
            DiskClassLoader diskLoader = new DiskClassLoader("/Users/wpy/Desktop/org/wpy/classloader");
            try {
                //加载class文件
                Class c = diskLoader.loadClass("org.wpy.classloader.NetClassLoaderSimple");
                if (c != null) {
                    try {
                        Object obj = c.newInstance();
                        Method method = c.getDeclaredMethod("say", null);
                        //通过反射调用Test类的say方法
                        method.invoke(obj, null);
                    } catch (InstantiationException | IllegalAccessException
                            | NoSuchMethodException
                            | SecurityException |
                            IllegalArgumentException |
                            InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 相同的类加载器去加载一个类，并将该类缓存。并能从该类加载器中的得到该类的历史引用。
     *
     * @throws ClassNotFoundException
     */
    @org.junit.Test
    public void test4() throws ClassNotFoundException {
        //创建自定义classloader对象。
        DiskClassLoader diskLoader = new DiskClassLoader("/Users/wpy/Desktop/org/wpy/classloader");
        //加载class文件，先查找该类加载器中->父类加载器中，是否有该Class的引用。
        Class c = diskLoader.loadClass("org.wpy.classloader.NetClassLoaderSimple");
        for (int i = 0; i < 100; i++) {
            try {
                if (c != null) {
                    Object obj = c.newInstance();
                    Method method = c.getDeclaredMethod("say", null);
                    //通过反射调用Test类的say方法
                    method.invoke(obj, null);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 重复类的定义 Class对象定义
     * <p>
     * 不存在重复定义类的问题：异常：attempted  duplicate class definition for name: "org/wpy/classloader/NetClassLoaderSimple"
     *
     * @throws ClassNotFoundException
     */
    @org.junit.Test
    public void test5() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
        //得到defineClass的函数实例
        Method defineClassMethod = ClassLoader.class.getDeclaredMethod("defineClass", new Class[]{String.class, byte[].class, int.class, int.class});
        defineClassMethod.setAccessible(true);
        String fileName = "/Users/wpy/Desktop/org/wpy/classloader/NetClassLoaderSimple.class";
        File file = new File(fileName);
        try {
            FileInputStream is = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            int len = 0;
            try {
                while ((len = is.read()) != -1) {
                    bos.write(len);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            byte[] data = bos.toByteArray();
            is.close();
            bos.close();
            for (int i = 0; i < 100; i++) {
                Class c = (Class) defineClassMethod.invoke(Test.class.getClassLoader(), new Object[]{"org.wpy.classloader.NetClassLoaderSimple", data, 0, data.length});
                System.out.println(c);
                Object obj = c.newInstance();
                Method method = c.getDeclaredMethod("say", null);
                //通过反射调用Test类的say方法
                method.invoke(obj, null);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
