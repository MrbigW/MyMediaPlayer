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
 * Usage: 热门搜索和历史记录的Adapter
 * -------------------=.=------------------------
 */

public class CommonAdapter extends BaseAdapter {
    private Context context;
    private List<String> datas;

    public CommonAdapter(Context context, List<String> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public int getCount() {
        //最大返回40条数据
        return Math.min(40, datas.size());
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

        String data = datas.get(position);
        holder.tv_item_hint.setText(data);

        return convertView;
    }

    public void updateRecordList(List<String> historySearchDatas) {
        this.datas = historySearchDatas;
        notifyDataSetChanged();
    }

    static class ViewHolder {
        TextView tv_item_hint;
    }
}
