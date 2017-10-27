package ws.dyt.pagelist.controller;

import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.Spannable;
import android.text.Spanned;
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
import ws.dyt.adapter.adapter.core.base.BaseAdapter;

/**
 * Created by yangxiaowei on 16/8/30.
 *
 * 空白页面控制器
 */
public class EmptyViewController implements IRelease{

    private EmptyStatusViewWrapper emptyStatusViewWrapper;

    private BaseAdapter adapter;

    private FrameLayout sectionEmptyView;
    private ImageView ivEmpty;
    private TextView tvEmpty;
    private TextView tvRefresh;

    public EmptyViewController(LayoutInflater inflater, final View rootView, BaseAdapter adapter) {
        this.adapter = adapter;

        this.emptyStatusViewWrapper = new EmptyStatusViewWrapper();

        this.sectionEmptyView = ViewInject.findView(R.id.section_empty, rootView);
        //加载空白页面布局及控件
        View emptyView = inflater.inflate(this.emptyStatusViewWrapper.LayoutResOfEmptyView, null, false);
        this.ivEmpty = ViewInject.findView(R.id.rll_empty_iv_id, emptyView);
        this.tvEmpty = ViewInject.findView(R.id.rll_empty_tv_id, emptyView);
        this.tvRefresh = ViewInject.findView(R.id.rll_empty_tv_refresh_id, emptyView);

        //初始化一些显示数据
        this.handleEmptyImageView(this.emptyStatusViewWrapper.DrawableResOfEmpty);

        this.handleEmptyTextView(this.emptyStatusViewWrapper.TextResOfEmpty);

        //添加空白页面
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER;
        this.sectionEmptyView.addView(emptyView, lp);
    }

    private View.OnClickListener refreshClickListener;
    public EmptyViewController setRefreshClickListener(View.OnClickListener listener) {

        this.refreshClickListener = listener;
        return this;
    }

    /**
     * 初始加载数据(如果客户端设置了显示加载动画, 则只在无数据时进行加载)
     */
    public void withInitLoading() {
        if (this.adapter.getDataSectionItemCount() != 0) {
            return;
        }

        this.handleEmptyTextView(this.emptyStatusViewWrapper.TextResOfInitLoading);

        this.handleEmptyImageView(this.emptyStatusViewWrapper.DrawableResOfInitLoading);

        this.tvRefresh.setVisibility(View.GONE);

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

        this.handleEmptyTextView(this.emptyStatusViewWrapper.TextResOfException);

        this.handleEmptyImageView(this.emptyStatusViewWrapper.DrawableResOfException);

        this.sectionEmptyView.setVisibility(View.VISIBLE);

        if (this.emptyStatusViewWrapper.IsShowRefreshViewWhenFailure) {
            this.tvRefresh.setVisibility(View.VISIBLE);
            this.tvRefresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != refreshClickListener) {
                        refreshClickListener.onClick(v);
                    }
                }
            });
        }
    }

    /**
     * 无数据
     */
    public void withNoneData() {
        this.handleEmptyTextView(this.emptyStatusViewWrapper.TextResOfEmpty);

        this.handleEmptyImageView(this.emptyStatusViewWrapper.DrawableResOfEmpty);

        this.tvRefresh.setVisibility(View.GONE);

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

    private void destroyAnimationDrawable(Drawable drawable) {
        if (null == drawable) {
            return;
        }
        if (drawable instanceof AnimationDrawable) {
            AnimationDrawable ad = (AnimationDrawable) drawable;
            if (ad.isRunning()) {
                ad.stop();
            }
        }
    }

    private void handleEmptyImageView(@DrawableRes int resId) {
        if (resId > 0) {
            this.ivEmpty.setImageResource(resId);
        }else {
            this.ivEmpty.setImageDrawable(null);
        }
        this.ivEmpty.setVisibility(resId > 0 ? View.VISIBLE : View.GONE);
    }

    private void handleEmptyTextView(@StringRes int resId) {
        this.tvEmpty.setText(this.getEmptyString(resId));
        this.tvEmpty.setVisibility(resId > 0 ? View.VISIBLE : View.GONE);
    }

    private Spanned getEmptyString(@StringRes int resId) {

        return Html.fromHtml(tvEmpty.getContext().getString(resId > 0 ? resId : R.string.rll_tips_null));
    }

    @Override
    public void release() {
        if (null != this.ivEmpty) {
            this.destroyAnimationDrawable(this.ivEmpty.getDrawable());
        }

        if (null != sectionEmptyView) {
            this.sectionEmptyView.removeAllViewsInLayout();
        }
    }
}
