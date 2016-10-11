package com.wrk.mymeadiaplayer.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wrk.mymeadiaplayer.R;
import com.wrk.mymeadiaplayer.bean.MediaItem;

import java.util.ArrayList;

/**
 * Created by MrbigW on 2016/10/11.
 * weChat:1024057635
 * Github:MrbigW
 * Usage: -.-
 * -------------------=.=------------------------
 */

public class MyMusicListAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<MediaItem> mMediaItems;

    public MyMusicListAdapter(Context context, ArrayList<MediaItem> data) {
        mMediaItems = new ArrayList<>();
        this.mContext = context;
        this.mMediaItems = data;

    }

    @Override
    public int getCount() {
        return mMediaItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mMediaItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mHolder = null;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.poplist_item_layout, null);
            mHolder = new ViewHolder();

            mHolder.tv_top_list = (TextView) convertView.findViewById(R.id.tv_top_list);

            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }

        String list = mMediaItems.get(position).getName();
        mHolder.tv_top_list.setText(list);

        return convertView;
    }

    class ViewHolder {
        TextView tv_top_list;
    }
}
