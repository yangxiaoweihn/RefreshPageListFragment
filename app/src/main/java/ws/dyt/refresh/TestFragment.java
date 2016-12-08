package ws.dyt.refresh;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ws.dyt.pagelist.config.ResponseResultWrapper;
import ws.dyt.pagelist.entity.IPageIndex;
import ws.dyt.pagelist.fragment.PageListFragment;
import ws.dyt.view.adapter.SuperAdapter;
import ws.dyt.view.viewholder.BaseViewHolder;

/**
 * Created by yangxiaowei on 16/6/29.
 */
public class TestFragment extends PageListFragment<TestFragment.TestEntity, TestFragment.TestEntity> {



    @Override
    public RecyclerView.LayoutManager initLayoutManager() {
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        return llm;
    }

    @Override
    public SuperAdapter<TestEntity> initAdapter() {
        return new SuperAdapter<TestEntity>(getContext(), new ArrayList<TestEntity>(), R.layout.item_text_c){
            @Override
            public void convert(BaseViewHolder holder, int position) {
                holder.setText(R.id.tv_text, getItem(position).des);
            }
        };
    }

    private static final int PAGE_SIZE = 10;
    @Override
    public int initPageSize() {
        return PAGE_SIZE;
    }

    int current = 0;
    @Override
    public void sendRequest(int index) {
        if (count == 1) {
            count = 0;
            setOnSuccessPath(new ResponseResultWrapper<>(1, "Success", new ArrayList<TestEntity>()));
            return;
        }
        count ++;


        if (isError) {
            setOnSuccessPath(new ResponseResultWrapper<TestEntity>(9, "XXX", null));
            return;
        }
        List<TestEntity> data = this.generate(index);
        current += data.size();
        setOnSuccessPath(new ResponseResultWrapper<>(0, "Success", data));
    }

    int count = 0;
    private List<TestEntity> generate(int pre) {

        if (0 == pre) {
            current = 0;
        }
        int r = dataCount - current;
        r =  r >= PAGE_SIZE ? PAGE_SIZE : r;
        List<TestEntity> data = new ArrayList<>();
        for (int i = 1; i <= r; i++) {
            data.add(new TestEntity(i, "测试测试--"+(pre + i)));
        }
        return data;
    }


    private int dataCount = 5;
    /**
     * 模拟总数据量
     * @return
     */
    protected void amulatorDataCounts(int dataCount) {
        this.dataCount = dataCount;
    }

    private boolean isError = false;
    /**
     * 模拟网络异常
     * @return
     */
    protected void amulatorError(boolean isError) {
        this.isError = isError;
    }


    @Override
    public List<TestEntity> flatMap(List<TestEntity> datas) {
        return datas;
    }

    static class TestEntity implements IPageIndex {
        public int id;
        public String des;

        public TestEntity(int id, String des) {
            this.id = id;
            this.des = des;
        }

        @Override
        public int getPageIndex() {
            return this.id;
        }
    }
}
