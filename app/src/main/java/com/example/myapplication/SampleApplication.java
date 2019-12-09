package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import androidx.multidex.MultiDex;

import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.beta.interfaces.BetaPatchListener;
import com.tencent.tinker.loader.app.TinkerApplication;
import com.tencent.tinker.loader.shareutil.ShareConstants;

import cn.jpush.android.api.JPushInterface;

/**
 * 描述：
 *
 * @author Created by heiduo
 * @time Created on 2019/10/31
 */
public class SampleApplication extends TinkerApplication implements BetaPatchListener {
    /**
     * 主线程Handler
     */
    private static Handler mMainThreadHandler;

    public SampleApplication() {
        super(ShareConstants.TINKER_ENABLE_ALL, "com.example.myapplication.SampleApplicationLike",
                "com.tencent.tinker.loader.TinkerLoader", false);
    }
    protected SampleApplication(int tinkerFlags) {
        super(tinkerFlags);
    }

    protected SampleApplication(int tinkerFlags, String delegateClassName, String loaderClassName, boolean tinkerLoadVerifyFlag) {
        super(tinkerFlags, delegateClassName, loaderClassName, tinkerLoadVerifyFlag);
    }

    protected SampleApplication(int tinkerFlags, String delegateClassName) {
        super(tinkerFlags, delegateClassName);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mMainThreadHandler = new Handler();

        // 这里实现SDK初始化，appId替换成你的在Bugly平台申请的appId
        // 调试时，将第三个参数改为true
//        Bugly.init(this, "850a3b0cc5", false);

        Beta.autoInit = true;//自动初始化
        Beta.autoCheckUpgrade = true;//
        Beta.upgradeCheckPeriod = 60 * 60 *1000;
        Beta.initDelay = 3 *1000;

        Log.d("simple application","onCreate");

//        JPushInterface.setDebugMode(true);
//        JPushInterface.init(this);     		// 初始化 JPush
//        JPushInterface.getRegistrationID(this);

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        // you must install multiDex whatever tinker is installed!
        MultiDex.install(base);


        // 安装tinker
        Beta.installTinker();

        Beta.betaPatchListener = this;
    }

    /**
     * 获取主线程的handler
     */
    public static Handler getMainThreadHandler() {
        return mMainThreadHandler;
    }

    @Override
    public void onPatchReceived(String s) {

    }

    @Override
    public void onDownloadReceived(long l, long l1) {

    }

    @Override
    public void onDownloadSuccess(String s) {

    }

    @Override
    public void onDownloadFailure(String s) {

    }

    @Override
    public void onApplySuccess(String s) {
        /**补丁包应用成功回调，在这里杀进程，重启app，完成热更新。
         否则需要等待用户下次自己主动杀进程重启后才能完成更新*/
//        restartApp();
    }

    @Override
    public void onApplyFailure(String s) {

    }

    @Override
    public void onPatchRollback() {

    }

    /**
     * 杀进程，重启app
     */
    private void restartApp() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
