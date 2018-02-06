package org.wpy.mbean;

/**
 * @author <a href="py.wang@bkjk.com">py.wang</a>
 * @Description:
 * @Package org.wpy.mbean
 * @date 2017/12/29 12:04
 * @see
 */
public class Test implements TestMBean {
    private String name;

    public void printHelloWorld() {
        System.out.println(name + ",welcome to this world.");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
