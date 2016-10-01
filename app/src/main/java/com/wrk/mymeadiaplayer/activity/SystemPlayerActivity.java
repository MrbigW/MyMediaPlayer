package com.wrk.mymeadiaplayer.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.wrk.mymeadiaplayer.R;
import com.wrk.mymeadiaplayer.util.Utils;
import com.wrk.mymeadiaplayer.bean.MediaItem;
import com.wrk.mymeadiaplayer.view.VideoView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by MrbigW on 2016/9/28.
 * weChat:1024057635
 * Github:MrbigW
 * Usage: 系统播放器
 * -------------------=.=------------------------
 */
public class SystemPlayerActivity extends Activity implements View.OnClickListener {

    // 常量-视频进度更新
    private final static int PROGRESS = 0x001;
    // 常量-隐藏控制面板
    private static final int HIDE_MEDIACONTROLL = 0X002;

    // 默认
    private static final int SCREEN_DEFULT = 1;
    // 全屏
    private static final int SCREEN_FULL = 2;

    private VideoView videoview;
    private Uri mUri;
    private Utils mUtils;

    private LinearLayout llTop;
    private TextView tvName;
    private ImageView ivBattery;
    private TextView tvTime;
    private TextView tv_current;
    private TextView tv_duration;
    private Button btnVideoVoice;
    private SeekBar seekBar_voice;
    private SeekBar seekBar_video;
    private Button btnVideoSwitchPlayer;
    private LinearLayout llBottom;
    private Button btnVideoExit;
    private Button btnVideoPre;
    private Button btnVideoStartPause;
    private Button btnVideoNext;
    private Button btnVideoScreen;
    private MyReceiver mReceiver;

    // 视频列表数据
    private ArrayList<MediaItem> mMedialist;
    // 得到播放位置
    private int mPos;

    // 定义手势识别器
    private GestureDetector mdetector;
    // 是否全屏
    private boolean isFullScreen = false;

    //屏幕的宽和高
    private int videoWidth;
    private int videoHeight;

    // 视频的宽和高
    private int screenWidth;
    private int screenHeight;

    // 调节声音
    private AudioManager mAudioManager;
    private int currentVolume; //当前
    private int maxVolume; // 最大

    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2016-09-29 23:55:03 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews() {
        setContentView(R.layout.activity_systemplayer);
        tv_current = (TextView) findViewById(R.id.tv_current);
        videoview = (VideoView) findViewById(R.id.videoview);
        llTop = (LinearLayout) findViewById(R.id.ll_top);
        tvName = (TextView) findViewById(R.id.tv_name);
        ivBattery = (ImageView) findViewById(R.id.iv_battery);
        tv_duration = (TextView) findViewById(R.id.tv_duration);
        tvTime = (TextView) findViewById(R.id.tv_time);
        btnVideoVoice = (Button) findViewById(R.id.btn_video_voice);
        seekBar_voice = (SeekBar) findViewById(R.id.seekBar_voice);
        seekBar_video = (SeekBar) findViewById(R.id.seekBar_video);
        btnVideoSwitchPlayer = (Button) findViewById(R.id.btn_video_switch_player);
        llBottom = (LinearLayout) findViewById(R.id.ll_bottom);
        btnVideoExit = (Button) findViewById(R.id.btn_video_exit);
        btnVideoPre = (Button) findViewById(R.id.btn_video_pre);
        btnVideoStartPause = (Button) findViewById(R.id.btn_video_start_pause);
        btnVideoNext = (Button) findViewById(R.id.btn_video_next);
        btnVideoScreen = (Button) findViewById(R.id.btn_video_screen);

        btnVideoVoice.setOnClickListener(this);
        btnVideoSwitchPlayer.setOnClickListener(this);
        btnVideoExit.setOnClickListener(this);
        btnVideoPre.setOnClickListener(this);
        btnVideoStartPause.setOnClickListener(this);
        btnVideoNext.setOnClickListener(this);
        btnVideoScreen.setOnClickListener(this);

        // 最大音量
        seekBar_voice.setMax(maxVolume);
        seekBar_voice.setProgress(currentVolume);
    }

    // 默认静音
    private boolean isMute = false;

    /**
     * Handle button click events<br />
     * <br />
     * Auto-created on 2016-09-29 23:55:03 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    @Override
    public void onClick(View v) {
        if (v == btnVideoVoice) {
            isMute = !isMute;
            updateVolumn(currentVolume);
        } else if (v == btnVideoSwitchPlayer) {
            // Handle clicks for btnVideoSwitchPlayer
        } else if (v == btnVideoExit) {
            // Handle clicks for btnVideoExit
        } else if (v == btnVideoPre) {
            setPlayPreVideo();
        } else if (v == btnVideoStartPause) {
            setStartAndPause();
        } else if (v == btnVideoNext) {
            setPlayNextVideo();
        } else if (v == btnVideoScreen) {
            setVideoMode();
        }
        mHandle.removeMessages(HIDE_MEDIACONTROLL);
        mHandle.sendEmptyMessageDelayed(HIDE_MEDIACONTROLL, 3500);
    }

    private void setStartAndPause() {
        if (videoview.isPlaying()) {
            videoview.pause(); // 暂停
            btnVideoStartPause.setBackgroundResource(R.drawable.btn_video_start_selector);
        } else {
            videoview.start(); // 暂停
            btnVideoStartPause.setBackgroundResource(R.drawable.btn_video_pause_selector);
        }
    }


    private Handler mHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case PROGRESS:  // 设置进度
                    // 得到当前进度
                    int currentPosition = videoview.getCurrentPosition();
                    seekBar_video.setProgress(currentPosition);

                    // 设置时间的改变
                    tv_current.setText(mUtils.stringForTime(currentPosition));

                    // 得到系统的时间
                    tvTime.setText(getSystemTime());


                    // 移除消息，循环发送消息，以更新进度
                    removeMessages(PROGRESS);
                    sendEmptyMessageDelayed(PROGRESS, 1000);

                    break;
                case HIDE_MEDIACONTROLL:  // 隐藏控制面板
                    hideMediaConroller();
                    break;

            }
        }
    };

    /**
     * 获取系统时间
     *
     * @return
     */
    private String getSystemTime() {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        return format.format(new Date());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        findViews();

        initData();

        getData();

        setListener();

        setData();
//        videoview.setMediaController(new MediaController(SystemPlayerActivity.this));
    }

    private void setData() {
        if (mMedialist != null && mMedialist.size() > 0) {
            // 有列表数据
            MediaItem mediaItem = mMedialist.get(mPos);
            tvName.setText(mediaItem.getName());
            videoview.setVideoPath(mediaItem.getData());

        }
        // 第三方应用
        else if (mUri != null) {
            // 设置播放地址
            videoview.setVideoURI(mUri);
        } else {
            Toast.makeText(this, "没有传递数据进入播放器", Toast.LENGTH_SHORT).show();
        }

        setButtonState();
    }

    private void getData() {
        mUri = getIntent().getData();  //视频播放地址
        // 得到播放列表
        mMedialist = (ArrayList<MediaItem>) getIntent().getSerializableExtra("medialist");
        // 点击视频在列表中的位置
        mPos = getIntent().getIntExtra("pos", 0);

    }

    private void initData() {
        mUtils = new Utils();

        // 注册电量接收广播
        mReceiver = new MyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(mReceiver, filter);

        // 创建手势识别器
        mdetector = new GestureDetector(this, new MySimpleOnGestureListener());

        // 得到屏幕的宽和高
        DisplayMetrics outMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        screenWidth = outMetrics.widthPixels;
        screenHeight = outMetrics.heightPixels;

        // 得到当前相关音量信息
        mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

    }

    class MySimpleOnGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public void onLongPress(MotionEvent e) {
            setStartAndPause();
            super.onLongPress(e);
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            setVideoMode();
            return super.onDoubleTap(e);
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            if (isshowMediaConroller) {
                hideMediaConroller();
                // 移除消息
                mHandle.removeMessages(HIDE_MEDIACONTROLL);
            } else {
                showMediaConroller();
                mHandle.sendEmptyMessageDelayed(HIDE_MEDIACONTROLL, 3600);
            }
            return super.onSingleTapConfirmed(e);
        }
    }

    private void setVideoMode() {
        if (isFullScreen) {
            // 默认
            setVideoType(SCREEN_DEFULT);
        } else {
            // 全屏
            setVideoType(SCREEN_FULL);
        }
    }

    /**
     * 设置屏幕的类型
     *
     * @param videoType
     */
    private void setVideoType(int videoType) {
        switch (videoType) {
            case SCREEN_FULL:
                isFullScreen = true;
                videoview.setVideoSize(screenWidth, screenHeight);
                // 按钮状态
                btnVideoScreen.setBackgroundResource(R.drawable.btn_video_screen_full);

                break;
            case SCREEN_DEFULT:
                isFullScreen = false;

                // 视频z真实的宽和高
                int mVideoWidth = videoWidth;
                int mVideoHeight = videoHeight;
                // 屏幕真实的宽和高
                int width = screenWidth;
                int height = screenHeight;

                // for compatibility, we adjust size based on aspect ratio
                if (mVideoWidth * height < width * mVideoHeight) {
                    //Log.i("@@@", "image too wide, correcting");
                    width = height * mVideoWidth / mVideoHeight;
                } else if (mVideoWidth * height > width * mVideoHeight) {
                    //Log.i("@@@", "image too tall, correcting");
                    height = width * mVideoHeight / mVideoWidth;
                }

                videoview.setVideoSize(width, height);
                // 按钮状态
                btnVideoScreen.setBackgroundResource(R.drawable.btn_video_screen_default);
                break;
        }
    }

    private boolean isshowMediaConroller = false;

    /**
     * 隐藏控制面板
     */
    private void hideMediaConroller() {
        isshowMediaConroller = false;
        llTop.setVisibility(View.GONE);
        llBottom.setVisibility(View.GONE);
    }

    /**
     * 显示控制面板
     */
    private void showMediaConroller() {
        isshowMediaConroller = true;
        llTop.setVisibility(View.VISIBLE);
        llBottom.setVisibility(View.VISIBLE);
    }


    class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context contex, Intent intent) {
            int batteryLevel = intent.getIntExtra("level", 0);  // 得到电量0~100
            setBattery(batteryLevel);
        }
    }

    /**
     * 显示电量
     *
     * @param batteryLevel
     */
    private void setBattery(int batteryLevel) {
        if (batteryLevel < 0) {
            ivBattery.setImageResource(R.drawable.ic_battery_0);
        } else if (batteryLevel <= 10) {
            ivBattery.setImageResource(R.drawable.ic_battery_10);
        } else if (batteryLevel <= 20) {
            ivBattery.setImageResource(R.drawable.ic_battery_20);
        } else if (batteryLevel < 40) {
            ivBattery.setImageResource(R.drawable.ic_battery_40);
        } else if (batteryLevel < 60) {
            ivBattery.setImageResource(R.drawable.ic_battery_60);
        } else if (batteryLevel < 80) {
            ivBattery.setImageResource(R.drawable.ic_battery_80);
        } else if (batteryLevel < 100) {
            ivBattery.setImageResource(R.drawable.ic_battery_100);
        } else {
            ivBattery.setImageResource(R.drawable.ic_battery_100);
        }
    }

    private void setListener() {
        // 设置视频播放的准备，错误，完成的监听
        videoview.setOnPreparedListener(new MyOnPreparedListener());

        videoview.setOnErrorListener(new MyOnErrorListener());

        videoview.setOnCompletionListener(new MyOnCompletionListener());

        // 设置视频拖动
        seekBar_video.setOnSeekBarChangeListener(new MyVideoOnSeekBarChangeListener());

        // 设置音量的拖动
        seekBar_voice.setOnSeekBarChangeListener(new MyVolumeOnSeekBarChangeListener());
    }

    class MyVolumeOnSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser) {
                updateProgress(progress);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

            mHandle.removeMessages(HIDE_MEDIACONTROLL);
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            mHandle.sendEmptyMessageDelayed(HIDE_MEDIACONTROLL, 3500);
        }
    }

    private void updateVolumn(int progress) {
        if (isMute) {
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
            seekBar_voice.setProgress(0);
        } else {
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            currentVolume = progress;
            seekBar_voice.setProgress(progress);
        }

    }

    private void updateProgress(int progress) {
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
        currentVolume = progress;
        seekBar_voice.setProgress(progress);
        if (progress > 0) {
            isMute = false;
        } else {
            isMute = true;
        }
    }

    class MyVideoOnSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {
        /**
         * @param seekBar
         * @param progress
         * @param fromUser 自动为false，用户操作true
         */
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            if (fromUser) {
                videoview.seekTo(progress);
            }

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            mHandle.removeMessages(HIDE_MEDIACONTROLL);
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            mHandle.sendEmptyMessageDelayed(HIDE_MEDIACONTROLL, 3500);
        }
    }

    class MyOnPreparedListener implements MediaPlayer.OnPreparedListener {

        @Override
        public void onPrepared(MediaPlayer mp) {

            // 得到视频的宽和高
            videoWidth = mp.getVideoWidth();
            videoHeight = mp.getVideoHeight();

            videoview.start();

            // 获得视频总时长
            int duration = videoview.getDuration();
            seekBar_video.setMax(duration);
            tv_duration.setText(mUtils.stringForTime(duration));

            // 默认隐藏控制面板
            hideMediaConroller();

            // 发消息更新进度
            mHandle.sendEmptyMessage(PROGRESS);

            setVideoType(SCREEN_DEFULT);
//            videoview.setVideoSize(50, 50);
        }
    }

    class MyOnErrorListener implements MediaPlayer.OnErrorListener {

        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            Toast.makeText(SystemPlayerActivity.this, "播放出错~~~", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    class MyOnCompletionListener implements MediaPlayer.OnCompletionListener {

        @Override
        public void onCompletion(MediaPlayer mp) {
//            mp.seekTo(0);
//            mp.start();
            setPlayNextVideo();
        }

    }


    private void setPlayPreVideo() {

        if (mMedialist != null && mMedialist.size() > 0) {
            mPos--;
            if (mPos >= 0) {
                MediaItem item = mMedialist.get(mPos);
                tvName.setText(item.getName());
                videoview.setVideoPath(item.getData());

                setButtonState();
                if (mPos == mMedialist.size() - 1) {

                }

            } else {
                // 播放完成
                mPos = 0;
            }
        }

    }


    private void setPlayNextVideo() {
        if (mMedialist != null && mMedialist.size() > 0) {
            mPos++;
            if (mPos < mMedialist.size()) {
                MediaItem item = mMedialist.get(mPos);
                tvName.setText(item.getName());
                videoview.setVideoPath(item.getData());

                setButtonState();
                if (mPos == mMedialist.size() - 1) {
                    Toast.makeText(this, "正在播放最后一个视频", Toast.LENGTH_SHORT).show();
                }

            } else {
                // 播放完成
                finish();
            }
        } else if (mUri != null) { // 只有一个播放地址
            mPos = mMedialist.size() - 1;
            finish();
        }

    }

    /**
     * 设置按钮可点性
     */
    private void setButtonState() {
        if (mMedialist != null && mMedialist.size() > 0) {
            // 设置上一个和下一个可以点击
            setIsEnableButton(true);

            // 如果是第0个，上一个不可点
            if (mPos == 0) {
                btnVideoPre.setEnabled(false);
                btnVideoPre.setBackgroundResource(R.drawable.btn_pre_gray);
            }
            if (mPos == mMedialist.size() - 1) {
                btnVideoNext.setEnabled(false);
                btnVideoNext.setBackgroundResource(R.drawable.btn_next_gray);
            }
            // 如果是最后一个，下一个不可点

        } else if (mUri != null) {
            setIsEnableButton(false);
        }
    }

    private void setIsEnableButton(boolean enable) {

        if (enable) {
            btnVideoPre.setBackgroundResource(R.drawable.btn_video_pre_selector);
            btnVideoNext.setBackgroundResource(R.drawable.btn_video_next_selector);
        } else {
            btnVideoPre.setBackgroundResource(R.drawable.btn_pre_gray);
            btnVideoNext.setBackgroundResource(R.drawable.btn_next_gray);
        }
        btnVideoPre.setEnabled(enable);
        btnVideoNext.setEnabled(enable);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    @Override
    protected void onDestroy() {
        // 移除所有的消息和回调
        mHandle.removeCallbacksAndMessages(null);

        // 解除电量的广播接受者
        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
            mReceiver = null;

        }

        super.onDestroy();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 把事件传入手势识别器
        mdetector.onTouchEvent(event);
        super.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                break;
        }

        return true;
    }
}
