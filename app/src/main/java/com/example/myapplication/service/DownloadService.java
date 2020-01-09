package com.example.myapplication.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.content.FileProvider;

import com.example.myapplication.Bean.Constants;
import com.example.myapplication.Bean.CustomEvent;
import com.example.myapplication.R;
import com.example.myapplication.core.SampleApplication;
import com.example.myapplication.utils.ApkController;
import com.example.myapplication.utils.DownloadListener;
import com.example.myapplication.utils.DownloadResponseBody;
import com.example.myapplication.utils.ExampleUtil;
import com.example.myapplication.utils.Logger;
import com.example.myapplication.utils.StringUtils;
import com.example.myapplication.utils.UIUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 描述：
 *
 * @author Created by heiduo
 * @time Created on 2019/11/20
 */
public class DownloadService extends MyService {
    private static final String TAG = "DownLoad";
    private int TYPE;
    public static final int DOWNLOAD_APK = 0;
    public static final int DOWNLOAD_OTHER = 1;

    public static final String DOWNLOAD_TYPE = "download";
    public static final String APK_URL = "apk_url";
    public static final String APK_NAME = "apk_name";
    public static final String APK_PATH = "apk_path";
    private String apk_url;
    private String apk_name;
    private String apk_path;

    private final int NotificationID = 0x0001;
    private NotificationManager mNotificationManager = null;
    private NotificationCompat.Builder builder;

    private static boolean downloadApk = false;

    // 文件保存路径(如果有SD卡就保存SD卡,如果没有SD卡就保存到手机包名下的路径)
    private String APK_dir = "";

    /*@IntDef ({DOWNLOAD_APK,DOWNLOAD_OTHER})
    private @interface DOWNLOAD_TYPE;*/


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //保存下载位置
//        initDownloadDir();
        Logger.d(TAG, "onCreate");
        APK_dir = getDownloadDir();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Logger.d(TAG, "onStartCommand");
        if (null != intent) {
            TYPE = intent.getIntExtra(DOWNLOAD_TYPE, 0);
            Logger.d(TAG, "开始下载");

            switch (TYPE) {
                case DOWNLOAD_APK:
                    if (!downloadApk) {
                        downloadApk = true;
                        apk_url = intent.getStringExtra(APK_URL);
                        apk_name = intent.getStringExtra(APK_NAME);

                        DownFile(apk_url, APK_dir + "/" + "TvScreen" + apk_name + ".apk", downloadListener);
                    } else {
                        Logger.d(TAG, "已有下载任务");
                    }

                    break;
                case DOWNLOAD_OTHER:

                    break;
                default:
                    break;
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopSelf();
    }

    /**
     * @param file_url    下载链接
     * @param target_name 保存路径
     */
    private void DownFile(String file_url, String target_name, DownloadListener downloadListener) {
        Request request = new Request.Builder()
                .url(file_url)
                .build();

        //重写ReposeBody监听下载进度
        Interceptor interceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response mResponse = chain.proceed(chain.request());
                return mResponse.newBuilder()
                        .body(new DownloadResponseBody(mResponse, downloadListener))
                        .build();
            }
        };

        OkHttpClient.Builder dlOkhttp = new OkHttpClient.Builder()
                .addInterceptor(interceptor);
        //绕开证书
//        setSSL(dlOkhttp);

        //发起请求
        Call call = dlOkhttp.build().newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Logger.e(TAG, "下载失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Logger.d(TAG, "下载中");
                long length = response.body().contentLength();
                if (length == 0) {
                    //文件下载完毕,跳转安装
                    downloadListener.complete(target_name);
                    return;
                }
                downloadListener.start(length);
                //保存文件到本地
                InputStream is = null;
                RandomAccessFile randomAccessFile = null;
                BufferedInputStream bis = null;
                byte[] buff = new byte[2048];
                int len = 0;

                try {
                    is = response.body().byteStream();
                    bis = new BufferedInputStream(is);

                    File file = new File(target_name);
                    //随机访问文件，为以后断点续传准备
                    randomAccessFile = new RandomAccessFile(file, "rwd");
//                randomAccessFile.seek(startPoint);
                    while ((len = bis.read(buff)) != -1) {
                        randomAccessFile.write(buff, 0, len);
                    }

                    //下载完成
                    downloadListener.complete(String.valueOf(file.getAbsoluteFile()));
                    Logger.d(TAG, "download complete:" + file.getAbsoluteFile());
                } catch (Exception e) {
                    e.printStackTrace();
                    downloadListener.loadfail(e.getMessage());
                } finally {
                    if (is != null) {
                        is.close();
                    }
                    if (bis != null) {
                        bis.close();
                    }
                    if (randomAccessFile != null) {
                        randomAccessFile.close();
                    }
                }
            }
        });

    }

    DownloadListener downloadListener = new DownloadListener() {
        long max = 0;

        @Override
        public void start(long max) {
            UIUtils.showToastSafe("开始下载文件");
            this.max = max;
            String id = "dongniID";
            String name = "dongni";
            mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            builder = new NotificationCompat.Builder(DownloadService.this, id);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel mChannel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_LOW);
                Logger.d(TAG, "DownloadApk:" + mChannel.toString());
                mNotificationManager.createNotificationChannel(mChannel);
                builder.setChannelId(id);
            }
            builder.setSmallIcon(R.mipmap.ic_launcher)
                    .setTicker("正在下载新版本")
                    .setContentTitle(ExampleUtil.getVersion(DownloadService.this))
                    .setContentText("正在下载，请稍后...")
                    .setWhen(System.currentTimeMillis()) //设置时间
                    .setNumber(0)
                    .setAutoCancel(true);
            mNotificationManager.notify(NotificationID, builder.build());
        }

        @Override
        public void loading(int progress) {
            Logger.d(TAG, "文件下载中");
            int length = Long.valueOf(max).intValue();
            builder.setProgress(length, progress, false);
            builder.setContentInfo(StringUtils.getPercent(progress, length));
            mNotificationManager.notify(NotificationID, builder.build());
        }

        @Override
        public void complete(String path) {
            Logger.d(TAG, "下载完成");
            //开始安装
            Intent installIntent = new Intent(Intent.ACTION_VIEW);
            Uri uri = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                uri = FileProvider.getUriForFile(DownloadService.this,
                        getApplication().getPackageName() + ".fileProvider", new File(path));
            } else {
                uri = Uri.fromFile(new File(path));
            }
            installIntent.setDataAndType(uri, "application/vnd.android.package-archive");
            installIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent mPI = PendingIntent.getActivity(DownloadService.this, 0, installIntent, 0);

            builder.setContentText("下载完成，点击安装");
            builder.setContentIntent(mPI);
//            mNotificationManager.notify(NotificationID, builder.build());

            stopSelf();
//            startActivity(installIntent);
//            mNotificationManager.cancel(NotificationID);

            SampleApplication.getShare().edit().putString(APK_NAME, apk_name)
                    .putString(APK_PATH, path)
                    .apply();
            EventBus.getDefault().post(new CustomEvent(Constants.DOWNLOAD_PATH, path));

            downloadApk = false;

        }

        @Override
        public void fail(String code, String message) {
            UIUtils.showToastSafe("文件下载失败");
            Logger.d(TAG, "文件下载失败");
            downloadApk = false;
        }

        @Override
        public void loadfail(String message) {
//            UIUtils.showToastSafe("文件下载失败");
            Logger.d(TAG, "文件加载失败");
            downloadApk = false;
        }
    };

    private String getDownloadDir() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
    }

    public static boolean isDownloadApk() {
        return downloadApk;
    }
}
