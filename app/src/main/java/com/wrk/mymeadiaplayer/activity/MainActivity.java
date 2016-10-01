package com.wrk.mymeadiaplayer.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.widget.RadioGroup;

import com.wrk.mymeadiaplayer.R;
import com.wrk.mymeadiaplayer.fragment.AudioFrament;
import com.wrk.mymeadiaplayer.fragment.BaseFragment;
import com.wrk.mymeadiaplayer.fragment.NetAudioFrament;
import com.wrk.mymeadiaplayer.fragment.NetVideoFrament;
import com.wrk.mymeadiaplayer.fragment.VideoFrament;

import java.util.ArrayList;

/**
 * Created by 那位程序猿Mr.W on 2016/9/28.
 * 微信:1024057635
 * gitHub:MrbigW
 * =.=
 */

public class MainActivity extends FragmentActivity {

    private RadioGroup rg_main;

    // 所有Fragmet的集合
    private ArrayList<BaseFragment> mFragments;

    // 列表中对应的Fragment的位置
    private int pos;

    private Fragment mFra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rg_main = (RadioGroup) findViewById(R.id.rg_main);

        initFragments();

        // 对RadioGroup设置监听
        rg_main.setOnCheckedChangeListener(new MyOncheckedChangeListener());

        rg_main.check(R.id.rb_local_video);

    }

    class MyOncheckedChangeListener implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.rb_local_video:
                    pos = 0;
                    break;
                case R.id.rb_local_audio:
                    pos = 1;
                    break;
                case R.id.rb_net_video:
                    pos = 2;
                    break;
                case R.id.rb_net__audio:
                    pos = 3;
                    break;
            }

            Fragment toFrag = getFragment(pos);
            switchFragment(mFra, toFrag);

        }
    }

    /**
     * 将所有fragment加入到集合中并初始化
     */
    private void initFragments() {
        mFragments = new ArrayList<>();
        mFragments.add(new VideoFrament());
        mFragments.add(new AudioFrament());
        mFragments.add(new NetVideoFrament());
        mFragments.add(new NetAudioFrament());

    }

    /**
     * 切换Fragment
     * 在项目中切换Fragment，一直都是用replace()方法来替换Fragment。
     * 但是这样做有一个问题，每次切换的时候Fragment都会重新实列化，
     * 重新加载一次数据，这样做会非常消耗性能和用户的流量。
     * 官方文档解释说：replace()这个方法只是在上一个Fragment不再需要时采用的简便方法。
     * 正确的切换方式是add()，切换时hide()，add()另一个Fragment；
     * 再次切换时，只需hide()当前，show()另一个。
     * 这样就能做到多个Fragment切换不重新实例化：
     */
    private void switchFragment(Fragment fromFrag, Fragment toFrag) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (mFra != toFrag) {
            mFra = toFrag;
            if (toFrag != null) {
                // 判断toFrag是否添加
                if (!toFrag.isAdded()) {
                    //如果没有添加
                    if (fromFrag != null) {
                        transaction.hide(fromFrag);
                    }
                    transaction.add(R.id.fl_main_container, toFrag).commit();
                } else {
                    //如果添加了
                    if (fromFrag != null) {
                        transaction.hide(fromFrag);
                    }
                    transaction.show(toFrag).commit();
                }
            }

        }

    }

    /**
     * 得到某个Fragment
     *
     * @param pos
     * @return
     */
    public Fragment getFragment(int pos) {
        return mFragments.get(pos);
    }
}
