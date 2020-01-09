package com.example.myapplication.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.myapplication.R;
import com.example.myapplication.core.SampleApplication;
import com.example.myapplication.core.WDActivity;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

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
 * @time Created on 2020/1/6
 */
public class DownlodUtils {
    private static final String TAG = "Download";

    private int TYPE;
    public static final int DOWNLOAD_APK = 0;
    public static final int DOWNLOAD_OTHER = 1;

    private final int NotificationID = 0x0001;
    private NotificationManager mNotificationManager = null;
    private NotificationCompat.Builder builder;

    // 文件保存路径(如果有SD卡就保存SD卡,如果没有SD卡就保存到手机包名下的路径)
    private String APK_dir = "";
    /**
     * @param file_url    下载链接
     * @param target_name 保存路径
     */


    public static void DownFile(String file_url, String target_name, DownloadListener downloadListener) {
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

    public DownloadListener downloadListener = new DownloadListener() {
        long max  = 0;
        @Override
        public void start(long max) {
            UIUtils.showToastSafe("开始下载文件");
            this.max = max;
            String id = "dongniID";
            String name = "dongni";
            mNotificationManager = (NotificationManager) SampleApplication.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel mChannel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_LOW);
                Logger.d(TAG, "DownloadApk:" + mChannel.toString());
                mNotificationManager.createNotificationChannel(mChannel);
                builder = new NotificationCompat.Builder(SampleApplication.getContext(), id);
                builder.setChannelId(id);
            }
            builder.setSmallIcon(R.mipmap.ic_launcher)
                    .setTicker("正在下载新版本")
                    .setContentTitle(ExampleUtil.getVersion(SampleApplication.getContext()))
                    .setContentText("正在下载，请稍后...")
                    .setWhen(System.currentTimeMillis()) //设置时间
                    .setNumber(0)

                    .setAutoCancel(true);
            mNotificationManager.notify(NotificationID,builder.build());
        }

        @Override
        public void loading(int progress) {
            Logger.d(TAG,"文件下载中");
            int length =  Long.valueOf(max).intValue();
            builder.setProgress(length,progress,false);
            builder.setContentInfo(StringUtils.getPercent(progress,length));
            mNotificationManager.notify(NotificationID,builder.build());
        }

        @Override
        public void complete(String path) {
            Logger.d(TAG, "下载完成");
            //开始安装
            Intent installIntent = new Intent(Intent.ACTION_VIEW);
            Uri uri = Uri.fromFile(new File(path));
            installIntent.setDataAndType(uri, "application/vnd.android.package-archive");
            installIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent mPI = PendingIntent.getActivity(SampleApplication.getContext(), 0, installIntent, 0);

            builder.setContentText("下载完成，点击安装");
            builder.setContentIntent(mPI);
            mNotificationManager.notify(NotificationID, builder.build());

//            startActivity(installIntent);
            ApkController.install(path,SampleApplication.getContext());
            mNotificationManager.cancel(NotificationID);
        }

        @Override
        public void fail(String code, String message) {
            UIUtils.showToastSafe("文件下载失败");
            Logger.d(TAG,"文件下载失败");
        }

        @Override
        public void loadfail(String message) {
//            UIUtils.showToastSafe("文件下载失败");
            Logger.d(TAG,"文件加载失败");

        }
    };


}
