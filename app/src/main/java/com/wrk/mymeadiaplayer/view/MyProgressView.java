package com.wrk.mymeadiaplayer.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by MrbigW on 2016/10/9.
 * weChat:1024057635
 * Github:MrbigW
 * Usage: -.-
 * -------------------=.=------------------------
 */

public class MyProgressView extends View {

    private Paint mPaint;
    private Path mPath;
    private PathMeasure mPathMeasure;
    private int mWidth, mHeight;
    private ValueAnimator mValueAnimator;

    //  用来接收ValueAnimator的返回值，代表整个动画的进度
    private float t;
    private Path mDst;


    public MyProgressView(Context context) {
        this(context, null);
        initPaint();

        initPath();

        initVAnimation();
    }

    public MyProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        initPaint();

        initPath();

        initVAnimation();
    }



    public MyProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initPaint();

        initPath();

        initVAnimation();
    }

    //
    private void initVAnimation() {
        mValueAnimator = ValueAnimator.ofFloat(0f, 1f).setDuration(2000);
        mValueAnimator.setRepeatCount(-1);
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                t = (float) animation.getAnimatedValue();
                invalidate();
            }
        });

        mValueAnimator.start();
    }

    private void initPath() {
        mPath = new Path();
        RectF rect = new RectF(-60, -60, 60, 60);
        mPath.addArc(rect, -90, 359.9f);    //这里角度不能选360，否则会测量失误，具体原因和android的内部优化有关
        mPathMeasure = new PathMeasure(mPath, false);
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(13);
        mPaint.setColor(Color.WHITE);
        //  设置画笔为圆笔
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        //  抗锯齿
        mPaint.setAntiAlias(true);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(mWidth / 2, mHeight / 2);
        mDst = new Path();
//        mPathMeasure.getSegment(mPathMeasure.getLength() * t, mPathMeasure.getLength() * t + 1, mDst, true);
        int num = (int) (t / 0.05);
        float s, y, x;
        switch (num) {
            default:
            case 3:
                x = t - 0.15f * (1 - t);
                s = mPathMeasure.getLength();
                y = -s * x * x + 2 * s * x;
                mPathMeasure.getSegment(y, y + 1, mDst, true);
            case 2:
                x = t - 0.10f * (1 - t);
                s = mPathMeasure.getLength();
                y = -s * x * x + 2 * s * x;
                mPathMeasure.getSegment(y, y + 1, mDst, true);
            case 1:
                x = t - 0.05f * (1 - t);
                s = mPathMeasure.getLength();
                y = -s * x * x + 2 * s * x;
                mPathMeasure.getSegment(y, y + 1, mDst, true);
            case 0:
                x = t;
                s = mPathMeasure.getLength();
                y = -s * x * x + 2 * s * x;
                mPathMeasure.getSegment(y, y + 1, mDst, true);
                break;
        }
        if (t >= 0.95) {
            canvas.drawPoint(0, -60, mPaint);
        } else {
            canvas.drawPath(mDst, mPaint);
        }
        canvas.drawPath(mDst, mPaint);
        mDst.reset();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

}
