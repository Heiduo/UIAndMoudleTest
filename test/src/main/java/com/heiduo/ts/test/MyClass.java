package com.heiduo.ts.test;

import com.heiduo.ts.test.BuilderPattern.Actor;
import com.heiduo.ts.test.BuilderPattern.ActorBuilder;
import com.heiduo.ts.test.BuilderPattern.ActorController;
import com.heiduo.ts.test.Utils.XMLUtils;

public class MyClass {
    public static void main(String []args){
        System.out.println("math: sin 30:" + Math.sin(1));
        String s1 = "1.0.2";
        String s2 = "1.0.5";
        System.out.println("small: " + s1.compareTo(s2) + ", big:" + s2.compareTo(s1));
        String s3 = "1.1.1.1";
        System.out.println("small: " + s2.compareTo(s3) + ", big:" + s3.compareTo(s2));

        ActorBuilder ab = (ActorBuilder) XMLUtils.getBean();
        ActorController ac = new ActorController();
        Actor actor = ac.construct(ab);

        System.out.println("actor:" + actor.toString());
    }
}
