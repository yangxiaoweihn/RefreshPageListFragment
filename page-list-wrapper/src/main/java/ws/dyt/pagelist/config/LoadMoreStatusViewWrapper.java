package ws.dyt.pagelist.config;

import android.support.annotation.IntDef;
import android.support.annotation.LayoutRes;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import ws.dyt.pagelist.R;

/**
 * 加载更多状态信息
 */
public class LoadMoreStatusViewWrapper {
    //加载状态布局[作为recyclerview的footer]
    @LayoutRes
    public int LayoutResOfLoadMoreStatusView = R.layout.rll_view_loadmore_footer;
    //加载状态提示信息[总共3种状态，必须]
    public String[] TextTipsOfLoadingStatus;

    //所有数据加载完毕时是否显示加载完毕状态
    public boolean IsShowStatusWhenAllDataDidLoad = false;
    //下拉刷新时是否显示加载状态[true: 将会添加状态footer]
    public boolean IsShowStatusWhenRefresh = false;


    @Retention(RetentionPolicy.SOURCE)
    @IntDef({
            StatusWrapper.LOAD_PRE,
            StatusWrapper.LOAD_ING,
            StatusWrapper.LOAD_END})
    @interface StatusWrapperWhere {
    }

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