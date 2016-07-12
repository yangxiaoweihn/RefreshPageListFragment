package ws.dyt.pagelist.ui;

/**
 * 针对fragment刷新
 */
public interface OnVisibleble {
    /**
     * 需要刷新数据时调用该方法
     * @param visible
     */
    void onVisibleToUser(boolean visible);
}