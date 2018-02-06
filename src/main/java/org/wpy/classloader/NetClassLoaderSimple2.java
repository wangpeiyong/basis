package org.wpy.classloader;

/**
 * DESC
 *
 * @author
 * @create 2017-07-20 上午11:42
 **/
public class NetClassLoaderSimple2 {
    private NetClassLoaderSimple2 instance;

    public void setNetClassLoaderSimple(Object obj) {
        this.instance = (NetClassLoaderSimple2) obj;
    }

    public void say() throws InterruptedException {
        Thread.sleep(2000);
        System.out.println("----------------" + this.getClass().getClassLoader());
    }
}
