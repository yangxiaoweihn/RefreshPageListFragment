package ws.dyt.refresh.main;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ws.dyt.pagelist.ui.OnVisibleble;
import ws.dyt.refresh.R;
import ws.dyt.refresh.sub.NewsTabFragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListTabFragment extends Fragment {


    public ListTabFragment() {
        // Required empty public constructor
    }


    View rootView;
    ViewPager viewPager;
    TabLayout tabLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_main_tab_list, container, false);
        this.init();
        return rootView;
    }

    private void init() {
        viewPager = (ViewPager) rootView.findViewById(R.id.view_pager);
        tabLayout = (TabLayout) rootView.findViewById(R.id.tab_layout);

        List<String> titles = new ArrayList<>();
        List<Fragment> fragments = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            String title = "模块 "+i;
            titles.add(title);
            fragments.add(NewsTabFragment.newInstance(title));
        }

        viewPager.setOffscreenPageLimit(titles.size());

        viewPager.addOnPageChangeListener(pageChangeListener);

        adapter = new TabPager(getChildFragmentManager(), fragments, titles);
        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);

        viewPager.setCurrentItem(0);

        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

//        viewPager.addOnPageChangeListener(pageChangeListener);
    }

    private TabPager adapter;

    ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            Log.e("DEBUG", "index -> news: "+position);
            ((OnVisibleble) adapter.getItem(position)).onVisibleToUser(true);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };


    private class TabPager extends FragmentPagerAdapter {
        List<String> titles;
        List<Fragment> fragments;

        public TabPager(FragmentManager fm, List<Fragment> fragments, List<String> titles) {
            super(fm);
            this.fragments = fragments;
            this.titles = titles;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return titles.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }
}
