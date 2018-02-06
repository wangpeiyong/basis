package org.wpy.regex;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * DESC
 *
 * @author
 * @create 2017-06-20 上午11:47
 **/
public class Start {
    public static void main(String[] args) throws Exception {
        File file = new File("/Users/wpy/Desktop/java.txt");

        byte[] data = new byte[(int) file.length()];
        int len = 0;

        System.out.println("以子节为单位读取文件内容，一次读一个字节");
        InputStream in = new FileInputStream(file);
        int read;
        while ((read = in.read()) != -1) {
            data[len++] = (byte) read;
            // 单个输出,非汉字正确输出.转换成字符输出
            System.out.write(read);
        }
        String result = new String(data, 0, len);
        // 非汉字时正常输出
        System.out.println("result:" + result);

        String str = result;
        //将字符串中的.替换成_，因为.是特殊字符，所以要用\.表达，又因为\是特殊字符，所以要用\\.来表达.
        str = str.replaceAll("[0-9]*", "");
        str = str.replaceAll(" ", " ");

        System.out.println(str);
    }
}
