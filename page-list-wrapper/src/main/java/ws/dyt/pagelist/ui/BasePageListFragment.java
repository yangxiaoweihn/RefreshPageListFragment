package ws.dyt.pagelist.ui;


import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.CallSuper;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

import ws.dyt.pagelist.utils.ViewInject;
import ws.dyt.view.adapter.MultiAdapter;
import ws.dyt.pagelist.R;


/**
 * @param <T_RESPONSE>   服务端返回的数组数据，用来真实分页的数据
 * @param <T_ADAPTER>   适配器装载的数据（有可能真实的数据里会添加一些别的数据进去显示）
 */
abstract
public class BasePageListFragment<T_RESPONSE, T_ADAPTER> extends BasePageFragment implements ResponseResultDelegate<T_RESPONSE, T_ADAPTER>, DataStatusDelegate{
    private static final String TAG = "lib_BaseListFragment";

    protected RecyclerView recyclerView;
    protected SwipeRefreshLayout refreshLayout;
    protected MultiAdapter<T_ADAPTER> adapter;
    protected View rootView;
    private FrameLayout sectionEmptyView;
    private ImageView ivEmpty;
    private TextView tvEmtpy;

    /*-----------------------------------------------------------------------*/
    /*-----------------------------------------------------------------------*/
    abstract
    protected RecyclerView.LayoutManager setLayoutManager();

    abstract
    protected MultiAdapter<T_ADAPTER> setAdapter();

    /**
     * 列表数据转换为适配器数据
     * @param datas
     * @return
     */
    abstract
    protected List<T_ADAPTER> convertData(List<T_RESPONSE> datas);

    /**
     * 设置每次请求数据条数
     * @return
     */
    abstract
    protected int setPageSize();

    /**
     * 请求加载数据
     * @param index
     */
    abstract
    protected void fetchData(int index);
    /*-----------------------------------------------------------------------*/
    /*-----------------------------------------------------------------------*/

    /*< < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < <*/
    /**
     * 空白页面信息
     */
    public static class EmptyStatusViewWrapper {
        //空白页面布局
        @LayoutRes
        public int LayoutResOfEmptyView = R.layout.rll_view_refresh_list_empty;

        //空白页面中图标
        @DrawableRes
        public int DrawableResOfEmpty;
        //空白页面中文字提示
        @StringRes
        public int TextResOfEmpty = R.string.rll_tips_data_empty;

        //初始加载显示时图标[可以是一个动画]
        @DrawableRes
        public int DrawableResOfInitLoading;
        //初始加载显示时文字提示
        @StringRes
        public int TextResOfInitLoading = R.string.rll_tips_data_initloading;

        //网路异常时图标[无连接或者服务器状态非0,初始无数据时才会显示]
        @DrawableRes
        public int DrawableResOfException;
        //网络异常时文字提示
        @StringRes
        public int TextResOfException = R.string.rll_tips_data_exception;

        //初始加载是是否显示无数据的空白页面
        public boolean IsShowEmptyViewBeforeInitLoading = false;
    }

    private EmptyStatusViewWrapper emptyStatusViewWrapper = new EmptyStatusViewWrapper();

    /**
     * 设置没有数据时的信息
     * @param wrapper
     */
    public void onConfigEmptyStatusViewInfo(EmptyStatusViewWrapper wrapper) {}
    /*> > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > >*/


    /*< < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < <*/
    /**
     * 加载更多状态信息
     */
    public static class LoadMoreStatusViewWrapper {
        //加载状态布局[作为recyclerview的footer]
        public @LayoutRes int LayoutResOfLoadMoreStatusView = R.layout.rll_view_loadmore_footer;
        //加载状态提示信息[总共3种状态，必须]
        public String[] TextTipsOfLoadingStatus;
        //所有数据加载完毕时是否显示加载完毕状态
        public boolean IsShowStatusWhenAllDataDidLoad = false;
        //下拉刷新时是否显示加载状态[true: 将会添加状态footer]
        public boolean IsShowStatusWhenRefresh = false;


        @Retention(RetentionPolicy.SOURCE)
        @IntDef({StatusWrapper.LOAD_PRE, StatusWrapper.LOAD_ING, StatusWrapper.LOAD_END})
        @interface StatusWrapperWhere {}
        public String getStatusByCode(@StatusWrapperWhere int code) {
            if (null == this.TextTipsOfLoadingStatus || this.TextTipsOfLoadingStatus.length != 3) {
                return null;
            }
            return this.TextTipsOfLoadingStatus[code];
        }
        public interface StatusWrapper {
            //开始加载前
            int LOAD_PRE = 0;
            //正在加载
            int LOAD_ING = 1;
            //所有数据全部加载
            int LOAD_END = 2;
        }
    }

    private LoadMoreStatusViewWrapper loadMoreStatusViewWrapper = new LoadMoreStatusViewWrapper();
    @CallSuper
    public void onConfigLoadMoreStatusViewInfo(LoadMoreStatusViewWrapper wrapper) {
        String[] e = getResources().getStringArray(R.array.rll_load_status);
        wrapper.TextTipsOfLoadingStatus = e;
    }
    /*> > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > >*/


    /*< < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < <*/
    public class ResponseResultWrapper<T>{
        public int StatusCode;
        public String StatusMessage;
        public List<T> Data;

        public ResponseResultWrapper(int statusCode, String statusMessage, List<T> data) {
            this.StatusCode = statusCode;
            this.StatusMessage = statusMessage;
            this.Data = data;
        }
    }
    /*> > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > >*/


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.rootView = inflater.inflate(R.layout.base_refresh_fragment, container, false);
        this.sectionEmptyView = ViewInject.findView(R.id.section_empty, rootView);
        this.recyclerView = ViewInject.findView(R.id.recyclerView, rootView);
        this.refreshLayout = ViewInject.findView(R.id.refreshLayout, rootView);

        this.onConfigLoadMoreStatusViewInfo(this.loadMoreStatusViewWrapper);
        //empty view info
        this.onConfigEmptyStatusViewInfo(this.emptyStatusViewWrapper);

        View emptyView = inflater.inflate(this.emptyStatusViewWrapper.LayoutResOfEmptyView, null, false);
        this.ivEmpty = ViewInject.findView(R.id.rll_empty_iv_id, emptyView);
        this.tvEmtpy = ViewInject.findView(R.id.rll_empty_tv_id, emptyView);
        if (this.emptyStatusViewWrapper.DrawableResOfEmpty > 0) {
            this.ivEmpty.setImageResource(this.emptyStatusViewWrapper.DrawableResOfEmpty);
        }
        if (this.emptyStatusViewWrapper.TextResOfEmpty > 0) {
            this.tvEmtpy.setText(this.emptyStatusViewWrapper.TextResOfEmpty);
        }

        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER;
        this.sectionEmptyView.addView(emptyView, lp);

        this.init();
        return rootView;
    }

    private void init() {
        this.recyclerView.setLayoutManager(this.setLayoutManager());

        this.adapter = this.setAdapter();

        this.setUpViewBeforeSetAdapter();

        this.recyclerView.setAdapter(adapter);

        this.refreshLayout.setOnRefreshListener(onRefreshListener);
        this.recyclerView.addOnScrollListener(onScrollListener);
    }

    private SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            BasePageListFragment.this.pullDown();
        }
    };

    private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            //刷新
            if (refreshLayout.isRefreshing() == false
                    && newState == RecyclerView.SCROLL_STATE_IDLE
                    && mLastVisibleItem == adapter.getItemCount() - 1
                    && isRequestEnd == true
                    && isAllDataDidLoad == false) {
                BasePageListFragment.this.pullUp();
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            RecyclerView.LayoutManager lm = recyclerView.getLayoutManager();
            if (null == lm) {
                return;
            }

            if (lm instanceof GridLayoutManager) {
                mLastVisibleItem = ((GridLayoutManager) lm).findLastVisibleItemPosition();
            } else if (lm instanceof StaggeredGridLayoutManager) {
                mLastVisibleItem = ((StaggeredGridLayoutManager) lm).findLastVisibleItemPositions(new int[1])[0];
            } else if (lm instanceof LinearLayoutManager) {
                mLastVisibleItem = ((LinearLayoutManager) lm).findLastVisibleItemPosition();
            }

        }
    };
    /**
     * 在recyclerview设置适配器之前调用
     */
    protected void setUpViewBeforeSetAdapter() {}


    private int mLastVisibleItem;

    private View footerView;
    private ProgressBar loadMoreProgressBar;
    private TextView loadMoreTextView;
    private void initLoadMoreView() {
        this.footerView = LayoutInflater.from(getContext()).inflate(this.loadMoreStatusViewWrapper.LayoutResOfLoadMoreStatusView, recyclerView, false);
        this.adapter.setSysFooterView(footerView);
        this.loadMoreProgressBar = ViewInject.findView(R.id.rll_load_more_progress_id, footerView);
        this.loadMoreTextView = ViewInject.findView(R.id.rll_load_more_tv_id, footerView);
//        this.loadMoreTextView.setText(loadMoreStatusViewWrapper.getStatusByCode(LoadMoreStatusViewWrapper.StatusWrapper.LOAD_PRE));//"加载更多"
//        if (null != loadMoreProgressBar) {
//            this.loadMoreProgressBar.setVisibility(View.GONE);
//        }

        this.loadMorePre();

        this.footerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pullUp();
            }
        });
    }


    /*-------------------------------------------------------------------------------*/
    private void loadMorePre() {
        if (null != this.footerView) {
            this.loadMoreTextView.setText(loadMoreStatusViewWrapper.getStatusByCode(LoadMoreStatusViewWrapper.StatusWrapper.LOAD_PRE));//"加载更多"
        }
        if (null != loadMoreProgressBar) {
            this.loadMoreProgressBar.setVisibility(View.GONE);
        }
    }

    private void loadMoreIng() {
        if (null != footerView) {
            this.loadMoreTextView.setText(loadMoreStatusViewWrapper.getStatusByCode(LoadMoreStatusViewWrapper.StatusWrapper.LOAD_ING));//"正在加载。。。"
        }
        if (null != footerView && null != loadMoreProgressBar) {
            this.loadMoreProgressBar.setVisibility(View.VISIBLE);
        }
    }

    private void loadMoreEndWithAllDataDidLoad() {
        if (null != footerView) {
            this.loadMoreTextView.setText(loadMoreStatusViewWrapper.getStatusByCode(LoadMoreStatusViewWrapper.StatusWrapper.LOAD_END));//"数据已全部加载完成"
        }
        if (null != loadMoreProgressBar) {
            this.loadMoreProgressBar.setVisibility(View.GONE);
        }
    }
    /*-------------------------------------------------------------------------------*/


    private void pullDown() {
        if (!isRequestEnd) {
            return;
        }
        this.emptyInitLoading();
        Log.d(TAG, "pullDown()");
        this.mVisibleLastIndex = 0;
        this.isRequestEnd = false;
        this.isAllDataDidLoad = false;
        if (this.loadMoreStatusViewWrapper.IsShowStatusWhenRefresh) {
            if (null == footerView) {
                this.initLoadMoreView();
            }
//            if (null != footerView) {
//                this.loadMoreTextView.setText(loadMoreStatusViewWrapper.getStatusByCode(LoadMoreStatusViewWrapper.StatusWrapper.LOAD_ING));//"正在加载。。。"
//            }
//            if (null != footerView && null != loadMoreProgressBar) {
//                this.loadMoreProgressBar.setVisibility(View.VISIBLE);
//            }
            this.loadMoreIng();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                fetchData(mVisibleLastIndex);
            }
        }, delayTest);
    }
    private static final int delayTest = 2000;


    private void pullUp() {
        if (isAllDataDidLoad || !isRequestEnd) {
            return;
        }
        Log.d(TAG, "pullUp()");
        this.isRequestEnd = false;

//        if (null != footerView) {
//            this.loadMoreTextView.setText(loadMoreStatusViewWrapper.getStatusByCode(LoadMoreStatusViewWrapper.StatusWrapper.LOAD_ING));//"正在加载。。。"
//        }
//        if (null != footerView && null != loadMoreProgressBar) {
//            this.loadMoreProgressBar.setVisibility(View.VISIBLE);
//        }
        this.loadMoreIng();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                fetchData(mVisibleLastIndex);
            }
        }, delayTest);
    }


    /**
     * 初次加载数据,或者强制刷新-都是重新获取数据
     */
    @Override
    final
    protected void fetchData() {
        this.refreshLayout.setRefreshing(true);
        this.pullDown();
    }

    //一次请求是否完毕
    private boolean isRequestEnd = true;
    //是否数据已经全部加载完毕
    private boolean isAllDataDidLoad = false;

    private int mVisibleLastIndex = 0;

    /**{@link ResponseResultDelegate}
     * ---------------------------------------------------------------------------*/
    @Override
    public void setOnSuccessPath(ResponseResultWrapper<T_RESPONSE> result) {
        Log.e(TAG, "setOnSuccessPath");
        this.setOnSuccessPath_(result);
    }

    @Override
    public void setOnFailurePath(){
        Log.e(TAG, "setOnFailurePath");
        this.setOnFailurePath_();
    }
    /**---------------------------------------------------------------------------*/

    private void setOnSuccessPath_(ResponseResultWrapper<T_RESPONSE> result) {
        this.isRequestEnd = true;
//        if (null != footerView) {
//            this.loadMoreTextView.setText(loadMoreStatusViewWrapper.getStatusByCode(LoadMoreStatusViewWrapper.StatusWrapper.LOAD_PRE));//"加载更多"
//        }
//        if (null != loadMoreProgressBar) {
//            this.loadMoreProgressBar.setVisibility(View.GONE);
//        }
        this.loadMorePre();

        if (null == result || result.StatusCode != 0) {
            this.refreshLayout.setRefreshing(false);

            //exception
            this.emptyException();
            return;
        }

        final List<T_RESPONSE> datas = null == result.Data ? new ArrayList<T_RESPONSE>() : result.Data;
        int count = datas.size();

        //下拉刷新
        if (0 == mVisibleLastIndex) {
            this.adapter.clear();
        }

        int dataSize = adapter.getDataSectionItemCount();
        if (0 == dataSize && 0 == count) {
            this.isAllDataDidLoad = true;
            this.adapter.removeSysFooterView(footerView);
            this.footerView = null;

            this.emptyNoData();
            this.onNoData();
        }else {
            this.sectionEmptyView.setVisibility(View.GONE);
            if (0 == mVisibleLastIndex) {
                this.adapter.notifyDataSetChanged();
            }
            if (null == footerView) {
                this.initLoadMoreView();
            }

            //数据已经全部加载完毕
            if (count < setPageSize()) {
                this.isAllDataDidLoad = true;
                if (this.loadMoreStatusViewWrapper.IsShowStatusWhenAllDataDidLoad) {
//                    this.loadMoreTextView.setText(loadMoreStatusViewWrapper.getStatusByCode(LoadMoreStatusViewWrapper.StatusWrapper.LOAD_END));//"数据已全部加载完成"
//                    if (null != loadMoreProgressBar) {
//                        this.loadMoreProgressBar.setVisibility(View.GONE);
//                    }
                    this.loadMoreEndWithAllDataDidLoad();

                }else {
                    this.adapter.removeSysFooterView(footerView);
                    this.footerView = null;
                }
                this.onAllDataDidLoad();
            }
            this.adapter.addAll(convertData(datas));
        }

        this.mVisibleLastIndex += count;
        this.refreshLayout.setRefreshing(false);
    }

    private void setOnFailurePath_() {
        this.isRequestEnd = true;
        this.refreshLayout.setRefreshing(false);
//        if (null != footerView) {
//            this.loadMoreTextView.setText(loadMoreStatusViewWrapper.getStatusByCode(LoadMoreStatusViewWrapper.StatusWrapper.LOAD_PRE));//"加载更多"
//        }
//        if (null != loadMoreProgressBar) {
//            this.loadMoreProgressBar.setVisibility(View.GONE);
//        }
        this.loadMorePre();

        //exception
        this.emptyException();
    }


    /*----------------------------------------------------------------->>>*/
    private boolean isFirstInited = false;
    private void emptyInitLoading() {
        //只有初次加载时显示[如果配置初次显示的话]
        if (isFirstInited) {
            return;
        }
        this.tvEmtpy.setText(this.emptyStatusViewWrapper.TextResOfInitLoading);
        this.ivEmpty.setImageResource(this.emptyStatusViewWrapper.DrawableResOfInitLoading);
        this.sectionEmptyView.setVisibility(this.emptyStatusViewWrapper.IsShowEmptyViewBeforeInitLoading ? View.VISIBLE : View.GONE);
        this.isFirstInited = true;
        Drawable drawable = this.ivEmpty.getDrawable();
        if (null != drawable && drawable instanceof AnimationDrawable) {
            AnimationDrawable ad = (AnimationDrawable) drawable;
            if(ad.isRunning()) {
                ad.stop();
            }
            ad.start();
        }
    }

    private void emptyException() {
        //只有在列表中没有数据项时[不包括 XX-header XX-footer]
        if (adapter.getDataSectionItemCount() != 0) {
            return;
        }
        this.tvEmtpy.setText(this.emptyStatusViewWrapper.TextResOfException);
        this.ivEmpty.setImageResource(this.emptyStatusViewWrapper.DrawableResOfException);
        this.sectionEmptyView.setVisibility(View.VISIBLE);
    }

    private void emptyNoData() {
        this.tvEmtpy.setText(this.emptyStatusViewWrapper.TextResOfEmpty);
        this.ivEmpty.setImageResource(this.emptyStatusViewWrapper.DrawableResOfEmpty);
        this.sectionEmptyView.setVisibility(View.VISIBLE);
    }
    /*<<<-----------------------------------------------------------------*/

    public boolean isRequestIng() {
        return !this.isRequestEnd;
    }


    /**{@link DataStatusDelegate}
     * ---------------------------------------------------------------------------*/
    /**
     * 暂无数据回调
     */
    @Override
    public void onNoData() {
        Log.e(TAG, "onNoData()");
    }

    /**
     * 所有数据全部加载完毕回调
     */
    @Override
    public void onAllDataDidLoad() {
        Log.e(TAG, "onAllDataDidLoad()");
        toast("数据已全部加载完成");
    }
    /**---------------------------------------------------------------------------*/

    /**
     * 数据设置
     */
    @Deprecated
    protected static class Settings{
        //所有数据加载完毕时是否显示加载完毕状态
        public boolean isShowStatusWhenAllDataDidLoad = false;
        //下拉刷新时是否显示加载状态 true: 将会添加状态footer
        public boolean isShowStatusWhenRefresh = true;
        //设置每次请求数据条数
        public int pageSize = 50;
        //数据加载时状态控件布局(footer)
        public @LayoutRes int loadingStatusLayoutId = 0;
    }

    private Settings settings = new Settings();
    protected void configSettings(Settings settings) {

    }


    private void toast(String toast) {
        Toast.makeText(getContext(), toast, Toast.LENGTH_SHORT).show();
    }
}
