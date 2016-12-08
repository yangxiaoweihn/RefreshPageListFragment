package ws.dyt.refresh.coordilayout;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import ws.dyt.refresh.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CoordiLayoutFragment extends Fragment {


    public CoordiLayoutFragment() {
        // Required empty public constructor
    }


    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_coordi_layout, container, false);
        this.init();
        return rootView;
    }


    private void init() {
        FrameLayout dataContainer = (FrameLayout) rootView.findViewById(R.id.section_data_container);

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.section_data_container, new DataCollectionFragment(), "DataCollectionFragment")
                .commit();

    }
}
