package com.example.myapplication.view;

import java.io.Serializable;

/**
 * 描述：
 *
 * @author Created by heiduo
 * @time Created on 2019/11/25
 */
public class DataBean {
    private String data;
    private String code;
    private boolean flag;

    public DataBean() {
    }

    public DataBean(String data, String code, boolean flag) {
        this.data = data;
        this.code = code;
        this.flag = flag;
    }

    @Override
    public String toString() {
        return "DataBean{" +
                "data='" + data + '\'' +
                ", code='" + code + '\'' +
                ", flag=" + flag +
                '}';
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}
