package com.wrk.mymeadiaplayer.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.wrk.mymeadiaplayer.IMusicPlayerService;
import com.wrk.mymeadiaplayer.R;
import com.wrk.mymeadiaplayer.activity.AudioPlayerActivity;
import com.wrk.mymeadiaplayer.bean.MediaItem;
import com.wrk.mymeadiaplayer.util.CacheUtils;
import com.wrk.mymeadiaplayer.util.MusicUtils;

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
    };


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
        showNotification();

    }

    private void showNotification() {
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Intent intent = new Intent(this, AudioPlayerActivity.class);
        intent.putExtra("notification", true); // 从状态栏进入音乐播放页面
        // 延期意图
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notification = new Notification.Builder(this)
                    .setSmallIcon(R.drawable.notification_music_playing)
                    .setLargeIcon(MusicUtils.getArtwork(this, Long.parseLong(mediaItem.getSongId()), Long.parseLong(mediaItem.getAlbumId()), true))
                    .setContentTitle("321影音")
                    .setColor(Color.parseColor("#ff3097fd"))
                    .setContentText("正在播放:" + getAudioName())
                    .setContentIntent(pendingIntent)
                    .build();
        } else {
            notification = new Notification.Builder(this)
                    .setSmallIcon(R.drawable.notification_music_playing)
                    .setLargeIcon(MusicUtils.getArtwork(this, Long.parseLong(mediaItem.getSongId()), Long.parseLong(mediaItem.getAlbumId()), true))
                    .setContentTitle("321影音")
                    .setContentText("正在播放:" + getAudioName())
                    .setContentIntent(pendingIntent)
                    .build();
        }


        notification.flags = Notification.FLAG_ONGOING_EVENT;// 点击不会取消

        mNotificationManager.notify(NOTIFICATION_ID, notification);
    }

    /**
     * 暂停音乐
     */
    private void pause() {
        mMediaPlayer.pause();

        // 隐藏通知栏
        mNotificationManager.cancel(NOTIFICATION_ID);
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

}






























































