package com.wrk.mymeadiaplayer.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wrk.mymeadiaplayer.R;
import com.wrk.mymeadiaplayer.bean.NetMedia;

import java.util.ArrayList;

/**
 * Created by MrbigW on 2016/10/4.
 * weChat:1024057635
 * Github:MrbigW
 * Usage: -.-
 * -------------------=.=------------------------
 */

public class MyTopListAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<NetMedia> mNetMedias;

    public MyTopListAdapter(Context context, ArrayList<NetMedia> data) {
        mNetMedias = new ArrayList<>();
        this.mContext = context;
        this.mNetMedias = data;

    }

    @Override
    public int getCount() {
        return mNetMedias.size();
    }

    @Override
    public Object getItem(int position) {
        return mNetMedias.get(position);
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

        String list = mNetMedias.get(position).getVideoTitle();
        mHolder.tv_top_list.setText(list);

//        mHolder.tv_top_list.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

        return convertView;
    }

    class ViewHolder {
        TextView tv_top_list;
    }
}
