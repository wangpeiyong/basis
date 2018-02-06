package org.wpy.classloader.hostswap2;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandlerFactory;
import java.util.Arrays;
import java.util.Optional;

public class MyURLClassLoad extends URLClassLoader {
    public MyURLClassLoad(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }

    public MyURLClassLoad(URL[] urls) {
        super(urls);
    }

    public MyURLClassLoad(URL[] urls, ClassLoader parent, URLStreamHandlerFactory factory) {
        super(urls, parent, factory);
    }


    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        String fileName = name.substring(name.lastIndexOf(".") + 1, name.length());
        Optional<File> file1 = Arrays.stream(super.getURLs()).map(item -> new File(item.getPath() + File.separator + fileName + ".class"))
                .filter(file -> file.exists()).findAny();
        if (file1.isPresent()) {
            File file = file1.get();
            try {
                byte[] data = FileUtils.readFileToByteArray(file);
                Class<?> aClass = defineClass(name, data, 0, data.length);
                return aClass;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return super.findClass(name);
    }
}
