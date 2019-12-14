package com.heiduo.ts.test.BuilderPattern;

/**
 * 描述：
 *
 * @author Created by heiduo
 * @time Created on 2019/12/14
 */
public class Actor {
    private String type;
    private String sex;
    private String face;
    private String costume;
    private String hairstyle;

    @Override
    public String toString() {
        return "Actor{" +
                "type='" + type + '\'' +
                ", sex='" + sex + '\'' +
                ", face='" + face + '\'' +
                ", constume='" + costume + '\'' +
                ", hairstyle='" + hairstyle + '\'' +
                '}';
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getFace() {
        return face;
    }

    public void setFace(String face) {
        this.face = face;
    }

    public String getCostume() {
        return costume;
    }

    public void setCostume(String constume) {
        this.costume = constume;
    }

    public String getHairstyle() {
        return hairstyle;
    }

    public void setHairstyle(String hairstyle) {
        this.hairstyle = hairstyle;
    }
}
