package ws.dyt.pagelist.ui;

import ws.dyt.pagelist.entity.PageIndex;

/**
 * Created by yangxiaowei on 16/7/9.
 */
public interface ResponseResultDelegate<T extends PageIndex, M> {
//    class ResponseResultWrapper<T>{
//        public int StatusCode;
//        public String StatusMessage;
//        public List<T> Data;
//
//        public ResponseResultWrapper(int statusCode, String statusMessage, List<T> data) {
//            this.StatusCode = statusCode;
//            this.StatusMessage = statusMessage;
//            this.Data = data;
//        }
//    }

    /**
     * @param result
     */
    void setOnSuccessPath(BasePageListFragment<T, M>.ResponseResultWrapper<T> result);

    /**
     *
     */
    void setOnFailurePath();
}
