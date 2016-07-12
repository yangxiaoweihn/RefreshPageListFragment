package ws.dyt.refresh;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ws.dyt.pagelist.ui.BasePageListFragment;
import ws.dyt.view.adapter.MultiAdapter;
import ws.dyt.view.viewholder.BaseViewHolder;

/**
 * Created by yangxiaowei on 16/6/29.
 */
public class TestFragment extends BasePageListFragment<String, String> {

    @Override
    protected RecyclerView.LayoutManager setLayoutManager() {
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        return llm;
    }

    @Override
    protected MultiAdapter<String> setAdapter() {
        return new MultiAdapter<String>(getContext(), new ArrayList<String>(), R.layout.item_text_c){
            @Override
            public void convert(BaseViewHolder holder, int position) {
                holder.setText(R.id.tv_text, getItem(position));
            }
        };
    }

    private static final int PAGE_SIZE = 10;
    @Override
    protected int setPageSize() {
        return PAGE_SIZE;
    }

    int kk = 0;
    int current = 0;
    @Override
    protected void fetchData(int index) {
        List<String> data = this.generate(index);
        if (kk % 2 == 0) {
            current += data.size();
            setOnSuccessPath(new ResponseResultWrapper<String>(0, "Success", data));
        }else {
            setOnSuccessPath(new ResponseResultWrapper<String>(9, "XXX", data));
        }
//        kk++;
    }

    int all = 44;
    private List<String> generate(int pre) {
        int r = all - current;
        r =  r >= PAGE_SIZE ? PAGE_SIZE : r;
        List<String> data = new ArrayList<>();
        for (int i = 1; i <= r; i++) {
            data.add("测试测试--"+(pre + i));
        }
        return data;
    }

    @Override
    protected List<String> convertData(List<String> datas) {
        return datas;
    }
}
