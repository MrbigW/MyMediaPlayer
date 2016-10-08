package com.wrk.mymeadiaplayer.adapter;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.wrk.mymeadiaplayer.R;
import com.wrk.mymeadiaplayer.activity.MainActivity;
import com.wrk.mymeadiaplayer.activity.SystemPlayerActivity;
import com.wrk.mymeadiaplayer.bean.NetMedia;

import java.io.Serializable;
import java.util.List;

/**
 * Created by MrbigW on 2016/10/6.
 * weChat:1024057635
 * Github:MrbigW
 * Usage: -.-
 * -------------------=.=------------------------
 */

public class MyRecyDataFragAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int NORMAL_ITEM = 0;
    private static final int GROUP_ITEM = 1;

    private MainActivity mContext;
    private List<NetMedia> mNetMedias;
    private LayoutInflater mInflater;

    public MyRecyDataFragAdapter(MainActivity context, List<NetMedia> netMedias) {
        mContext = context;
        mNetMedias = netMedias;
        mInflater = LayoutInflater.from(mContext);
    }

    /**
     * 渲染具体的ViewHolder
     *
     * @param parent   ViewHolder的容器
     * @param viewType 一个标志，根据此标志可以渲染不同类型的ViewHolder
     * @return
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == NORMAL_ITEM) {
            return new NormalItemHolder(mInflater.inflate(R.layout.recyfragment_base_swipe_list, parent, false));
        } else if (viewType == GROUP_ITEM) {
            return new GroupItemHolder(mInflater.inflate(R.layout.recyfragment_base_swipe_group_item, parent, false));
        } else {
            return null;
        }
    }

    /**
     * 绑定ViewHolder的数据
     *
     * @param holder   ViewHolder
     * @param position 数据源list的下标
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        NetMedia netMedia = mNetMedias.get(position);
        if (null == netMedia) {
            return;
        }
        if (holder instanceof GroupItemHolder) {
            bindGroupItem(netMedia, (GroupItemHolder) holder);
        } else if (holder instanceof NormalItemHolder) {
            bindNormalItem(netMedia, (NormalItemHolder) holder);
        } else {
            return;
        }
    }


    /**
     * 数目
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return mNetMedias.size();
    }

    /**
     * 决定元素的布局使用哪种类型
     *
     * @param position 数据源的标
     * @return 一个int型标志，传递给onCreateViewHolder的第二个参数
     */
    @Override
    public int getItemViewType(int position) {

        if (position == 0) {
            return GROUP_ITEM;
        }

        String currentType = mNetMedias.get(position).getType().get(0);
        int prevIndex = position - 1;
        boolean isDifferent = !mNetMedias.get(prevIndex).getType().get(0).equals(currentType);
        return isDifferent ? GROUP_ITEM : NORMAL_ITEM;
    }

    @Override
    public long getItemId(int position) {
        return mNetMedias.get(position).getId();
    }

    /**
     * 绑定GroupItemHolder的数据
     *
     * @param media
     * @param holder
     */
    private void bindGroupItem(NetMedia media, GroupItemHolder holder) {
        bindNormalItem(media, holder);

        holder.movieType.setText(media.getType().get(0));
    }

    /**
     * 绑定NormalItemHolder的数据
     *
     * @param media
     * @param holder
     */
    private void bindNormalItem( NetMedia media,  NormalItemHolder holder) {
        Picasso.with(mContext).load(media.getCoverImg()).into(holder.iv_actionIcon);
        holder.tv_actionTitle.setText(media.getVideoTitle());
    }

    /**
     * ActionMovie标题
     */
    public class NormalItemHolder extends RecyclerView.ViewHolder {

        TextView tv_actionTitle;
        ImageView iv_actionIcon;

        public NormalItemHolder(View itemView) {
            super(itemView);
            tv_actionTitle = (TextView) itemView.findViewById(R.id.base_swipe_item_title);
            iv_actionIcon = (ImageView) itemView.findViewById(R.id.base_swipe_item_icon);

            itemView.findViewById(R.id.base_swipe_item_container).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, SystemPlayerActivity.class);
                    for (int i = 0; i < mNetMedias.size(); i++) {
                        if (mNetMedias.get(i).getVideoTitle().equals(tv_actionTitle.getText().toString())) {
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("netmedialist", (Serializable) mNetMedias);
                            intent.putExtras(bundle);
                            intent.setData(Uri.parse(mNetMedias.get(i).getUrl()));
                            intent.putExtra("name", mNetMedias.get(i).getVideoTitle());
                            intent.putExtra("id", mNetMedias.get(i).getId());
                            mContext.startActivity(intent);
                        }
                    }
                }
            });
        }
    }

    /**
     * 带日期的ActionMovie标题
     */
    public class GroupItemHolder extends NormalItemHolder {

        TextView movieType;

        public GroupItemHolder(View itemView) {
            super(itemView);
            movieType = (TextView) itemView.findViewById(R.id.base_swipe_group_item_time);
        }
    }

}
























