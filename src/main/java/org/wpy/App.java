package org.wpy;


import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.wpy.value.SingleInstance;

import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Hello world!
 */

public class App {
    /**
     * @param args
     * @throws Exception
     */
    private static final ReentrantLock REENTRANT_LOCK = new ReentrantLock(true);

    /**
     * 处理sql语句格式
     *
     * @param str
     * @return
     */
    public static String replaceBlank(String str) {

        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll(" ");

            p = Pattern.compile("[ ]+");
            m = p.matcher(dest);
            dest = m.replaceAll(" ");
        }
        return dest;
    }
    /*-----------------------------------

    笨方法：String s = "你要去除的字符串";

            1.去除空格：s = s.replace('\\s','');

            2.去除回车：s = s.replace('\n','');

    这样也可以把空格和回车去掉，其他也可以照这样做。

    注：\n 回车(\u000a)
    \t 水平制表符(\u0009)
    \s 空格(\u0008)
    \r 换行(\u000d)*/

    public static void main22(String[] args) {
        /*System.out.println(App.replaceBlank("just do " + "it !"));


        System.out.println(App.replaceBlank("SELECT column_name(s)\n" +
                "FROM table_name1 table1\n" +
                "LEFT\t\t\t\t\tJOIN table_name2 table2\n" +
                "ON \n\n\r\ntable2.column_name=table1.column_name  "));*/



        /*String str = String.format("name\\s.+=\\s(.+)wangsan\\s(.*)",  "name","=","wangsan");

        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher("name=wangsan");
        System.out.println(m.find());
        System.out.println(m.replaceAll("zhhhhhhhhhh "));*/


        String patter = String.format("\\s%s\\s*" + "%s" + "\\s*\\w+\\s", "name", "=");
        Matcher matcher = Pattern.compile(patter).matcher("所说的方式  where name = wangsan and nems-sldlf ");
        if (matcher.find()) {
            System.out.println(matcher.group());
            System.out.println(matcher.replaceAll(String.format(" %s %s %s ", "name", "=", "lisisisisis")));
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println(SingleInstance.class.getClass().getClassLoader());

        System.out.println(SingleInstance.getInstance());
        System.out.println(SingleInstance.getInstance());
    }


    /**
     * integer的最大值：
     * <p>
     * 得到前31个1111的相加
     *
     * @param val2
     * @param sum
     * @return
     */
    static double num(double val2, double sum) {
        if (val2 == 0)
            return sum + 1;
        sum = Math.pow(2.0, val2) + sum;
        return num(--val2, sum);
    }


    public void function() {
        throw new RuntimeException("-------运行时异常不需要声明异常-------");
    }


    @Test
    public void test1() {
        function();
    }

    @Test
    public void test2() {
        DataVO dataVO = new DataVO();
        dataVO.setName("wpy".getBytes());
        dataVO.setAge(24);
        String data = JSON.toJSONString(dataVO);
        System.out.println(data);
        byte[] name = JSON.parseObject(data, DataVO.class).getName();
        System.out.println(new String(name));
    }


}

class DataVO {
    private byte[] name;

    private int age;

    public byte[] getName() {
        return name;
    }

    public void setName(byte[] name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}