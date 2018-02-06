package org.wpy.classloader.hostswap2;

import org.apache.catalina.startup.ClassLoaderFactory;
import org.junit.Test;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ClassLoadTest {


    public static void main(String[] args) throws Exception {
        //MyURLClassLoad myURLClassLoad = new MyURLClassLoad(new URL[]{new URL("file:/Users/wpy/Desktop/gre/com/wisely")}/*,Thread.currentThread().getContextClassLoader()*/);

        MyURLClassLoad myURLClassLoad = new MyURLClassLoad(new URL[]{new URL("file:/Users/wpy/Desktop/gre/com/wisely")});

        Thread.currentThread().setContextClassLoader(myURLClassLoad);
        Class<DisplayService> aClass = (Class<DisplayService>) myURLClassLoad.loadClass("com.wisely.DisplaySerivceImpl");
        DisplayService displayService = aClass.newInstance();
        displayService.display("-------test2-wpy-------");
    }

    /**
     * NoClassDefFoundError: com/wisely/DisplayService
     * 未设置parentclassload：
     * <p>
     * loadClass DisplayService找不到接口。
     */
    @Test
    public void test1() throws Exception {
        String rootUrl = "/Users/wpy/Desktop/gre/com/wisely";
        String className = "com.wisely.DisplaySerivceImpl";
        DiskClassLoader ncl1 = new DiskClassLoader(rootUrl, Thread.currentThread().getContextClassLoader());
        Class<?> clazz1 = ncl1.loadClass(className);
        DisplayService displayService = (DisplayService) clazz1.newInstance();
        displayService.display("-------test1-wpy-------");
    }

    @Test
    public void test2() throws Exception {
        List<ClassLoaderFactory.Repository> repositories = new ArrayList<>(1);
        repositories.add(new ClassLoaderFactory.Repository("/Users/wpy/Desktop/gre/com/wisely", ClassLoaderFactory.RepositoryType.GLOB));
        final ClassLoader classLoader = ClassLoaderFactory.createClassLoader(repositories, Thread.currentThread().getContextClassLoader());
        Class<DisplayService> aClass = (Class<DisplayService>) classLoader.loadClass("com.wisely.DisplaySerivceImpl");
        aClass.newInstance().display("-------test2-wpy-------");
    }

    /**
     * URLClassLoad会默认设置为parentClassLoader为系统appclassload。
     *
     * @throws Exception
     */
    @Test
    public void test3() throws Exception {
        MyURLClassLoad myURLClassLoad = new MyURLClassLoad(new URL[]{new URL("file:/Users/wpy/Desktop/gre/com/wisely")}/*,Thread.currentThread().getContextClassLoader()*/);
        Thread.currentThread().setContextClassLoader(myURLClassLoad);
        Class<DisplayService> aClass = (Class<DisplayService>) myURLClassLoad.loadClass("com.wisely.DisplaySerivceImpl");
        DisplayService displayService = aClass.newInstance();
        displayService.display("-------test2-wpy-------");
    }


}
