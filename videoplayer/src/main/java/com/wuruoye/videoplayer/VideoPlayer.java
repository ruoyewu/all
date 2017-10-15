package com.wuruoye.videoplayer;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.PointF;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.transitionseverywhere.Fade;
import com.transitionseverywhere.TransitionManager;
import com.transitionseverywhere.TransitionSet;

/**
 * Created by wuruoye on 2017/8/17.
 * this file is to do
 */

public class VideoPlayer extends RelativeLayout{
    private VideoView mVideoView;
    private RelativeLayout mTopView;
    private RelativeLayout mBottomView;
    private VideoPlayButton mPlayerView;
    private TextView mSeekTimeView;
    private ImageView mBackView;
    private ImageView mMenuView;
    private TextView mTitleView;
    private TextView mTimeFromView;
    private TextView mTimeToView;
    private VideoProgress mProgressView;
    private VideoProgress mCenterProgressView;
    private ImageView mOrientationView;

    //当前状态码 播放，暂停，加载
    private static final int MODE_PLAY = 1;
    private static final int MODE_PAUSE = 2;
    private static final int MODE_LOAD = 3;

    //滑动状态 亮度，音量，进度
    private static final int MODE_LIGHT = 4;
    private static final int MODE_SOUND = 5;
    private static final int MODE_PROGRESS = 6;

    private int videoWidth;
    private int mCurrentStateMode = MODE_PAUSE;
    private boolean isOpenBar;
    private boolean isMoveListen = true;
    private float mMinMove = 20;
    private int mCurrentMoveMode = 0;
    private PointF mStartPoint = new PointF();
    private boolean isClick;
    private int autoTime = 0;
    private OnVideoListener onVideoListener;
    private AudioManager audioManager;
    private int originalVolume;
    private Window mWindow;
    private float originalBrightness;
    private AlertDialog menuDialog;
    private OnMenuListener menuListener;

    private String TAG = "ruoye";

    public void setPath(String path){
        mVideoView.setVideoPath(path);
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                start();
            }
        });
    }

    public void setTitle(String title){
        mTitleView.setText(title);
    }

    public void setWindow(Window window){
        mWindow = window;
    }

    public void setMenu(String[] items, OnMenuListener menuListener){
        initDialog(items);
        this.menuListener = menuListener;
        mMenuView.setVisibility(VISIBLE);
    }

    public void setIsMoveListen(boolean is){
        isMoveListen = is;
        mBackView.setVisibility(is ? VISIBLE : GONE);
    }

    public void setOnVideoListener(OnVideoListener listener){
        this.onVideoListener = listener;
    }

    public void start(){
        mCurrentStateMode = MODE_PLAY;
        autoTime = 0;
        mVideoView.start();
        initTime();
        mPlayerView.changeTo(VideoPlayButton.PLAY);
    }

    public void pause(){
        mCurrentStateMode = MODE_PAUSE;
        mPlayerView.setVisibility(VISIBLE);
        mVideoView.pause();
        mPlayerView.changeTo(VideoPlayButton.PAUSE);
    }

    public void load(){
        mCurrentStateMode = MODE_LOAD;
        mPlayerView.setVisibility(VISIBLE);
        mPlayerView.changeTo(VideoPlayButton.LOAD);
    }

    private void init(){
        initViews();
        autoHideBar();
        audioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
    }

    private void initViews(){
        removeAllViews();
        View view = LayoutInflater.from(getContext())
                .inflate(R.layout.view_video_player, null);
        mVideoView = view.findViewById(R.id.vv_video);
        mTopView = view.findViewById(R.id.rl_video_top);
        mBottomView = view.findViewById(R.id.rl_video_bottom);
        mPlayerView = view.findViewById(R.id.vpb_video_button);
        mSeekTimeView = view.findViewById(R.id.tv_video_seek);
        mBackView = view.findViewById(R.id.iv_video_back);
        mMenuView = view.findViewById(R.id.iv_video_menu);
        mTitleView = view.findViewById(R.id.tv_video_title);
        mTimeFromView = view.findViewById(R.id.tv_video_time_f);
        mTimeToView = view.findViewById(R.id.tv_video_time_t);
        mProgressView = view.findViewById(R.id.vp_video_progress);
        mCenterProgressView = view.findViewById(R.id.vp_video_center);
        mOrientationView = view.findViewById(R.id.iv_video_orientation);
        addView(view);
        view.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        mPlayerView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onCenterClick();
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            mVideoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                @Override
                public boolean onInfo(MediaPlayer mediaPlayer, int i, int i1) {
                    VideoPlayer.this.onInfo(i);
                    return false;
                }
            });
        }
        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                pause();
            }
        });
        mProgressView.setListener(new VideoProgress.OnProgressListener() {
            @Override
            public void onProgress(float progress) {
                autoTime = 0;
                int time = (int) (mVideoView.getDuration() * progress);
                mVideoView.seekTo(time);
            }
        });
        mCenterProgressView.setOrientation(VideoProgress.VERTICAL);
        mBackView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onVideoListener != null){
                    onVideoListener.onBackClick();
                }
            }
        });
        mOrientationView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onVideoListener != null){
                    onVideoListener.changeOrientation();
                }
            }
        });
        mMenuView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                menuDialog.show();
                pause();
            }
        });
    }

    private void initTime(){
        try {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    int duration = mVideoView.getDuration();
                    int position = mVideoView.getCurrentPosition();
                    mTimeToView.setText("- " + ms2time(duration - position));
                    mTimeFromView.setText(ms2time(position));
                    float progress = (float) (position * 1.0 / duration * 100);
                    mProgressView.setProgress(progress);
                    if (mCurrentStateMode == MODE_PLAY) {
                        initTime();
                    }
                }
            }, 250);
        } catch (Exception ignored) {

        }
    }

    private void initDialog(String[] items){
        menuDialog = new AlertDialog.Builder(getContext())
                .setTitle("选择操作")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        menuListener.onMenuClick(i);
                    }
                })
                .create();
    }

    private void autoHideBar(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                autoTime ++;
                if (autoTime == 2 && mCurrentStateMode == MODE_PLAY){
                    isOpenBar = false;
                    openBar();
                }
                autoHideBar();
            }
        }, 1000);
    }

    private void onCenterClick(){
        autoTime = 0;
        switch (mCurrentStateMode){
            case MODE_PLAY: {
                pause();
                break;
            }
            case MODE_PAUSE: {
                start();
                break;
            }
        }
    }

    private void onDoubleClick(){
        onCenterClick();
    }

    private void onInfo(int what){
        switch (what){
            case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                load();
                break;
            case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                start();
                autoTime = 0;
                break;
        }
    }

    private void openBar(){
        TransitionSet set = new TransitionSet()
                .addTransition(new Fade());
        TransitionManager.beginDelayedTransition(this, set);
        mTopView.setVisibility(isOpenBar ? VISIBLE : GONE);
        if (mCurrentStateMode == MODE_LOAD || mCurrentStateMode == MODE_PAUSE){
            mPlayerView.setVisibility(VISIBLE);
        }else {
            mPlayerView.setVisibility(isOpenBar ? VISIBLE : GONE);
        }
        mBottomView.setVisibility(isOpenBar ? VISIBLE : GONE);
    }

    private void seekTo(int length, boolean isSeek){
        mPlayerView.setVisibility(GONE);
        mSeekTimeView.setVisibility(isSeek ? GONE : VISIBLE);
        int position = mVideoView.getCurrentPosition();
        int target = position + length;
        if (target < 0){
            target = 0;
        }else if (target > mVideoView.getDuration()){
            target = mVideoView.getDuration();
        }
        String text = ms2time(target) + " / " + ms2time(mVideoView.getDuration());
        mSeekTimeView.setText(text);
        if (isSeek) {
            mVideoView.seekTo(target);
            start();
            if (isOpenBar){
                mPlayerView.setVisibility(VISIBLE);
            }
        }else {
            pause();
            mPlayerView.setVisibility(GONE);
        }
    }

    private void changeSound(int length){
        length = length / 50;
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int current = length + originalVolume;
        float progress = (float) (current * 1.0 / maxVolume * 100);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, current, AudioManager.AUDIO_SESSION_ID_GENERATE);
        mCenterProgressView.setVisibility(VISIBLE);
        mCenterProgressView.setProgress(progress);
    }

    private void changeLight(int length){
        length = length / 5;
        float maxBrightness = 255;
        float current = originalBrightness + length;
        if (current > 255){
            current = 255;
        } else if (current < 0) {
            current = 0;
        }
        float progress = current / maxBrightness * 100;
        WindowManager.LayoutParams lp = mWindow.getAttributes();
        lp.screenBrightness = current / 255f;
        mWindow.setAttributes(lp);
        mCenterProgressView.setVisibility(VISIBLE);
        mCenterProgressView.setProgress(progress);
        Log.e("ruoye", "changeLight: " + progress + " + " + length + " + " + originalBrightness);
    }

    private String ms2time(int ms){
        ms /= 1000;
        int second = ms % 60;
        ms /= 60;
        int minute = ms % 60;
        ms /= 60;
        int hour = ms;
        StringBuilder builder = new StringBuilder();
        if (hour == 0){

        }else {
            if (hour < 10){
                builder.append(0).append(hour).append(":");
            }else {
                builder.append(hour).append(":");
            }
        }
        if (minute < 10){
            builder.append(0).append(minute).append(":");
        }else {
            builder.append(minute).append(":");
        }
        if (second < 10){
            builder.append(0).append(second);
        }else {
            builder.append(second);
        }
        return builder.toString();
    }

    public VideoPlayer(Context context) {
        super(context);
        init();
    }

    public VideoPlayer(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        videoWidth = w;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        autoTime = 0;
        if (isMoveListen) {
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:{
                    originalVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                    originalBrightness = mWindow.getAttributes().screenBrightness * 255f;
                    if (originalBrightness < 0){
                        originalBrightness = 0;
                    }
                    mStartPoint.set(event.getRawX(), event.getRawY());
                    if (!isClick){
                        isClick = true;
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (isClick){
                                    isClick = false;
                                    isOpenBar = !isOpenBar;
                                    openBar();
                                }
                            }
                        }, 300);
                    }else {
                        isClick = false;
                        onDoubleClick();
                    }
                    break;
                }
                case MotionEvent.ACTION_MOVE:{
                    float dx = event.getRawX() - mStartPoint.x;
                    float dy = event.getRawY() - mStartPoint.y;
                    if (dx != 0 || dy != 0){
                        isClick = false;
                    }
                    if (mCurrentMoveMode == 0){
                        if (dx > mMinMove || dx < -mMinMove){
                            mCurrentMoveMode = MODE_PROGRESS;
                        }else if (dy > mMinMove || dy < -mMinMove){
                            if (mStartPoint.x < videoWidth / 2){
                                mCurrentMoveMode = MODE_LIGHT;
                            }else {
                                mCurrentMoveMode = MODE_SOUND;
                            }
                        }
                    }else if (mCurrentMoveMode == MODE_PROGRESS){
                        int length = (int) ((dx - mMinMove) * 30);
                        seekTo(length, false);
                    }else if (mCurrentMoveMode == MODE_SOUND){
                        int length = -(int) (dy - mMinMove);
                        changeSound(length);
                    }else if (mCurrentMoveMode == MODE_LIGHT){
                        int length = (int) -(dy - mMinMove);
                        changeLight(length);
                    }
                    break;
                }
                case MotionEvent.ACTION_UP:{
                    if (mCurrentMoveMode == MODE_PROGRESS){
                        float dx = event.getRawX() - mStartPoint.x;
                        int length = (int) ((dx - mMinMove) * 30);
                        seekTo(length, true);
                    }
                    mCenterProgressView.setVisibility(GONE);
                    mCurrentMoveMode = 0;
                    break;
                }
            }
            return true;
        }
        return false;
    }

    public interface OnVideoListener{
        void onBackClick();
        void changeOrientation();
    }

    public interface OnMenuListener{
        void onMenuClick(int position);
    }
}
