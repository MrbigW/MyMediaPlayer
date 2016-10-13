package me.yokeyword.sample.demo_flow.ui.fragment.discover;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by 那位程序猿Mr.W on 2016/9/28.
 * weChat:1024057635
 * gitHub:MrbigW
 * Usage:BaseFragment，基类，公共类
 * VideoFragment,AudioFragment,NetVideoFragment,NetAudioFragment 继承该类
 * =.=
 */

public abstract class BaseFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return initView();
    }

    public abstract View initView();

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    public void initData() {

    }
}











































