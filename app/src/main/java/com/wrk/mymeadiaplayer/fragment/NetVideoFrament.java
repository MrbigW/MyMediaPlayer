package com.wrk.mymeadiaplayer.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.google.gson.Gson;
import com.wrk.mymeadiaplayer.R;
import com.wrk.mymeadiaplayer.adapter.MyNetMediaItemAdapter;
import com.wrk.mymeadiaplayer.bean.NetMediaItem;
import com.wrk.mymeadiaplayer.util.CacheUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;

/**
 * Created by MrbigW on 2016/9/28.
 * weChat:1024057635
 * Github:MrbigW
 * Usage: NetVideoFrament
 * -------------------=.=------------------------
 */

public class NetVideoFrament extends BaseFragment {

    private MaterialRefreshLayout netvideo_refresh;

    public static final String NETMEADIAURLPATH = "http://s.budejie.com/topic/list/jingxuan/1/budejie-android-6.2.8/0-20.json?market=baidu&udid=863425026599592&appname=baisibudejie&os=4.2.2&client=android&visiting=&mac=98%3A6c%3Af5%3A4b%3A72%3A6d&ver=6.2.8";

    private Context mContext;
    private ListView lv_netvideo;
    private MyNetMediaItemAdapter mAdapter;
    private ProgressBar progressBar;
    private TextView tv_nodata;

    private ArrayList<NetMediaItem> mItems;


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

        View view = View.inflate(mContext, R.layout.frag_netvideo, null);
        lv_netvideo = (ListView) view.findViewById(R.id.lv_netvideo);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        tv_nodata = (TextView) view.findViewById(R.id.tv_nodata);
        netvideo_refresh = (MaterialRefreshLayout) view.findViewById(R.id.netvideo_refresh);

        // 设置下拉刷新的监听和加载更多的监听
        netvideo_refresh.setMaterialRefreshListener(new MyMaterialRefreshListener());

        return view;
    }

    class MyMaterialRefreshListener extends MaterialRefreshListener {

        @Override
        public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
            getDataFromNet();
        }

        @Override
        public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
            super.onRefreshLoadMore(materialRefreshLayout);
        }

    }

    /**
     * 绑定数据
     */
    @Override
    public void initData() {
        super.initData();


        // 取缓存的数据
        String saveJson = CacheUtils.getString(mContext, NETMEADIAURLPATH);
        if (!TextUtils.isEmpty(saveJson)) {
            processData(saveJson);
        }

        getDataFromNet();

    }

    private void getDataFromNet() {

        RequestParams paramas = new RequestParams(NETMEADIAURLPATH);

        x.http().get(paramas, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                // 数据缓存
                Log.e("xUtils", result);

                CacheUtils.putString(mContext, NETMEADIAURLPATH, result);
                processData(result);

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("xUtils", ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Log.e("xUtils", cex.getMessage());
            }

            @Override
            public void onFinished() {
                Log.e("xUtils", "请求数据完成");
            }
        });

    }


    /**
     * 解析Json数据
     *
     * @param result
     */
    private void processData(String result) {

        mItems = parsedJson(result);

        Log.e("333", mItems.toString());

        if (mItems != null && mItems.size() > 0) {

            tv_nodata.setVisibility(View.GONE);

            //设置适配器
            mAdapter = new MyNetMediaItemAdapter(mContext, mItems);
            lv_netvideo.setAdapter(mAdapter);

        } else {
            tv_nodata.setVisibility(View.VISIBLE);
            tv_nodata.setText("请求网络失败");
        }

        progressBar.setVisibility(View.GONE);

        // 把下拉刷新的状态还原
        netvideo_refresh.finishRefresh();
    }


    private ArrayList<NetMediaItem> parsedJson(String result) {

        ArrayList<NetMediaItem> mediasList = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("list");
            Gson gson = new Gson();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                NetMediaItem item = gson.fromJson(obj.toString(), NetMediaItem.class);
                mediasList.add(item);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


        return mediasList;
    }

}





















