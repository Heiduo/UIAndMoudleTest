package com.heiduo.ts.test.SingletonPattern;

/**
 * 描述：
 *
 * @author Created by heiduo
 * @time Created on 2019/12/14
 */
public class LazySynchronizedSingleton {
    private static LazySynchronizedSingleton instance = null;

    private LazySynchronizedSingleton() {

    }

    public static LazySynchronizedSingleton getInstance() {
        if (instance == null) {
            synchronized (LazySynchronizedSingleton.class){
                if(instance == null){
                    instance = new LazySynchronizedSingleton();
                }
            }
        }
        return instance;
    }
}
