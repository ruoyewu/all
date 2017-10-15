package com.wuruoye.videoplayer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by wuruoye on 2017/8/18.
 * this file is to do
 */

public class VideoProgress extends View {
    public static final int HORIZONTAL = 1;
    public static final int VERTICAL = 2;

    private int width;
    private int height;
    private float progress = 0;
    private int progressRadius = 10;
    private int progressHeight = 8;
    private Paint mPaint;
    private OnProgressListener listener;
    private int mDirection = HORIZONTAL;

    public void setProgress(float progress){
        if (progress > 100){
            this.progress = 100;
        }else if (progress < 0){
            this.progress = 0;
        }else {
            this.progress = progress;
        }
        invalidate();
    }

    public void setOrientation(int direct){
        mDirection = direct;
        invalidate();
    }

    public void setListener(OnProgressListener listener){
        this.listener = listener;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        float fromX, fromY, toX, toY;
        if (mDirection == HORIZONTAL) {
            fromX = progressRadius;
            toX = (float) (progressRadius + (width - progressRadius * 2) * 1.0 / 100 * progress);
            fromY = (float) ((height - progressHeight) * 1.0 / 2);
            toY = (float) ((height + progressHeight) * 1.0 / 2);
            canvas.drawCircle(toX, (height / 2), progressRadius, mPaint);
        }else {
            fromX = (float) ((width - progressHeight) * 1.0 / 2);
            toX = (float) ((width + progressHeight) * 1.0 / 2);
            fromY = height - progressRadius;
            toY =  (float) (height - progressRadius - (height - progressRadius * 2) * 1.0 / 100 * progress);
            canvas.drawCircle(width / 2, toY, progressRadius, mPaint);
        }
        RectF rect = new RectF(fromX, fromY, toX, toY);
        canvas.drawRoundRect(rect, 2, 2, mPaint);
    }

    private void init(){
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setColor(ActivityCompat.getColor(getContext(), R.color.romance));
        mPaint.setAntiAlias(true);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:{
                float progress = 0;
                if (mDirection == HORIZONTAL) {
                    float x = event.getX();
                    progress = (x - progressRadius) / (width - progressRadius * 2);
                }
                if (listener != null) {
                        listener.onProgress(progress);
                }
                break;
            }
            case MotionEvent.ACTION_MOVE:{
                float progress = 0;
                if (mDirection == HORIZONTAL) {
                    float x = event.getX();
                    progress = (x - progressRadius) / (width - progressRadius * 2);
                }
                if (listener != null) {
                    listener.onProgress(progress);
                }
                break;
            }
        }
        return true;
    }

    public VideoProgress(Context context) {
        super(context);
        init();
    }

    public VideoProgress(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.width = w;
        this.height = h;
    }

    interface OnProgressListener{
        void onProgress(float progress);
    }
}
