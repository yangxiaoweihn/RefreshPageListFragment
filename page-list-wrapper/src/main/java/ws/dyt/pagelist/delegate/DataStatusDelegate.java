package ws.dyt.pagelist.delegate;

import java.util.List;

import ws.dyt.pagelist.entity.IPageIndex;

/**
 * Created by yangxiaowei on 16/7/9.
 */
public interface DataStatusDelegate<T extends IPageIndex> {
    /**
     * 成功获取到数据
     */
    void onSuccessResponsePre(List<T> data);

    void onSuccessResponseEnd();

    void onFailureResponsePre();
    void onFailureResponseEnd();

    /**
     * 暂无数据回调
     */
    void onNoneData();

    /**
     * 所有数据全部加载完毕回调
     */
    void onAllDataDidLoad();
}
