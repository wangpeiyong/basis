package org.wpy.aop;

/**
 * @Author wangpeiyong
 * @Date 2018/2/9 11:11
 */
public class Test {

    public static void main(String[] args) {

        Object bean = new BeanFactory().getBean("xxx");

        System.out.println(bean.getClass().getSimpleName());

        System.out.println(bean.toString());
    }
}
