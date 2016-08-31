package ws.dyt.pagelist.config;

import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;

import ws.dyt.pagelist.R;

/**
 * 空白页面配置信息
 */
public class EmptyStatusViewWrapper {
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
    public int DrawableResOfInitLoading = R.drawable.rll_indi_init_loading;
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