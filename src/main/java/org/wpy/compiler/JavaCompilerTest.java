package org.wpy.compiler;

import javax.tools.JavaCompiler;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * java 编译java 为class
 *
 * @author wpy
 */
public class JavaCompilerTest {

    public static final String filename = "D:/JavaTest.java";

    public static void main(String[] args) throws IOException, ClassNotFoundException, Exception {

        //得到系统当前的路径,即是java工程的路径(E:\workspace\base)
        System.out.println(System.getProperty("user.dir"));

        //在程序里对java文件进行编译，也应该就是动态编译了
        writeJavaFile();


        //得到系统当前的java编译器，也就是javac
        JavaCompiler javac = ToolProvider.getSystemJavaCompiler();


        //不能是jre的运行环境，因为这个是纯净版本，不包含javac等，需要使用jdk才行,否则会是null
        //先得到一个文件管理对象
        //该对象的第一个参数是诊断监听器,
        StandardJavaFileManager javaFile = javac.getStandardFileManager(null, null, null);

        //编译单元，可以有多个
        Iterable units = javaFile.getJavaFileObjects(filename);
        //编译任务
        JavaCompiler.CompilationTask compilationTask = javac.getTask(null, javaFile, null, null, null, units);
        compilationTask.call();
        javaFile.close();

        ////把刚才在D:/下生成的class文件JavaTest.class加载进内存并生成实例对象
        URL[] urls = new URL[]{new URL("file:/d:/")};
        URLClassLoader classLoad = new URLClassLoader(urls);
        Class clazz = classLoad.loadClass("JavaTest");
        Method method = clazz.getMethod("main", String[].class);

        //注意，调用Method类的方法invoke(Object,Object), main方法是类Run的静态方法，调用时是不需要对象实例的。
        //Java中通过反射调用其他类中的main方法时要注意的问题

        method.invoke(clazz.newInstance(), (Object) new String[]{});

        AccessController.doPrivileged((PrivilegedAction<File>) () -> new File(""));
    }


    /**
     * 创建java文件
     *
     * @throws IOException
     */
    public static void writeJavaFile() throws IOException {
        File file = new File("D:/JavaTest.java");
        if (!file.exists()) {
            file.createNewFile();
        }
        FileWriter fw = new FileWriter(file);
        fw.write(javaClassContent());
        fw.close();
    }


    /**
     * 用字符串连接成一个java类
     *
     * @return
     */
    public static String javaClassContent() {
        String rt = "\r\n";
        String java = new String();
        java += "public class JavaTest{" + rt;
        java += " public static void main(String[] args){" + rt;
        java += "     System.out.println(\"hello world\");" + rt;
        java += "     show();" + rt;
        java += " }" + rt;
        java += " public static void show(){" + rt;
        java += "     for(int i=0;i<4;i++){" + rt;
        java += "         System.out.println(\"i=:\"+i);" + rt;
        java += "     }" + rt;
        java += " }" + rt;
        java += "}";
        return java;
    }


}


