package ws.dyt.refresh;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ws.dyt.pagelist.entity.PageIndex;
import ws.dyt.pagelist.ui.BasePageListFragment;
import ws.dyt.view.adapter.SuperAdapter;
import ws.dyt.view.viewholder.BaseViewHolder;

/**
 * Created by yangxiaowei on 16/6/29.
 */
public class TestFragment extends BasePageListFragment<TestFragment.TestEntity, TestFragment.TestEntity> {


    @Override
    protected void setUpViewBeforeSetAdapter() {

    }

    @Override
    protected RecyclerView.LayoutManager setLayoutManager() {
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        return llm;
    }

    @Override
    protected SuperAdapter<TestEntity> setAdapter() {
        return new SuperAdapter<TestEntity>(getContext(), new ArrayList<TestEntity>(), R.layout.item_text_c){
            @Override
            public void convert(BaseViewHolder holder, int position) {
                holder.setText(R.id.tv_text, getItem(position).des);
            }
        };
    }

    private static final int PAGE_SIZE = 10;
    @Override
    protected int setPageSize() {
        return PAGE_SIZE;
    }

    int current = 0;
    @Override
    protected void fetchData(int index) {
        if (count == 3) {
            count = 0;
            setOnSuccessPath(new ResponseResultWrapper<>(0, "Success", new ArrayList<TestEntity>()));
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
    protected List<TestEntity> convertData(List<TestEntity> datas) {
        return datas;
    }

    static class TestEntity implements PageIndex{
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
