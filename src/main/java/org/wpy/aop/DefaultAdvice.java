package org.wpy.aop;

/**
 * @Author wangpeiyong
 * @Date 2018/2/9 10:51
 */
public class DefaultAdvice implements IAdvice {
    @Override
    public void before() {
        System.out.println("before...");
    }

    @Override
    public void after() {
        System.out.println("after...");
    }
}
