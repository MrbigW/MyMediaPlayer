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
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.wrk.mymeadiaplayer.R;
import com.wrk.mymeadiaplayer.adapter.MyTopListAdapter;
import com.wrk.mymeadiaplayer.adapter.MyTopMoreAdapter;
import com.wrk.mymeadiaplayer.bean.MediaItem;
import com.wrk.mymeadiaplayer.bean.NetMedia;
import com.wrk.mymeadiaplayer.util.DensityUtil;
import com.wrk.mymeadiaplayer.util.Utils;
import com.wrk.mymeadiaplayer.view.VideoView;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import master.flame.danmaku.controller.IDanmakuView;
import master.flame.danmaku.danmaku.loader.ILoader;
import master.flame.danmaku.danmaku.loader.IllegalDataException;
import master.flame.danmaku.danmaku.loader.android.DanmakuLoaderFactory;
import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.DanmakuTimer;
import master.flame.danmaku.danmaku.model.IDanmakus;
import master.flame.danmaku.danmaku.model.IDisplayer;
import master.flame.danmaku.danmaku.model.android.DanmakuContext;
import master.flame.danmaku.danmaku.model.android.Danmakus;
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser;
import master.flame.danmaku.danmaku.parser.IDataSource;
import master.flame.danmaku.danmaku.parser.android.BiliDanmukuParser;

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
    private Button btn_video_danmu;
    private Button btn_video_list;
    private Button btn_video_more;
    private LinearLayout ll_top_top;

    // top_list的popupwindow
    private PopupWindow popListWin;
    private ListView topList;

    // 网络视频列表
    private ArrayList<NetMedia> mNetMedias;

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

    // 默认静音
    private boolean isMute = false;

    // 调节声音
    private AudioManager mAudioManager;
    private int currentVolume; //当前
    private int maxVolume; // 最大

    // 视频质量的popupWindow
    private PopupWindow popMoreWin;
    private ListView popMoreList;

    // 弹幕view
    private IDanmakuView mDanmakuView;
    // 弹幕解析器
    private BaseDanmakuParser mParser;
    // 弹幕上下文
    private DanmakuContext mContext;

    private boolean isshowMediaConroller = false;

    private boolean isDanmuShowing = false;


    private void findViews() {
        setContentView(R.layout.activity_systemplayer);
        ll_top_top = (LinearLayout) findViewById(R.id.ll_top_top);
        btn_video_more = (Button) findViewById(R.id.btn_video_more);
        btn_video_list = (Button) findViewById(R.id.btn_video_list);
        btn_video_danmu = (Button) findViewById(R.id.btn_video_danmu);
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

        btn_video_more.setOnClickListener(this);
        btn_video_danmu.setOnClickListener(this);
        btn_video_list.setOnClickListener(this);
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


    @Override
    public void onClick(View v) {
        if (v == btnVideoVoice) {
            isMute = !isMute;
            updateVolumn(currentVolume);
            mHandle.removeMessages(HIDE_MEDIACONTROLL);
            mHandle.sendEmptyMessageDelayed(HIDE_MEDIACONTROLL, 3500);
        } else if (v == btnVideoSwitchPlayer) {
            // Handle clicks for btnVideoSwitchPlayer
            mHandle.removeMessages(HIDE_MEDIACONTROLL);
            mHandle.sendEmptyMessageDelayed(HIDE_MEDIACONTROLL, 3500);
        } else if (v == btnVideoExit) {
            // Handle clicks for btnVideoExit
            mHandle.removeMessages(HIDE_MEDIACONTROLL);
            mHandle.sendEmptyMessageDelayed(HIDE_MEDIACONTROLL, 3500);
        } else if (v == btnVideoPre) {
            setPlayPreVideo();
            mHandle.removeMessages(HIDE_MEDIACONTROLL);
            mHandle.sendEmptyMessageDelayed(HIDE_MEDIACONTROLL, 3500);
        } else if (v == btnVideoStartPause) {
            setStartAndPause();
            mHandle.removeMessages(HIDE_MEDIACONTROLL);
            mHandle.sendEmptyMessageDelayed(HIDE_MEDIACONTROLL, 3500);
        } else if (v == btnVideoNext) {
            setPlayNextVideo();
            mHandle.removeMessages(HIDE_MEDIACONTROLL);
            mHandle.sendEmptyMessageDelayed(HIDE_MEDIACONTROLL, 3500);
        } else if (v == btnVideoScreen) {
            setVideoMode();
            mHandle.removeMessages(HIDE_MEDIACONTROLL);
            mHandle.sendEmptyMessageDelayed(HIDE_MEDIACONTROLL, 3500);
        } else if (v == btn_video_danmu) {
            if (!isDanmuShowing) {
                isDanmuShowing = true;
                mDanmakuView.setVisibility(View.VISIBLE);
                btn_video_danmu.setBackgroundResource(R.drawable.btn_danmu_preesed);

            } else {
                isDanmuShowing = false;
                mDanmakuView.setVisibility(View.GONE);
                btn_video_danmu.setBackgroundResource(R.drawable.btn_danmu_normal);
            }
            mHandle.removeMessages(HIDE_MEDIACONTROLL);
            mHandle.sendEmptyMessageDelayed(HIDE_MEDIACONTROLL, 3500);
        } else if (v == btn_video_list) {
            hidePopMoreWin();
            initPopListWin();
            popListWin.showAsDropDown(ll_top_top, screenWidth / 2, 0);
            mHandle.removeMessages(HIDE_MEDIACONTROLL);
        } else if (v == btn_video_more) {
            hidePopListWin();
            initPopMoreWin();
            popMoreWin.showAsDropDown(btn_video_more, 0, 1);
            mHandle.removeMessages(HIDE_MEDIACONTROLL);
        }

    }

    private void initPopListWin() {
        if (popListWin == null) {
            btn_video_list.setBackgroundResource(R.drawable.btn_danmu_preesed);
            popListWin = new PopupWindow(SystemPlayerActivity.this);
            popListWin.setWidth(screenWidth / 2);
            popListWin.setHeight(screenHeight - (ll_top_top.getHeight()));
            popListWin.setContentView(topList);
            popListWin.setFocusable(false);
        }
    }

    private void initPopMoreWin() {
        if (popMoreWin == null) {
            btn_video_more.setBackgroundResource(R.drawable.btn_danmu_preesed);
            popMoreWin = new PopupWindow(SystemPlayerActivity.this);
            popMoreWin.setWidth(DensityUtil.dip2px(SystemPlayerActivity.this, 65));
            popMoreWin.setHeight(DensityUtil.dip2px(SystemPlayerActivity.this, 75));
            popMoreWin.setContentView(popMoreList);
            popMoreWin.setFocusable(false);
        }
    }


    private BaseDanmakuParser createParser(InputStream stream) {
        if (stream == null) {
            return new BaseDanmakuParser() {
                @Override
                protected IDanmakus parse() {
                    return new Danmakus();
                }
            };
        }

        /**
         * DanmakuLoaderFactory.create(DanmakuLoaderFactory.TAG_BILI) //xml解析
         * DanmakuLoaderFactory.create(DanmakuLoaderFactory.TAG_ACFUN) //json文件格式解析
         */
        ILoader loader = DanmakuLoaderFactory.create(DanmakuLoaderFactory.TAG_BILI);

        try {
            loader.load(stream);
        } catch (IllegalDataException e) {
            e.printStackTrace();
        }

        BaseDanmakuParser parser = new BiliDanmukuParser();
        IDataSource<?> dataSource = loader.getDataSource();
        parser.load(dataSource);
        return parser;
    }

    private void initDanmu() {
        // 设置最大显示行数
        HashMap<Integer, Integer> maxLinesPair = new HashMap<Integer, Integer>();
        maxLinesPair.put(BaseDanmaku.TYPE_SCROLL_RL, 5); // 滚动弹幕最大显示5行
        // 设置是否禁止重叠
        HashMap<Integer, Boolean> overlappingEnablePair = new HashMap<Integer, Boolean>();
        overlappingEnablePair.put(BaseDanmaku.TYPE_SCROLL_RL, true);
        overlappingEnablePair.put(BaseDanmaku.TYPE_FIX_TOP, true);

        mDanmakuView = (IDanmakuView) findViewById(R.id.sv_danmaku);
        mContext = DanmakuContext.create();
        mContext.setDanmakuStyle(IDisplayer.DANMAKU_STYLE_STROKEN, 3)
                .setDuplicateMergingEnabled(false)//是否启用合并重复弹幕
                .setScrollSpeedFactor(1.2f)   //设置弹幕滚动速度系数,只对滚动弹幕有效
                .setScaleTextSize(1.2f)    // 设置弹幕文字大小
//                .setCacheStuffer(new SpannedCacheStuffer(), mCacheStufferAdapter) // 图文混排使用SpannedCacheStuffer
//               .setCacheStuffer(new BackgroundCacheStuffer())  // 绘制背景使用BackgroundCacheStuffer
                .setMaximumLines(maxLinesPair) //设置最大显示行数
                .preventOverlapping(overlappingEnablePair);  //设置防弹幕重叠，null为允许重叠
        if (mDanmakuView != null) {
            mParser = createParser(this.getResources().openRawResource(R.raw.comments)); //创建解析器对象，从raw资源目录下解析comments.xml文本
            mDanmakuView.setCallback(new master.flame.danmaku.controller.DrawHandler.Callback() {
                @Override
                public void updateTimer(DanmakuTimer timer) {
                }

                @Override
                public void drawingFinished() {

                }

                @Override
                public void danmakuShown(BaseDanmaku danmaku) {
//                    showMediaConroller();
//                    mHandle.removeMessages(HIDE_MEDIACONTROLL);
//                    mHandle.sendEmptyMessageDelayed(HIDE_MEDIACONTROLL, 3500);
                    if (danmaku == null) {
                        showMediaConroller();
                    }
                }

                @Override
                public void prepared() {
                    mDanmakuView.start();
                }
            });

            mDanmakuView.setOnDanmakuClickListener(new IDanmakuView.OnDanmakuClickListener() {

                @Override
                public void onDanmakuClick(BaseDanmaku latest) {
                    Toast.makeText(SystemPlayerActivity.this, latest.text, Toast.LENGTH_SHORT).show();
                    mHandle.removeMessages(HIDE_MEDIACONTROLL);
                    mHandle.sendEmptyMessageDelayed(HIDE_MEDIACONTROLL, 3500);
                    showMediaConroller();
                }

                @Override
                public void onDanmakuClick(IDanmakus danmakus) {
                    mHandle.removeMessages(HIDE_MEDIACONTROLL);
                    mHandle.sendEmptyMessageDelayed(HIDE_MEDIACONTROLL, 3500);
                    showMediaConroller();
                }
            });
            mDanmakuView.prepare(mParser, mContext);
            mDanmakuView.enableDanmakuDrawingCache(true);
        }

        mDanmakuView.setVisibility(View.GONE);
    }


    /**
     * 隐藏PopList
     */
    private void hidePopListWin() {
        if (popListWin != null && popListWin.isShowing()) {
            popListWin.dismiss();
            popListWin = null;
            btn_video_list.setBackgroundResource(R.drawable.btn_danmu_normal);
        }
    }

    /**
     * PopMore
     */
    private void hidePopMoreWin() {
        if (popMoreWin != null && popMoreWin.isShowing()) {
            popMoreWin.dismiss();
            popMoreWin = null;
            btn_video_more.setBackgroundResource(R.drawable.btn_danmu_normal);
        }
    }

    private void setStartAndPause() {
        if (videoview.isPlaying()) {
            videoview.pause(); // 暂停
            mDanmakuView.pause();
            btnVideoStartPause.setBackgroundResource(R.drawable.btn_video_start_selector);
        } else {
            videoview.start(); // 暂停
            mDanmakuView.resume();
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
            tvName.setText(getIntent().getStringExtra("name"));
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

        /**
         * 初始化弹幕信息
         */
        initDanmu();

        /**
         * 为topMore填充数据
         */
        initTopMoreData();

        /**
         * 为topList填充数据
         */
        initTopListData();


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

    private MyTopMoreAdapter ad;
    private boolean isHignUri = false;


    private void initTopMoreData() {
        ad = new MyTopMoreAdapter(this);
        popMoreList = new ListView(this);
        popMoreList.setBackgroundResource(R.drawable.bg_player_top_control);
        popMoreList.setAdapter(ad);
        popMoreList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0 && !isHignUri) {
                    Toast.makeText(SystemPlayerActivity.this, "目前为标清格式~", Toast.LENGTH_SHORT).show();
                } else if (position == 0 && isHignUri) {
                    isHignUri = false;
                    int netMediaId = getIntent().getIntExtra("id", 0);

                    for (int i = 0; i < mNetMedias.size(); i++) {
                        if (netMediaId == mNetMedias.get(i).getId()) {
                            videoview.setVideoURI(Uri.parse(mNetMedias.get(i).getUrl()));
                            hidePopMoreWin();
                        }
                    }
                    btn_video_more.setText("标清");
                } else if (position == 1 && !isHignUri) {
                    isHignUri = true;
                    int netMediaId = getIntent().getIntExtra("id", 0);
                    for (int i = 0; i < mNetMedias.size(); i++) {
                        if (netMediaId == mNetMedias.get(i).getId()) {
                            videoview.setVideoURI(Uri.parse(mNetMedias.get(i).getHightUrl()));
                            hidePopMoreWin();
                        }
                    }
                    btn_video_more.setText("高清");
                } else if (position == 1 && isHignUri) {
                    Toast.makeText(SystemPlayerActivity.this, "目前为高清格式~", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initTopListData() {
        topList = new ListView(SystemPlayerActivity.this);
        topList.setBackgroundResource(R.drawable.bg_player_top_control);
        mNetMedias = (ArrayList<NetMedia>) getIntent().getSerializableExtra("netmedialist");
        Log.e("666", mNetMedias.toString());
        if (mNetMedias != null && mNetMedias.size() > 0) {
            topList.setAdapter(new MyTopListAdapter(SystemPlayerActivity.this, mNetMedias));
            topList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    NetMedia netMedia = mNetMedias.get(position);
                    Toast.makeText(SystemPlayerActivity.this, netMedia.getMovieName(), Toast.LENGTH_SHORT).show();
                    Uri uri = Uri.parse(netMedia.getUrl());
                    videoview.setVideoURI(uri);
                    hidePopListWin();
                }
            });
        }
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


    /**
     * 隐藏控制面板
     */
    private void hideMediaConroller() {
        isshowMediaConroller = false;
        llTop.setVisibility(View.GONE);
        llBottom.setVisibility(View.GONE);
        hidePopListWin();
        hidePopMoreWin();
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
        } else if (mNetMedias != null && mNetMedias.size() > 0) {
            finish();
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

        // 恢复弹幕
        if (mDanmakuView != null && mDanmakuView.isPrepared() && mDanmakuView.isPaused()) {
            mDanmakuView.resume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        // 暂停弹幕
        if (mDanmakuView != null && mDanmakuView.isPrepared()) {
            mDanmakuView.pause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 移除所有的消息和回调
        mHandle.removeCallbacksAndMessages(null);

        // 解除电量的广播接受者
        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
            mReceiver = null;

        }
        // 销毁并释放弹幕view资源
        if (mDanmakuView != null) {
            // dont forget release!
            mDanmakuView.release();
            mDanmakuView = null;
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // 销毁并释放弹幕view资源
        if (mDanmakuView != null) {
            // dont forget release!
            mDanmakuView.release();
            mDanmakuView = null;
        }
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
