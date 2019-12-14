package com.heiduo.ts.test.BuilderPattern;

/**
 * 描述：
 *
 * @author Created by heiduo
 * @time Created on 2019/12/14
 */
public abstract class ActorBuilder {
    protected Actor actor = new Actor();
    public abstract void buildType();
    public abstract void buildSex();
    public abstract void buildFace();
    public abstract void buildCostume();
    public abstract void buildHairstyle();

    public Actor creteActor(){
        return actor;
    }
}
