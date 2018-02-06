package org.wpy.mbean;

import javax.management.Attribute;
import javax.management.MBeanServer;
import javax.management.MBeanServerInvocationHandler;
import javax.management.ObjectName;
import java.lang.management.*;

/**
 * @author <a href="py.wang@bkjk.com">py.wang</a>
 * @Description: 预演
 * @Package org.wpy.sys
 * @date 2017/12/29 11:09
 * @see
 */
public class JVMHealthInfo {


    public static void main(String[] args) throws Exception {
        jvmHealthCheck();
        MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
        ObjectName objectName = new ObjectName("agent:name=test");
        Test testMBean = new Test();
        mBeanServer.registerMBean(testMBean, objectName);

        mBeanServer.setAttribute(objectName, new Attribute("Name", "---------wang--------"));
        mBeanServer.invoke(objectName, "printHelloWorld", null, null);


        TestMBean proxy = MBeanServerInvocationHandler.newProxyInstance(mBeanServer, objectName, TestMBean.class, false);
        proxy.printHelloWorld();
    }


    public static void jvmHealthCheck() throws Exception {
        /**
         * 1、得到Mbean服务
         */
        MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();

        /**
         * 2、通过Mbean名字、Mbean类型得到Mbean。
         */
        MemoryMXBean memBean = ManagementFactory.newPlatformMXBeanProxy(mBeanServer, ManagementFactory.MEMORY_MXBEAN_NAME, MemoryMXBean.class);
        MemoryUsage heapMemoryUsage = memBean.getHeapMemoryUsage();
        MemoryUsage nonHeapMemoryUsage = memBean.getNonHeapMemoryUsage();
        int objectPendingFinalizationCount = memBean.getObjectPendingFinalizationCount();
        boolean verbose = memBean.isVerbose();
        System.out.println("堆：[-verbose:class]" + verbose);
        System.out.println("堆：" + heapMemoryUsage);
        System.out.println("非堆：" + nonHeapMemoryUsage);
        System.out.println("等待FinalizationCount:" + objectPendingFinalizationCount);


        OperatingSystemMXBean opMXbean = ManagementFactory.newPlatformMXBeanProxy(mBeanServer, ManagementFactory.OPERATING_SYSTEM_MXBEAN_NAME, OperatingSystemMXBean.class);
        double systemLoadAverage = opMXbean.getSystemLoadAverage();
        int availableProcessors = opMXbean.getAvailableProcessors();
        String name = opMXbean.getName();
        String arch = opMXbean.getArch();
        System.out.println(String.format("systemLoadAverage:%s\tavailableProcessors:%s\tname:%s\tarch:%s", systemLoadAverage, availableProcessors, name, arch));


        int gcCounts = 0;
        for (final GarbageCollectorMXBean garbageCollector : ManagementFactory.getGarbageCollectorMXBeans()) {
            gcCounts += garbageCollector.getCollectionCount();
        }

        System.out.println("gcCounts:" + gcCounts);
    }
}
