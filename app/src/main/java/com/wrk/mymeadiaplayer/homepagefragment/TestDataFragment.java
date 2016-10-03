package com.wrk.mymeadiaplayer.homepagefragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wrk.mymeadiaplayer.R;
import com.wrk.mymeadiaplayer.adapter.MyDataFragAdapter;
import com.wrk.mymeadiaplayer.bean.NetMedia;
import com.wrk.mymeadiaplayer.fragment.BaseFragment;
import com.wrk.mymeadiaplayer.loader.ImageLoader;
import com.wrk.mymeadiaplayer.util.DensityUtil;
import com.wrk.mymeadiaplayer.view.MyListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by MrbigW on 2016/10/3.
 * weChat:1024057635
 * Github:MrbigW
 * Usage: -.-
 * -------------------=.=------------------------
 */

public class TestDataFragment extends BaseFragment {

    //  每页最多显示的数目
    private final static int SHOW_ITEM_MAX = 8;

    private static final String URLPATH = "http://api.m.mtime.cn/PageSubArea/TrailerList.api";


    public static final int MAX = Integer.MAX_VALUE;

    private static int pos;


    // 图片资源ID
    private final int[] imageIds = {
            R.drawable.a,
            R.drawable.b,
            R.drawable.c,
            R.drawable.d,
            R.drawable.e};


    // 图片标题集合
    private final String[] imageDescriptions = {
            "尚硅谷拔河争霸赛！",
            "凝聚你我，放飞梦想！",
            "抱歉没座位了！",
            "7月就业名单全部曝光！",
            "平均起薪11345元"
    };


    //  上一次高亮显示的位置
    private int lastPosition;

    //  判断当前Activity是否销毁
    private boolean isFragmentDestroyView = false;

    //  判断是否为拖拽状态
    private boolean isDragging = false;

    //  使用Handler机制完成自动滑动
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            int item = vp_frg_image.getCurrentItem() + 1;
            vp_frg_image.setCurrentItem(item);

            //利用自己给自己传消息来完成viewpager的循环滚动

            handler.sendEmptyMessageDelayed(0, 3000);


        }
    };

    private Context mContext;

    private ViewPager vp_frg_image;
    private TextView tv_main_title;
    private LinearLayout ll_main_point;
    private MyListView lv_frg_data;
    private ArrayList<ImageView> mImageViews;
    private ImageLoader mImageLoader;

    private MyDataFragAdapter adapter;

    private List<NetMedia> mNetMedias;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        Log.e("000", "onCreate");

    }

    @Override
    public View initView() {
        Log.e("444", pos + "");
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.testdatafragment_layout, null);

        vp_frg_image = (ViewPager) view.findViewById(R.id.vp_frg_image);
        tv_main_title = (TextView) view.findViewById(R.id.tv_main_title);
        ll_main_point = (LinearLayout) view.findViewById(R.id.ll_main_point);
        lv_frg_data = (MyListView) view.findViewById(R.id.lv_frg_data);
        mImageLoader = new ImageLoader(mContext);
        mNetMedias = new ArrayList<>();
        mImageViews = new ArrayList<>();

        return view;
    }

    @Override
    public void initData() {
        super.initData();

        // 下载数据
        startAsyncTask();


    }

    /**
     * 开启异步任务加载数据
     */
    private void startAsyncTask() {
        new AsyncTask<String, Void, List<NetMedia>>() {


            @Override
            protected List<NetMedia> doInBackground(String... params) {
                return getPortJsonData(params[0]);
            }

            @Override
            protected void onPostExecute(List<NetMedia> medias) {
                super.onPostExecute(medias);
                mNetMedias = medias;
                Log.e("222", medias.toString());


                for (int i = 0; i < 5; i++) {
                    String url = mNetMedias.get(i).getCoverImg();
                    ImageView imag = new ImageView(mContext);
                    imag.setTag(url);
                    mImageLoader.showImageByAsyncTask(imag, mNetMedias.get(i).getCoverImg());

                    mImageViews.add(imag);


                    //  添加指示点，有多少个页面就添加多少个
                    ImageView point = new ImageView(mContext);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            DensityUtil.dip2px(mContext, 8), DensityUtil.dip2px(mContext, 8));

                    point.setBackgroundResource(R.drawable.point_selector);

                    params.leftMargin = DensityUtil.dip2px(mContext, 10);
                    point.setEnabled(false);


                    point.setLayoutParams(params);
                    ll_main_point.addView(point);
                }

                tv_main_title.setText(mNetMedias.get(0).getMovieName());
                vp_frg_image.addOnPageChangeListener(new MyonPageChangeListener());

                //  设置一开始就可以向左滑动
                int item = MAX / 2 - (MAX / 2) % mImageViews.size();
                vp_frg_image.setCurrentItem(item); // 该方法是设置viewpager显示的位置
                vp_frg_image.setAdapter(new MyPagerAdapter());
                ll_main_point.getChildAt(pos).setEnabled(true);
                //  利用handler开始循环滑动
                if (!isFragmentDestroyView) {
                    ll_main_point.getChildAt(0).setEnabled(true);
                } else {
                    ll_main_point.getChildAt(pos).setEnabled(false);
                }
                handler.sendEmptyMessageDelayed(0, 3000);

//                adapter = new MyDataFragAdapter(mContext, mNetMedias);

//                lv_frg_data.setAdapter(adapter);
                showListView(mNetMedias);
            }

        }.execute(URLPATH);

    }


    class MyonPageChangeListener implements ViewPager.OnPageChangeListener {

        /**
         * 当页面滚动了的时候回调这个方法(必须掌握)
         *
         * @param position             滚动页面的位置
         * @param positionOffset       当前滑动页面的百分比，例如滑动一半是0.5
         * @param positionOffsetPixels 当前页面滑动的像素
         */
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        /**
         * 当页面改变了的时候回调这个方法
         *
         * @param position 当前被选中页面的位置
         */
        @Override
        public void onPageSelected(int position) {
            int nowPosition = position % mImageViews.size();
            pos = nowPosition;
//            String title = imageDescriptions[nowPosition];
            String title = mNetMedias.get(nowPosition).getMovieName();
            tv_main_title.setText(title);

            ll_main_point.getChildAt(nowPosition).setEnabled(true);
            ll_main_point.getChildAt(lastPosition).setEnabled(false);


            lastPosition = nowPosition;
        }

        /**
         * 当页面状态发送变化的时候回调这个方法
         * 静止到滑动
         * 滑动到静止
         *
         * @param state
         */
        @Override
        public void onPageScrollStateChanged(int state) {
            //  如果当前是拖拽状态
            if (state == ViewPager.SCROLL_STATE_DRAGGING) {
                isDragging = true;
                handler.removeMessages(0);
            } else if (state == ViewPager.SCROLL_STATE_IDLE) {
                //  如果当前是空闲状态
            } else if (state == ViewPager.SCROLL_STATE_SETTLING && isDragging) {
                //  如果当前是滑动状态
                isDragging = false;
                handler.removeMessages(0);
                handler.sendEmptyMessageDelayed(0, 3000);
            }
        }
    }

    class MyPagerAdapter extends PagerAdapter {

        /**
         * 销毁
         *
         * @param container
         * @param position
         * @param object
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
//            super.destroyItem(container, position, object);  该语句必须删除
            container.removeView((View) object);
        }

        /**
         * 作用类似于getView();
         * 把页面添加到ViewPager中
         * 并且返回当前页面的相关的特性
         * container:容器就是ViewPager自身
         * position：实例化当前页面的位置
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            final ImageView image = mImageViews.get(position % mImageViews.size());

            container.addView(image);

            //  对imageview进行触摸和点击监听
            image.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            handler.removeMessages(0);
                            break;
                        case MotionEvent.ACTION_UP:
                            handler.sendEmptyMessageDelayed(0, 3000);
                            break;
                    }
                    return false;
                }
            });

            image.setTag(position);
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, mNetMedias.get(((int) image.getTag()) % mImageViews.size()).getMovieName(), Toast.LENGTH_SHORT).show();
                }
            });
            return image;
        }

        /**
         * 返回总条数
         *
         * @return
         */
        @Override
        public int getCount() {
            return MAX;
        }

        /**
         * view和object比较是否是同一个View
         * 如果相同返回true
         * 不相同返回false
         * object:其实就是instantiateItem()方法返回的值
         */
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

    @Override
    public void onPause() {
        Log.e("000", "onPause");
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.e("000", "onStop");
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        Log.e("000", "onDestroyView");
        isFragmentDestroyView = true;
        handler.removeCallbacksAndMessages(null);
        super.onDestroyView();
    }


    public void showListView(List<NetMedia> list) {
        if (adapter == null) {
            lv_frg_data.setInterface(new MyListView.ILoadListenner() {
                @Override
                public void onLoad() {
                    new AsyncTask<String, Void, List<NetMedia>>() {
                        @Override
                        protected List<NetMedia> doInBackground(String... params) {
                            SystemClock.sleep(1000);
                            return getPortJsonData(params[0]);
                        }

                        @Override
                        protected void onPostExecute(List<NetMedia> medias) {
                            super.onPostExecute(medias);
                            showListView(medias);
                            lv_frg_data.loadComplete();
                        }
                    }.execute(URLPATH);
                }

                @Override
                public void onReflash() {
                    new AsyncTask<String, Void, List<NetMedia>>() {

                        @Override
                        protected List<NetMedia> doInBackground(String... params) {
                            tmp = 0;
                            SystemClock.sleep(1000);
                            return getPortJsonData(params[0]);
                        }

                        @Override
                        protected void onPostExecute(List<NetMedia> medias) {
                            super.onPostExecute(medias);
                            showListView(medias);
                            lv_frg_data.reflashComplete();
                        }
                    }.execute(URLPATH);
                }
            });
            adapter = new MyDataFragAdapter(mContext, list);
            lv_frg_data.setAdapter(adapter);
        } else {
            adapter.onDataChange(list);
        }
    }

    /**
     * 获取最大item数目的Json数据
     *
     * @param Url
     * @return
     */
    int tmp = 0;

    private List<NetMedia> getPortJsonData(String Url) {
        List<NetMedia> newsList = new ArrayList<>();
        String jsonString = null;
        try {
            jsonString = readStream(new URL(Url).openStream());
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObject.getJSONArray("trailers");
            if (SHOW_ITEM_MAX > jsonArray.length()) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    getNewsList(newsList, jsonArray, jsonObject, i);
                }
            } else {
                if (tmp + SHOW_ITEM_MAX >= jsonArray.length()) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        getNewsList(newsList, jsonArray, jsonObject, i);
                    }
                } else {
                    for (int i = 0; i < tmp + SHOW_ITEM_MAX; i++) {
                        getNewsList(newsList, jsonArray, jsonObject, i);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        tmp += SHOW_ITEM_MAX;
        return newsList;
    }

    /**
     * 为newsList赋值
     *
     * @param newsList
     * @param jsonArray
     * @param jsonObject
     * @param count
     */
    private void getNewsList(List<NetMedia> newsList, JSONArray jsonArray, JSONObject jsonObject, int count) {
        try {
            jsonObject = jsonArray.getJSONObject(count);
            Log.e("222", jsonObject.toString());
            NetMedia netMedia = new NetMedia();
            netMedia.setCoverImg(jsonObject.getString("coverImg"));
            netMedia.setHightUrl(jsonObject.getString("hightUrl"));
            netMedia.setId(jsonObject.getInt("id"));
            netMedia.setMovieId(jsonObject.getInt("movieId"));
            netMedia.setMovieName(jsonObject.getString("movieName"));
            netMedia.setSummary(jsonObject.getString("summary"));
            netMedia.setUrl(jsonObject.getString("url"));
            netMedia.setVideoLength(jsonObject.getInt("videoLength"));
            netMedia.setVideoTitle(jsonObject.getString("videoTitle"));
//                netMedia.setType((List<String>) jsonObject.get("type"));
            Log.e("222", netMedia.toString());
            newsList.add(netMedia);
        } catch (JSONException e) {
            e.printStackTrace();
        }
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




































