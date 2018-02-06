package org.wpy.classloader.hotloader;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Date;


public class Server {

    String codePath = "D:\\java\\workspace\\busservice\\bin\\";
    String busServiceClass = "org.wpy.classloader.hotloader";
    BusService busService;

    public String doWork(String name) {
        if (null != busService) {
            return busService.doIt(name);
        }
        return "default";
    }

    public void init() {
        new Thread() {
            long lastTime = 0;

            public void run() {
                File f = new File(codePath + "version.txt");
                while (true) {
                    //判断是否更新
                    if (lastTime != f.lastModified()) {
                        lastTime = f.lastModified();
                        ClassLoader cl = this.getClass().getClassLoader();
                        System.out.println(cl);
                        MyClassLoader myLoader = new MyClassLoader(new URL[0]);
                        try {
                            myLoader.addDir(codePath);
                            Class<BusService> clazz = (Class<BusService>) myLoader.loadClass(busServiceClass);
                            BusService busService2 = clazz.newInstance();
                            BusService temp = busService;
                            busService = busService2;
                            temp.close();//释放资源，尤其是线程，若线程不关闭的话，则类不会卸载，且会一直运行。旧的类卸载

                            ClassLoader c = temp.getClass().getClassLoader();
                            if (c instanceof URLClassLoader)
                                ((URLClassLoader) c).close();//释放资源，卸载旧的classload。

                            System.out.println("busService:" + busService + "  ,classloader:" + busService.getClass().getClassLoader());
                            System.out.println("end test " + new Date().toLocaleString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }
}
