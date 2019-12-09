package com.example.myapplication.Transition;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.transition.ChangeImageTransform;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.Gravity;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.myapplication.R;

/**
 * 描述：
 *
 * @author Created by heiduo
 * @time Created on 2019/11/5
 */
public class SlideActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setWindowAnimations();

        setContentView(R.layout.test_slide_activity);
        findViewById(R.id.tvTest).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SlideActivity.this,FadeActivity.class);
                startActivity(intent);

            }
        });
    }

    private void setWindowAnimations() {
        Slide slide = new Slide();
        slide.setSlideEdge(Gravity.END);
        slide.setDuration(500);
        getWindow().setEnterTransition(slide);
        getWindow().setExitTransition(null);

        getWindow().setAllowEnterTransitionOverlap(true);
        getWindow().setAllowReturnTransitionOverlap(true);

        // or create directly
        ChangeImageTransform changeImageTransform = new ChangeImageTransform();
        getWindow().setSharedElementEnterTransition(changeImageTransform);
    }
}
