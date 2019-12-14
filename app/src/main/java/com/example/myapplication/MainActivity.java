package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.example.myapplication.CrystalBall.CrystalBallTrueActivity;
import com.example.myapplication.Transition.FadeActivity;
import com.example.myapplication.view.DataBean;
import com.example.myapplication.view.RoundActivity;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.parry.zxing.activity.CaptureActivity;
import com.parry.zxing.activity.CodeUtils;

import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.OnClick;

public class MainActivity extends WDActivity {
    public static final String TAG = "main";
    public static final int REQUEST_CAMERA = 101;
    public static final int REQUEST_CAPTURE = 102;
    LineChart lineChart;
    TextView tvNotification;
    TextView tvCheckUpdate;

    TextView tvSystemTime;
    TextView tvCurrentTime;

    BluetoothAdapter bluetoothAdapter = null;

    NotificationManager manager;
    private int count = 0;

    private boolean clicked = false;

    MyJPushMessageReceiver registerReceiver;

    Calendar calendar;
    long timeDifference;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            long time2 = (long) msg.obj;
            tvCurrentTime.setText(format.format(new Date(time2)));
            Logger.e(TAG,"date2:" + format.format(new Date(time2)));
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置退出动画
        setupWindowAnimations();
        init();

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    private void init() {
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    @Override
    public void initView() {
        setBarStatusColor(true);
        //获取版本号
        Log.d(TAG,"版本code ：" + getVersionCode(this) + ", 版本名：" + getVersionName(this));
        //手机系统版本 型号 厂商
        Log.d(TAG,"系统版本：" + getSystemVersion()
                + "\n手机型号：" + getDeviceBrand()+ " " + getSystemModel());

        SharedPreferences shared = getSharedPreferences("share.xml",MODE_PRIVATE);

        DataBean dataBean =  new DataBean("test","code",true);
        String data = JSONObject.toJSONString(dataBean);
        Logger.e(TAG,"data0:" + data);
        shared.edit().putString("data",data).commit();
        String data2 = shared.getString("data","");
        Logger.e(TAG,"data1:" + data2);
        DataBean dataBean1 = JSONObject.parseObject(data2,DataBean.class);
        Logger.e(TAG,"data2:" + dataBean1.toString());

        tvNotification = findViewById(R.id.tvNotification);
        tvCheckUpdate = findViewById(R.id.tvCheckUpdate);
        tvNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count++;
                Intent intent = new Intent(MainActivity.this,TestActivity.class);//通知跳转测试
                intent.putExtra("test","通知内容");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent pi = PendingIntent.getActivity(MainActivity.this,0,intent,PendingIntent.FLAG_CANCEL_CURRENT);
                NotificationCompat.Builder notify = new NotificationCompat.Builder(MainActivity.this ,"1");
                notify.setContentTitle("通知测试")  //标题
                        .setContentText("通知内容") //内容
                        .setWhen(System.currentTimeMillis()) //设置时间
                        .setSmallIcon(R.mipmap.ic_launcher) //
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher_round))
                        .setContentIntent(pi)//设置点击事件
                        .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                        .setVisibility(Notification.VISIBILITY_PRIVATE)
                        .setAutoCancel(true);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){//8.0以上需要添加通知渠道
                    String channelID = "1";
                    String channelName = "通知测试";
                    NotificationChannel channel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_DEFAULT);
                    manager.createNotificationChannel(channel);
                    //创建通知时指定channelID
                    notify.setChannelId(channelID);
                }

                manager.notify(count,notify.build());
                tvNotification.setText(tvNotification.getText().toString() + count);
            }
        });

        BluetoothManager manager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
        bluetoothAdapter = manager.getAdapter();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{/*Manifest.permission.ACCESS_COARSE_LOCATION,*/Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }

        /*if (!bluetoothAdapter.isEnabled()){
            bluetoothAdapter.enable();

        }else {
            bluetoothAdapter.disable();
        }*/

        tvCheckUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Beta.checkUpgrade();
                Log.d("main","check update");

                if (!bluetoothAdapter.isEnabled()){
                    bluetoothAdapter.enable();
                    Toast.makeText(MainActivity.this,"open",Toast.LENGTH_SHORT).show();

                }else {
                    bluetoothAdapter.disable();
                    Toast.makeText(MainActivity.this,"close",Toast.LENGTH_SHORT).show();

                }

            }
        });


        lineChart = findViewById(R.id.lineChart);
        lineChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,CanvasActivity.class);
                startActivity(intent);
            }
        });
        List<Entry> entries = new ArrayList<>();
        String []label = new String[12];
        final List<Float> xAxisValue = new ArrayList<>();
        for (int i = 0 ;i<12;i++){
            xAxisValue.add((float) i);
            entries.add(new Entry(i,i));
            label[i] = String.valueOf(i);
        }
        LineDataSet dataSet = new LineDataSet(entries," ");
        dataSet.setMode(LineDataSet.Mode.LINEAR_DISCONNECT_ZERO);

        XAxis xAxis = lineChart.getXAxis();//横轴设置
//        xAxis.setDrawGridLines(false);
//        xAxis.setDrawAxisLine(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//设置横轴位置在底部
        xAxis.setLabelCount(7);//设置X轴上面Label的数量，如果没有这一句，标签会出错
        IndexAxisValueFormatter formatter = new IndexAxisValueFormatter(label);//设置标签

        xAxis.setValueFormatter(formatter);
        LineData lineData = new LineData(dataSet);
        lineData.setValueFormatter(formatter);


        YAxis rightAxis = lineChart.getAxisRight();
        LimitLine limitLine = new LimitLine(5,  "label");
        limitLine.setTextColor(Color.parseColor("#ffffff"));
        limitLine.setTextSize(12f);
        //限制线颜色
        limitLine.setLineColor(Color.parseColor("#ffffff"));
        //限制线宽度
        limitLine.setLineWidth(0.1f);
        //右y轴添加限制线
        rightAxis.addLimitLine(limitLine);
        lineChart.setData(lineData);
        lineChart.notifyDataSetChanged();


        System.out.println("<--------------------  Dividing line  ---------------------->");

        findViewById(R.id.tvFade).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FadeActivity.class);
                startActivity(intent);
            }
        });



        findViewById(R.id.tvSlide).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CrystalBallActivity.class);
                startActivity(intent);
//                Toast.makeText(MainActivity.this,"bugly热更新成功了！！啪啪啪",Toast.LENGTH_SHORT).show();
            }
        });

        System.out.println("<--------------------  Dividing line bugly ---------------------->");

        tvSystemTime = findViewById(R.id.tvSystemTime);
        tvCurrentTime = findViewById(R.id.tvNetworkTime);

        calendar = (Calendar) Calendar.getInstance().clone();

        findViewById(R.id.tvSystem).setOnClickListener(v->{
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            long time1 = Calendar.getInstance().getTimeInMillis();
            String date1 = format.format(new Date(time1));
            Logger.e(TAG,"date1:" + date1);
            tvSystemTime.setText(date1);
            new Thread(){
                @Override
                public void run() {
                    long time2 = getNetworkTime();
                    timeDifference = time2 - Calendar.getInstance().getTimeInMillis();
                    calendar.setTimeInMillis(time2);
                    Message msg = new Message();
                    msg.what = 1;
                    msg.obj = time2;
                    handler.sendMessage(msg);
                    Logger.e(TAG,"thread ID:" + this.getId());
                    try {
                        sleep(6*1000);
                        String date2 = format.format(new Date(Calendar.getInstance().getTimeInMillis() + timeDifference));
                        Logger.e(TAG,"date3:" + date2);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
            Logger.e(TAG,"thread ID0:" + Process.myPid());
        });

        findViewById(R.id.tvNetworkTime).setOnClickListener(v->{
            Intent intent = new Intent(MainActivity.this, RoundActivity.class);
            startActivity(intent);
        });

    }

    @OnClick({R.id.tvSystemTime,R.id.swTest})
    public void onMainClick(View view){
        switch (view.getId()){
            case R.id.tvSystemTime:
//                intent(CrystalBallTrueActivity.class);
                if (ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.CAMERA},REQUEST_CAMERA);
                }else {
                    goScanQRCode();
                }
                break;
            case R.id.swTest:
                /*if (ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.CAMERA},REQUEST_CAMERA);
                }else {
                    goScanQRCode();
                }*/
                break;
            default:
                break;
        }
    }

    /**
     *扫描二维码
     */
    private void goScanQRCode() {
        Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
        startActivityForResult(intent,REQUEST_CAPTURE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAPTURE){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                goScanQRCode();
                Logger.d(TAG,"相机权限授予成功");
            }else {
                Toast.makeText(this,"权限拒绝",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CAPTURE){
            if (null!=data){
                Bundle bundle = data.getExtras();
                if (bundle == null){
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS){
                    Toast.makeText(this,bundle.getString(CodeUtils.RESULT_STRING),Toast.LENGTH_SHORT).show();
                }else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED){
                    Toast.makeText(this, "解析二维码失败", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void setupWindowAnimations(){
//        Fade slide = new Fade();
//        slide.setDuration(1000);
//        getWindow().setExitTransition(slide);
    }

    /**
     * 获取版本名
     * @param context
     * @return
     */
    public static String getVersionName(Context context){
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(),0);
            return pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "Unkonwn";
        }
    }

    /**
     * 获取版本code
     * @param context
     * @return
     */
    public static int getVersionCode(Context context){
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(),0);
            return pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 获取当前手机系统语言
     * @return 返回当前系统语言。例如：当前设置的是“中文-中国”，则返回“zh-CN”
     */
    public static String getSystemLanguage(){
        return Locale.getDefault().getLanguage();
    }

    /**
     * 获取手机系统语言列表
     * @return 语言列表
     */
    public static Locale[] getSystemLanguageList(){
        return Locale.getAvailableLocales();
    }

    /**
     * 获取手机系统版本号
     * @return 系统版本号
     */
    public static String getSystemVersion(){
        return Build.VERSION.RELEASE;
    }

    /**
     * 获取手机型号
     * @return 手机型号
     */
    public static String getSystemModel(){
        return Build.MODEL;
    }

    /**
     * 获取手机厂商
     * @return 手机厂商
     */
    public static String getDeviceBrand(){
        return Build.BRAND;
    }

    /**
     * 获取国家网络授时时间
     * @return
     */
    public static long getNetworkTime(){
        String webUrl = "http://www.ntsc.ac.cn/";//中国科学院国家授时中心
        try {
            URL url = new URL(webUrl);
            URLConnection uc = url.openConnection();
            uc.setReadTimeout(5000);
            uc.setConnectTimeout(5000);
            uc.connect();
            return uc.getDate();
        } catch (Exception e) {
            e.printStackTrace();
            return Calendar.getInstance().getTimeInMillis();
        }
    }
}
