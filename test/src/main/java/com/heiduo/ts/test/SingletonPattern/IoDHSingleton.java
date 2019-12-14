package com.heiduo.ts.test.SingletonPattern;

/**
 * 描述：Initialization Demand Holder
 * 仅限java
 * @author Created by heiduo
 * @time Created on 2019/12/14
 */
public class IoDHSingleton {
    private IoDHSingleton(){

    }

    private static class HolderClass{
        private final static IoDHSingleton instance = new IoDHSingleton();
    }

    public static IoDHSingleton getInstance(){
        return HolderClass.instance;
    }
}
