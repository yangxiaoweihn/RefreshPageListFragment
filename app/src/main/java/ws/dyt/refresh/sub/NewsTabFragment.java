package ws.dyt.refresh.sub;


import android.os.Bundle;
import android.support.v4.app.Fragment;

import ws.dyt.pagelist.config.EmptyStatusViewWrapper;
import ws.dyt.pagelist.config.LoadMoreStatusViewWrapper;
import ws.dyt.refresh.R;
import ws.dyt.refresh.TestFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsTabFragment extends TestFragment {

    private static final String PARAM_DATA_COUNTS = "param_data_counts";
    private static final String PARAM_IS_ERROR = "param_is_error";


    public NewsTabFragment() {
        // Required empty public constructor
    }

    private static final String PARAM_TEXT = "param_text";
    public static Bundle newArgs(int dataCount, boolean isError, String text) {
        Bundle arg = new Bundle();
        arg.putString(PARAM_TEXT, text);
        arg.putInt(PARAM_DATA_COUNTS, dataCount);
        arg.putBoolean(PARAM_IS_ERROR, isError);
        return arg;
    }
    public static NewsTabFragment newInstance(int dataCount, boolean isError, String text) {

        NewsTabFragment fragment = new NewsTabFragment();
        fragment.setArguments(newArgs(dataCount, isError, text));
        return fragment;
    }

    @Override
    public void setUpView() {
        Bundle arg = getArguments();
        int dataCount = arg.getInt(PARAM_DATA_COUNTS, 0);
        boolean isError = arg.getBoolean(PARAM_IS_ERROR, false);
        this.amulatorDataCounts(dataCount);
        this.amulatorError(isError);
    }

    //    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        TextView textView = new TextView(getActivity());
//        textView.setText(getArguments().getString(PARAM_TEXT));
//        return textView;
//    }


    @Override
    public void onConfigLoadMoreStatusViewInfo(LoadMoreStatusViewWrapper wrapper) {
        super.onConfigLoadMoreStatusViewInfo(wrapper);
    }

    @Override
    public void onConfigEmptyStatusViewInfo(EmptyStatusViewWrapper wrapper) {
//        wrapper.TextResOfInitLoading = "正在努力加载中。。。。";
//        wrapper.DrawableResOfInitLoading = R.drawable.rll_test;
        wrapper.DrawableResOfInitLoading = R.drawable.rll_indi_init_loading;
        wrapper.IsShowEmptyViewBeforeInitLoading = true;
    }

    @Override
    public boolean isLazyLoadEnabled() {
        return true;
    }
}
