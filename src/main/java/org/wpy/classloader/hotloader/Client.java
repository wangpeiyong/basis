package org.wpy.classloader.hotloader;


public class Client {

    public static void main(String[] args) {
        Server server = new Server();
        server.init();
        int i = 0;
        while (true) {
            i++;
            String name = "name" + i;
            String result = server.doWork(name);
            System.out.println(result);
            try {
                Thread.sleep(1000 * 5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }

}
