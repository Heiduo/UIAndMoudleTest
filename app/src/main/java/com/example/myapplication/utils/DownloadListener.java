package com.example.myapplication.utils;

/**
 * 描述：
 *
 * @author Created by heiduo
 * @time Created on 2019/11/20
 */
public interface DownloadListener {

    /**
     *  开始下载
     */
    void start(long max);
    /**
     *  正在下载
     */
    void loading(int progress);
    /**
     *  下载完成
     */
    void complete(String path);
    /**
     *  请求失败
     */
    void fail(String code, String message);
    /**
     *  下载过程中失败
     */
    void loadfail(String message);
}