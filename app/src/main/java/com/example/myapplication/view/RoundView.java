package com.example.myapplication.view;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 描述：旋转父容器
 *
 * @author Created by heiduo
 * @time Created on 2019/11/28
 */
public class RoundView extends FrameLayout {
    //从这个角度开始画View
    private static final float START_ANGLE = 270f;

    //父容器的边界 单位dp
    private static final int PADDING = 80;

    //绕x周旋转的角度70度对应的弧度
    private static double ROTATE_X = 0/*Math.PI * 7 / 18*/;

    /**
     * 角度偏移差
     */
    private float sweepAngle = 0f;

    /**
     * 轨迹的半径
     */
    private float mRadius;

    /**
     * 父容器边界，单位px
     */
    private int mPadding;

    //自动旋转角度，16ms(一帧）旋转的角度，值越大，旋转越快
    private static final float AUTO_SWEEP_ANGLE = 0.3f;

    //px转为angle的比例
    private static final float SCALE_PX_ANGLE = 0.2f;

    public RoundView(@NonNull Context context) {
//        super(context);
        this(context, null);
    }

    public RoundView(@NonNull Context context, @Nullable AttributeSet attrs) {
//        super(context, attrs);
        this(context, attrs, 0);
    }

    public RoundView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPadding = (int) (context.getResources().getDisplayMetrics().density * PADDING);
        initAnim();
    }

    public RoundView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public static double getRotateX() {
        return ROTATE_X;
    }

    public static void setRotateX(double rotateX) {
        ROTATE_X = rotateX;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
//        super.onLayout(changed, left, top, right, bottom);
        mRadius = getMeasuredWidth() / 2f - mPadding;
        layoutChildren();
        postDelayed(autoScrollRunnable,100);
    }

    private void layoutChildren() {
        int childCount = getChildCount();
        if (childCount == 0)
            return;

        //行星之间的角度
        View centerView = centerView();

        float averageAngle;
        if (centerView == null) {
            averageAngle = 360f / childCount;
        } else {
            averageAngle = 360f / (childCount - 1);
        }

        int number = 0;

        for (int index = 0; index < childCount; index++) {
            View child = getChildAt(index);
            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();

            if ("center".equals(child.getTag())) {
                //居中显示
                child.layout(getMeasuredWidth() / 2 - childWidth / 2, getMeasuredHeight() / 2 - childHeight / 2,
                        getMeasuredWidth() / 2 + childWidth / 2, getMeasuredHeight() / 2 + childHeight / 2);
            } else {
                //第index 子View的角度
                double angle = (START_ANGLE - averageAngle * number + sweepAngle) * Math.PI / 180;
                double sin = Math.sin(angle);
                double cos = Math.cos(angle);

                double coordinateX = getMeasuredWidth() / 2f - mRadius * cos;
                //Math.cos(ROTATE_X)代表将y坐标转换为旋转之后的y坐标
                double coordinateY = getMeasuredHeight() / 2f - mRadius * sin * Math.cos(ROTATE_X);

                child.layout((int) (coordinateX - childWidth / 2),
                        (int) (coordinateY - childHeight / 2),
                        (int) (coordinateX + childWidth / 2),
                        (int) (coordinateY + childHeight / 2));


                if (getRotateX() != 0){
                    float scale = (float) ((1 - 0.3f) / 2 * (1 - Math.sin(angle)) + 0.3f);
                    child.setScaleX(scale);
                    child.setScaleY(scale);

                }



                number++;
            }

        }

        changeZ();
    }

    private View centerView() {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if ("center".equals(child.getTag())) {
                return child;
            }
        }
        return null;
    }

    private void changeZ() {
        View centerView = centerView();
        float centerViewScaleY = 1f;
        if (centerView != null) {
            centerViewScaleY = centerView.getScaleY();
            centerView.setScaleY(0.5f);
        }
        List<View> children = new ArrayList<>();
        for (int i = 0; i < getChildCount(); i++) {
            children.add(getChildAt(i));
        }
        //按照ScaleY排序
        Collections.sort(children, new Comparator<View>() {
            @Override
            public int compare(View o1, View o2) {
                return (int) ((o1.getScaleY() - o2.getScaleY()) * 1000000);
            }
        });

        float z = 0.1f;
        for (int i = 0; i < children.size(); i++) {
            children.get(i).setZ(z);
            z += 0.1f;
        }

        if (centerView != null) {
            centerView.setScaleY(centerViewScaleY);
        }
    }

    private Runnable autoScrollRunnable = new Runnable() {
        @Override
        public void run() {
            sweepAngle += AUTO_SWEEP_ANGLE;
            sweepAngle %= 360;
            layoutChildren();
            postDelayed(this,16);
        }
    };


    private void initAnim() {
        velocityAnim.setDuration(1000);
        velocityAnim.setInterpolator(new DecelerateInterpolator());
        velocityAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                sweepAngle += (value*SCALE_PX_ANGLE);
                layoutChildren();
            }
        });
    }

    /**
     * 手势处理
     */
    private float downX = 0f;

    /**
     * 手指按下的角度
     */
    private float downAngle = sweepAngle;

    /**
     * 速度追踪器
     */
    private VelocityTracker velocity = VelocityTracker.obtain();

    /**
     * 滑动结束后的动画
     */
    private ValueAnimator velocityAnim = new ValueAnimator();

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        velocity.addMovement(event);
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                downX = x;
                downAngle = sweepAngle;
                //取消动画和自动旋转
                velocityAnim.cancel();
                removeCallbacks(autoScrollRunnable);
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = downX - x;
                sweepAngle = (dx * SCALE_PX_ANGLE + downAngle);
                layoutChildren();
                break;
            case MotionEvent.ACTION_UP:
                velocity.computeCurrentVelocity(16);
                //速度为负值代表顺时针
                scrollByVelocity(velocity.getXVelocity());
                postDelayed(autoScrollRunnable,16);
                break;
        }
        return true;
    }

    private void scrollByVelocity(float velocity) {
        float end;
        if (velocity<0){
            end = -AUTO_SWEEP_ANGLE;
        }else {
            end = 0f;
        }
        velocityAnim.setFloatValues(-velocity,end);
        velocityAnim.start();
    }

    /**
     * 暂停
     */
    public void pause(){
        velocityAnim.cancel();
        removeCallbacks(autoScrollRunnable);
    }

    /**
     * 开始
     */
    public void start(){
        postDelayed(autoScrollRunnable,16);
    }
}