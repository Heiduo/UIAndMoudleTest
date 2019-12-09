package com.example.myapplication.view;

/**
 * 描述：
 *
 * @author Created by heiduo
 * @time Created on 2019/11/26
 */
public class DataBeanResult {
    private DataBean data;

    @Override
    public String toString() {
        return "DataBeanResult{" +
                "data=" + data +
                '}';
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }
}
