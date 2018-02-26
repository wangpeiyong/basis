package org.wpy.proxy.cglib;

/**
 * DESC
 *
 * @author
 * @create 2017-07-19 下午3:27
 **/
public class DoCGLib {
    public static void main(String[] args) {
        CglibProxy proxy = new CglibProxy();

        //通过生成子类的方式创建代理类
        SayHello proxyImp = (SayHello) proxy.getProxy(SayHello.class);

        proxyImp.say("wpy");
        proxyImp.sayFinal("Final wpy"); // 定义为final的方法无法代理
    }
}
