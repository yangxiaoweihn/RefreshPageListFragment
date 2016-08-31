package ws.dyt.pagelist.controller;

import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import ws.dyt.pagelist.R;
import ws.dyt.pagelist.config.EmptyStatusViewWrapper;
import ws.dyt.pagelist.utils.ViewInject;
import ws.dyt.view.adapter.SuperAdapter;

/**
 * Created by yangxiaowei on 16/8/30.
 *
 * 空白页面控制器
 */
public class EmptyViewController {

    private EmptyStatusViewWrapper emptyStatusViewWrapper;

    private SuperAdapter adapter;

    private FrameLayout sectionEmptyView;
    private ImageView ivEmpty;
    private TextView tvEmtpy;

    public EmptyViewController(LayoutInflater inflater, View rootView, SuperAdapter adapter) {
        this.adapter = adapter;

        this.emptyStatusViewWrapper = new EmptyStatusViewWrapper();

        this.sectionEmptyView = ViewInject.findView(R.id.section_empty, rootView);
        //加载空白页面布局及控件
        View emptyView = inflater.inflate(this.emptyStatusViewWrapper.LayoutResOfEmptyView, null, false);
        this.ivEmpty = ViewInject.findView(R.id.rll_empty_iv_id, emptyView);
        this.tvEmtpy = ViewInject.findView(R.id.rll_empty_tv_id, emptyView);

        //初始化一些显示数据
        if (this.emptyStatusViewWrapper.DrawableResOfEmpty > 0) {
            this.ivEmpty.setImageResource(this.emptyStatusViewWrapper.DrawableResOfEmpty);
        }
        if (this.emptyStatusViewWrapper.TextResOfEmpty > 0) {
            this.tvEmtpy.setText(this.emptyStatusViewWrapper.TextResOfEmpty);
        }

        //添加空白页面
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER;
        this.sectionEmptyView.addView(emptyView, lp);
    }

    /**
     * 初始加载数据(如果客户端设置了显示加载动画, 则只在无数据时进行加载)
     */
    public void withInitLoading() {
        if (this.adapter.getDataSectionItemCount() != 0) {
            return;
        }

        this.tvEmtpy.setText(this.emptyStatusViewWrapper.TextResOfInitLoading);
        this.ivEmpty.setImageResource(this.emptyStatusViewWrapper.DrawableResOfInitLoading);
        this.sectionEmptyView.setVisibility(this.emptyStatusViewWrapper.IsShowEmptyViewBeforeInitLoading ? View.VISIBLE : View.GONE);

        Drawable drawable = this.ivEmpty.getDrawable();
        if (null == drawable) {
            return;
        }
        if (drawable instanceof AnimationDrawable) {
            AnimationDrawable ad = (AnimationDrawable) drawable;
            if(ad.isRunning()) {
                ad.stop();
            }
            ad.start();
            return;
        }
    }

    /**
     * 异常
     */
    public void withException() {
        //只有在列表中没有数据项时[不包括 XX-header XX-footer]
        if (this.adapter.getDataSectionItemCount() != 0) {
            return;
        }

        this.tvEmtpy.setText(this.emptyStatusViewWrapper.TextResOfException);
        this.ivEmpty.setImageResource(this.emptyStatusViewWrapper.DrawableResOfException);
        this.sectionEmptyView.setVisibility(View.VISIBLE);
    }

    /**
     * 无数据
     */
    public void withNoneDatas() {
        this.tvEmtpy.setText(this.emptyStatusViewWrapper.TextResOfEmpty);
        this.ivEmpty.setImageResource(this.emptyStatusViewWrapper.DrawableResOfEmpty);
        this.sectionEmptyView.setVisibility(View.VISIBLE);
    }

    /**
     * 移除空白提示页面
     */
    public void withRemoveEmptyView() {
        this.sectionEmptyView.setVisibility(View.GONE);
    }

    public EmptyStatusViewWrapper getEmptyStatusViewWrapper() {
        return emptyStatusViewWrapper;
    }
}
