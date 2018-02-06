package org.wpy.classloader.hotloader;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Date;

public class MyClassLoader extends URLClassLoader {

    Date startDate = new Date();
    private MyClassLoader loader = null;

    public MyClassLoader(URL[] urls) {
        super(urls);
    }

    public MyClassLoader(ClassLoader parent) {
        super(new URL[0], parent);
    }

    @Override
    public void close() throws IOException {
        super.close();
    }

    /**
     * Adds a jar file from the filesystems into the jar loader list.
     *
     * @param jarfile The full path to the jar file.
     * @throws MalformedURLException
     */
    public void addJarFile(String jarfile) throws MalformedURLException {
        URL url = new URL("file:" + jarfile);
        addURL(url);
    }

    public void addDir(String path) throws MalformedURLException {
        path = "file:" + path;
        URL url = new URL(path);
        addURL(url);
    }


    @Override
    public String toString() {
        return super.toString() + ",time:" + startDate.toLocaleString();
    }

}
