package ws.dyt.pagelist.ui;

import ws.dyt.pagelist.entity.PageIndex;

/**
 * Created by yangxiaowei on 16/7/9.
 */
public interface ResponseResultDelegate<T extends PageIndex, M> {
    /**
     * @param result
     */
    void setOnSuccessPath(BasePageListFragment<T, M>.ResponseResultWrapper<T> result);

    /**
     *
     */
    void setOnFailurePath();
}
