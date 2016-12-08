package ws.dyt.pagelist.delegate;

import ws.dyt.pagelist.config.EmptyStatusViewWrapper;
import ws.dyt.pagelist.config.LoadMoreStatusViewWrapper;

/**
 * Created by yangxiaowei on 16/12/7.
 */

public interface IOnConfigCallback {
    /**
     * 空白页面初始化配置
     * @param wrapper
     */
    void onConfigEmptyStatusViewInfo(EmptyStatusViewWrapper wrapper);

    /**
     * 加载数据初始化配置
     * @param wrapper
     */
    void onConfigLoadMoreStatusViewInfo(LoadMoreStatusViewWrapper wrapper);
}
