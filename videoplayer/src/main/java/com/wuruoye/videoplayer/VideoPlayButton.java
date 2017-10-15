package com.wuruoye.videoplayer;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by wuruoye on 2017/8/18.
 * this file is to do
 */

public class VideoPlayButton extends View {
    public static final int PLAY = 1;
    public static final int PAUSE = 2;
    public static final int LOAD = 3;

    private int width;
    private int height;
    private int mCurrentState = 0;
    private int rotation = 0;
    private Paint mPaint;
    private Path mPathLeft = new Path();
    private Path mPathRight = new Path();
    private PointF[] originPoint = new PointF[8];
    private PointF[] centerPoint = new PointF[8];
    private int aUnit;

    public void changeTo(int state){
        mCurrentState = state;
        switch (state){
            case PLAY:{
                float leftX = width / 2 - width / 6;
                float rightX = width / 2 + width / 6;
                setPoint(centerPoint, new float[]{
                        leftX, aUnit * 2, leftX, aUnit * 2, leftX, height - aUnit * 2, leftX, height - aUnit * 2,
                        rightX, aUnit * 2, rightX, aUnit * 2, rightX, height - aUnit * 2, rightX, height - aUnit * 2
                });
                doAnimation();
                break;
            }
            case PAUSE:{
                float leftX = width / 2;
                float rightX = width / 2;
                setPoint(centerPoint, new float[]{
                        leftX, aUnit * 2, leftX - aUnit * 3, aUnit * 2, leftX - aUnit * 3, height - aUnit * 2, leftX, height - aUnit * 2,
                        rightX, aUnit * 2, rightX + aUnit * 5, aUnit * 4, rightX + aUnit * 5, height - aUnit * 4, rightX, height - aUnit * 2
                });
                doAnimation();
                break;
            }
            case LOAD:{
                float leftX = width / 2 - width / 10;
                float rightX = width / 2 + width / 10;
                setPoint(centerPoint, new float[]{
                        leftX, aUnit * 2, leftX - aUnit * 3, aUnit * 3, leftX - aUnit * 3, height - aUnit * 3, leftX, height - aUnit * 2,
                        rightX, aUnit * 2, rightX + aUnit * 3, aUnit * 3, rightX + aUnit * 3, height - aUnit * 3, rightX, height - aUnit * 2
                });
                doAnimation();
                break;
            }
        }
    }

    private void doAnimation(){
        ValueAnimator[] valueAnimators = new ValueAnimator[16];
        for (int i = 0; i < valueAnimators.length; i ++){
            if (i % 2 == 0){
                valueAnimators[i] = ValueAnimator.ofFloat(originPoint[i / 2].x, centerPoint[i / 2].x);
                final int finalI = i;
                valueAnimators[i].addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        float value = (float) valueAnimator.getAnimatedValue();
                        originPoint[finalI / 2].set(value, originPoint[finalI / 2].y);
                        invalidate();
                    }
                });
            }else {
                valueAnimators[i] = ValueAnimator.ofFloat(originPoint[i / 2].y, centerPoint[i / 2].y);
                final int finalI1 = i;
                valueAnimators[i].addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        float value = (float) valueAnimator.getAnimatedValue();
                        originPoint[finalI1 / 2].set(originPoint[finalI1 / 2].x, value);
                        invalidate();
                    }
                });
            }
        }
        AnimatorSet set = new AnimatorSet();
        set.setDuration(300);
        set.playTogether(valueAnimators);
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                animatorEnd();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        set.start();
    }

    private void animatorEnd(){
        if (mCurrentState == LOAD){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    rotation += 3;
                    setRotation(rotation);
                    animatorEnd();
                }
            }, 10);
        }else {
            rotation = 0;
            setRotation(0);
        }
    }

    private void init(){
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(5);
        mPaint.setColor(ActivityCompat.getColor(getContext(), R.color.romance));
        for (int i = 0; i < 8; i ++){
            originPoint[i] = new PointF();
            centerPoint[i] = new PointF();
        }
    }

    private void setPoint(PointF[] points, float[] values){
        for (int i = 0; i < points.length; i ++){
            points[i].set(values[i * 2], values[i * 2 + 1]);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        mPathLeft.reset();
        mPathRight.reset();

        mPathLeft.moveTo(originPoint[0].x, originPoint[0].y);
        mPathLeft.cubicTo(originPoint[1].x, originPoint[1].y, originPoint[2].x, originPoint[2].y, originPoint[3].x, originPoint[3].y);
        mPathRight.moveTo(originPoint[4].x, originPoint[4].y);
        mPathRight.cubicTo(originPoint[5].x, originPoint[5].y, originPoint[6].x, originPoint[6].y, originPoint[7].x, originPoint[7].y);
        canvas.drawPath(mPathLeft, mPaint);
        canvas.drawPath(mPathRight, mPaint);
    }

    public VideoPlayButton(Context context) {
        super(context);
        init();
    }

    public VideoPlayButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.width = w;
        this.height = h;
        aUnit = w / 10;
        float leftX = w / 2 - w / 6;
        float rightX = w / 2 + w / 6;
        setPoint(originPoint, new float[]{
                leftX, aUnit * 2, leftX, aUnit * 2, leftX, height - aUnit * 2, leftX, height - aUnit * 2,
                rightX, aUnit * 2, rightX, aUnit * 2, rightX, height - aUnit * 2, rightX, height - aUnit * 2
        });
    }
}
