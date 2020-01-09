package com.example.myapplication.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.myapplication.MainActivity;
import com.example.myapplication.utils.UIUtils;

/**
 * 我们通过广播来启动Activity的时候如果不设置intent的FLAG_ACTIVITY_NEW_TASK属性，就会报这个异常：
 * android.util.AndroidRuntimeException:
 * Calling startActivity() from outside of an Activity  context requires the FLAG_ACTIVITY_NEW_TASK flag.
 * 就是说在activity上下文之外调用startActivity需要FLAG_ACTIVITY_NEW_TASK属性。
 * @author Administrator
 */
public class InstallReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.PACKAGE_REPLACED")){
            //            Toast.makeText(context,"升级了一个安装包",Toast.LENGTH_SHORT).show();
//            Logc.d("静默启动成功");
            UIUtils.showToastSafe("静默启动成功");
            Intent intent2 = new Intent(context, MainActivity.class);
            intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent2);
        }
    }
}