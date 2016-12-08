package ws.dyt.pagelist.delegate;

/**
 * Created by yangxiaowei on 16/7/9.
 */
public interface DataStatusDelegate {
    /**
     * 暂无数据回调
     */
    void onNoneData();

    /**
     * 所有数据全部加载完毕回调
     */
    void onAllDataDidLoad();
}
