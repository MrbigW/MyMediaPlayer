package com.wrk.mymeadiaplayer.homepagefragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.wrk.mymeadiaplayer.R;
import com.wrk.mymeadiaplayer.activity.MainActivity;
import com.wrk.mymeadiaplayer.adapter.MyRecyDataFragAdapter;
import com.wrk.mymeadiaplayer.bean.NetMedia;
import com.wrk.mymeadiaplayer.fragment.BaseFragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by MrbigW on 2016/10/6.
 * weChat:1024057635
 * Github:MrbigW
 * Usage: -.-
 * -------------------=.=------------------------
 */

public class RecyDataFragment extends BaseFragment {

    private List<NetMedia> mNetMedias;

    private Context mContext;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager layoutManager;
    private MyRecyDataFragAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        mContext = getActivity();
        super.onCreate(savedInstanceState);
    }

    @Override
    public View initView() {

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.recydatafragment_layout, null);

        // 获取RecyclerView对象
        mRecyclerView = (RecyclerView) view.findViewById(R.id.base_swipe_list);

        // 创建线性布局管理器（默认垂直）
        layoutManager = new LinearLayoutManager(mContext);

        // 为RecyclerView指定布局管理对象
        mRecyclerView.setLayoutManager(layoutManager);

        return view;
    }

    @Override
    public void initData() {
        startAsyncTask();

    }

    private void startAsyncTask() {

        new AsyncTask<String, Void, List<NetMedia>>() {

            @Override
            protected List<NetMedia> doInBackground(String... params) {
                return getJsonData(params[0]);
            }

            @Override
            protected void onPostExecute(List<NetMedia> medias) {
                super.onPostExecute(medias);

                mNetMedias = medias;
//                Log.e("999", mNetMedias.toString());
                // 适配
                mAdapter = new MyRecyDataFragAdapter((MainActivity) mContext, mNetMedias);

                mRecyclerView.setAdapter(mAdapter);

            }
        }.execute(HomeDataFragment.URLPATH);
    }


    /**
     * 通过Url将返回的数据解析成json格式数据，并转换为我们所封装的NewsBean
     *
     * @param Url
     * @return
     */
    private List<NetMedia> getJsonData(String Url) {
        List<NetMedia> mediasList = new ArrayList<>();
        try {
            String jsonString = readStream(new URL(Url).openStream());

            JSONObject jsonObject = new JSONObject(jsonString);

            //  得到JsonObj对象中data的JsonArray
            JSONArray jsonArray = jsonObject.getJSONArray("trailers");

            //  得到JsonArray中的数据并将其赋给NewsBean对象
            NetMedia netMedia;
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);
                netMedia = new NetMedia();
                netMedia.setCoverImg(jsonObject.getString("coverImg"));
                netMedia.setHightUrl(jsonObject.getString("hightUrl"));
                netMedia.setId(jsonObject.getInt("id"));
                netMedia.setMovieId(jsonObject.getInt("movieId"));
                netMedia.setMovieName(jsonObject.getString("movieName"));
                netMedia.setSummary(jsonObject.getString("summary"));
                netMedia.setUrl(jsonObject.getString("url"));
                netMedia.setVideoLength(jsonObject.getInt("videoLength"));
                netMedia.setVideoTitle(jsonObject.getString("videoTitle"));
//                netMedia.setType((List<String>) jsonObject.getJSONArray("type"));
//                Log.e("999", String.valueOf(jsonObject.getJSONArray("type")));
                JSONArray typeArray = jsonObject.getJSONArray("type");
                List<String> types = new ArrayList<>();
                for (int j = 0; j < typeArray.length(); j++) {
                    types.add(typeArray.getString(j));
                }
                netMedia.setType(types);
//                Log.e("999", netMedia.toString());
                mediasList.add(netMedia);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mediasList;
    }

    /**
     * 将从服务器得到的输入流传进来读取
     *
     * @param is
     * @return
     */
    private String readStream(InputStream is) {
        InputStreamReader isr = null;
        StringBuilder sb = new StringBuilder("");
        BufferedReader br = null;
        String line = "";
        try {
            isr = new InputStreamReader(is, "UTF-8");
            br = new BufferedReader(isr);
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (isr != null) {
                try {
                    isr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();
    }

}



























































