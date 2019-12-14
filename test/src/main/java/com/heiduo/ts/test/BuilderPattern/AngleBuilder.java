package com.heiduo.ts.test.BuilderPattern;

/**
 * 描述：
 *
 * @author Created by heiduo
 * @time Created on 2019/12/14
 */
public class AngleBuilder extends ActorBuilder {
    @Override
    public void buildType() {
        actor.setType("天使");
    }

    @Override
    public void buildSex() {
        actor.setSex("女");
    }

    @Override
    public void buildFace() {
        actor.setFace("漂亮");
    }

    @Override
    public void buildCostume() {
        actor.setCostume("翅膀");
    }

    @Override
    public void buildHairstyle() {
        actor.setHairstyle("长发");
    }
}
