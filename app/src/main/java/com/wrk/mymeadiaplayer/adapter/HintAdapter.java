package com.wrk.mymeadiaplayer.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wrk.mymeadiaplayer.R;

import java.util.List;

/**
 * Created by MrbigW on 2016/10/6.
 * weChat:1024057635
 * Github:MrbigW
 * Usage: 提示的listView的Adapter
 * -------------------=.=------------------------
 */

public class HintAdapter extends BaseAdapter {
    private Context context;
    private List<String> hintDatas;
    private int maxHintLines;

    public HintAdapter(Context context, List<String> hintDatas, int maxHintLines) {
        this.context = context;
        this.hintDatas = hintDatas;
        this.maxHintLines = maxHintLines;
    }


    @Override
    public int getCount() {
        if (maxHintLines == -1) {//表示未设置，默认返回集合长度
            return hintDatas.size();
        } else {
            //如果设置的最大行数小于集合的长度，则返回设置的最大行数，否则返回集合的长度
            return Math.min(hintDatas.size(), maxHintLines);
        }
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_search, null);
            holder.tv_item_hint = (TextView) convertView.findViewById(R.id.tv_item_hint);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String data = hintDatas.get(position);
        holder.tv_item_hint.setText(data);

        return convertView;
    }

    public void notifyRefresh(List<String> hintDatas) {
        this.hintDatas = hintDatas;
        notifyDataSetChanged();
    }

    static class ViewHolder {
        TextView tv_item_hint;
    }
}
