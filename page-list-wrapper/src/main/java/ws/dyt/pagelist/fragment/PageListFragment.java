package ws.dyt.pagelist.fragment;


import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

import ws.dyt.pagelist.R;
import ws.dyt.pagelist.config.EmptyStatusViewWrapper;
import ws.dyt.pagelist.config.LoadMoreStatusViewWrapper;
import ws.dyt.pagelist.config.ResponseResultWrapper;
import ws.dyt.pagelist.controller.EmptyViewController;
import ws.dyt.pagelist.controller.LoadMoreStatusViewController;
import ws.dyt.pagelist.delegate.DataStatusDelegate;
import ws.dyt.pagelist.delegate.IInitConfig;
import ws.dyt.pagelist.delegate.IOnConfigCallback;
import ws.dyt.pagelist.delegate.ResponseResultDelegate;
import ws.dyt.pagelist.entity.IPageIndex;
import ws.dyt.pagelist.fragment.lazy.LazyLoadFragment;
import ws.dyt.pagelist.utils.ViewInject;
import ws.dyt.adapter.adapter.core.base.BaseAdapter;


/**
 *
 * 该类封装分页加载数据及错误处理，同时适用于上述情景
 *
 * 基本步骤：
 * 1. 复写必要方法
 * 2. 重写部分方法
 * 3. 请求数据
 * 4. 设置请求结果数据集{@link #setOnSuccessPath(ws.dyt.pagelist.config.ResponseResultWrapper), {@link #setOnFailurePath()}}
 *
 * {@link #initPageSize()} 指定每页加载的数据量，默认为{@link IInitConfig#PAGE_SIZE} 条，当某次获取的数据量<{@link #initPageSize()}
 *      时，认为所有数据加载完毕
 *
 * @param <T_RESPONSE>   服务端返回的数组数据，用来真实分页的数据
 * @param <T_ADAPTER>   适配器装载的数据（有可能真实的数据里会添加一些别的数据进去显示）
 */
abstract
public class PageListFragment<T_RESPONSE extends IPageIndex, T_ADAPTER> extends LazyLoadFragment
        implements IInitConfig<T_RESPONSE, T_ADAPTER>, ResponseResultDelegate<T_RESPONSE>, DataStatusDelegate<T_RESPONSE>, IOnConfigCallback {

    private static final String TAG = "lib_BaseListFragment";

    protected LayoutInflater inflater;
    protected Bundle savedInstanceState;
    protected FrameLayout mSectionRootPoolFrameLayout;
    protected RecyclerView recyclerView;
    protected SwipeRefreshLayout refreshLayout;
    protected BaseAdapter<T_ADAPTER> adapter;
    protected View rootView;

    public void scrollToPosition(int position) {
        if (isRemoving() || isDetached() || null == this.recyclerView) {
            return;
        }
        if (isRequestIng()) {
            return;
        }
        this.recyclerView.scrollToPosition(position);//.smoothScrollToPosition(0);
    }

    /**
     * 默认线性纵向布局管理器
     * @return
     */
    @Override
    public RecyclerView.LayoutManager initLayoutManager() {
        LinearLayoutManager ll = new LinearLayoutManager(getContext());
        ll.setOrientation(LinearLayoutManager.VERTICAL);
        return ll;
    }

    @Override
    public int initPageSize() {
        return PAGE_SIZE;
    }

    @Override
    public void setUpView() {}

    private EmptyViewController emptyViewController;
    @Override
    public void onConfigEmptyStatusViewInfo(EmptyStatusViewWrapper wrapper) {}


    private LoadMoreStatusViewController loadMoreStatusViewController;
    @Override
    public void onConfigLoadMoreStatusViewInfo(LoadMoreStatusViewWrapper wrapper) {}

    @Override
    public View onLazyCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.inflater = inflater;
        this.savedInstanceState = savedInstanceState;
        this.rootView = this.onInflateView(inflater, container, savedInstanceState);
        this.recyclerView = ViewInject.findView(R.id.recyclerView, rootView);
        this.refreshLayout = ViewInject.findView(R.id.refreshLayout, rootView);
        this.mSectionRootPoolFrameLayout = ViewInject.findView(R.id.section_root_pool, rootView);

        this.init(inflater);
        return rootView;
    }

    protected View onInflateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(this.setContentView(), container, false);
    }

    private void init(LayoutInflater inflater) {
        this.recyclerView.setLayoutManager(this.initLayoutManager());

        this.adapter = this.initAdapter();

        this.loadMoreStatusViewController = new LoadMoreStatusViewController(inflater, this.recyclerView, this.adapter);
        this.onConfigLoadMoreStatusViewInfo(this.loadMoreStatusViewController.getLoadMoreStatusViewWrapper());

        this.emptyViewController = new EmptyViewController(inflater, this.rootView, adapter).setRefreshClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRefreshListener.onRefresh();
            }
        });

        View rootContainer = this.rootView.findViewById(R.id.section_root_container);
        if (null != rootContainer) {

            rootContainer.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return recyclerView.onTouchEvent(event);
                }
            });
        }

        this.onConfigEmptyStatusViewInfo(this.emptyViewController.getEmptyStatusViewWrapper());

        this.setUpView();

        this.recyclerView.setAdapter(adapter);

        this.refreshLayout.setOnRefreshListener(onRefreshListener);
        this.recyclerView.addOnScrollListener(onScrollListener);
    }

    protected @LayoutRes int setContentView() {
        return R.layout.rll_fragment_recyclerview_refresh;
    }

    private SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            emptyViewController.withRemoveEmptyView();
            PageListFragment.this.pullDown();
        }
    };


    //最后一个可见item位置
    private int lastVisibleItemPosition;
    private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            //刷新
            if (refreshLayout.isRefreshing() == false
                    && newState == RecyclerView.SCROLL_STATE_IDLE
                    && lastVisibleItemPosition == adapter.getItemCount() - 1
                    && isRequestEnd == true
                    && isAllDataDidLoad == false) {
                PageListFragment.this.pullUp();
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
                lastVisibleItemPosition = ((GridLayoutManager) lm).findLastVisibleItemPosition();
            } else if (lm instanceof StaggeredGridLayoutManager) {
                lastVisibleItemPosition = ((StaggeredGridLayoutManager) lm).findLastVisibleItemPositions(new int[1])[0];
            } else if (lm instanceof LinearLayoutManager) {
                lastVisibleItemPosition = ((LinearLayoutManager) lm).findLastVisibleItemPosition();
            }
        }
    };

    //标记是否为下拉刷新请求
    private boolean isPullDownRequesting = false;
    private void pullDown() {
        Log.d(TAG, "pullDown()");
        if (!isRequestEnd) {
            return;
        }
        this.refreshLayout.setRefreshing(true);
        this.emptyViewController.withInitLoading();

        this.isPullDownRequesting = true;
        this.pageStartIndex = 0;
        this.isRequestEnd = false;
        if (!supportPullDownLoadMore()) {

            this.realDataCount = 0;
            this.isAllDataDidLoad = false;
        }
        this.loadMoreStatusViewController.withPullDown();

        //针对下拉第一页进行处理
        this.pageStartIndex = this.filterPageIndexOffset(this.realDataCount, this.pageStartIndex);

        this.fetchData(pageStartIndex);
    }

    private void pullUp() {
        if (isAllDataDidLoad || !isRequestEnd) {
            return;
        }
        Log.d(TAG, "pullUp()");
        this.isRequestEnd = false;

        this.loadMoreStatusViewController.withPullUp();

        this.fetchData(pageStartIndex);
    }

    /**
     * 初次加载数据,或者强制刷新-都是重新获取数据
     */
    final
    protected void fetchData() {
        if (null == refreshLayout) {
            return;
        }
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                pullDown();
            }
        });
    }

    //一次请求是否完毕
    private boolean isRequestEnd = true;
    //是否数据已经全部加载完毕
    private boolean isAllDataDidLoad = false;

    //分页索引id
    private long pageStartIndex = 0;

    /**{@link ResponseResultDelegate}
     * ---------------------------------------------------------------------------*/
    @Override
    final
    public void setOnSuccessPath(ResponseResultWrapper<T_RESPONSE> result) {
        Log.e(TAG, "setOnSuccessPath");
        this.isRequestEnd = true;

        this.onSuccessResponsePre(null == result ? null : result.Data);

        this.setOnSuccessPath_(result);
        this.isPullDownRequesting = false;

        this.onSuccessResponseEnd();
    }

    @Override
    final
    public void setOnFailurePath(){
        Log.e(TAG, "setOnFailurePath");
        this.isRequestEnd = true;
        this.onFailureResponsePre();

        this.setOnFailurePath_();
        this.isPullDownRequesting = false;

        this.onFailureResponseEnd();
    }

    @Override
    public void onSuccessResponsePre(List<T_RESPONSE> data) {}

    @Override
    public void onSuccessResponseEnd() {}

    @Override
    public void onFailureResponsePre() {}

    @Override
    public void onFailureResponseEnd() {}

    /**
     * 下拉是否支持加载更多
     * @return
     */
    protected boolean supportPullDownLoadMore() {
        return false;
    }
    /**---------------------------------------------------------------------------*/

    private int realDataCount = 0;
    private void setOnSuccessPath_(ResponseResultWrapper<T_RESPONSE> result) {
        //接口数据异常
        if (null == result || result.StatusCode != 0) {
            this.setOnFailurePath();
            return;
        }

        final List<T_RESPONSE> data = null == result.Data ? new ArrayList<T_RESPONSE>() : result.Data;
        final int count = data.size();
        final long empIndexId = count == 0 ? 0 : data.get(count - 1).getPageIndex();
        this.realDataCount += count;

        //下拉刷新后且获取到数据, 此时清除旧数据
        //这里会产生一个歧义(看情况): 刷新的时候如果未获取到数据(一切状态正常只是无数据), 此时要不要清楚旧数据的问题. 我这里采用的是清除的方式
        if (this.isPullDownRequesting) {
            this.adapter.clear();
        }

        int dataSize = adapter.getDataSectionItemCount();
        boolean isSupportPullDown = supportPullDownLoadMore();
//        boolean isNew = isSupportPullDown  && isOperationIsPullDown;
        //列表无数据且未获取到数据，此时认为无数据
        if (0 == dataSize && 0 == count) {
            this.isAllDataDidLoad = true;
            this.loadMoreStatusViewController.withRemoveLoadStatusView();

            this.emptyViewController.withNoneData();
            this.onNoneData();
        }else {
            //有数据，需要判断数据是否已全部加载完毕

            this.emptyViewController.withRemoveEmptyView();

            if (this.isPullDownRequesting) {
                //为了确保第一页定位问题, 不然会定位到最底部
                this.adapter.notifyDataSetChanged();
            }

            //当前获取的数据少于一页数据，此时认为数据已经全部加载完毕
            if (count < initPageSize()) {
                this.isAllDataDidLoad = true;
                this.loadMoreStatusViewController.withLoadSuccess();
                this.onAllDataDidLoad();
            }else {
                this.loadMoreStatusViewController.withResetStatus();
            }
            this.adapter.addAll(this.flatMap(data));
        }

        this.pageStartIndex = this.filterPageIndexOffset(this.realDataCount, empIndexId);
        this.refreshLayout.setRefreshing(false);
    }

    private void setOnFailurePath_() {
        this.refreshLayout.setRefreshing(false);

        this.loadMoreStatusViewController.withException();

        //exception
        this.emptyViewController.withException();
    }

    /**
     * 分页页码处理逻辑, 默认按照分页id
     *
     * @param realDataCount         从数据源获取到的数据累积量
     * @param lastItemPageIndexId   从数据源获取的当前页最后一项分页id
     * @return
     */
    private long filterPageIndexOffset(long realDataCount, long lastItemPageIndexId) {
        realDataCount = this.filterPageIndexOffset(realDataCount);
        return -1 == realDataCount ? lastItemPageIndexId : realDataCount;
    }

    /**
     * 重写分页标识
     *
     * @param realDataCount
     * @return
     */
    protected long filterPageIndexOffset(long realDataCount) {
        //比如通过页码分页可以这么写(分页从1...n)
        //return realDataCount / setPageSize() + 1;
        return -1;
    }

    public boolean isRequestIng() {
        return !this.isRequestEnd;
    }


    /**{@link DataStatusDelegate}
     * ---------------------------------------------------------------------------*/
    /**
     * 暂无数据回调
     */
    @Override
    public void onNoneData() {
        Log.e(TAG, "onNoneData");
    }

    /**
     * 所有数据全部加载完毕回调
     */
    @Override
    public void onAllDataDidLoad() {
        Log.e(TAG, "onAllDataDidLoad()");
    }
    /**---------------------------------------------------------------------------*/

    public void remove(int index) {
        if (index >= 0 && null != adapter && !adapter.isEmptyOfData()) {
            adapter.remove(index);

            this.emptyDataCheck();
        }
    }

    public void remove(T_ADAPTER item) {
        if (null != item && null != adapter && !adapter.isEmptyOfData()) {
            adapter.remove(item);

            this.emptyDataCheck();
        }
    }

    public void remove(List<T_ADAPTER> items) {

        if (null != items && !items.isEmpty() && !adapter.isEmptyOfData()) {
            if (1 == items.size()) {

                remove(items.get(0));
            }else {

                adapter.removeAll(items);

                this.emptyDataCheck();
            }
        }
    }

    public void add(int index, T_ADAPTER item) {

        if (index >= 0 && null != item && null != adapter) {

            adapter.add(index, item);
            if (adapter.getDataSectionItemCount() == 1) {

                this.emptyViewController.withRemoveEmptyView();

            }
        }
    }

    private void emptyDataCheck() {
        final int count = adapter.getDataSectionItemCount();
        if (0 == count) {
            setOnSuccessPath(new ResponseResultWrapper(0, "", null));
        }
    }


    private boolean check() {
        if (isRemoving() || isDetached() || null == recyclerView) {
            return false;
        }
        if (isRequestIng()) {
            return false;
        }
        return true;
    }

    public void toListTop() {
        if (!check()) {
            return;
        }
        recyclerView.scrollToPosition(0);//.smoothScrollToPosition(0);
    }

    public void toRefresh() {
        if (!check()) {
            return;
        }
        fetchData(0);
    }

    public void toListTopAndRefresh() {
        this.toListTop();
        this.toRefresh();
    }

    public void toListTopOrRefresh() {

    }

    @Override
    @CallSuper
    protected void onFragVisibilityChanged(boolean visible) {
        if (visible && null != adapter && adapter.isEmptyOfData()) {
            fetchData();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (null != this.adapter) {
            this.adapter.clear();
        }

        if (null != refreshLayout) {
            this.refreshLayout.setOnRefreshListener(null);
            this.onRefreshListener = null;
        }

        if (null != recyclerView) {
            this.recyclerView.removeOnScrollListener(this.onScrollListener);
            this.onScrollListener = null;
        }

        if (null != emptyViewController) {
            emptyViewController.release();
        }

        if (null != loadMoreStatusViewController) {
            loadMoreStatusViewController.release();
        }
    }
}
