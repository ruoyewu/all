package com.wuruoye.photoview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

/**
 * Created by wuruoye on 2017/10/8.
 * this file is to do
 */

public class PhotoView extends android.support.v7.widget.AppCompatImageView {

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    public PhotoView(Context context) {
        super(context);
    }

    public PhotoView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PhotoView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
