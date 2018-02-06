package org.wpy.proxy.cglib2;

import net.sf.cglib.core.DebuggingClassWriter;
import net.sf.cglib.proxy.Enhancer;
import org.wpy.utils.User;

public class Start {
    public static void main(String[] args) {
        System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY, "/Users/wpy/IdeaProjects/myproject/hw-bigdata/java-basic/src/main/java/org/wpy/proxy/cglib2");

        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(UserServiceImpl.class); // 设置父类，父亲不能为final形态。fastClass通过函数签名来得到非反射直接调用。
        enhancer.setCallback(new MyMethodInterceptor());  // 拦截器
        UserServiceImpl userService = (UserServiceImpl) enhancer.create();

        User user = new User();
        user.setName("wang");
        user.setAge(222);
        userService.add(user);

    }
}
