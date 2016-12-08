package ws.dyt.refresh.coordilayout;


import android.support.v4.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import ws.dyt.pagelist.config.ResponseResultWrapper;
import ws.dyt.pagelist.entity.IPageIndex;
import ws.dyt.pagelist.fragment.PageListFragment;
import ws.dyt.refresh.R;
import ws.dyt.view.adapter.SuperAdapter;
import ws.dyt.view.adapter.core.base.BaseAdapter;
import ws.dyt.view.viewholder.BaseViewHolder;

/**
 * A simple {@link Fragment} subclass.
 */
public class DataCollectionFragment extends PageListFragment<DataCollectionFragment.ResponseData, DataCollectionFragment.ResponseData> {

    @Override
    public BaseAdapter<ResponseData> initAdapter() {
        return new SuperAdapter<ResponseData>(getContext(), null, R.layout.item_text_c) {
            @Override
            public void convert(BaseViewHolder holder, int position) {
                holder.setText(R.id.tv_text, getItem(position).data);
            }
        };
    }

    @Override
    public List<ResponseData> flatMap(List<ResponseData> dataList) {
        return dataList;
    }

    @Override
    public void sendRequest(final int pageIndex) {
        new android.os.Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        setOnSuccessPath(new ResponseResultWrapper(0, "success", generate(pageIndex)));
                    }
                },
                2000
        );
    }

    public static class ResponseData implements IPageIndex {
        public int index;
        public String data;

        public ResponseData(int index, String data) {
            this.index = index;
            this.data = data;
        }

        @Override
        public int getPageIndex() {
            return index;
        }
    }

    private List<ResponseData> generate(int start) {
        List<ResponseData> datas = new ArrayList<>();
        if (start > 30) {
            //模拟数据已经加载完毕
            return datas;
        }
        final int pageSize = 20;
        for (int i = start; i < start + pageSize; i++) {
            datas.add(new ResponseData(i, "测试测试数据-- > "+i));
        }
        return datas;
    }


    //    public DataCollectionFragment() {
//        // Required empty public constructor
//    }
//
//    private View rootView;
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        rootView = inflater.inflate(R.layout.fragment_data_collection, container, false);
//        this.init();
//        return rootView;
//    }
//
//
//    private void init() {
//        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
//
//        LinearLayoutManager ll = new LinearLayoutManager(getContext());
//        ll.setOrientation(LinearLayoutManager.VERTICAL);
//
//        recyclerView.setLayoutManager(ll);
//
//        recyclerView.setAdapter(new SuperAdapter<String>(getContext(), generate(1), R.layout.item_text_c) {
//            @Override
//            public void convert(BaseViewHolder holder, int position) {
//                holder.setText(R.id.tv_text, getItem(position));
//            }
//        });
//    }
//
//    private List<String> generate(int start) {
//        List<String> datas = new ArrayList<>();
//        final int pageSize = 20;
//        for (int i = start; i < start + pageSize; i++) {
//            datas.add("测试测试数据-- > "+i);
//        }
//        return datas;
//    }
}
