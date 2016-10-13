package me.yokeyword.sample.demo_flow.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import org.xutils.common.util.DensityUtil;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import me.yokeyword.sample.PhotoViewActivity;
import me.yokeyword.sample.R;
import me.yokeyword.sample.demo_flow.entity.NetMediaItem;
import me.yokeyword.sample.demo_flow.util.Utils;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by MrbigW on 2016/10/13.
 * weChat:1024057635
 * Github:MrbigW
 * Usage: -.-
 * -------------------=.=------------------------
 */

public class MyNetMediaRecyItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


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

    private LayoutInflater mInflater;

    public MyNetMediaRecyItemAdapter(Context context, ArrayList<NetMediaItem> data) {
        this.mContext = context;
        this.mItems = data;
        utils = new Utils();
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

        switch (viewType) {
            case TYPE_VIDEO://视频
                return new VideoViewHolder(mInflater.inflate(R.layout.all_video_item, parent, false));
            case TYPE_IMAGE://图片
                return new ImageViewHolder(mInflater.inflate(R.layout.all_image_item, parent, false));
            case TYPE_TEXT://文字
                return new TextViewHolder(mInflater.inflate(R.layout.all_text_item, parent, false));
            case TYPE_GIF://gif
                return new GifViewHolder(mInflater.inflate(R.layout.all_gif_item, parent, false));
            case TYPE_AD:
                return new ADViewHolder(mInflater.inflate(R.layout.all_ad_item, parent, false));
        }

        return null;
    }

    /**
     * 绑定ViewHolder的数据
     *
     * @param holder   ViewHolder
     * @param position 数据源list的下标
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        NetMediaItem netmeia = mItems.get(position);

        if (null == netmeia) {
            return;
        }

        binAllData(position, (CommonViewHolder) holder);


    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    /**
     * 决定元素的布局使用哪种类型
     *
     * @param position 数据源的标
     * @return 一个int型标志，传递给onCreateViewHolder的第二个参数
     */
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
    public long getItemId(int position) {
        return Long.parseLong(mItems.get(position).getId());
    }


    /**
     * 公共的ViewHolder
     */
    public class CommonViewHolder extends RecyclerView.ViewHolder {
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

        public CommonViewHolder(View itemView) {
            super(itemView);
            //user info
            iv_headpic = (ImageView) itemView.findViewById(R.id.iv_headpic);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_time_refresh = (TextView) itemView.findViewById(R.id.tv_time_refresh);
            iv_right_more = (ImageView) itemView.findViewById(R.id.iv_right_more);
            //bottom
            iv_video_kind = (ImageView) itemView.findViewById(R.id.iv_video_kind);
            tv_video_kind_text = (TextView) itemView.findViewById(R.id.tv_video_kind_text);
            tv_shenhe_caishu = (TextView) itemView.findViewById(R.id.tv_caishu);
            tv_shenhe_dingshu = (TextView) itemView.findViewById(R.id.tv_dingshu);
            tv_fenxiangshu = (TextView) itemView.findViewById(R.id.tv_fenxiangshu);
            ll_download = (LinearLayout) itemView.findViewById(R.id.ll_download);
            // 中间公共部分---共有的
            tv_context = (TextView) itemView.findViewById(R.id.tv_context);
        }
    }


    public class VideoViewHolder extends CommonViewHolder {

        //Video
        TextView tv_play_nums;
        TextView tv_video_duration;
        ImageView iv_commant;
        TextView tv_commant_context;
        JCVideoPlayerStandard jcv_videoplayer;

        public VideoViewHolder(View itemView) {
            super(itemView);

            tv_play_nums = (TextView) itemView.findViewById(R.id.tv_play_nums);
            tv_video_duration = (TextView) itemView.findViewById(R.id.tv_video_duration);
            iv_commant = (ImageView) itemView.findViewById(R.id.iv_commant);
            tv_commant_context = (TextView) itemView.findViewById(R.id.tv_commant_context);
            jcv_videoplayer = (JCVideoPlayerStandard) itemView.findViewById(R.id.jcv_videoplayer);

        }
    }

    public class ImageViewHolder extends CommonViewHolder {

        ImageView iv_image_icon;

        public ImageViewHolder(View itemView) {
            super(itemView);

            iv_image_icon = (ImageView) itemView.findViewById(R.id.iv_image_icon);

        }
    }

    public class TextViewHolder extends CommonViewHolder {

        public TextViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class GifViewHolder extends CommonViewHolder {

        GifImageView iv_image_gif;

        public GifViewHolder(View itemView) {
            super(itemView);

            iv_image_gif = (GifImageView) itemView.findViewById(R.id.iv_image_gif);
        }
    }

    public class ADViewHolder extends CommonViewHolder {

        Button btn_install;

        public ADViewHolder(View itemView) {
            super(itemView);

            btn_install = (Button) itemView.findViewById(R.id.btn_install);
        }
    }

    /**
     * 绑定CommonViewHolder的数据
     *
     * @param netMediaItem
     * @param viewHolder
     */
    private void bindCommonData(NetMediaItem netMediaItem, CommonViewHolder viewHolder) {
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

    /**
     * 根据位置与类型绑定数据
     *
     * @param pos
     * @param viewHolder
     */
    private void binAllData(int pos, final CommonViewHolder viewHolder) {
        final NetMediaItem mediaItem = mItems.get(pos);
        bindCommonData(mediaItem, viewHolder);
        if (viewHolder instanceof VideoViewHolder) {
            //第一个参数是视频播放地址，第二个参数是显示封面的地址，第三参数是标题
            ((VideoViewHolder) viewHolder).jcv_videoplayer.setUp(mediaItem.getVideo().getVideo().get(0), JCVideoPlayer.CURRENT_STATE_NORMAL, "");
            Glide.with(mContext).load(mediaItem.getVideo().getThumbnail().get(0)).asBitmap().into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    ((VideoViewHolder) viewHolder).jcv_videoplayer.thumbImageView.setImageBitmap(resource);
                }
            });

            ((VideoViewHolder) viewHolder).tv_play_nums.setText(mediaItem.getVideo().getPlaycount() + "次播放");
            ((VideoViewHolder) viewHolder).tv_video_duration.setText(utils.stringForTime(mediaItem.getVideo().getDuration() * 1000) + "");

        } else if (viewHolder instanceof ImageViewHolder) {
            ((ImageViewHolder) viewHolder).iv_image_icon.setImageResource(R.drawable.bg_item);
            int height = mediaItem.getImage().getHeight() <= DensityUtil.getScreenHeight() * 0.75 ? mediaItem.getImage().getHeight() : (int) (DensityUtil.getScreenHeight() * 0.75);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DensityUtil.getScreenWidth(), height);
            ((ImageViewHolder) viewHolder).iv_image_icon.setLayoutParams(params);
            if (mediaItem.getImage() != null && mediaItem.getImage().getBig() != null && mediaItem.getImage().getBig().size() > 0) {
                Glide.with(mContext).load(mediaItem.getImage().getBig().get(0)).placeholder(R.drawable.bg_item).error(R.drawable.bg_item).diskCacheStrategy(DiskCacheStrategy.ALL).into(((ImageViewHolder) viewHolder).iv_image_icon);
            }

            ((ImageViewHolder) viewHolder).iv_image_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, PhotoViewActivity.class);
                    intent.putExtra("imageurl", mediaItem.getImage().getBig().get(0));
                    mContext.startActivity(intent);
                }
            });
        } else if (viewHolder instanceof TextViewHolder) {

        } else if (viewHolder instanceof GifViewHolder) {
            Glide.with(mContext).load(mediaItem.getGif().getImages().get(0)).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(((GifViewHolder) viewHolder).iv_image_gif);
        }

            // 设置文版
            viewHolder.tv_context.setText(mediaItem.getText());

    }

//
//    /**
//     * 根据itemViewType初始化的布局
//     *
//     * @param convertView
//     * @param viewHolder
//     * @param itemViewType
//     * @return
//     */
//    private View initTypeView(View convertView, MyNetMediaItemAdapter.ViewHolder viewHolder, int itemViewType) {
//        switch (itemViewType) {
//
//            case TYPE_VIDEO:
//                convertView = View.inflate(mContext, R.layout.all_video_item, null);
//                viewHolder.tv_play_nums = (TextView) convertView.findViewById(tv_play_nums);
//                viewHolder.tv_video_duration = (TextView) convertView.findViewById(tv_video_duration);
//                viewHolder.iv_commant = (ImageView) convertView.findViewById(R.id.iv_commant);
//                viewHolder.tv_commant_context = (TextView) convertView.findViewById(R.id.tv_commant_context);
//                viewHolder.jcv_videoplayer = (JCVideoPlayerStandard) convertView.findViewById(jcv_videoplayer);
//                break;
//            case TYPE_IMAGE:
//                convertView = View.inflate(mContext, R.layout.all_image_item, null);
//                viewHolder.iv_image_icon = (ImageView) convertView.findViewById(R.id.iv_image_icon);
//                break;
//            case TYPE_TEXT:
//                convertView = View.inflate(mContext, R.layout.all_text_item, null);
//
//                break;
//            case TYPE_GIF:
//                convertView = View.inflate(mContext, R.layout.all_gif_item, null);
//                viewHolder.iv_image_gif = (GifImageView) convertView.findViewById(R.id.iv_image_gif);
//                break;
//            case TYPE_AD:
//                convertView = View.inflate(mContext, R.layout.all_ad_item, null);
//                viewHolder.btn_install = (Button) convertView.findViewById(R.id.btn_install);
//                viewHolder.iv_image_icon = (ImageView) convertView.findViewById(R.id.iv_image_icon);
//                break;
//
//        }
//        return convertView;
//    }

//    /**
//     * 初始化公共的控件
//     *
//     * @param convertView
//     * @param itemViewType
//     * @param viewHolder
//     */
//    private void initCommonView(View convertView, int itemViewType, MyNetMediaItemAdapter.ViewHolder viewHolder) {
//
//        switch (itemViewType) {
//            case TYPE_VIDEO://视频
//            case TYPE_IMAGE://图片
//            case TYPE_TEXT://文字
//            case TYPE_GIF://gif
//                //加载除开广告部分的公共部分视图
//                //user info
//                viewHolder.iv_headpic = (ImageView) convertView.findViewById(R.id.iv_headpic);
//                viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
//                viewHolder.tv_time_refresh = (TextView) convertView.findViewById(R.id.tv_time_refresh);
//                viewHolder.iv_right_more = (ImageView) convertView.findViewById(R.id.iv_right_more);
//                //bottom
//                viewHolder.iv_video_kind = (ImageView) convertView.findViewById(R.id.iv_video_kind);
//                viewHolder.tv_video_kind_text = (TextView) convertView.findViewById(R.id.tv_video_kind_text);
//                viewHolder.tv_shenhe_caishu = (TextView) convertView.findViewById(R.id.tv_caishu);
//                viewHolder.tv_shenhe_dingshu = (TextView) convertView.findViewById(R.id.tv_dingshu);
//                viewHolder.tv_fenxiangshu = (TextView) convertView.findViewById(R.id.tv_fenxiangshu);
//                viewHolder.ll_download = (LinearLayout) convertView.findViewById(R.id.ll_download);
//
//                break;
//        }
//
//        // 中间公共部分---共有的
//        viewHolder.tv_context = (TextView) convertView.findViewById(R.id.tv_context);
//
//    }

//
//    /**
//     * 根据位置与类型绑定数据
//     *
//     * @param pos
//     * @param itemViewType
//     * @param viewHolder
//     */
//    private void binData(int pos, int itemViewType, final MyNetMediaItemAdapter.ViewHolder viewHolder) {
//        final NetMediaItem mediaItem = mItems.get(pos);
//
//        switch (itemViewType) {
//            case TYPE_VIDEO:
//                // 先绑定公共数据
//                bindCommonData(viewHolder, mediaItem);
//
//                //第一个参数是视频播放地址，第二个参数是显示封面的地址，第三参数是标题
//                viewHolder.jcv_videoplayer.setUp(mediaItem.getVideo().getVideo().get(0), JCVideoPlayer.CURRENT_STATE_NORMAL, "");
//                Glide.with(mContext).load(mediaItem.getVideo().getThumbnail().get(0)).asBitmap().into(new SimpleTarget<Bitmap>() {
//                    @Override
//                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//                        viewHolder.jcv_videoplayer.thumbImageView.setImageBitmap(resource);
//                    }
//                });
//
//
//                viewHolder.tv_play_nums.setText(mediaItem.getVideo().getPlaycount() + "次播放");
//                viewHolder.tv_video_duration.setText(utils.stringForTime(mediaItem.getVideo().getDuration() * 1000) + "");
//                break;
//
//            case TYPE_IMAGE:
//                bindCommonData(viewHolder, mediaItem);
//                viewHolder.iv_image_icon.setImageResource(R.drawable.bg_item);
//                int height = mediaItem.getImage().getHeight() <= DensityUtil.getScreenHeight() * 0.75 ? mediaItem.getImage().getHeight() : (int) (DensityUtil.getScreenHeight() * 0.75);
//
//                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DensityUtil.getScreenWidth(), height);
//                viewHolder.iv_image_icon.setLayoutParams(params);
//                if (mediaItem.getImage() != null && mediaItem.getImage().getBig() != null && mediaItem.getImage().getBig().size() > 0) {
//                    Glide.with(mContext).load(mediaItem.getImage().getBig().get(0)).placeholder(R.drawable.bg_item).error(R.drawable.bg_item).diskCacheStrategy(DiskCacheStrategy.ALL).into(viewHolder.iv_image_icon);
//                }
//
//                viewHolder.iv_image_icon.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent = new Intent(mContext, PhotoViewActivity.class);
//                        intent.putExtra("imageurl", mediaItem.getImage().getBig().get(0));
//                        mContext.startActivity(intent);
//                    }
//                });
//
//                break;
//            case TYPE_TEXT:
//                bindCommonData(viewHolder, mediaItem);
//                break;
//            case TYPE_GIF:
//                bindCommonData(viewHolder, mediaItem);
//                Glide.with(mContext).load(mediaItem.getGif().getImages().get(0)).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(viewHolder.iv_image_gif);
//                break;
//            case TYPE_AD:
//                break;
//
//        }
//
//        // 设置文版
//        viewHolder.tv_context.setText(mediaItem.getText());
//
//    }

//    /**
//     * 绑定公共数据
//     *
//     * @param viewHolder
//     * @param netMediaItem
//     */
//    private void bindCommonData(MyNetMediaItemAdapter.ViewHolder viewHolder, NetMediaItem netMediaItem) {
//        if (netMediaItem.getU() != null && netMediaItem.getU().getHeader() != null && netMediaItem.getU().getHeader().get(0) != null) {
//            // 加载图片
//            x.image().bind(viewHolder.iv_headpic, netMediaItem.getU().getHeader().get(0), imageOptions);
//        }
//        if (netMediaItem.getU() != null && netMediaItem.getU().getName() != null) {
//            viewHolder.tv_name.setText(netMediaItem.getU().getName());
//        }
//        viewHolder.tv_time_refresh.setText(netMediaItem.getPasstime());
//
////         设置点赞，踩，转发
//        viewHolder.tv_shenhe_dingshu.setText(netMediaItem.getUp());
//        viewHolder.tv_shenhe_caishu.setText(String.valueOf(netMediaItem.getDown()));
//        viewHolder.tv_fenxiangshu.setText(String.valueOf(netMediaItem.getForward()));
//
//        List<NetMediaItem.TagsBean> tags = netMediaItem.getTags();
//        if (tags != null && tags.size() > 0) {
//            StringBuffer sb = new StringBuffer();
//            for (int i = 0; i < tags.size(); i++) {
//                sb.append(tags.get(i).getName() + " ");
//            }
//            viewHolder.tv_video_kind_text.setText(sb.toString());
//        }
//
//    }


}


































