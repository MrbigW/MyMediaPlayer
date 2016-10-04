package com.wrk.mymeadiaplayer.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wrk.mymeadiaplayer.R;

import java.util.Arrays;
import java.util.List;

/**
 * Created by MrbigW on 2016/10/5.
 * weChat:1024057635
 * Github:MrbigW
 * Usage: -.-
 * -------------------=.=------------------------
 */

public class MyTopMoreAdapter extends BaseAdapter {

    private Context mContext;
    private List<String> mMorelist = Arrays.asList("标清", "高清");


    public MyTopMoreAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return mMorelist.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = View.inflate(mContext, R.layout.popmore_list_item, null);

        TextView tv = (TextView) convertView.findViewById(R.id.tv_top_more);

        tv.setText(mMorelist.get(position));

        return convertView;
    }
}
