// IMusicPlayerService.aidl
package com.wrk.mymeadiaplayer;

// Declare any non-default types here with import statements

interface IMusicPlayerService {

     /**
            * 根据位置播放对应的音频文件
            *
            * @param pos
            */
           void openAudio(int pos);


           /**
            * 播放音乐
            */
           void start();

           /**
            * 暂停音乐
            */
           void pause();

           /**
            * 设置播放模式
            *
            * @param playMode
            */
           void setPlayMode(int playMode);

           /**
            * 得到播放模式
            *
            * @return
            */
           int getPlayMode();

           /**
            * 得到艺术家
            *
            * @return
            */
           String getArtist();

           /**
            * 得到歌曲名
            *
            * @return
            */
           String getAudioName();

           /**
            * 得到音频的总时长
            *
            * @return
            */
           int getDuration();

           /**
            * 得到当前的播放进度
            *
            * @return
            */
           int getCurrentPosition();

           /**
            * 音频的拖动
            *
            * @param position
            */
           void seekTo(int position);

           /**
            * 播放下一个
            */
            void next();

           /**
            * 播放上一个
            */
            void pre();

            /**
            * 是否正在播放
            */
            boolean isPlaying();


            void notifyChange(String action);

            /**
            * 得到歌曲播放的绝对路径
            */
           String getAudioPath();

           /**
           *  跳转到某一首
           */
           void seekToother(int pos);


}
