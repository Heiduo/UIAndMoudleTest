package com.example.myapplication.core;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;

import androidx.multidex.MultiDex;

import com.example.myapplication.MainActivity;
import com.example.myapplication.utils.ApkController;
import com.example.myapplication.utils.Logger;
import com.parry.zxing.activity.ZXingLibrary;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.beta.UpgradeInfo;
import com.tencent.bugly.beta.download.DownloadListener;
import com.tencent.bugly.beta.download.DownloadTask;
import com.tencent.bugly.beta.interfaces.BetaPatchListener;
import com.tencent.bugly.beta.upgrade.UpgradeListener;
import com.tencent.tinker.loader.app.TinkerApplication;
import com.tencent.tinker.loader.shareutil.ShareConstants;

import cn.bmob.v3.Bmob;
import cn.jpush.android.api.JPushInterface;

/**
 * 描述：
 *
 * @author Created by heiduo
 * @time Created on 2019/10/31
 */
public class SampleApplication extends TinkerApplication implements BetaPatchListener {
    private static final String TAG = "SampleApplication";
    /**
     * 主线程Handler
     */
    private static Handler mMainThreadHandler;

    private static Context mContext;
    /**
     * 主线程ID
     */
    private static int mMainThreadId = -1;

    private static SharedPreferences share;

    public SampleApplication() {
        super(ShareConstants.TINKER_ENABLE_ALL, "com.example.myapplication.core.SampleApplicationLike",
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

        mContext = this;
        mMainThreadId = android.os.Process.myTid();
        mMainThreadHandler = new Handler();
        share = getSharedPreferences("shares.xml",MODE_PRIVATE);

        // 这里实现SDK初始化，appId替换成你的在Bugly平台申请的appId
        // 调试时，将第三个参数改为true
        Bugly.init(this, "850a3b0cc5", false);

        Log.d("simple application","onCreate");


        Bmob.initialize(this,"aafe4282cf8d96dbee2d58d12a9e9451");

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
//
//        Beta.betaPatchListener = this;
//        Beta.downloadListener = mDownloadListener;
//        Beta.upgradeListener= mUpgradeListener;

        ZXingLibrary.initDisplayOpinion(this);
    }
    /**
     * 获取主线程ID
     */
    public static int getMainThreadId() {
        return mMainThreadId;
    }

    /**
     * 获取主线程的handler
     */
    public static Handler getMainThreadHandler() {
        return mMainThreadHandler;
    }

    UpgradeListener mUpgradeListener = new UpgradeListener() {
        @Override
        public void onUpgrade(int i, UpgradeInfo upgradeInfo, boolean b, boolean b1) {
            Logger.d(TAG,"onUpgrade");
            Logger.d(TAG,"ret:" + i + /*",upgrade:"+ upgradeInfo.versionName +*/ "\n isManual:" + b + ",isSlicence:" + b1);
        }
    };

    DownloadListener mDownloadListener= new DownloadListener() {
        @Override
        public void onReceive(DownloadTask downloadTask) {
            Logger.d(TAG,"onReceive");
        }

        @Override
        public void onCompleted(DownloadTask downloadTask) {
            Logger.d(TAG,"onCompleted:" + downloadTask.getSaveFile().getAbsolutePath());
            ApkController.install(downloadTask.getSaveFile().getAbsolutePath(),mContext);
        }

        @Override
        public void onFailed(DownloadTask downloadTask, int i, String s) {
            Logger.d(TAG,"onFailed：" + s);
        }
    };

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
        restartApp();
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

    public static Context getContext(){
        return mContext;
    }

    public static Handler getHandler(){
        return mMainThreadHandler;
    }

    public static SharedPreferences getShare(){
        return share;
    }
}
