package org.wpy.value;

public class SingleInstance {

    //私有化构造方法
    private SingleInstance() {
    }

    public static SingleInstance getInstance() {
        System.out.println("getInstance");
        return SingletonHolder.instance;
    }

    // 静态内部类保存单例类实例
    private static class SingletonHolder {
        private static SingleInstance instance = new SingleInstance();

        static {
            System.out.println("....SingletonHolder init ....");
        }
    }


}
