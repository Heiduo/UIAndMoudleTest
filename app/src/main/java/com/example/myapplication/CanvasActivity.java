package com.example.myapplication;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;

import com.example.myapplication.view.CanvasView;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarRoundChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 描述：
 *
 * @author Created by heiduo
 * @time Created on 2019/11/14
 */
public class CanvasActivity extends WDActivity {
    @BindView(R.id.bcBar)
    BarRoundChart barChart;
    @BindView(R.id.cvTest)
    CanvasView canvasView;
    @BindView(R.id.btCanvas)
    Button btCanvas;

    int click = 0;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_canvas;
    }

    @Override
    protected void initView() {

        //y轴数据的设置
        List<BarEntry> yvalue = new ArrayList<>();

        for (int i = 0; i < 12; i++) {
            yvalue.add(new BarEntry(i, i));
        }

        BarDataSet set = new BarDataSet(yvalue, "");
        //柱装图颜色设置

        set.setColors(new int[]{getResources().getColor(R.color.colorAccent)}); //设置柱状图颜色
        set.setValueTextColor(Color.parseColor("#ffffffff"));
        //x轴标签的设置

        XAxis xAxis = barChart.getXAxis();//横轴设置
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setTextColor(getResources().getColor(R.color.colorPrimary));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//设置横轴位置在底部
        xAxis.setLabelCount(8);//设置X轴上面Label的数量，如果没有这一句，标签会出错
        IndexAxisValueFormatter formatter = new IndexAxisValueFormatter() {//设置标签
            @Override
            public String getFormattedValue(float value) {
                return "" + (int) value;
            }
        };
        xAxis.setValueFormatter(formatter);
        //y轴的设置
        YAxis yAxisRight = barChart.getAxisRight();//获取右边边的纵轴
        yAxisRight.setAxisMaximum(100f); //最大情绪指数
        yAxisRight.setDrawZeroLine(true);//在y=0处画一条水平线
        yAxisRight.setDrawAxisLine(false);//左边的纵轴不显示
        yAxisRight.setDrawGridLines(false);//纵轴的网格线不显示
        yAxisRight.setDrawLabels(false);
        yAxisRight.setEnabled(true);

        barChart.setDrawValueAboveBar(true);  //数据在图上显示

        barChart.setNoDataText(" ");//无数据时显示     No Chart Data
        barChart.getAxisRight().setEnabled(true);//右边的纵轴
        barChart.getAxisLeft().setAxisMinimum(0);
        barChart.getAxisRight().setAxisMinimum(0);
        barChart.animateY(2500, Easing.Linear, "alpha");

        //将数据设置进入chart对象
        BarData data = new BarData(set);
        barChart.setData(data);
        barChart.setScaleEnabled(false);//禁止缩放
        Description description = new Description();//关闭图例显示
        description.setEnabled(false);
        barChart.setDescription(description);
        barChart.getLegend().setEnabled(false);
    }

    @OnClick(R.id.btCanvas)
    public void onCanvasClick(View view) {
        switch (view.getId()) {
            case R.id.btCanvas:
                canvasView.setType(click);
                click++;

                ValueAnimator animator = ValueAnimator.ofInt(0, 10);
                animator.setDuration(3000);
                animator.addUpdateListener(animation -> {
                    int curValue = (int) animation.getAnimatedValue();
                    barChart.layout(curValue, curValue, curValue + barChart.getWidth(), curValue + barChart.getHeight());
                });
                animator.start();
                animator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        barChart.notifyDataSetChanged();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                break;
            default:
                break;
        }
    }
}
