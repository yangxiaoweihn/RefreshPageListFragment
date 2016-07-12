package ws.dyt.refresh.main;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ws.dyt.refresh.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainTabFragment extends Fragment {


    View rootView;
    ViewPager viewPager;
    TabLayout tabLayout;
    public MainTabFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_main_tab, container, false);
        this.init();
        return rootView;


    }

    private void init() {
        viewPager = (ViewPager) rootView.findViewById(R.id.view_pager);
        tabLayout = (TabLayout) rootView.findViewById(R.id.tab_layout);

        String[] titles = new String[]{"列表", "首页", "我的"};

        Fragment[] fragments = new Fragment[]{new ListTabFragment(), new IndexTabFragment(), new MyTabFragment()};

        viewPager.setOffscreenPageLimit(titles.length);

        viewPager.addOnPageChangeListener(pageChangeListener);

        viewPager.setAdapter(new TabPager(getFragmentManager(), new ArrayList<Fragment>(Arrays.asList(fragments)), new ArrayList<String>(Arrays.asList(titles))));

        tabLayout.setupWithViewPager(viewPager);

        viewPager.setCurrentItem(0);


//        viewPager.addOnPageChangeListener(pageChangeListener);
    }


    ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            Log.e("DEBUG", "index -> main: "+position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };


    private class TabPager extends FragmentPagerAdapter{
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
