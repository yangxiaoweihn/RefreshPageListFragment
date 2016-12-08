package ws.dyt.pagelist.delegate;

import ws.dyt.pagelist.config.ResponseResultWrapper;
import ws.dyt.pagelist.entity.IPageIndex;

/**
 * Created by yangxiaowei on 16/7/9.
 */
public interface ResponseResultDelegate<T extends IPageIndex> {
    /**
     * @param result
     */
    void setOnSuccessPath(ResponseResultWrapper<T> result);

    /**
     *
     */
    void setOnFailurePath();
}
