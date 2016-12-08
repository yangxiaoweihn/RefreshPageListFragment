package ws.dyt.refresh.sub;


import android.support.v4.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class DDD extends NewsTabFragment {
    public static DDD newInstance(int dataCount, boolean isError, String text) {

        DDD fragment = new DDD();
        fragment.setArguments(newArgs(dataCount, isError, text));
        return fragment;
    }

    @Override
    public boolean isLazyLoadEnabled() {
        return false;
    }
}
