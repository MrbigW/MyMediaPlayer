package com.wrk.mymeadiaplayer.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

/**
 * Created by MrbigW on 2016/9/28.
 * weChat:1024057635
 * Github:MrbigW
 * Usage: -.-
 * -------------------=.=------------------------
 */

public class NetAudioFrament extends BaseFragment {

    private Context mContext;
    private TextView mTextView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    /**
     * 初始化视图
     *
     * @return
     */
    @Override
    public View initView() {
        mTextView = new TextView(mContext);
        mTextView.setTextSize(25);
        mTextView.setTextColor(Color.RED);
        mTextView.setGravity(Gravity.CENTER);
        return mTextView;
    }

    /**
     * 绑定数据
     */
    @Override
    public void initData() {
        super.initData();
        mTextView.setText("网络音乐");
    }
}
