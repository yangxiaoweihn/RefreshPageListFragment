package ws.dyt.pagelist.controller;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import ws.dyt.pagelist.R;
import ws.dyt.pagelist.config.LoadMoreStatusViewWrapper;
import ws.dyt.pagelist.utils.ViewInject;
import ws.dyt.view.adapter.core.base.BaseAdapter;

/**
 * Created by yangxiaowei on 16/8/30.
 *
 * 加载更多状态控件控制器
 */
public class LoadMoreStatusViewController implements IRelease{

    private LoadMoreStatusViewWrapper loadMoreStatusViewWrapper;

    private LayoutInflater inflater;
    private BaseAdapter adapter;
    private RecyclerView recyclerView;

    private View footerView;
    private ProgressBar loadMoreProgressBar;
    private TextView loadMoreTextView;

    public LoadMoreStatusViewController(LayoutInflater inflater, RecyclerView recyclerView, BaseAdapter adapter) {
        this.inflater = inflater;
        this.recyclerView = recyclerView;
        this.adapter = adapter;

        this.loadMoreStatusViewWrapper = new LoadMoreStatusViewWrapper(recyclerView.getResources());
    }

    /**
     * 初始化加载  加载更多状态控件
     */
    private void initLoadMoreView() {
        this.footerView = inflater.inflate(this.loadMoreStatusViewWrapper.LayoutResOfLoadMoreStatusView, recyclerView, false);
        this.adapter.setSysFooterView(footerView);
        this.loadMoreProgressBar = ViewInject.findView(R.id.rll_load_more_progress_id, footerView);
        this.loadMoreTextView = ViewInject.findView(R.id.rll_load_more_tv_id, footerView);
    }

    /**
     * 重置状态数据
     */
    private void loadMorePre() {
        if (null == this.footerView) {
            this.initLoadMoreView();
        }

        if (null != this.footerView) {
            this.loadMoreTextView.setText(loadMoreStatusViewWrapper.getStatusByCode(LoadMoreStatusViewWrapper.StatusWrapper.LOAD_PRE));//"加载更多"
        }
        if (null != loadMoreProgressBar) {
            this.loadMoreProgressBar.setVisibility(View.GONE);
        }
    }

    /**
     * 加载中数据设置
     */
    private void loadMoreIng() {
        if (null == this.footerView) {
            this.initLoadMoreView();
        }

        if (null != footerView) {
            this.loadMoreTextView.setText(loadMoreStatusViewWrapper.getStatusByCode(LoadMoreStatusViewWrapper.StatusWrapper.LOAD_ING));//"正在加载。。。"
        }
        if (null != footerView && null != loadMoreProgressBar) {
            this.loadMoreProgressBar.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 加载完成数据设置
     */
    private void loadMoreEndWithAllDataDidLoad() {
        if (null == this.footerView) {
            this.initLoadMoreView();
        }

        if (null != footerView) {
            this.loadMoreTextView.setText(loadMoreStatusViewWrapper.getStatusByCode(LoadMoreStatusViewWrapper.StatusWrapper.LOAD_END));//"数据已全部加载完成"
        }
        if (null != loadMoreProgressBar) {
            this.loadMoreProgressBar.setVisibility(View.GONE);
        }
    }

    public void withResetStatus() {
        this.loadMorePre();
    }

    /**
     * 下拉刷新时客户端可能有设置了配置项
     */
    public void withPullDown() {
        if (this.loadMoreStatusViewWrapper.IsShowStatusWhenRefresh) {
            this.loadMoreIng();
        }
    }

    /**
     * 上拉加载更多
     */
    public void withPullUp() {
        this.loadMoreIng();
    }

    public void withRemoveLoadStatusView() {
        this.adapter.removeSysFooterView(footerView);
        this.footerView = null;
    }

    /**
     * 数据加载完成后根据客户端设置的状态进行操作
     */
    public void withLoadSuccess() {
        //家在完成后仍然显示状态视图
        if (this.loadMoreStatusViewWrapper.IsShowStatusWhenAllDataDidLoad) {
            this.loadMoreEndWithAllDataDidLoad();

        }else {
            this.adapter.removeSysFooterView(footerView);
            this.footerView = null;
        }
    }

    /**
     * 异常
     */
    public void withException() {
        //异常且列表无数据
        if (this.adapter.getDataSectionItemCount() == 0) {
            this.withRemoveLoadStatusView();
        }else {
            this.withResetStatus();
        }
    }

    public LoadMoreStatusViewWrapper getLoadMoreStatusViewWrapper() {
        return loadMoreStatusViewWrapper;
    }

    @Override
    public void release() {

    }
}
