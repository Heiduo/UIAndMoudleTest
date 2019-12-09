package com.example.myapplication.Transition;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.transition.ChangeBounds;
import android.transition.ChangeImageTransform;
import android.transition.ChangeScroll;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Scene;
import android.transition.Slide;
import android.transition.TransitionInflater;
import android.transition.TransitionManager;
import android.transition.Visibility;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;

/**
 * 描述：
 *
 * @author Created by heiduo
 * @time Created on 2019/11/5
 */
public class FadeActivity extends Activity {
    ImageView blueIconImageView;
    Button btChange;

    private ViewGroup mSceneRootView;
    private Scene     mSceneStart;
    private Scene     mSceneEnd;
    private boolean   mStartSceneState;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setWindowAnimations();
        setContentView(R.layout.test_fade_activity);

        initView();

        btChange = findViewById(R.id.btChange);
        btChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * 不指定默认就是AutoTransition
                 */
                TransitionManager.go(mStartSceneState ? mSceneEnd : mSceneStart,new Slide());
                mStartSceneState = !mStartSceneState;
            }
        });

        blueIconImageView = findViewById(R.id.ivTest);
        findViewById(R.id.tvTest).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FadeActivity.this,SlideActivity.class);
//                startActivity(intent);

                View sharedView = blueIconImageView;
                String transitionName = getString(R.string.blue_name);
                ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(FadeActivity.this, sharedView, transitionName);
                startActivity(intent, transitionActivityOptions.toBundle());

            }
        });
    }

    private void initView() {
        mSceneRootView = findViewById(R.id.flFadeChange);
        mSceneStart = Scene.getSceneForLayout(mSceneRootView,R.layout.fl_first,this);
        mSceneEnd = Scene.getSceneForLayout(mSceneRootView,R.layout.fl_second,this);

        /**
         * 切换到开始场景状态
         */
        TransitionManager.go(mSceneStart);
        mStartSceneState = true;
    }

    private void setWindowAnimations() {
        Fade slide = new Fade();
        slide.setMode(Visibility.MODE_OUT);
        slide.setDuration(1000);
        slide.excludeTarget(blueIconImageView,true);
        getWindow().setEnterTransition(slide);
        getWindow().setAllowEnterTransitionOverlap(true);
        getWindow().setAllowReturnTransitionOverlap(true);

        // or create directly
        ChangeImageTransform changeImageTransform = new ChangeImageTransform();
        getWindow().setSharedElementExitTransition(changeImageTransform);
    }
}
