package com.example.myapplication.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 描述：
 *
 * @author Created by heiduo
 * @time Created on 2019/11/29
 */
public class RotateView extends FrameLayout {
    //从这个角度开始画View
    private static final float START_ANGLE = 0f;

    //绕x周旋转的角度70度对应的弧度
    private static final double ROTATE_X = Math.PI * 7 / 18;

    //父容器边界
    private static final int PADDING = 80;

    /**
     * 角度偏移差
     */
    private float sweepAngle = 0f;

    //轨迹半径
    private int mRadius;

    /**
     * 父容器边界，单位px
     */
    private int mPadding;

    //自动旋转角度，16ms(一帧）旋转的角度，值越大，旋转越快
    private static final float AUTO_SWEEP_ANGLE = 0.3f;

    //px转为angle的比例
    private static final float SCALE_PX_ANGLE = 0.2f;

    public RotateView(@NonNull Context context) {
        this(context, null);
    }

    public RotateView(@NonNull Context context, @Nullable AttributeSet attrs) {
//        super(context, attrs);
        this(context, attrs, 0);
    }

    public RotateView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPadding = (int) (context.getResources().getDisplayMetrics().density * PADDING);
    }

    public RotateView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
//        super.onLayout(changed, left, top, right, bottom);
        int childWidth = 0;
        if (getChildCount() >= 0) {
            childWidth = getChildAt(0).getMeasuredWidth() / 2;
        }
        mRadius = getMeasuredWidth() / 2 - childWidth;

        layoutChildren();
    }

    private void layoutChildren() {
        int childCount = getChildCount();
        if (childCount == 0) {
            return;
        }

        float averageAngle = 360f / childCount;
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);

            //第i个子view的角度
            double angle = (START_ANGLE - averageAngle * i + sweepAngle) * Math.PI / 180;
            double sin = Math.sin(angle);
            double cos = Math.cos(angle);

        }

    }
}
