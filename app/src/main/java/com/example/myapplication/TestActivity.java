package com.example.myapplication;

import android.app.Activity;
import android.app.NotificationManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.core.SampleApplication;
import com.example.myapplication.utils.Logger;

/**
 * 描述：
 *
 * @author Created by heiduo
 * @time Created on 2019/10/29
 */
public class TestActivity extends Activity {
    TextView tvTest;
    NotificationManager manager;
    BluetoothAdapter bluetoothAdapter = null;

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity);

        init();
        initView();
    }

    private void init() {
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    private void initView() {
        tvTest = findViewById(R.id.tvTest);
        String content = (String) getIntent().getSerializableExtra("test");
        Logger.e("TEST", getIntent().toString());
        handler = SampleApplication.getMainThreadHandler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Logger.e("TEST", "3s后打印");
                Toast.makeText(TestActivity.this,"3s过后",Toast.LENGTH_SHORT).show();
            }
        },3*1000);




        BluetoothManager manager1 = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
        bluetoothAdapter = manager1.getAdapter();

        tvTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!bluetoothAdapter.isEnabled()) {
                    bluetoothAdapter.enable();
                    Toast.makeText(TestActivity.this, "open", Toast.LENGTH_SHORT).show();
                    //测试崩溃
//                    CrashReport.testJavaCrash();

                } else {
                    bluetoothAdapter.disable();
                    Toast.makeText(TestActivity.this, "close", Toast.LENGTH_SHORT).show();

                }
            }
        });
        if (content != null) {
            tvTest.setText(content);
        }
        manager.cancel(1);

        //测试崩溃
//        CrashReport.testJavaCrash();

    }
}
