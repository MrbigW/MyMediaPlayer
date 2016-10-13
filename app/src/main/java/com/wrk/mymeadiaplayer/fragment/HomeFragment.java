package com.wrk.mymeadiaplayer.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wrk.mymeadiaplayer.R;
import com.wrk.mymeadiaplayer.homepagefragment.HomeDataFragment;
import com.wrk.mymeadiaplayer.homepagefragment.RecyDataFragment;
import com.wrk.mymeadiaplayer.homepagefragment.VpSimpleFragment;
import com.wrk.mymeadiaplayer.view.ViewPagerIndicator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by MrbigW on 2016/10/1.
 * weChat:1024057635
 * Github:MrbigW
 * Usage: -.-
 * -------------------=.=------------------------
 */

public class HomeFragment extends BaseFragment {
    private Context mContext;
    private ViewPager mViewPager;
    private ViewPagerIndicator mIndicator;

    private List<String> mTitles;

    private List<Fragment> mContents;

    protected FragmentPagerAdapter mAdapter;


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
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.homefragment_layout, null);

        mViewPager = (ViewPager) view.findViewById(R.id.id_viewpager);
        mIndicator = (ViewPagerIndicator) view.findViewById(R.id.id_indicator);


        return view;
    }

    /**
     * 绑定数据
     */
    @Override
    public void initData() {
        super.initData();
        mTitles = Arrays.asList("首页", "Recy", "剧情", "爱情", "科幻", "冒险", "犯罪", "奇幻", "惊悚", "悬疑", "动画", "爱情");
        mContents = new ArrayList<Fragment>();

        for (String title : mTitles) {

            if (title.equals("首页")) {
                mContents.add(new HomeDataFragment());
            } else if (title.equals("Recy")) {
                mContents.add(new RecyDataFragment());
            } else if (title.equals("剧情")) {
                mContents.add(new NetVideoFrament());
            } else if (title.equals("爱情")) {
                mContents.add(new NetAudioFrament());
            } else {
                VpSimpleFragment vpSimpleFragment = VpSimpleFragment.newInstance(title);
                mContents.add(vpSimpleFragment);
            }
        }

        mAdapter = new FragmentPagerAdapter(getFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mContents.get(position);
            }

            @Override
            public int getCount() {
                return mContents.size();
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                // 使fragment不会被销毁
//                super.destroyItem(container, position, object);
            }
        };

        mIndicator.setVisibleTabCount(5);

        mIndicator.setTabItemTitles(mTitles);

        mViewPager.setAdapter(mAdapter);

        mIndicator.setViewPager(mViewPager, 0);
    }
}
