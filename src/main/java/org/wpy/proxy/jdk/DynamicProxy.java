package org.wpy.proxy.jdk;


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by wpy on 2017/3/22.
 */
public class DynamicProxy implements InvocationHandler {

    //被代理的实例
    private Object targer;

    public DynamicProxy(Object targer) {
        this.targer = targer;
    }

    //proxy代理类的实例
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        System.out.println(targer.toString() + "  执行 ");


        return method.invoke(targer, args);
    }
}
