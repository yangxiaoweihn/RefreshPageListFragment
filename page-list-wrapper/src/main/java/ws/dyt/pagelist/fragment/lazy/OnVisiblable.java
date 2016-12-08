package ws.dyt.pagelist.fragment.lazy;

/**
 * 针对fragment刷新
 */
public interface OnVisiblable {
    /**
     * 需要刷新数据时调用该方法
     * @param visible
     */
    void onVisibleToUser(boolean visible);
}