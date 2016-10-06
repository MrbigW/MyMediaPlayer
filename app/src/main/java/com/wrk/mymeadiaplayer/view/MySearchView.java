package com.wrk.mymeadiaplayer.view;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.wrk.mymeadiaplayer.R;
import com.wrk.mymeadiaplayer.adapter.CommonAdapter;
import com.wrk.mymeadiaplayer.adapter.HintAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MrbigW on 2016/10/6.
 * weChat:1024057635
 * Github:MrbigW
 * Usage: 自定义搜索View
 * -------------------=.=------------------------
 */

public class MySearchView extends LinearLayout {

    private static final int HOT_ITEM = 1;  // 热搜数目
    private static final int HINT_ITEM = 2; // 提示数目
    private static final int HISTORY_ITEM = 3; // 历史数目
    private Context mContext;

    // 返回键
    private ImageView iv_search_back;
    // 输入的文本框
    private EditText et_search_content;
    // 清空搜索内容
    private ImageView iv_search_clear;
    // 搜索的按钮
    private TextView tv_search_search;
    // 提示的列表
    private ListView lv_search_hint;
    // 热搜的列表
    private NoScrollGridView gv_search_hot;
    // 历史的列表
    private NoScrollGridView gv_search_history;
    //  三个线性布局
    private LinearLayout ll_item_delete;
    private LinearLayout ll_search_history;
    private LinearLayout ll_search_hot;

    // 存放历史记录的sp
    private SharedPreferences sp;

    public MySearchView(Context context) {
        super(context);
        this.mContext = context;
        LayoutInflater.from(mContext).inflate(R.layout.search_view, this);
        initView();
    }

    public MySearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        LayoutInflater.from(mContext).inflate(R.layout.search_view, this);
        initView();
    }

    private void initView() {
        iv_search_back = (ImageView) findViewById(R.id.iv_search_back);
        et_search_content = (EditText) findViewById(R.id.et_search_content);
        iv_search_clear = (ImageView) findViewById(R.id.iv_search_clear);
        tv_search_search = (TextView) findViewById(R.id.tv_search_search);
        lv_search_hint = (ListView) findViewById(R.id.lv_search_hint);
        gv_search_hot = (NoScrollGridView) findViewById(R.id.gv_search_hot);
        gv_search_history = (NoScrollGridView) findViewById(R.id.gv_search_history);
        ll_search_history = (LinearLayout) findViewById(R.id.ll_search_history);
        ll_search_hot = (LinearLayout) findViewById(R.id.ll_search_hot);
        ll_item_delete = (LinearLayout) findViewById(R.id.ll_item_delete);

        /**
         * 用SP存储保存搜索的历史纪录
         */
        if (sp == null) {
            sp = mContext.getSharedPreferences("search_history", Context.MODE_PRIVATE);
        }

        setListener();
    }

    private void setListener() {
        iv_search_back.setOnClickListener(new SearchOnClickListener());
        iv_search_clear.setOnClickListener(new SearchOnClickListener());
        tv_search_search.setOnClickListener(new SearchOnClickListener());
        et_search_content.addTextChangedListener(new SearchTextChangedListener());
        /**
         * 热搜列表的Item点击监听
         */
        gv_search_hot.setOnItemClickListener(new listItemOnClickListener(HOT_ITEM));
        /**
         * 历史纪录的Item点击监听
         */
        gv_search_history.setOnItemClickListener(new listItemOnClickListener(HISTORY_ITEM));
        /**
         * 提示列表的Item点击监听
         */
        lv_search_hint.setOnItemClickListener(new listItemOnClickListener(HINT_ITEM));

        /**
         * 监听软键盘的搜索按钮
         */
        et_search_content.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    startSearch(et_search_content.getText().toString());
                }
                return true;
            }
        });

        ll_item_delete.setOnClickListener(new SearchOnClickListener());

    }


    /**
     * 单击事件的监听
     */
    private class SearchOnClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_search_back:
                    ((Activity) mContext).finish();
                    break;
                case R.id.iv_search_clear:
                    et_search_content.setText(null);
                    break;
                case R.id.tv_search_search:
                    startSearch(et_search_content.getText().toString().trim());
                    break;
                case R.id.ll_item_delete:
                    clearHistoryRecord();
                    break;

            }
        }
    }

    // 历史记录列表的adapter;
    private CommonAdapter historyAdapter;
    private boolean isAutoKeep = false; // 默认不保存
    private List<String> historyDatas;
    private int maxHistoryRecordCount = 6; // 设置最大的历史保存记录为6


    /**
     * 设置最大的历史纪录数，默认为6条
     *
     * @param maxHistoryRecordCount
     */
    public void setMaxHistoryRecordCount(int maxHistoryRecordCount) {
        this.maxHistoryRecordCount = maxHistoryRecordCount;
    }

    /**
     * 保存历史记录
     *
     * @param text
     */
    private void keepSearchHistory(String text) {
        if (historyDatas == null) {
            historyDatas = new ArrayList<>(maxHistoryRecordCount);
            historyDatas.add(0, text);
        } else {
            for (int i = 0; i < historyDatas.size(); i++) {
                if (historyDatas.get(i).equals(text)) {
                    historyDatas.remove(i);
                }
            }

            // 如果超过最大值就移除最后一条
            if (historyDatas.size() >= maxHistoryRecordCount) {
                historyDatas.remove(maxHistoryRecordCount - 1);
            }
            // 将此条历史记录添加首条
            historyDatas.add(0, text);

            // 保存历史记录到sp中
            sp.edit().putString("history_search_datas", new Gson().toJson(historyDatas))
                    .commit();
        }

        if (historyAdapter == null) {
            historyAdapter = new CommonAdapter(mContext, historyDatas);
            gv_search_history.setAdapter(historyAdapter);
        } else {
            historyAdapter.updateRecordList(historyDatas);
        }

    }

    /**
     * 设置是否自动保存搜索记录
     *
     * @param isAutoKeep
     */
    public void keepSearchHistory(boolean isAutoKeep) {
        this.isAutoKeep = isAutoKeep;

        // 如果没有设为自动保存，则返回
        if (!isAutoKeep) {
            return;
        }

        String history_Datas = sp.getString("history_search_datas", "");
        if (!TextUtils.isEmpty(history_Datas)) {
            historyDatas = new Gson().fromJson(history_Datas, List.class);
        } else { // sp中无数据直接返回
            return;
        }
        if (historyAdapter == null && isAutoKeep) {
            ll_search_history.setVisibility(VISIBLE);
            historyAdapter = new CommonAdapter(mContext, historyDatas);
            gv_search_history.setAdapter(historyAdapter);
        }

    }


    /**
     * 热门搜索的Adapter
     */
    private CommonAdapter hotAdapter;
    private List<String> hotSearchDatas;
    private int hotNumColumns = 2;


    /**
     * 设置热搜数据的显示列数，默认为2列
     * 需要在setHotSearchDatas()前设置
     *
     * @param hotNumColumns
     */
    public void setHotNumColumns(int hotNumColumns) {
        this.hotNumColumns = hotNumColumns;
    }


    public void setHotSearchDatas(List<String> hotSearchDatas) {
        this.hotSearchDatas = hotSearchDatas;
        if (hotSearchDatas != null && hotSearchDatas.size() > 0) {
            ll_search_hot.setVisibility(VISIBLE);
            hotAdapter = new CommonAdapter(mContext, hotSearchDatas);
            gv_search_hot.setNumColumns(hotNumColumns);
            gv_search_hot.setAdapter(hotAdapter);
        } else {
            ll_search_hot.setVisibility(GONE);
        }
    }


    /**
     * 提示的listView的Adapter
     */
    private HintAdapter hintAdapter;
    private List<String> hintDatas;
    private int maxHintLines = -1;


    /**
     * 设置提示的最大显示行数,默认显示所有
     *
     * @param maxHintLines
     */
    public void setMaxHintLines(int maxHintLines) {
        this.maxHintLines = maxHintLines;
    }


    public void updateHintList(List<String> hintDatas) {
        this.hintDatas = hintDatas;
        if (hintDatas != null && hintAdapter == null) {
            hintAdapter = new HintAdapter(mContext, hintDatas, maxHintLines);
            lv_search_hint.setAdapter(hintAdapter);
        } else {
            hintAdapter.notifyRefresh(hintDatas);
        }
    }

    /**
     * 刷新热搜和提示列表的状态
     */
    private void refreshListState() {
        et_search_content.setSelection(et_search_content.getText().length());
        lv_search_hint.setVisibility(GONE);
        ll_search_hot.setVisibility(GONE);
        // 隐藏软键盘
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }


    /**
     * 清空历史纪录
     */
    private void clearHistoryRecord() {
        if (historyDatas != null && historyAdapter != null) {
            historyDatas.clear();//清空
            historyAdapter.updateRecordList(historyDatas);
            ll_search_history.setVisibility(GONE);
            //删除SP存储的历史纪录
            sp.edit().putString("history_search_datas", "").commit();
        }
    }

    private void startSearch(String text) {
        if (onSearchListener != null) {
            onSearchListener.onSearch(text);
            refreshListState();
            if(isAutoKeep) {
                keepSearchHistory(text);
            }
        }
    }

    private class listItemOnClickListener implements AdapterView.OnItemClickListener {

        private int tag;
        private List<String> datas;

        public listItemOnClickListener(int tag) {
            this.tag = tag;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            switch (tag) {
                case HOT_ITEM:
                    datas = hotSearchDatas;
                    break;
                case HINT_ITEM:
                    datas = hintDatas;
                    break;
                case HISTORY_ITEM:
                    datas = historyDatas;
                    break;
            }

            String item = datas.get(position);
            et_search_content.setText(item);
            startSearch(item);
        }
    }

    class SearchTextChangedListener implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!TextUtils.isEmpty(s.toString().trim())) { // 当s不为空时
                ll_search_hot.setVisibility(GONE);
                ll_search_history.setVisibility(GONE);
                iv_search_clear.setVisibility(VISIBLE);//显示清除搜索框内容的按钮
                lv_search_hint.setVisibility(VISIBLE);
            } else {
                iv_search_clear.setVisibility(GONE);
                lv_search_hint.setVisibility(GONE);
                if (hotSearchDatas != null && hotSearchDatas.size() > 0) {
                    ll_search_hot.setVisibility(VISIBLE);
                }
                if (historyAdapter != null && historyDatas.size() > 0 && isAutoKeep) {
                    ll_search_history.setVisibility(VISIBLE);
                }
            }

            if (onSearchListener != null) {
                onSearchListener.onRefreshHintList(s.toString());
            }

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    /**
     * 自定义SearchView的监听
     */

    private OnSearchListener onSearchListener;

    public void setOnSearchListener(OnSearchListener listener) {
        this.onSearchListener = listener;
    }

    public interface OnSearchListener {
        /**
         * 当进行搜索时回调此方法
         *
         * @param searchText 进行搜索的文本
         */
        void onSearch(String searchText);

        /**
         * 当输入框文本变化，需刷新提示的ListView时调用
         *
         * @param changedText 改变后的文本
         */
        void onRefreshHintList(String changedText);
    }


}










































































