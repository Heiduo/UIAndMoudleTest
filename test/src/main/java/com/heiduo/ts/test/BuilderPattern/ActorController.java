package com.heiduo.ts.test.BuilderPattern;

/**
 * 描述：
 *
 * @author Created by heiduo
 * @time Created on 2019/12/14
 */
public class ActorController {
    public Actor construct(ActorBuilder ab){
        Actor actor;
        ab.buildType();
        ab.buildSex();
        ab.buildFace();
        ab.buildCostume();
        ab.buildHairstyle();
        return ab.creteActor();
    }
}
