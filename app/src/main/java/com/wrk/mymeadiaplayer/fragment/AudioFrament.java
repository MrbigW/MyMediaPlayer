package com.wrk.mymeadiaplayer.fragment;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.wrk.mymeadiaplayer.R;
import com.wrk.mymeadiaplayer.activity.AudioPlayerActivity;
import com.wrk.mymeadiaplayer.bean.MediaItem;
import com.wrk.mymeadiaplayer.util.MusicUtils;
import com.wrk.mymeadiaplayer.util.Utils;

import java.util.ArrayList;

/**
 * Created by MrbigW on 2016/9/28.
 * weChat:1024057635
 * Github:MrbigW
 * Usage: 本地音频
 * -------------------=.=------------------------
 */

public class AudioFrament extends BaseFragment {

    private Context mContext;

    private TextView tv_nomedia;
    private ListView listview;

    private ArrayList<MediaItem> mItems;

    private MyAudiAdapter adapter;

    private Utils utils;


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);


            if (mItems != null && mItems.size() > 0) {
                tv_nomedia.setVisibility(View.GONE);
                adapter = new MyAudiAdapter();
                listview.setAdapter(adapter);
            } else {
                tv_nomedia.setVisibility(View.VISIBLE);
            }

        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getActivity();
    }

    /**
     * 初始化视图
     *
     * @return
     */
    @Override
    public View initView() {

        View view = View.inflate(mContext, R.layout.frag_video, null);

        tv_nomedia = (TextView) view.findViewById(R.id.tv_nomedia);
        listview = (ListView) view.findViewById(R.id.listview);

        // 设置点击事件
        listview.setOnItemClickListener(new MyOnItemClickListener());

        return view;
    }

    class MyOnItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            Intent intent = new Intent(mContext, AudioPlayerActivity.class);

            Bundle bundle = new Bundle();
            bundle.putSerializable("medialist", mItems);
            intent.putExtra("pos", position);
            intent.putExtras(bundle);
            mContext.startActivity(intent);

        }
    }

    /**
     * 绑定数据
     */
    @Override
    public void initData() {
        super.initData();
        utils = new Utils();
        getData();
    }

    public void getData() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                mItems = new ArrayList<MediaItem>();
                String[] objs = new String[]{
                        MediaStore.Audio.Media.DISPLAY_NAME,// 在sdCard的名称
                        MediaStore.Audio.Media.DURATION,// 视频的时长，毫秒
                        MediaStore.Audio.Media.SIZE,// 文件大小，单位字节
                        MediaStore.Audio.Media.ARTIST, // 演唱者
                        MediaStore.Audio.Media.DATA, // 在sdCard的路径
                        MediaStore.Audio.Media._ID ,// songid
                        MediaStore.Audio.Media.ALBUM_ID // albumid
                };
                ContentResolver resolver = mContext.getContentResolver();
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
                        mItems.add(mediaItem);
                    }

                    cursor.close();
                }

                mHandler.sendEmptyMessage(0);
            }
        }.start();
    }

    class MyAudiAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mItems.size();
        }

        @Override
        public Object getItem(int position) {
            return mItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder mHolder;
            if (convertView == null) {
                mHolder = new ViewHolder();
                convertView = View.inflate(mContext, R.layout.item_video_frag, null);
                mHolder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
                mHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                mHolder.tv_duration = (TextView) convertView.findViewById(R.id.tv_duration);
                mHolder.tv_size = (TextView) convertView.findViewById(R.id.tv_size);

                convertView.setTag(mHolder);
            } else {
                mHolder = (ViewHolder) convertView.getTag();
            }

            MediaItem item = mItems.get(position);
            mHolder.tv_name.setText(item.getName());
            mHolder.tv_duration.setText(utils.stringForTime((int) item.getDuration()));
            mHolder.tv_size.setText(android.text.format.Formatter.formatFileSize(mContext, item.getSize()));

            /**
             * 为本地视频的imageView加载封面图
             *
             */
            long songid = Long.parseLong(item.getSongId());
            long albumid = Long.parseLong(item.getAlbumId());
            mHolder.iv_icon.setImageBitmap(MusicUtils.getArtwork(mContext,songid,albumid,true));
            return convertView;
        }
    }

    static class ViewHolder {
        ImageView iv_icon;
        TextView tv_name;
        TextView tv_duration;
        TextView tv_size;
    }

}
