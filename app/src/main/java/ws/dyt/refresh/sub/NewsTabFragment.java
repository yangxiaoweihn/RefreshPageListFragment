package ws.dyt.refresh.sub;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ws.dyt.pagelist.ui.BasePageListFragment;
import ws.dyt.refresh.TestFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsTabFragment extends TestFragment {


    public NewsTabFragment() {
        // Required empty public constructor
    }

    private static final String PARAM_TEXT = "param_text";
    public static NewsTabFragment newInstance(String text) {
        Bundle arg = new Bundle();
        arg.putString(PARAM_TEXT, text);
        NewsTabFragment fragment = new NewsTabFragment();
        fragment.setArguments(arg);
        return fragment;
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

    }
}
