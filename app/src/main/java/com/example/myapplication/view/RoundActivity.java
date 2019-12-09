package com.example.myapplication.view;

import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;

import com.example.myapplication.Logger;
import com.example.myapplication.R;
import com.example.myapplication.WDActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 描述：
 *
 * @author Created by heiduo
 * @time Created on 2019/11/28
 */
public class RoundActivity extends WDActivity {
    @BindView(R.id.ivCenter)
    ImageView ivCenter;
    @Override
    protected int getLayoutId() {
        return R.layout.round_activity;
    }

    @Override
    protected void initView() {

        //设置监听
//        rotation.setAnimationListener( new  DisplayNextView(position));
    }

    @OnClick(R.id.ivCenter)
    public void onClickView(View view){
        switch (view.getId()){
            case R.id.ivCenter:
                Rotate3dAnimation rotation =
                        new  Rotate3dAnimation(0, 360, ivCenter.getWidth()/2.0f,
                                ivCenter.getHeight()/2.0f,  310.0f,  true);
                rotation.setDuration( 500 );
                rotation.setFillAfter( true );
                rotation.setInterpolator( new AccelerateInterpolator());
                ivCenter.setAnimation(rotation);
                Logger.e(TAG,"click");
                break;
            default:
                break;
        }
    }
}
