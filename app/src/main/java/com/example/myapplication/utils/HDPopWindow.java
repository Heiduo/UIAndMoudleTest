package com.example.myapplication.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;

import androidx.annotation.RequiresApi;

import java.security.Key;

/**
 * 描述：简单模仿封装PopupWindow
 * 使用建造者模式，支持链式调用
 *
 * @author Created by heiduo
 * @time Created on 2019/12/20
 */
public class HDPopWindow implements PopupWindow.OnDismissListener {
    private static final String TAG = "HDPopWindow";
    private static final float DEFAULT_ALPHA = 0.7F;//默认背景透明度
    private Context mContext;
    private int mWidth;
    private int mHeight;
    private boolean mIsFocusable = true;
    private boolean mIsOutside = true;
    private int mResLayoutId = -1;
    private View mContentView;
    private PopupWindow mPopupWindow;
    private int mAnimationStyle = -1;

    private boolean mClipEnable = true;
    private boolean mIgnoreCheekPres = false;
    private int mInputMode = -1;
    private PopupWindow.OnDismissListener mOnDismissListener;
    private int mSoftInputMode = -1;
    private boolean mTouchable = true;
    private View.OnTouchListener mOnTouchListener;

    private Window mWindow;//当前Activity的窗口

    /**
     * 弹出PopWindow时,背景是否变暗，默认false
     */
    private boolean mIsBackgroundDark = false;

    /**
     * 背景变暗的alpha值，0--1
     */
    private float mBackgroundDarkValue = 0;

    /**
     * 默认点击popupWindow外的区域消失
     */
    private boolean enableOutsideTouchDismiss = true;

    private HDPopWindow(Context context) {
        this.mContext = context;
    }

    public int getWidth() {
        return mWidth;
    }

    public int getHeight() {
        return mHeight;
    }

    public HDPopWindow showAsDropDown(View anchor) {
        if (null != mPopupWindow) {
            mPopupWindow.showAsDropDown(anchor);
        }
        return this;
    }

    public boolean isShowing(){
        if (mPopupWindow == null){
            return false;
        }
        return mPopupWindow.isShowing();
    }

    /**
     * 相对于控件的位置
     *
     * @param anchor 控件位置
     * @param xOff   相对于控件x轴的偏移量（>0 向右偏。< 0 向左偏）
     * @param yOff   相对于控件y轴的偏移量（>0 向下偏。< 0 向上偏）
     * @return
     */
    public HDPopWindow showAsDropDown(View anchor, int xOff, int yOff) {
        if (null != mPopupWindow) {
            mPopupWindow.showAsDropDown(anchor, xOff, yOff);
        }
        return this;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public HDPopWindow showAsDropDown(View anchor, int xOff, int yOff, int gravity) {
        if (null != mPopupWindow) {
            mPopupWindow.showAsDropDown(anchor, xOff, yOff, gravity);
        }
        return this;
    }

    /**
     * 在父布局中的位置（通过设置Gravity.CENTER，下方Gravity.BOTTOM等 ），可以设置具体位置坐标
     *
     * @param parent  父控件
     * @param gravity 相对于父控件中的位置
     * @param x       x方向偏移
     * @param y       y方向偏移
     * @return
     */
    public HDPopWindow showAtLocation(View parent, int gravity, int x, int y) {
        if (null != mPopupWindow) {
            mPopupWindow.showAtLocation(parent, gravity, x, y);
        }
        return this;
    }

    /**
     * 添加一些属性设置
     *
     * @param popupWindow
     */
    private void apply(PopupWindow popupWindow) {
        popupWindow.setClippingEnabled(mClipEnable);
        if (mIgnoreCheekPres) {
            popupWindow.setIgnoreCheekPress();
        }
        if (mInputMode != -1) {
            popupWindow.setInputMethodMode(mInputMode);
        }
        if (mSoftInputMode != -1) {
            popupWindow.setSoftInputMode(mSoftInputMode);
        }
        if (null != mOnDismissListener) {
            popupWindow.setOnDismissListener(mOnDismissListener);
        }
        if (null != mOnTouchListener) {
            popupWindow.setTouchInterceptor(mOnTouchListener);
        }
        popupWindow.setTouchable(mTouchable);
    }

    private PopupWindow build() {
        if (null == mContentView) {
            mContentView = LayoutInflater.from(mContext).inflate(mResLayoutId, null);
        }
        //获取当前Activity的window 设置背景变暗
        Activity activity = (Activity) mContentView.getContext();
        if (null != activity && mIsBackgroundDark) {
            //若设置的背景暗色在0-1范围内，则使用可用的值，否则使用默认值
            final float alpha = (mBackgroundDarkValue > 0 && mBackgroundDarkValue < 1) ? mBackgroundDarkValue : DEFAULT_ALPHA;
            mWindow = activity.getWindow();
            WindowManager.LayoutParams params = mWindow.getAttributes();
            params.alpha = alpha;
            mWindow.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            mWindow.setAttributes(params);
        }

        if (mWidth != 0 && mHeight != 0) {
            mPopupWindow = new PopupWindow(mContentView, mWidth, mHeight);
        } else {
            mPopupWindow = new PopupWindow(mContentView, ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        if (mAnimationStyle != -1) {
            mPopupWindow.setAnimationStyle(mAnimationStyle);
        }

        apply(mPopupWindow);//设置一些属性

        //获取试图长高
        if (mWidth == 0 || mHeight == 0) {
            mPopupWindow.getContentView().measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            mWidth = mPopupWindow.getContentView().getMeasuredWidth();
            mHeight = mPopupWindow.getContentView().getMeasuredHeight();
        }

        //添加dismiss监听
        mPopupWindow.setOnDismissListener(this);

        //判断是否点击PopupWindow外的位置关闭
        if (!enableOutsideTouchDismiss) {
            mPopupWindow.setFocusable(true);
            mPopupWindow.setOutsideTouchable(false);
            mPopupWindow.setBackgroundDrawable(null);
            //注意ContentView
            mPopupWindow.getContentView().setFocusable(true);
            mPopupWindow.getContentView().setFocusableInTouchMode(true);
            mPopupWindow.getContentView().setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        mPopupWindow.dismiss();
                        return true;
                    }
                    return false;
                }
            });

            //Android6.0以上需要拦截事件
            mPopupWindow.setTouchInterceptor(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    final int x = (int) event.getX();
                    final int y = (int) event.getY();
                    if ((event.getAction() == MotionEvent.ACTION_DOWN)
                            && ((x < 0) || (x >= mWidth) || (y < 0)) || (y >= mHeight)) {
                        return true;
                    } else if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {

                        return true;
                    }
                    return false;
                }
            });
        } else {
            mPopupWindow.setFocusable(mIsFocusable);
            mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            mPopupWindow.setOutsideTouchable(mIsOutside);
        }
        //update
        mPopupWindow.update();

        return mPopupWindow;
    }

    @Override
    public void onDismiss() {
        dismiss();
    }

    /**
     * 关闭popupWindow
     */
    public void dismiss() {
        if (mOnDismissListener != null) {
            mOnDismissListener.onDismiss();
        }

        //如果设置了背景变暗，需要在返回时还原
        if (mWindow != null) {
            WindowManager.LayoutParams params = mWindow.getAttributes();
            params.alpha = 1.0f;
            mWindow.setAttributes(params);
        }
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
    }

    public PopupWindow getPopupWindow() {
        return mPopupWindow;
    }

    public static class PopupWindowBuilder {
        private HDPopWindow mHDPopWindow;

        public PopupWindowBuilder(Context context) {
            mHDPopWindow = new HDPopWindow(context);
        }

        public PopupWindowBuilder size(int width, int height) {
            mHDPopWindow.mWidth = width;
            mHDPopWindow.mHeight = height;
            return this;
        }

        public PopupWindowBuilder setFocusable(boolean focusable) {
            mHDPopWindow.mIsFocusable = focusable;
            return this;
        }

        public PopupWindowBuilder setView(int resLayoutId) {
            mHDPopWindow.mResLayoutId = resLayoutId;
            return this;
        }

        public PopupWindowBuilder setView(View view) {
            mHDPopWindow.mContentView = view;
            mHDPopWindow.mResLayoutId = -1;
            return this;
        }

        public PopupWindowBuilder setOutsideTouchable(boolean outsideTouchable) {
            mHDPopWindow.mIsOutside = outsideTouchable;
            return this;
        }

        /**
         * 设置弹窗动画
         *
         * @param animationStyle 动画
         * @return
         */
        public PopupWindowBuilder setAnimationStyle(int animationStyle) {
            mHDPopWindow.mAnimationStyle = animationStyle;
            return this;
        }

        public PopupWindowBuilder setClippingEnable(boolean enable) {
            mHDPopWindow.mClipEnable = enable;
            return this;
        }

        public PopupWindowBuilder setIgnoreCheekPress(boolean ignoreCheekPress) {
            mHDPopWindow.mIgnoreCheekPres = ignoreCheekPress;
            return this;
        }

        public PopupWindowBuilder setInputMode(int mode) {
            mHDPopWindow.mInputMode = mode;
            return this;
        }

        public PopupWindowBuilder setOnDismissListener(PopupWindow.OnDismissListener onDismissListener) {
            mHDPopWindow.mOnDismissListener = onDismissListener;
            return this;
        }

        public PopupWindowBuilder setSoftInputMode(int softInputMode) {
            mHDPopWindow.mSoftInputMode = softInputMode;
            return this;
        }

        public PopupWindowBuilder setTouchable(boolean touchable){
            mHDPopWindow.mTouchable = touchable;
            return this;
        }

        public PopupWindowBuilder setTouchIntercepter(View.OnTouchListener touchIntercepter){
            mHDPopWindow.mOnTouchListener = touchIntercepter;
            return this;
        }

        public PopupWindowBuilder enableBackgroundDark(boolean isDark){
            mHDPopWindow.mIsBackgroundDark = isDark;
            return this;
        }

        public PopupWindowBuilder setBgDarkAlpha(float alpha){
            mHDPopWindow.mBackgroundDarkValue = alpha;
            return this;
        }

        public PopupWindowBuilder enableOutsideTouchableDismiss(boolean dismiss){
            mHDPopWindow.enableOutsideTouchDismiss = dismiss;
            return this;
        }

        public HDPopWindow create(){
            //构建PopupWindow
            mHDPopWindow.build();
            return mHDPopWindow;
        }
    }
}
