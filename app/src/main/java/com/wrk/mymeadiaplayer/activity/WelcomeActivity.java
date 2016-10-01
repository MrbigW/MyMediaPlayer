package com.wrk.mymeadiaplayer.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;

import com.wrk.mymeadiaplayer.R;

/**
 * Created by 那位程序猿Mr.W on 2016/9/28.
 * 微信:1024057635
 * gitHub:MrbigW
 * =.=
 */

public class WelcomeActivity extends Activity {

    private Handler mHandler;

    // 是否跳转到MainActivity
    private boolean isJumpToMainActivity = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        mHandler = new Handler();

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // 跳转到MainActivity
                    jumpToMainActivity();
            }
        }, 2000);

    }

    private void jumpToMainActivity() {
        if (!isJumpToMainActivity) {
            isJumpToMainActivity = true;
            startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
            finish();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        jumpToMainActivity();

        return super.onTouchEvent(event);
    }

    // 移除消息防止内存泄漏引发bug
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }
}
