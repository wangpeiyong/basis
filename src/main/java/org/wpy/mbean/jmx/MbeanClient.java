package org.wpy.mbean.jmx;


import org.wpy.mbean.TestMBean;

import javax.management.*;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.util.Arrays;

/**
 * @author <a href="py.wang@bkjk.com">py.wang</a>
 * @Description:
 * @Package org.wpy.mbean
 * @date 2017/12/29 13:55
 * @see
 */
public class MbeanClient {
    public static void main(String[] args) throws Exception {
        //connect JMX
        JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://localhost:1099/jmxrmi");
        JMXConnector jmxc = JMXConnectorFactory.connect(url, null);
        MBeanServerConnection mBeanServerConnection = jmxc.getMBeanServerConnection();
        ObjectName objectName = new ObjectName("agent:name=test");


        /**
         * 设置属性和执行方法
         */
        mBeanServerConnection.setAttribute(objectName, new Attribute("Name", "---------wang--------"));
        mBeanServerConnection.invoke(objectName, "printHelloWorld", null, null);
        Object attribute = mBeanServerConnection.getAttribute(objectName, "Name");
        System.out.println(attribute);


        /**
         * 得到远程代理实例
         */
        TestMBean proxy = MBeanServerInvocationHandler.newProxyInstance(mBeanServerConnection, objectName, TestMBean.class, false);
        proxy.printHelloWorld();

        /**
         * 打印出Mbean的属性
         */
        MBeanInfo mBeanInfo = mBeanServerConnection.getMBeanInfo(objectName);
        System.out.println(mBeanInfo);
        Arrays.stream(mBeanInfo.getAttributes()).forEach(System.out::println);


        /**
         *
         */
        System.out.println("MBean count = " + mBeanServerConnection.getMBeanCount());
        mBeanServerConnection.queryMBeans(null, null).stream()
                .map(item -> {
                    System.out.println(item.getClassName());
                    return item;
                })
                .filter(item -> item instanceof com.sun.management.GarbageCollectorMXBean)
                .forEach(System.err::println);


        jmxc.close();
    }
}
