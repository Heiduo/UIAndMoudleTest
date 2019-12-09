package com.heiduo.ts.test;

public class MyClass {
    public static void main(String []args){
        System.out.println("math: sin 30:" + Math.sin(1));
        String s1 = "1.0.2";
        String s2 = "1.0.5";
        System.out.println("small: " + s1.compareTo(s2) + ", big:" + s2.compareTo(s1));
        String s3 = "1.1.1.1";
        System.out.println("small: " + s2.compareTo(s3) + ", big:" + s3.compareTo(s2));

    }
}
