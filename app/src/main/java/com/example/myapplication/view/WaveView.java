package com.example.myapplication.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;

/**
 * 描述：
 *
 * @author Created by heiduo
 * @time Created on 2019/12/21
 */
public class WaveView extends View {
    private Context mContext;
    private int waveLength = 1500;
    private int waveCount = 2;
    private int offset = 0;
    private float centerX = 0;
    private float centerY = 0;


    private int screenWidth = 0;
    private int screenHeight = 0;

    private Path mPath;
    private Paint mPaint;

    private ValueAnimator mValueAnimator = new ValueAnimator();


    public WaveView(Context context) {
//        super(context);
        this(context, null);
    }

    public WaveView(Context context, @Nullable AttributeSet attrs) {
//        super(context, attrs);
        this(context, attrs, 0);
    }

    public WaveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        mPaint = new Paint();
        mPaint.setColor(Color.parseColor("#33ccff"));
        mPath = new Path();
        initAnim();
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        centerX = (right + left) / 2;
        centerY = (top + right) / 2;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        screenWidth = widthMeasureSpec;
        screenHeight = heightMeasureSpec;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPath.reset();
        mPath.moveTo(-waveLength + offset, centerY);

        for (int i = 0; i < waveCount; i++) {
            mPath.quadTo(-waveLength * 3 / 4 + i * waveLength + offset, centerY + 60, -waveLength / 2 + i * waveLength + offset
                    , centerY);
            mPath.quadTo(-waveLength / 4 + i * waveLength + offset, centerY - 60, i * waveLength + offset, centerY);
        }

        mPath.lineTo(screenWidth,screenHeight);
        mPath.lineTo(0,screenHeight);
        mPath.close();
        canvas.drawPath(mPath,mPaint);
    }

    private void initAnim() {
        mValueAnimator = ValueAnimator.ofInt(0,waveLength);
        mValueAnimator.setDuration(1000);
        mValueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mValueAnimator.setInterpolator(new LinearInterpolator());
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                offset = (int) animation.getAnimatedValue();
                invalidate();
            }
        });
        mValueAnimator.start();
    }

}
