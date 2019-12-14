package com.example.myapplication.view;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * 描述：
 *
 * @author Created by heiduo
 * @time Created on 2019/12/6
 */
public class Rotate3dAnimation extends Animation {
    //开始角度
    private   final   float  mFromDegrees;
    //结束角度
    private   final   float  mToDegrees;
    //中心点
    private   final   float  mCenterX;
    private   final   float  mCenterY;
    private   final   float  mDepthZ;
    //是否需要扭曲
    private   final   boolean  mReverse;
    //摄像头
    private  Camera mCamera;

    /**
     * 创建一个绕Y轴旋转的3D动画效果，旋转过程具有深度调节，可以指定旋转中心
     * @param fromDegrees 起始角度
     * @param toDegrees 结束角度
     * @param centerX 旋转中心x坐标
     * @param centerY 旋转中心y坐标
     * @param depthZ 最远达到的z轴坐标
     * @param reverse true表示由0到depthZ，false相反
     */
    public  Rotate3dAnimation( float  fromDegrees,  float  toDegrees,
                               float  centerX,  float  centerY,  float  depthZ,  boolean  reverse) {
        mFromDegrees = fromDegrees;
        mToDegrees = toDegrees;
        mCenterX = centerX;
        mCenterY = centerY;
        mDepthZ = depthZ;
        mReverse = reverse;
    }

    @Override
    public  void  initialize( int  width,  int  height,  int  parentWidth,  int  parentHeight) {
        super .initialize(width, height, parentWidth, parentHeight);
        mCamera =  new  Camera();
    }
    //生成Transformation
    @Override
    protected  void  applyTransformation( float  interpolatedTime, Transformation t) {
        final  float  fromDegrees = mFromDegrees;
        //生成中间角度
        float  degrees = fromDegrees + ((mToDegrees - fromDegrees) * interpolatedTime);

        final   float  centerX = mCenterX;
        final   float  centerY = mCenterY;
        final Camera camera = mCamera;

        final Matrix matrix = t.getMatrix();

        camera.save();

        //调节深度
        if  (mReverse) {
            camera.translate( 0.0f,  0.0f, mDepthZ * interpolatedTime);
        }  else  {
            camera.translate( 0.0f,  0.0f, mDepthZ * ( 1.0f - interpolatedTime));
        }
        //绕Y周旋转
        camera.rotateY(degrees);

        //取得变换后的矩阵
        camera.getMatrix(matrix);
        camera.restore();

        //调节中心点
        matrix.preTranslate(-centerX, -centerY);
        matrix.postTranslate(centerX, centerY);
    }
}
