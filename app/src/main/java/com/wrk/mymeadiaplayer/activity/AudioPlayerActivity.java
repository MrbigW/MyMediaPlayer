package com.wrk.mymeadiaplayer.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.wrk.mymeadiaplayer.IMusicPlayerService;
import com.wrk.mymeadiaplayer.R;
import com.wrk.mymeadiaplayer.adapter.MyMusicListAdapter;
import com.wrk.mymeadiaplayer.bean.MediaItem;
import com.wrk.mymeadiaplayer.service.MusicPlayerService;
import com.wrk.mymeadiaplayer.util.Utils;

import java.util.ArrayList;

public class AudioPlayerActivity extends Activity implements View.OnClickListener {

    /**
     * 进度
     */
    private static final int PROGRESS = 0x001;

    private int screenWidth;
    private int screenHeight;

    private ArrayList<MediaItem> mMediaItems;

    // 列表弹窗
    private PopupWindow popMusicWin;
    private ListView popMusicList;


    private TextView tvArtist;
    private TextView tvAudioName;
    private TextView audioTime;
    private SeekBar seekBarAudio;
    private Button btnAudioPlaymode;
    private Button btnAudioPre;
    private Button btnAudioStartPause;
    private Button btnAudioNext;
    private Button btnAudioList;
    private RelativeLayout activity_audio_player;


    private Utils mUtils;

    // 当前播放位置
    private int position;

    // 广播接受者
    private MyBroadcastReceiver mReceiver;


    private void findViews() {

        setContentView(R.layout.activity_audioplayer);
        activity_audio_player = (RelativeLayout) findViewById(R.id.activity_audio_player);
        tvArtist = (TextView) findViewById(R.id.tv_artist);
        tvAudioName = (TextView) findViewById(R.id.tv_audioName);
        audioTime = (TextView) findViewById(R.id.audioTime);
        seekBarAudio = (SeekBar) findViewById(R.id.seekBar_audio);
        btnAudioPlaymode = (Button) findViewById(R.id.btn_audio_playmode);
        btnAudioPre = (Button) findViewById(R.id.btn_audio_pre);
        btnAudioStartPause = (Button) findViewById(R.id.btn_audio_start_pause);
        btnAudioNext = (Button) findViewById(R.id.btn_audio_next);
        btnAudioList = (Button) findViewById(R.id.btn_audio_list);
        mUtils = new Utils();


        btnAudioPlaymode.setOnClickListener(this);
        btnAudioPre.setOnClickListener(this);
        btnAudioStartPause.setOnClickListener(this);
        btnAudioNext.setOnClickListener(this);
        btnAudioList.setOnClickListener(this);

        // 设置音频进度的拖拽

        seekBarAudio.setOnSeekBarChangeListener(new MyOnSeekBarChangeListener());

    }

    class MyOnSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            if (fromUser) {
                try {
                    mService.seekTo(progress);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }


    public void onClick(View v) {
        if (v == btnAudioPlaymode) {

            changePlayMode();

        } else if (v == btnAudioPre) {

            try {
                mService.pre();
            } catch (RemoteException e) {
                e.printStackTrace();
            }

        } else if (v == btnAudioStartPause) {

            startAndPause();

        } else if (v == btnAudioNext) {

            try {
                mService.next();
            } catch (RemoteException e) {
                e.printStackTrace();
            }

        } else if (v == btnAudioList) {
            showPopMusicList();
        }
    }

    private void showPopMusicList() {
        if (popMusicWin == null) {
            popMusicWin = new PopupWindow(AudioPlayerActivity.this);
            popMusicWin.setWidth(screenWidth);
            popMusicWin.setHeight(screenHeight/2);
            popMusicWin.setContentView(popMusicList);
            popMusicWin.setFocusable(true);
            popMusicWin.showAtLocation(activity_audio_player, Gravity.CENTER, 0, 2 * screenHeight / 5);
        }

        if (!popMusicWin.isShowing() && popMusicWin.isFocusable()) {
            popMusicWin.showAtLocation(activity_audio_player, Gravity.CENTER, 0, 2 * screenHeight / 5);
        }

    }

    private void changePlayMode() {

        try {
            int playMode = mService.getPlayMode();
            if (playMode == MusicPlayerService.REPEAT_NOMAL) {
                playMode = MusicPlayerService.REPEAT_SINGLE;
            } else if (playMode == MusicPlayerService.REPEAT_SINGLE) {
                playMode = MusicPlayerService.REPEAT_ALL;
            } else if (playMode == MusicPlayerService.REPEAT_ALL) {
                playMode = MusicPlayerService.REPEAT_NOMAL;
            } else {
                playMode = MusicPlayerService.REPEAT_NOMAL;
            }

            mService.setPlayMode(playMode);

            showPlayMode();

        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    /**
     * 显示播放模式
     */
    private void showPlayMode() {
        try {
            int playMode = mService.getPlayMode();
            if (playMode == MusicPlayerService.REPEAT_NOMAL) {
                btnAudioPlaymode.setBackgroundResource(R.drawable.btn_audio_playmode_nomal_selector);
            } else if (playMode == MusicPlayerService.REPEAT_SINGLE) {
                btnAudioPlaymode.setBackgroundResource(R.drawable.btn_audio_playmode_single_selector);
            } else if (playMode == MusicPlayerService.REPEAT_ALL) {
                btnAudioPlaymode.setBackgroundResource(R.drawable.btn_audio_playmode_all_selector);
            } else {
                btnAudioPlaymode.setBackgroundResource(R.drawable.btn_audio_playmode_nomal_selector);
            }


        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    /**
     * 校验显示播放模式
     */
    private void checkPlayMode() {
        try {
            int playMode = mService.getPlayMode();
            if (playMode == MusicPlayerService.REPEAT_NOMAL) {
                btnAudioPlaymode.setBackgroundResource(R.drawable.btn_audio_playmode_nomal_selector);
            } else if (playMode == MusicPlayerService.REPEAT_SINGLE) {
                btnAudioPlaymode.setBackgroundResource(R.drawable.btn_audio_playmode_single_selector);
            } else if (playMode == MusicPlayerService.REPEAT_ALL) {
                btnAudioPlaymode.setBackgroundResource(R.drawable.btn_audio_playmode_all_selector);
            } else {
                btnAudioPlaymode.setBackgroundResource(R.drawable.btn_audio_playmode_nomal_selector);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    private void startAndPause() {
        try {
            if (mService.isPlaying()) {
                // 暂停
                mService.pause();
                // 按钮设置为播放状态
                btnAudioStartPause.setBackgroundResource(R.drawable.btn_audio_play_selector);
            } else {
                // 播放
                mService.start();
                // 按钮设置为暂停状态
                btnAudioStartPause.setBackgroundResource(R.drawable.btn_audio_pause_selector);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case PROGRESS:
                    // 更新进度条
                    int currentPositin = 0;
                    try {
                        currentPositin = mService.getCurrentPosition();
                        seekBarAudio.setProgress(currentPositin);


                        // 更新时间
                        audioTime.setText(mUtils.stringForTime(currentPositin) + "/"
                                + mUtils.stringForTime(mService.getDuration()));
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }

                    removeMessages(PROGRESS);
                    sendEmptyMessageDelayed(PROGRESS, 1000);
                    break;
            }

        }
    };


    /**
     * 服务的代理类----aidl文件动态生成的类
     */
    private IMusicPlayerService mService;

    private ServiceConnection mConn = new ServiceConnection() {
        /**
         * 当和服务连接成功时回调
         * @param name
         * @param iBinder
         */
        @Override
        public void onServiceConnected(ComponentName name, IBinder iBinder) {
            // 得到服务的代理类对象
            mService = IMusicPlayerService.Stub.asInterface(iBinder);

            // 开始播放音乐
            try {
                if (!isFromNotification) {
                    // 如果来自列表就重新开启音乐
                    mService.openAudio(position);
                } else {
                    // 如果来自通知栏就发起通知
                    showProgress();
                    mService.notifyChange(MusicPlayerService.OPENAUDIO);
                }

            } catch (RemoteException e) {
                e.printStackTrace();
            }

            // 得到歌曲名称和演唱者名称并且显示
            showData();

            // 校验显示模式
            checkPlayMode();
        }

        /**
         * 当和服务断开时回调
         * @param name
         */
        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 得到屏幕的宽和高
        DisplayMetrics outMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        screenWidth = outMetrics.widthPixels;
        screenHeight = outMetrics.heightPixels;

        initData();

        findViews();

        getData();

        startAndBindService();


    }

    private void initData() {

        // 得到传过来的列表数据
        mMediaItems = new ArrayList<>();


        // 注册广播
        mReceiver = new MyBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MusicPlayerService.OPENAUDIO);

        registerReceiver(mReceiver, intentFilter);


    }

    private void dismissPopMusicList() {
        if (popMusicWin != null && popMusicWin.isShowing()) {
            popMusicWin.dismiss();
            popMusicWin = null;
        }
    }

    public void setData(MediaItem mediaItem) {
        // 歌曲进行更新
        showProgress();
        // 更新歌名
//        showData();
        tvArtist.setText(mediaItem.getArtist());
        tvAudioName.setText(mediaItem.getName());
        // 查验播放模式
        checkPlayMode();

    }


    class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            // 歌曲进行更新
            showProgress();
            // 更新歌名
            showData();
            // 查验播放模式
            checkPlayMode();


        }
    }

    private void showProgress() {
        try {
            seekBarAudio.setMax(mService.getDuration());
            mhandler.sendEmptyMessage(PROGRESS);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void showData() {

        try {
            tvArtist.setText(mService.getArtist());
            tvAudioName.setText(mService.getAudioName());
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }


    private void startAndBindService() {

        Intent intent = new Intent(this, MusicPlayerService.class);
        intent.setAction("com.wrk.mobilplayer.OPENAUDIO");
        bindService(intent, mConn, Context.BIND_AUTO_CREATE);
        // 防止服务多次实例化
        startService(intent);

    }

    private boolean isFromNotification;

    public void getData() {
        // true,来自状态栏   false,来自列表


        isFromNotification = getIntent().getBooleanExtra("notification", false);

        if (!isFromNotification) {
            position = getIntent().getIntExtra("pos", 0); // 列表
            mMediaItems = (ArrayList<MediaItem>) getIntent().getSerializableExtra("medialist");
        } else {

            mMediaItems = new ArrayList<MediaItem>();
            String[] objs = new String[]{
                    MediaStore.Audio.Media.DISPLAY_NAME,// 在sdCard的名称
            };
            ContentResolver resolver = AudioPlayerActivity.this.getContentResolver();
            Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            Cursor cursor = resolver.query(uri, objs, null, null, null);

            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String name = cursor.getString(0);
                    MediaItem mediaItem = new MediaItem();
                    mediaItem.setName(name);
                    mMediaItems.add(mediaItem);
                }
                cursor.close();
            }
        }

        if (mMediaItems != null && mMediaItems.size() > 0) {
            popMusicList = new ListView(AudioPlayerActivity.this);
            popMusicList.setAdapter(new MyMusicListAdapter(AudioPlayerActivity.this, mMediaItems));

            popMusicList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        mService.seekToother(position);

                        dismissPopMusicList();

                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            });

        }


    }


    @Override
    protected void onDestroy() {

        if (mConn != null) {
            unbindService(mConn);
            mConn = null;
        }

        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
            mReceiver = null;
        }

        dismissPopMusicList();

        super.onDestroy();
    }
}
