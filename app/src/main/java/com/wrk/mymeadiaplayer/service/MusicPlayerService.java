package com.wrk.mymeadiaplayer.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.wrk.mymeadiaplayer.IMusicPlayerService;
import com.wrk.mymeadiaplayer.R;
import com.wrk.mymeadiaplayer.activity.AudioPlayerActivity;
import com.wrk.mymeadiaplayer.bean.MediaItem;
import com.wrk.mymeadiaplayer.util.CacheUtils;
import com.wrk.mymeadiaplayer.util.MusicUtils;
import com.wrk.mymeadiaplayer.util.Utils;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by MrbigW on 2016/10/11.
 * weChat:1024057635
 * Github:MrbigW
 * Usage: -.-
 * -------------------=.=------------------------
 */

public class MusicPlayerService extends Service {


    public static final String OPENAUDIO = "com.wrk.mymobileplayer.broadcast.OPENAUDIO";
    private static final int NOTIFICATION_ID = 1;
    private static final int UPDATENOTIFICATIONTIME = 0x001;


    private MediaPlayer mMediaPlayer;

    private ArrayList<MediaItem> mMediaItems;

    private int position; // 当前位置

    private MediaItem mediaItem; // 当前音频


    // 通知栏
    private NotificationManager mNotificationManager;

    /**
     * 顺序播放
     */
    public static final int REPEAT_NOMAL = 0;
    /**
     * 单曲循环
     */
    public static final int REPEAT_SINGLE = 1;
    /**
     * 全部循环
     */
    public static final int REPEAT_ALL = 2;

    // 播放模式
    private int playMode;

    private IMusicPlayerService.Stub mStub = new IMusicPlayerService.Stub() {

        // 得到外部类的实例
        MusicPlayerService mServie = MusicPlayerService.this;

        @Override
        public void openAudio(int pos) throws RemoteException {
            mServie.openAudio(pos);
        }

        @Override
        public void start() throws RemoteException {
            mServie.start();
        }

        @Override
        public void pause() throws RemoteException {
            mServie.pause();
        }

        @Override
        public void setPlayMode(int playMode) throws RemoteException {
            mServie.setPlayMode(playMode);
        }

        @Override
        public int getPlayMode() throws RemoteException {
            return mServie.getPlayMode();
        }

        @Override
        public String getArtist() throws RemoteException {
            return mServie.getArtist();
        }

        @Override
        public String getAudioName() throws RemoteException {
            return mServie.getAudioName();
        }

        @Override
        public int getDuration() throws RemoteException {
            return mServie.getDuration();
        }

        @Override
        public int getCurrentPosition() throws RemoteException {
            return mServie.getCurrentPosition();
        }

        @Override
        public void seekTo(int position) throws RemoteException {
            mServie.seekTo(position);
        }

        @Override
        public void next() throws RemoteException {
            mServie.next();
        }

        @Override
        public void pre() throws RemoteException {
            mServie.pre();
        }

        @Override
        public boolean isPlaying() throws RemoteException {
            return mMediaPlayer.isPlaying();
        }

        @Override
        public void notifyChange(String action) throws RemoteException {
            mServie.notifyChange(action);
        }

        @Override
        public String getAudioPath() throws RemoteException {
            return mediaItem.getData();
        }

        @Override
        public void seekToother(int pos) throws RemoteException {
            mServie.seekToother(pos);
        }
    };
    private int requestCode = 0;
    private RemoteViews bigContentView;
    private RemoteViews contentView;


    @Override
    public void onCreate() {
        super.onCreate();

        playMode = CacheUtils.getPlayMode(this, "playmode");

        getData();

    }

    /**
     * 返回IBinder对象
     *
     * @param intent
     * @return
     */
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mStub;
    }


    /**
     * 根据位置播放对应的音频文件
     *
     * @param pos
     */
    private void openAudio(int pos) {

        if (mMediaItems != null && mMediaItems.size() > 0) {
            this.position = pos;

            if (pos < mMediaItems.size()) {
                mediaItem = mMediaItems.get(pos);
            }

            // 先释放MediaPlayer
            if (mMediaPlayer != null) {

                mMediaPlayer.reset();
                mMediaPlayer.release();
                mMediaPlayer = null;

            }

            try {
                // 创建MediaPlayer,并重新设置监听,重新准备
                mMediaPlayer = new MediaPlayer();
                mMediaPlayer.setOnPreparedListener(new MyOnPreparedListener());
                mMediaPlayer.setOnErrorListener(new MyOnErrorListener());
                mMediaPlayer.setOnCompletionListener(new MyOnCompletionListener());

                // 设置播放地址
                mMediaPlayer.setDataSource(mediaItem.getData());

                // 开始准备
                mMediaPlayer.prepareAsync();

            } catch (IOException e) {
                e.printStackTrace();
            }


        } else {
            Toast.makeText(this, "音频尚未加载完成", Toast.LENGTH_SHORT).show();
        }

    }

    class MyOnPreparedListener implements MediaPlayer.OnPreparedListener {

        @Override
        public void onPrepared(MediaPlayer mp) {
            // 开始播放
            start();

            //发送广播
            notifyChange(OPENAUDIO);


        }
    }

    /**
     * 发广播
     *
     * @param action
     */
    private void notifyChange(String action) {
        Intent intent = new Intent(action);
        intent.putExtra("updateprogress", true);
        sendBroadcast(intent);
    }

    class MyOnErrorListener implements MediaPlayer.OnErrorListener {

        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {

            next();

            return true;
        }
    }


    class MyOnCompletionListener implements MediaPlayer.OnCompletionListener {

        @Override
        public void onCompletion(MediaPlayer mp) {
            // 播放下一个
            Autonext();
        }
    }


    public void getData() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                mMediaItems = new ArrayList<MediaItem>();
                String[] objs = new String[]{
                        MediaStore.Audio.Media.DISPLAY_NAME,// 在sdCard的名称
                        MediaStore.Audio.Media.DURATION,// 视频的时长，毫秒
                        MediaStore.Audio.Media.SIZE,// 文件大小，单位字节
                        MediaStore.Audio.Media.ARTIST, // 演唱者
                        MediaStore.Audio.Media.DATA, // 在sdCard的路径
                        MediaStore.Audio.Media._ID,// songid
                        MediaStore.Audio.Media.ALBUM_ID // albumid
                };
                ContentResolver resolver = getContentResolver();
                Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                Cursor cursor = resolver.query(uri, objs, null, null, null);

                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        String name = cursor.getString(0);
                        long duration = cursor.getLong(1);
                        long size = cursor.getLong(2);
                        String artist = cursor.getString(3);
                        String data = cursor.getString(4);
                        MediaItem mediaItem = new MediaItem(duration, name, size, artist, data);
                        mediaItem.setSongId(cursor.getString(5));
                        mediaItem.setAlbumId(cursor.getString(6));
                        mMediaItems.add(mediaItem);
                        Log.e("111", mediaItem.toString());
                    }
                    cursor.close();
                }

            }
        }.start();
    }

    /**
     * 播放音乐
     */
    private void start() {
        mMediaPlayer.start();

        // 弹出通知栏
        showNotification(getCurrentPosition(), true);

    }


    private void showNotification(int currentPosition, boolean isPlaying) {
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification notification = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Notification.Builder builder = new Notification.Builder(this);
            builder.setSmallIcon(R.drawable.notification_music_playing);

            notification = builder.build();
            bigContentView = new RemoteViews(this.getPackageName(), R.layout.bignotification_layout);
            bigContentView.setTextViewText(R.id.tv_notifi_artist, mediaItem.getArtist());
            bigContentView.setTextViewText(R.id.tv_notifi_name, mediaItem.getName());
            bigContentView.setTextViewText(R.id.tv_notifi_time, new Utils().stringForTime(currentPosition));
            bigContentView.setImageViewBitmap(R.id.iv_notifi_cover, MusicUtils.getArtwork(this, Long.parseLong(mediaItem.getSongId()), Long.parseLong(mediaItem.getAlbumId()), true));


            if (isPlaying) {
                bigContentView.setImageViewResource(R.id.iv_notifi_play_pause, R.drawable.uamp_ic_pause_white_48dp);
            } else {
                bigContentView.setImageViewResource(R.id.iv_notifi_play_pause, R.drawable.uamp_ic_play_arrow_white_48dp);
            }

            // Notification的返回Activity
            Intent activityIntent = new Intent(this, AudioPlayerActivity.class);
            activityIntent.putExtra("notification", true); // 从状态栏进入音乐播放页面
            PendingIntent activityPIntent = PendingIntent.getActivity(this, requestCode, activityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            bigContentView.setOnClickPendingIntent(R.id.iv_notifi_cover, activityPIntent);
            bigContentView.setOnClickPendingIntent(R.id.tv_notifi_artist, activityPIntent);
            bigContentView.setOnClickPendingIntent(R.id.ll_nitifi_top, activityPIntent);


            // Notification的上一首
            Intent preIntent = new Intent(OPENAUDIO);
            preIntent.putExtra("nopre", true);
            PendingIntent prePIntent = PendingIntent.getBroadcast(this, requestCode + 1, preIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            bigContentView.setOnClickPendingIntent(R.id.iv_notifi_pre, prePIntent);
            // Notification的下一首
            Intent nextIntent = new Intent(OPENAUDIO);
            nextIntent.putExtra("nonext", true);
            PendingIntent nextPIntent = PendingIntent.getBroadcast(this, requestCode + 2, nextIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            bigContentView.setOnClickPendingIntent(R.id.iv_notifi_next, nextPIntent);
            //  Notification的播放与暂停
            Intent playandpauseIntent = new Intent(OPENAUDIO);
            playandpauseIntent.putExtra("noplayandpause", true);
            PendingIntent playandPausePIntent = PendingIntent.getBroadcast(this, requestCode + 3, playandpauseIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            bigContentView.setOnClickPendingIntent(R.id.iv_notifi_play_pause, playandPausePIntent);


            // contentView
            contentView = new RemoteViews(this.getPackageName(), R.layout.normalnotification_layout);
            contentView.setTextViewText(R.id.tv_normal_artist, mediaItem.getArtist());
            contentView.setTextViewText(R.id.tv_normal_name, mediaItem.getName());
            contentView.setTextViewText(R.id.tv_normal_time, new Utils().stringForTime(currentPosition));
            contentView.setImageViewBitmap(R.id.iv_normal_cover, MusicUtils.getArtwork(this, Long.parseLong(mediaItem.getSongId()), Long.parseLong(mediaItem.getAlbumId()), true));


            PendingIntent sapPIntent = PendingIntent.getBroadcast(this, requestCode + 4, playandpauseIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            contentView.setOnClickPendingIntent(R.id.iv_notifi_play_pause, sapPIntent);

            contentView.setOnClickPendingIntent(R.id.iv_normal_play_pause, playandPausePIntent);
            contentView.setOnClickPendingIntent(R.id.ll_normal_mid, activityPIntent);
            contentView.setOnClickPendingIntent(R.id.iv_normal_cover, activityPIntent);
            contentView.setOnClickPendingIntent(R.id.tv_normal_time, activityPIntent);

            if (isPlaying) {
                contentView.setImageViewResource(R.id.iv_normal_play_pause, R.drawable.uamp_ic_pause_white_48dp);
            } else {
                contentView.setImageViewResource(R.id.iv_normal_play_pause, R.drawable.uamp_ic_play_arrow_white_48dp);
            }

            notification.bigContentView = bigContentView;
            notification.contentView = contentView;

        }

        notification.flags = Notification.FLAG_ONGOING_EVENT;// 点击不会取消

        mNotificationManager.notify(NOTIFICATION_ID, notification);
    }

    /**
     * 暂停音乐
     */
    private void pause() {
        mMediaPlayer.pause();
        showNotification(getCurrentPosition(), false);
    }

    /**
     * 设置播放模式
     *
     * @param playMode
     */
    private void setPlayMode(int playMode) {
        this.playMode = playMode;

        // 保存模式
        CacheUtils.savePlayMode(this, "play_mode", playMode);

    }

    /**
     * 得到播放模式
     *
     * @return
     */
    private int getPlayMode() {
        return playMode;
    }

    /**
     * 得到艺术家
     *
     * @return
     */
    private String getArtist() {
        if (mediaItem != null) {
            return mediaItem.getArtist();
        }
        return "";
    }

    /**
     * 得到歌曲名
     *
     * @return
     */
    private String getAudioName() {
        if (mediaItem != null) {
            return mediaItem.getName();
        }
        return "";
    }


    /**
     * 得到音频的总时长
     *
     * @return
     */
    private int getDuration() {
        return mMediaPlayer.getDuration();
    }

    /**
     * 得到当前的播放进度
     *
     * @return
     */
    private int getCurrentPosition() {
        if (mMediaPlayer.isPlaying()) {
            showNotification(mMediaPlayer.getCurrentPosition(), true);
        }
        return mMediaPlayer.getCurrentPosition();
    }

    /**
     * 音频的拖动
     *
     * @param position
     */
    private void seekTo(int position) {
        mMediaPlayer.seekTo(position);
    }


    /**
     * 根据不同的播放模式播放下一个
     */
    private void next() {
        // 设置播放的位置
        setNextPosition();
        // 根据位置播放对应的音频
        openNextByPosition();
    }

    private void Autonext() {
        // 设置播放的位置
        setNextAutoPosition();
        // 根据位置播放对应的音频
        openNextByPosition();

    }


    private void setNextAutoPosition() {
        int playMode = getPlayMode();
        if (playMode == MusicPlayerService.REPEAT_NOMAL) {
            position++;
        } else if (playMode == MusicPlayerService.REPEAT_SINGLE) {

        } else if (playMode == MusicPlayerService.REPEAT_ALL) {
            position++;
            if (position > mMediaItems.size() - 1) {
                position = 0;
            }
        } else {
            position++;
        }
    }

    private void openNextByPosition() {

        int playMode = getPlayMode();
        if (playMode == MusicPlayerService.REPEAT_NOMAL) {
            if (position <= mMediaItems.size() - 1) {
                // 正常范围
                openAudio(position);
            } else {
                // 越界
                position = mMediaItems.size() - 1;
            }
        } else if (playMode == MusicPlayerService.REPEAT_SINGLE) {
            openAudio(position);
        } else if (playMode == MusicPlayerService.REPEAT_ALL) {
            openAudio(position);
        } else {
            if (position <= mMediaItems.size() - 1) {
                // 正常范围
                openAudio(position);
            } else {
                // 越界
                position = mMediaItems.size() - 1;
            }
        }
    }

    private void setNextPosition() {

        int playMode = getPlayMode();
        if (playMode == MusicPlayerService.REPEAT_NOMAL) {
            position++;
        } else if (playMode == MusicPlayerService.REPEAT_SINGLE) {
            position++;
            if (position > mMediaItems.size() - 1) {
                position = 0;
            }
        } else if (playMode == MusicPlayerService.REPEAT_ALL) {
            position++;
            if (position > mMediaItems.size() - 1) {
                position = 0;
            }

        } else {
            position++;
        }

    }

    /**
     * 播放上一个
     */
    private void pre() {
        setPrePosition();
        openPreByPosition();
    }

    private void openPreByPosition() {
        int playMode = getPlayMode();
        if (playMode == MusicPlayerService.REPEAT_NOMAL) {
            if (position >= 0) {
                // 正常范围
                openAudio(position);
            } else {
                // 越界
                position = 0;
            }
        } else if (playMode == MusicPlayerService.REPEAT_SINGLE) {
            openAudio(position);
        } else if (playMode == MusicPlayerService.REPEAT_ALL) {
            openAudio(position);
        } else {
            if (position >= 0) {
                // 正常范围
                openAudio(position);
            } else {
                // 越界
                position = 0;
            }
        }
    }


    private void setPrePosition() {

        int playMode = getPlayMode();
        if (playMode == MusicPlayerService.REPEAT_NOMAL) {
            position--;
        } else if (playMode == MusicPlayerService.REPEAT_SINGLE) {
            position--;
            if (position < 0) {
                position = mMediaItems.size() - 1;
            }
        } else if (playMode == MusicPlayerService.REPEAT_ALL) {
            position--;
            if (position < 0) {
                position = mMediaItems.size() - 1;
            }
        } else {
            position--;
        }

    }


    private void seekToother(int pos) {
        openAudio(pos);
    }


    @Override
    public void onDestroy() {
        mNotificationManager.cancel(NOTIFICATION_ID);
        super.onDestroy();
    }
}






























































