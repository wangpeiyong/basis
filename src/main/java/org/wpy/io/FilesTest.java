package org.wpy.io;

import java.io.IOException;
import java.nio.file.*;

/**
 * DESC
 *
 * @author
 * @create 2017-07-07 上午11:49
 **/
public class FilesTest {
    public static void main(String[] args) throws IOException {
        Path path = Paths.get("/Users/wpy/IdeaProjects/myproject/hw-bigdata/java-basic/src/main/java/org/wpy/io/data.txt");
        System.out.println(Files.exists(path, LinkOption.NOFOLLOW_LINKS));
        if (Files.exists(path, LinkOption.NOFOLLOW_LINKS))
            Files.deleteIfExists(path);
        //Files.createTempFile()
        Files.write(path, "wpy".getBytes(), StandardOpenOption.CREATE);
        System.out.println(Files.readAllLines(path));
    }
}
