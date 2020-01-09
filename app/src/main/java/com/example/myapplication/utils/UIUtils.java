package com.example.myapplication.utils;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import com.example.myapplication.core.SampleApplication;
import com.example.myapplication.core.WDActivity;

/**
 * 描述：
 *
 * @author Created by heiduo
 * @time Created on 2020/1/6
 */
public class UIUtils {
    public static Context getContext(){
        return SampleApplication.getContext();
    }

    public static long getMainThreadId() {
        return SampleApplication.getMainThreadId();
    }

    // 判断当前的线程是不是在主线程
    public static boolean isRunInMainThread() {
        return android.os.Process.myTid() == getMainThreadId();
    }

    /**
     * 获取主线程的handler
     */
    public static Handler getHandler() {
        return SampleApplication.getMainThreadHandler();
    }

    /**
     * 在主线程执行runnable
     */
    public static boolean post(Runnable runnable) {
        return getHandler().post(runnable);
    }

    public static void showToastSafe(final String str){
//        Toast.makeText(getContext(),str,Toast.LENGTH_LONG).show();
        if (isRunInMainThread()) {
            showToast(str);
        } else {
            post(new Runnable() {
                @Override
                public void run() {
                    showToast(str);
                }
            });
        }
    }

    private static void showToast(String str) {
        WDActivity frontActivity = WDActivity.getForegroundActivity();
        if (frontActivity != null) {
//            Toast.makeText(frontActivity, str, Toast.LENGTH_LONG).show();
            Toast.makeText(frontActivity, str, Toast.LENGTH_LONG).show();
        }
    }

}
