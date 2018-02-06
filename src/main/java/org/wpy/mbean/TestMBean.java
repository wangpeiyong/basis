package org.wpy.mbean;

/**
 * @author <a href="py.wang@bkjk.com">py.wang</a>
 * @Description: 必须以Mbean这个结尾
 * @Package org.wpy.mbean
 * @date 2017/12/29 12:03
 * @see
 */
public interface TestMBean {
    public void printHelloWorld();

    public String getName();

    public void setName(String name);
}
