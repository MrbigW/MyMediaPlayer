package com.wrk.mymeadiaplayer.view;

import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wrk.mymeadiaplayer.R;

import java.util.Date;

/**
 * Created by Mr.W on 2016/9/9.
 */
public class MyListView extends ListView implements AbsListView.OnScrollListener {

    //  下拉部分
    View header;    //  顶部布局文件
    View headerViewPager;
    int headerHeight;   // 顶部布局文件的高度
    int firstVisibleItem;   //  当前第一个可见的item的位置

    int scrollState;    //  listview当前滚动状态

    boolean isRemark;// 标记，当前是在listview的最顶端摁下的；
    int startY;//   摁下时开始的Y值

    int state;//    当前的状态
    final int NONE = 0;//   正常状态
    final int PULL = 1;//   下拉状态
    final int RELESE = 2;//   释放状态
    final int REFLASHING = 3;//   正在刷新状态


    //  分页部分
    private View footer;   //   底部布局文件


    int totalItemCount;     //  总数量;
    int lastVisibleItem;    //  最后一个可见的item

    boolean isLoading;  //正在加载

    //设置监听
    private ILoadListenner iLoadListenner;


    public MyListView(Context context) {
        super(context);
        initView(context);
    }

    public MyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public MyListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }


    /**
     * 添加底部加载提示到listview
     *
     * @param context
     */
    private void initView(Context context) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        footer = layoutInflater.inflate(R.layout.footer_layout, null);
        header = layoutInflater.inflate(R.layout.header_layout, null);
        headerViewPager = layoutInflater.inflate(R.layout.header_viewpager_layout, null);
        footer.findViewById(R.id.load_layout).setVisibility(GONE);

        //  header
        //  测量header的宽和高
        measureView(header);

        //  得到header的高
        headerHeight = header.getMeasuredHeight();
        topPadding(-headerHeight);

        this.addFooterView(footer);
        this.addHeaderView(header);
        this.addHeaderView(headerViewPager);
        this.setOnScrollListener(this);
    }

    /**
     * 通知父布局，header占用宽，高
     *
     * @param view
     */
    private void measureView(View view) {
        //  得到view的布局宽高
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (params == null) {
            params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        //ViewGroup.getChildMeasureSpec(int spec,int padding,int childDimension) 1.父view的详细尺寸 2.view当前尺寸的下的边距 3.child在当前尺寸下的宽（高）
        int width = ViewGroup.getChildMeasureSpec(0, 0, params.width);
        int height;
        int tempHeight = params.height;
        if (tempHeight > 0) {
            height = MeasureSpec.makeMeasureSpec(tempHeight, MeasureSpec.EXACTLY);
        } else {
            height = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        }
        view.measure(width, height);
    }

    /**
     * 设置header布局的上边距
     *
     * @param toPadding
     */
    private void topPadding(int toPadding) {
        header.setPadding(header.getPaddingLeft(), toPadding, header.getPaddingRight(), header.getPaddingBottom());
        // 重绘
        header.invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (firstVisibleItem == 0) {
                    isRemark = true;
                    startY = (int) ev.getY();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                onMOve(ev);
                break;
            case MotionEvent.ACTION_UP:
                if (state == RELESE) {
                    state = REFLASHING;
                    //  加载最新数据
                    reflashViewByState();
                    iLoadListenner.onReflash();
                } else if (state == PULL) {
                    state = NONE;
                    isRemark = false;
                    reflashViewByState();
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 在header操作中，判断移动过程操作
     *
     * @param ev
     */
    private void onMOve(MotionEvent ev) {
        if (!isRemark) {
            return;
        }
        // 得到此时Y坐标
        int tempY = (int) ev.getY();
        //  记录滑动距离
        int space = tempY - startY;
        //  比较滑动距离和header的高
        int topPadding = space - headerHeight;
        switch (state) {
            case NONE:
                if (space > 0) {
                    state = PULL;
                    reflashViewByState();
                }
                break;
            case PULL:
                if (space > headerHeight) {
                    topPadding(50);
                } else {
                    topPadding(topPadding);
                }
                if (space > headerHeight + 30 && scrollState == SCROLL_STATE_TOUCH_SCROLL) {
                    state = RELESE;
                    reflashViewByState();
                }
                break;
            case RELESE:
                topPadding(50);
                if (space < headerHeight + 30) {
                    state = PULL;
                    reflashViewByState();
                } else if (space <= 0) {
                    startY = NONE;
                    isRemark = false;
                    reflashViewByState();
                }
                break;
            case REFLASHING:
                break;
        }
    }

    /**
     * 在header中，根据当前状态，改变界面显示
     */
    private void reflashViewByState() {
        TextView tip = (TextView) header.findViewById(R.id.tip);
        ImageView arrow = (ImageView) header.findViewById(R.id.arrow);
        ProgressBar progress = (ProgressBar) header.findViewById(R.id.progress);

        //  箭头的动画
        RotateAnimation up = new RotateAnimation(0, 180, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        up.setDuration(500);
        up.setFillAfter(true);
        RotateAnimation down = new RotateAnimation(180, 0, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        down.setDuration(500);
        down.setFillAfter(true);
        switch (state) {
            case NONE:
                topPadding(-headerHeight);
                arrow.clearAnimation();
                break;
            case PULL:
                arrow.setVisibility(VISIBLE);
                progress.setVisibility(GONE);
                tip.setText("下拉可以刷新~~");
                arrow.clearAnimation();
                break;
            case RELESE:
                arrow.setVisibility(VISIBLE);
                progress.setVisibility(GONE);
                tip.setText("松开可以刷新~~");
                arrow.clearAnimation();
                arrow.setAnimation(up);
                break;
            case REFLASHING:
                topPadding(50);
                arrow.setVisibility(GONE);
                progress.setVisibility(VISIBLE);
                tip.setText("正在刷新~~");
                arrow.clearAnimation();
                break;
        }
    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        this.scrollState = scrollState;
        if (totalItemCount == lastVisibleItem && scrollState == SCROLL_STATE_IDLE) {
            if (!isLoading) {
                isLoading = true;
                footer.findViewById(R.id.load_layout).setVisibility(VISIBLE);
                //  加载更多
                iLoadListenner.onLoad();
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        this.firstVisibleItem = firstVisibleItem;
        this.lastVisibleItem = firstVisibleItem + visibleItemCount;
        this.totalItemCount = totalItemCount;
    }


    /**
     * footer获取完数据
     */
    public void loadComplete() {
        isLoading = false;
        footer.findViewById(R.id.load_layout).setVisibility(GONE);
    }

    /**
     * header获取完数据
     */
    public void reflashComplete() {
        state = NONE;
        isRemark = false;
        reflashViewByState();
        TextView lastupdateTime = (TextView) header.findViewById(R.id.lastupdate_time);
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 hh:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        String time = format.format(date);
        lastupdateTime.setText(time);
        lastupdateTime.setVisibility(VISIBLE);

    }

    public void loadFinish() {
        isLoading = true;
        footer.findViewById(R.id.load_layout).setVisibility(GONE);
    }

    public void setInterface(ILoadListenner iLoadListenner) {
        this.iLoadListenner = iLoadListenner;
    }

    //  加载更多数据的回调接口
    public interface ILoadListenner {
        void onLoad();

        void onReflash();
    }

}
