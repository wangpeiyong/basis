package org.wpy.io;

import java.io.*;

/**
 * DESC
 *
 * @author
 * @create 2017-07-06 下午12:21
 **/
@SuppressWarnings("all")
public class IOOp {
    public static void main(String[] args) throws Exception {
        RandomAccessFile randomAccessFile = new RandomAccessFile("/Users/wpy/IdeaProjects/myproject/hw-bigdata/java-basic/src/main/java/org/wpy/App.java", "r");

        byte[] data = new byte[1024];
        String context = null;
        while ((context = randomAccessFile.readLine()) != null) {
            if (context.contains("*")) {

                // skip 滑过 10byte
                randomAccessFile.skipBytes(10);

                // 定位到第1000的pos
                // randomAccessFile.seek(1000);
            }
            System.out.println(context);
        }


        RandomAccessFile randomAccessFile1 = new RandomAccessFile("/Users/wpy/IdeaProjects/myproject/hw-bigdata/java-basic/src/test/raf.txt", "rw");
        randomAccessFile1.writeUTF("wpy");
        randomAccessFile1.close();
        randomAccessFile1 = new RandomAccessFile("/Users/wpy/IdeaProjects/myproject/hw-bigdata/java-basic/src/test/raf.txt", "rw");
        System.out.println(randomAccessFile1.readUTF());


        randomAccessFile.close();
        randomAccessFile1.close();


        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File("/Users/wpy/IdeaProjects/myproject/hw-bigdata/java-basic/src/test/data.txt")));
        Student student = new Student(new Person("wang", 24), 90.3);
        oos.writeObject(student);
        oos.flush();
        oos.close();


        /**
         * 对象序列化 是将对象实例中对象的引用也做了序列化（对象中的对象的属性也序列化了）
         */
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File("/Users/wpy/IdeaProjects/myproject/hw-bigdata/java-basic/src/test/data.txt")));
        Student o = (Student) ois.readObject();
        System.out.println(o.getPerson().getName());
        ois.close();

    }


}

class Person implements Serializable {
    public String name;
    public int age;

    public Person() {
    }

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}


class Student implements Serializable {

    private Person person;
    private Double score;

    public Student() {
    }

    public Student(Person person, Double score) {
        this.person = person;
        this.score = score;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }
}
