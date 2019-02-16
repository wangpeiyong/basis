package org.wpy.value;

import org.junit.Test;

public class FinalTest {

    @Test
    public void test1() {
        A a = new A(2, 323L);
        System.out.println(a);
    }
}

class A {
    private static final String name;

    static {
        name = "zhangsan";
    }

    private final Integer age;
    private final Integer num;
    private Long PHONE;

    {
        age = 24;
    }

    public A(Integer num, Long PHONE) {
        this.num = num;
        this.PHONE = PHONE;
    }

    @Override
    public String toString() {
        return "A{" +
                "age=" + age +
                ", num=" + num +
                ", PHONE=" + PHONE +
                ", name=" + name +
                '}';
    }
}
