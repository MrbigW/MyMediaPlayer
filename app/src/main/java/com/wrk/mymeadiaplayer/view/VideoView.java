package com.wrk.mymeadiaplayer.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

/**
 * Created by MrbigW on 2016/10/1.
 * weChat:1024057635
 * Github:MrbigW
 * Usage: 自定义VideoView
 * -------------------=.=------------------------
 */

public class VideoView extends android.widget.VideoView {
    public VideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(widthMeasureSpec,heightMeasureSpec);
    }

    /**
     * 设置视频的宽和高
     *
     * @param videoWidth
     * @param videoHeight
     */
    public void setVideoSize(int videoWidth, int videoHeight) {
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        layoutParams.width = videoWidth;
        layoutParams.height = videoHeight;
        setLayoutParams(layoutParams);
    }
}
