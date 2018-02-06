package org.wpy.proxy.jdk;

/**
 * Created by wpy on 2017/3/22.
 */
public class Subject implements ISubject {
    @Override
    public void printHelloWorld() {
        System.out.println(String.format("--------------==========%s========------------", "hello world"));
    }
}
