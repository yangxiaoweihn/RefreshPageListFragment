package ws.dyt.pagelist.fragment.lazy;

import android.os.Bundle;
import android.util.Log;


/**
 * Created by yangxiaowei on 16/7/7.
 */
abstract
public class LazyLoadFragment extends android.support.v4.app.Fragment implements OnVisiblable {
    protected boolean isViewInitiated;
    protected boolean isVisibleToUser;
    protected boolean isDataInitiated;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.isViewInitiated = true;
        if (!isLazyLoadEnabled()) {
            if (!isDataInitiated) {
                fetchData();
                isDataInitiated = true;
            }
        } else {
            this.prepareToFetchData();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        this.isVisibleToUser = isVisibleToUser;
        this.prepareToFetchData();
    }

    /**
     * 加载数据
     */
    abstract
    protected void fetchData();

    private boolean prepareToFetchData() {
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
//        this.isVisibleToUser = visible;
//        this.prepareToFetchData();
    }

    /**
     * 是否是懒加载模式
     * 这个主要是针对Fragment是否在FragmentPagerAdapter中描述的
     * 如果Fragment在一个单页面中, 那么不启用懒加载直接进行加载即可
     * 如果Fragment被加载到FragmentPagerAdapter中, 此时setUserVisibleHint将会被自动调用, 此时可以开启懒加载模式
     * 其他情况需要手动去管理数据的加载, 可以根据可见性对setUserVisibleHint进行操作达到目的
     * @return
     */
    public boolean isLazyLoadEnabled() {
        return false;
    }

}

