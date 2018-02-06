package org.wpy.classloader.hotswap;

/**
 * DESC
 *
 * @author
 * @create 2017-08-24 下午2:27
 **/
public class Hot implements IHot {
    public static void main(String[] args) {

    }

    public void hot() {
        System.out.println(" version wang  : " + this.getClass().getClassLoader());
    }
}