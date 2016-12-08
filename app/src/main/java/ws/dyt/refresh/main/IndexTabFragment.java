package ws.dyt.refresh.main;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ws.dyt.refresh.base.SingleFragmentActivity;
import ws.dyt.refresh.sub.DDD;

/**
 * A simple {@link Fragment} subclass.
 */
public class IndexTabFragment extends Fragment {


    public IndexTabFragment() {
        // Required empty public constructor
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.e("BBBBBB", getClass().getSimpleName() + " : " + "首页" + " -> " + isVisibleToUser + " , " + getUserVisibleHint());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        TextView textView = new TextView(getActivity());
        textView.setText("首页");
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SingleFragmentActivity.to(getContext(), DDD.class, DDD.newArgs(20, false, "DDD"));
            }
        });
        return textView;
    }


}
