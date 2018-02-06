package org.wpy.nio;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * DESC
 *
 * @author
 * @create 2017-07-17 下午2:10
 **/
public class NioTest {

    public static void main(String[] args) throws Exception {
        writeFileByNioMap();
        readFileByNioMap();
    }


    public static void readWriteFileByNio() throws Exception {
        RandomAccessFile randomAccessFile = new RandomAccessFile("/Users/wpy/IdeaProjects/myproject/hw-bigdata/java-basic/src/main/java/org/wpy/io/data.txt", "rw");
        FileChannel fileChannel = randomAccessFile.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        fileChannel.read(byteBuffer);
        byteBuffer.flip();
        while (byteBuffer.hasRemaining()) {
            System.out.print((char) byteBuffer.get());
        }
        System.out.println(byteBuffer.limit());

        byteBuffer.clear();
        byteBuffer.put("wpy-2013432\n".getBytes());
        byteBuffer.put("lvhuawei-2013432\n".getBytes());
        byteBuffer.flip();

        System.out.println(byteBuffer.limit());
        fileChannel.write(byteBuffer);
        fileChannel.close();
        randomAccessFile.close();
    }

    public static void readFileByNioMap() throws Exception {
        RandomAccessFile randomAccessFile = new RandomAccessFile("/Users/wpy/IdeaProjects/myproject/hw-bigdata/java-basic/src/main/java/org/wpy/io/data.txt", "rw");
        MappedByteBuffer mappedByteBuffer = randomAccessFile.getChannel().map(FileChannel.MapMode.READ_WRITE, 0, randomAccessFile.length());

        while (mappedByteBuffer.hasRemaining())
            System.out.print((char) mappedByteBuffer.get());

        mappedByteBuffer.clear();
        randomAccessFile.close();
    }


    public static void writeFileByNioMap() throws Exception {
        RandomAccessFile randomAccessFile = new RandomAccessFile("/Users/wpy/IdeaProjects/myproject/hw-bigdata/java-basic/src/main/java/org/wpy/io/data.txt", "rw");

        char[] msg = "2017-07-17".toCharArray();
        CharBuffer mappedByteBuffer = randomAccessFile.getChannel().map(FileChannel.MapMode.READ_WRITE, randomAccessFile.length(), randomAccessFile.getChannel().size()).asCharBuffer();

        mappedByteBuffer.put(msg);
        randomAccessFile.close();
    }


    public static void writeFileByNioMap2() throws Exception {
        RandomAccessFile randomAccessFile = new RandomAccessFile("/Users/wpy/IdeaProjects/myproject/hw-bigdata/java-basic/src/main/java/org/wpy/io/data.txt", "rw");
        FileChannel channel = randomAccessFile.getChannel();
        MappedByteBuffer mappedByteBuffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, channel.size());
        mappedByteBuffer.put("2017-07-17".getBytes("utf-8"));
        randomAccessFile.close();
    }
}
