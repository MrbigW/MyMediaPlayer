package com.wrk.mymeadiaplayer.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.wrk.mymeadiaplayer.R;
import com.wrk.mymeadiaplayer.bean.NetMedia;
import com.wrk.mymeadiaplayer.homepagefragment.HomeDataFragment;
import com.wrk.mymeadiaplayer.util.SearchALG;
import com.wrk.mymeadiaplayer.view.MySearchView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends Activity {

    private MySearchView mSearchView;
    private SearchALG mALG;


    private List<String> changedHintDatas;
    //热搜数据
    private List<String> hot_datas;
    //提示列表数据
    private List<String> hint_datas;

    private List<NetMedia> mNetMedias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mSearchView = (MySearchView) findViewById(R.id.searchView);
        initData();
        mSearchView.setOnSearchListener(new MyOnSearchListener());
    }


    /**
     * 设置searview的监听
     */
    class MyOnSearchListener implements MySearchView.OnSearchListener {

        /**
         * 搜索回调
         *
         * @param searchText 进行搜索的文本
         */
        @Override
        public void onSearch(String searchText) {
            if (!TextUtils.isEmpty(searchText)) {
                Intent intent = new Intent(SearchActivity.this, SystemPlayerActivity.class);

                for (int i = 0; i < mNetMedias.size(); i++) {
                    if (mNetMedias.get(i).getVideoTitle().equals(searchText)) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("netmedialist", (Serializable) mNetMedias);
                        intent.putExtras(bundle);
                        intent.setData(Uri.parse(mNetMedias.get(i).getUrl()));
                        intent.putExtra("name", mNetMedias.get(i).getVideoTitle());
                        intent.putExtra("id", mNetMedias.get(i).getId());
                        SearchActivity.this.startActivity(intent);
                    }
                }
            } else {
                Toast.makeText(SearchActivity.this, "搜索内容不能为空！", Toast.LENGTH_SHORT).show();
            }
        }

        /**
         * 刷新提示列表
         *
         * @param changedText 改变后的文本
         */
        @Override
        public void onRefreshHintList(String changedText) {
            if (changedHintDatas == null) {
                changedHintDatas = new ArrayList<>();
            } else {
                changedHintDatas.clear();
            }
            if (TextUtils.isEmpty(changedText)) {
                return;
            }
            for (int i = 0; i < hint_datas.size(); i++) {
                String hint_data = hint_datas.get(i);
                boolean isAdd = mALG.isAddToHintList(hint_data, changedText);
                if (isAdd) {
                    changedHintDatas.add(hint_datas.get(i));
                }
            }

            /**
             * 根据搜索框文本的变化，动态的改变提示的listView
             */
            mSearchView.updateHintList(changedHintDatas);

        }
    }


    private void initData() {
        hot_datas = new ArrayList<>();
        hint_datas = new ArrayList<>();

        mALG = new SearchALG(this);

        startAsyncTask();
    }

    /**
     * 开启异步任务加载数据
     */
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

                for (int i = 0; i < 10; i++) {
                    hot_datas.add(mNetMedias.get(i).getVideoTitle());
                }

                //设置热搜数据显示的列数
                mSearchView.setHotNumColumns(2);
                //设置热搜数据
                mSearchView.setHotSearchDatas(hot_datas);


                /**
                 * 设置提示数据的集合
                 */
                for (int i = 0; i < mNetMedias.size(); i++) {
                    hint_datas.add(mNetMedias.get(i).getVideoTitle());
                }
                //设置提示列表的最大显示列数
                mSearchView.setMaxHintLines(8);


                /**
                 * 设置自动保存搜索记录
                 */
                mSearchView.keepSearchHistory(true);

                //设置保存搜索记录的个数
                mSearchView.setMaxHistoryRecordCount(6);

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
