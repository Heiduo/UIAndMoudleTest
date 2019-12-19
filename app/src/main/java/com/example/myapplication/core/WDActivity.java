package com.example.myapplication.core;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 描述：
 *
 * @author Created by heiduo
 * @time Created on 2019/11/14
 */
public abstract class WDActivity extends AppCompatActivity {
    protected final String TAG = getClass().getSimpleName();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());

        ButterKnife.bind(this);
        initBarStatus();
        initView();
    }

    public void setBarStatusColor(boolean color){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decor = getWindow().getDecorView();
            int ui = decor.getSystemUiVisibility();
            if (color) {
                ui |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR; //设置状态栏中字体的颜色为黑色
            } else {
                ui &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR; //设置状态栏中字体颜色为白色
            }
            decor.setSystemUiVisibility(ui);
        }
    }

    private void initBarStatus() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            Window window = getWindow();
            View decorView = window.getDecorView();
            //两个 flag 要结合使用，表示让应用的主体内容占用系统状态栏的空间
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //设置状态栏为透明，否则在部分手机上会呈现系统默认的浅灰色
            window.setStatusBarColor(Color.TRANSPARENT);
            //导航栏颜色也可以考虑设置为透明色
            //window.setNavigationBarColor(Color.TRANSPARENT);
        }
    }

    protected abstract int getLayoutId();

    protected abstract void initView();

    public void intent(Class activity){
        Intent intent = new Intent(this,activity);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
