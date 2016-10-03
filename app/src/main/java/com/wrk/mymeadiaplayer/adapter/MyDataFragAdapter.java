package com.wrk.mymeadiaplayer.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wrk.mymeadiaplayer.R;
import com.wrk.mymeadiaplayer.bean.NetMedia;
import com.wrk.mymeadiaplayer.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MrbigW on 2016/10/3.
 * weChat:1024057635
 * Github:MrbigW
 * Usage: -.-
 * -------------------=.=------------------------
 */

public class MyDataFragAdapter extends BaseAdapter {

    private Context mContext;
    private List<NetMedia> mNetMedias;
    private ImageLoader mImageLoader;

    public MyDataFragAdapter(Context context, List<NetMedia> medias) {
        mNetMedias = new ArrayList<>();
        this.mContext = context;
        this.mNetMedias = medias;
        mImageLoader = new ImageLoader(context);
    }


    public void onDataChange(List<NetMedia> list) {
        this.mNetMedias = list;
        this.notifyDataSetChanged();
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
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.testdata_item_layout, null);

            viewHolder.iv_data_item_icon = (ImageView) convertView.findViewById(R.id.iv_data_item_icon);
            viewHolder.tv_data_item_content = (TextView) convertView.findViewById(R.id.tv_data_item_content);
            viewHolder.tv_data_item_title = (TextView) convertView.findViewById(R.id.tv_data_item_title);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        String url = mNetMedias.get(position).getCoverImg();
        viewHolder.iv_data_item_icon.setImageResource(R.drawable.video_default);
        viewHolder.iv_data_item_icon.setBackgroundResource(R.drawable.center_collect_play);
        viewHolder.iv_data_item_icon.setTag(url);

        mImageLoader.showImageByAsyncTask(viewHolder.iv_data_item_icon, mNetMedias.get(position).getCoverImg());
        viewHolder.tv_data_item_title.setText(mNetMedias.get(position).getMovieName());
        viewHolder.tv_data_item_content.setText(mNetMedias.get(position).getSummary());

        return convertView;
    }

    class ViewHolder {
        private TextView tv_data_item_title, tv_data_item_content;
        private ImageView iv_data_item_icon;
    }
}
