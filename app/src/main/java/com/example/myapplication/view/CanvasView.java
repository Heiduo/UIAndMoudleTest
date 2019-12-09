package com.example.myapplication.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.myapplication.R;

/**
 * 描述：
 *
 * @author Created by heiduo
 * @time Created on 2019/11/14
 */
public class CanvasView extends View {
    private int type = 0;

    Paint mPaint;
    RectF mRect;

    int width;
    int height;

    public CanvasView(Context context) {
        super(context);
    }

    public CanvasView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(getResources().getColor(R.color.ecg_line));
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(4f);

        mRect = new RectF();
        Log.d("lc", "oncreate");
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width = (right - left)/10;
        height = (bottom - top)/10;
        Log.d("lc","layout");
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        width = widthMeasureSpec / 10;
//        height = heightMeasureSpec / 10;
        Log.d("lc","measure");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        switch (type % 2) {

            case 0:
                mRect.set(width, height, (type+2) * width, (type+2) * height);
                canvas.drawRect(mRect, mPaint);
                break;
            case 1:
                mRect.set(width, height, (type+2) * width, (type+2) * height);
                canvas.drawRoundRect(mRect, 20, 20, mPaint);
                break;
            /*case 2:

                break;
            case 3:

                break;
            case 4:

                break;
            case 5:

                break;
            case 6:

                break;
            case 7:

                break;
            case 8:

                break;*/
            default:
                break;
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
    }

    public void setType(int type) {
        this.type = type;
        invalidate();
    }
}
