package org.wpy.proxy.jdk;

import java.lang.reflect.Proxy;

/**
 * Created by wpy on 2017/3/22.
 */
public class Start {

    public static void main(String[] args) {

        /** AOP:  DynamicProxy(A) A的代理类 ,重写接口的实现，并前后作权限控制 */
        ISubject iSubject = (ISubject) Proxy.newProxyInstance(Start.class.getClassLoader(), new Class[]{ISubject.class}, new DynamicProxy(new Object()));

        iSubject.toString();

    }
}
