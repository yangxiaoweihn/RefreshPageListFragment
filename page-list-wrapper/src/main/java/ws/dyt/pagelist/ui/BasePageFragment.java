package ws.dyt.pagelist.ui;

import android.os.Bundle;
import android.util.Log;


/**
 * Created by yangxiaowei on 16/7/7.
 */
abstract
public class BasePageFragment extends android.support.v4.app.Fragment implements OnVisibleble{
    protected boolean isViewInitiated;
    protected boolean isVisibleToUser;
    protected boolean isDataInitiated;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.isViewInitiated = true;
        this.prepareToFetchData();
    }

//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        this.isVisibleToUser = isVisibleToUser;
//        this.prepareToFetchData();
//    }

    /**
     * 加载数据
     */
    abstract
    protected void fetchData();

    public boolean prepareToFetchData() {
        return prepareToFetchData(false);
    }

    public boolean prepareToFetchData(boolean forceUpdate) {
        if (isVisibleToUser && isViewInitiated && (!isDataInitiated || forceUpdate)) {
            fetchData();
            isDataInitiated = true;
            return true;
        }
        return false;
    }

    @Override
    public void onVisibleToUser(boolean visible) {
        Log.d("BasePageFragment", getClass().getName()+" -> "+visible);
        this.isVisibleToUser = visible;
        this.prepareToFetchData();
    }

}

