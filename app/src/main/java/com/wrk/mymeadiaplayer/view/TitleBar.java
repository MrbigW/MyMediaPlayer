package com.wrk.mymeadiaplayer.view;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wrk.mymeadiaplayer.R;
import com.wrk.mymeadiaplayer.activity.SearchActivity;

/**
 * Created by 那位程序猿Mr.W on 2016/9/28.
 * 微信:1024057635
 * gitHub:MrbigW
 * =.=
 */

public class TitleBar extends LinearLayout implements View.OnClickListener {

    private TextView mTextView;
    private RelativeLayout mRelativeLayout;
    private ImageView mImageView;
    private Context mContext;

    public TitleBar(Context context) {
        this(context, null);
    }

    public TitleBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mTextView = (TextView) getChildAt(1);
        mRelativeLayout = (RelativeLayout) getChildAt(2);
        mImageView = (ImageView) getChildAt(3);

        mTextView.setOnClickListener(this);
        mRelativeLayout.setOnClickListener(this);
        mImageView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_search:
                mContext.startActivity(new Intent(mContext, SearchActivity.class));
                break;
            case R.id.rl_game:
                Toast.makeText(mContext, "游戏", Toast.LENGTH_SHORT).show();
                break;
            case R.id.iv_history:
                Toast.makeText(mContext, "历史记录", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
