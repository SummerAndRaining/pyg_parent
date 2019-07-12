package cn.itcast.core.service.design_mode;

public class Singleton {
    //单例模式_饿汉式
//    private static Singleton instance = new Singleton();
//    private Singleton(){}
//    public static Singleton getInstance(){
//        return instance;
//    }

    //单例模式_懒汉式
    private static Singleton instence = null;

    private Singleton() {
    }

    public static synchronized Singleton getInstence() {
        if (instence == null) {
            instence = new Singleton();
        }
        return instence;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 100000; i++) {
            System.out.println(Singleton.getInstence());
        }
    }
}
