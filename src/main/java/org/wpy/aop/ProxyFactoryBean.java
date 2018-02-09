package org.wpy.aop;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @Author wangpeiyong
 * @Date 2018/2/9 10:47
 */
public class ProxyFactoryBean {

    private Object target;
    private IAdvice advice;

    public IAdvice getAdvice() {
        return advice;
    }

    public void setAdvice(IAdvice advice) {
        this.advice = advice;
    }

    public Object getTarget() {

        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public Object getProxy() {
        return Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), new InvocationHandler() {
            @Override
            public Object invoke(Object o, Method method, Object[] args) throws Throwable {
                advice.before();
                Object retVal = method.invoke(target, args);
                advice.after();
                return retVal;
            }
        });
    }

}
