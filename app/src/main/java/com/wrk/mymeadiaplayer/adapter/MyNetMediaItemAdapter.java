package com.wrk.mymeadiaplayer.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.wrk.mymeadiaplayer.R;
import com.wrk.mymeadiaplayer.activity.PhotoViewActivity;
import com.wrk.mymeadiaplayer.bean.NetMediaItem;
import com.wrk.mymeadiaplayer.util.Utils;

import org.xutils.common.util.DensityUtil;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by MrbigW on 2016/10/12.
 * weChat:1024057635
 * Github:MrbigW
 * Usage: MyNetMediaItemAdapter
 * -------------------=.=------------------------
 */

public class MyNetMediaItemAdapter extends BaseAdapter {

    // 视频
    private static final int TYPE_VIDEO = 0;

    // 图片
    private static final int TYPE_IMAGE = 1;

    // 文字
    private static final int TYPE_TEXT = 2;

    // GIF动图
    private static final int TYPE_GIF = 3;


    // 软件推广
    private static final int TYPE_AD = 4;


    private ArrayList<NetMediaItem> mItems;

    private Context mContext;
    private ImageOptions imageOptions;
    private Utils utils;

    public void onDataChange(List<NetMediaItem> list) {
        this.mItems = (ArrayList<NetMediaItem>) list;
        this.notifyDataSetChanged();
    }

    public MyNetMediaItemAdapter(Context context, ArrayList<NetMediaItem> data) {
        this.mContext = context;
        this.mItems = data;
        utils = new Utils();
        imageOptions = new ImageOptions.Builder()
                .setPlaceholderScaleType(ImageView.ScaleType.MATRIX)
                .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                .setLoadingDrawableId(R.drawable.bg_item)
                .setFailureDrawableId(R.drawable.bg_item)
                .build();
    }


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

    // 返回类型总数
    @Override
    public int getViewTypeCount() {
        return 5;
    }

    // 根据位置得到对象的类型
    @Override
    public int getItemViewType(int position) {
        int itemViewType = -1;
        String type = mItems.get(position).getType();

        if ("video".equals(type)) {
            itemViewType = TYPE_VIDEO;
        } else if ("image".equals(type)) {
            itemViewType = TYPE_IMAGE;
        } else if ("text".equals(type)) {
            itemViewType = TYPE_TEXT;
        } else if ("gif".equals(type)) {
            itemViewType = TYPE_GIF;
        } else {
            itemViewType = TYPE_AD;
        }

        return itemViewType;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        int itemViewType = getItemViewType(position);
        if (convertView == null) {
            viewHolder = new ViewHolder();


            convertView = initTypeView(convertView, viewHolder, itemViewType);


            // 初始化公共控件
            initCommonView(convertView, itemViewType, viewHolder);
            //根据itemViewType初始化的布局


            convertView.setTag(viewHolder);


        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        binData(position, itemViewType, viewHolder);

        return convertView;
    }

    /**
     * 根据itemViewType初始化的布局
     *
     * @param convertView
     * @param viewHolder
     * @param itemViewType
     * @return
     */
    private View initTypeView(View convertView, ViewHolder viewHolder, int itemViewType) {
        switch (itemViewType) {

            case TYPE_VIDEO:
                convertView = View.inflate(mContext, R.layout.all_video_item, null);
                viewHolder.tv_play_nums = (TextView) convertView.findViewById(R.id.tv_play_nums);
                viewHolder.tv_video_duration = (TextView) convertView.findViewById(R.id.tv_video_duration);
                viewHolder.iv_commant = (ImageView) convertView.findViewById(R.id.iv_commant);
                viewHolder.tv_commant_context = (TextView) convertView.findViewById(R.id.tv_commant_context);
                viewHolder.jcv_videoplayer = (JCVideoPlayerStandard) convertView.findViewById(R.id.jcv_videoplayer);
                break;
            case TYPE_IMAGE:
                convertView = View.inflate(mContext, R.layout.all_image_item, null);
                viewHolder.iv_image_icon = (ImageView) convertView.findViewById(R.id.iv_image_icon);
                break;
            case TYPE_TEXT:
                convertView = View.inflate(mContext, R.layout.all_text_item, null);

                break;
            case TYPE_GIF:
                convertView = View.inflate(mContext, R.layout.all_gif_item, null);
                viewHolder.iv_image_gif = (GifImageView) convertView.findViewById(R.id.iv_image_gif);
                break;
            case TYPE_AD:
                convertView = View.inflate(mContext, R.layout.all_ad_item, null);
                viewHolder.btn_install = (Button) convertView.findViewById(R.id.btn_install);
                viewHolder.iv_image_icon = (ImageView) convertView.findViewById(R.id.iv_image_icon);
                break;

        }
        return convertView;
    }

    /**
     * 初始化公共的控件
     *
     * @param convertView
     * @param itemViewType
     * @param viewHolder
     */
    private void initCommonView(View convertView, int itemViewType, ViewHolder viewHolder) {

        switch (itemViewType) {
            case TYPE_VIDEO://视频
            case TYPE_IMAGE://图片
            case TYPE_TEXT://文字
            case TYPE_GIF://gif
                //加载除开广告部分的公共部分视图
                //user info
                viewHolder.iv_headpic = (ImageView) convertView.findViewById(R.id.iv_headpic);
                viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                viewHolder.tv_time_refresh = (TextView) convertView.findViewById(R.id.tv_time_refresh);
                viewHolder.iv_right_more = (ImageView) convertView.findViewById(R.id.iv_right_more);
                //bottom
                viewHolder.iv_video_kind = (ImageView) convertView.findViewById(R.id.iv_video_kind);
                viewHolder.tv_video_kind_text = (TextView) convertView.findViewById(R.id.tv_video_kind_text);
                viewHolder.tv_shenhe_caishu = (TextView) convertView.findViewById(R.id.tv_caishu);
                viewHolder.tv_shenhe_dingshu = (TextView) convertView.findViewById(R.id.tv_dingshu);
                viewHolder.tv_fenxiangshu = (TextView) convertView.findViewById(R.id.tv_fenxiangshu);
                viewHolder.ll_download = (LinearLayout) convertView.findViewById(R.id.ll_download);

                break;
        }

        // 中间公共部分---共有的
        viewHolder.tv_context = (TextView) convertView.findViewById(R.id.tv_context);

    }


    static class ViewHolder {
        // top
        ImageView iv_headpic;
        TextView tv_name;
        TextView tv_time_refresh;
        ImageView iv_right_more;

        //bottom
        ImageView iv_video_kind;
        TextView tv_video_kind_text;
        TextView tv_shenhe_dingshu;
        TextView tv_shenhe_caishu;
        TextView tv_fenxiangshu;
        LinearLayout ll_download;

        //中间公共部分 -所有的都有
        TextView tv_context;

        //Video
        TextView tv_play_nums;
        TextView tv_video_duration;
        ImageView iv_commant;
        TextView tv_commant_context;
        JCVideoPlayerStandard jcv_videoplayer;

        //Image
        ImageView iv_image_icon;

        //Gif
        GifImageView iv_image_gif;

        //软件推广
        Button btn_install;
    }


    /**
     * 根据位置与类型绑定数据
     *
     * @param pos
     * @param itemViewType
     * @param viewHolder
     */
    private void binData(int pos, int itemViewType, final ViewHolder viewHolder) {
        final NetMediaItem mediaItem = mItems.get(pos);

        switch (itemViewType) {
            case TYPE_VIDEO:
                // 先绑定公共数据
                bindCommonData(viewHolder, mediaItem);

                //第一个参数是视频播放地址，第二个参数是显示封面的地址，第三参数是标题
                viewHolder.jcv_videoplayer.setUp(mediaItem.getVideo().getVideo().get(0), JCVideoPlayer.CURRENT_STATE_NORMAL, "");
                Glide.with(mContext).load(mediaItem.getVideo().getThumbnail().get(0)).asBitmap().into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        viewHolder.jcv_videoplayer.thumbImageView.setImageBitmap(resource);
                    }
                });


                viewHolder.tv_play_nums.setText(mediaItem.getVideo().getPlaycount() + "次播放");
                viewHolder.tv_video_duration.setText(utils.stringForTime(mediaItem.getVideo().getDuration() * 1000) + "");
                break;

            case TYPE_IMAGE:
                bindCommonData(viewHolder, mediaItem);
                viewHolder.iv_image_icon.setImageResource(R.drawable.bg_item);
                int height = mediaItem.getImage().getHeight() <= DensityUtil.getScreenHeight() * 0.75 ? mediaItem.getImage().getHeight() : (int) (DensityUtil.getScreenHeight() * 0.75);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DensityUtil.getScreenWidth(), height);
                viewHolder.iv_image_icon.setLayoutParams(params);
                if (mediaItem.getImage() != null && mediaItem.getImage().getBig() != null && mediaItem.getImage().getBig().size() > 0) {
                    Glide.with(mContext).load(mediaItem.getImage().getBig().get(0)).placeholder(R.drawable.bg_item).error(R.drawable.bg_item).diskCacheStrategy(DiskCacheStrategy.ALL).into(viewHolder.iv_image_icon);
                }

                viewHolder.iv_image_icon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, PhotoViewActivity.class);
                        intent.putExtra("imageurl", mediaItem.getImage().getBig().get(0));
                        mContext.startActivity(intent);
                    }
                });

                break;
            case TYPE_TEXT:
                bindCommonData(viewHolder, mediaItem);
                break;
            case TYPE_GIF:
                bindCommonData(viewHolder, mediaItem);
                Glide.with(mContext).load(mediaItem.getGif().getImages().get(0)).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(viewHolder.iv_image_gif);
                break;
            case TYPE_AD:
                break;

        }

        // 设置文版
        viewHolder.tv_context.setText(mediaItem.getText());

    }

    /**
     * 绑定公共数据
     *
     * @param viewHolder
     * @param netMediaItem
     */
    private void bindCommonData(ViewHolder viewHolder, NetMediaItem netMediaItem) {
        if (netMediaItem.getU() != null && netMediaItem.getU().getHeader() != null && netMediaItem.getU().getHeader().get(0) != null) {
            // 加载图片
            x.image().bind(viewHolder.iv_headpic, netMediaItem.getU().getHeader().get(0), imageOptions);
        }
        if (netMediaItem.getU() != null && netMediaItem.getU().getName() != null) {
            viewHolder.tv_name.setText(netMediaItem.getU().getName());
        }
        viewHolder.tv_time_refresh.setText(netMediaItem.getPasstime());

//         设置点赞，踩，转发
        viewHolder.tv_shenhe_dingshu.setText(netMediaItem.getUp());
        viewHolder.tv_shenhe_caishu.setText(String.valueOf(netMediaItem.getDown()));
        viewHolder.tv_fenxiangshu.setText(String.valueOf(netMediaItem.getForward()));

        List<NetMediaItem.TagsBean> tags = netMediaItem.getTags();
        if (tags != null && tags.size() > 0) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < tags.size(); i++) {
                sb.append(tags.get(i).getName() + " ");
            }
            viewHolder.tv_video_kind_text.setText(sb.toString());
        }

    }

}
































