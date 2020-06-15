package com.kuang2010.googleplay20;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;
import android.view.ViewTreeObserver;

import com.google.android.material.tabs.TabLayout;
import com.kuang2010.googleplay20.adapter.MainPagerAdapter;
import com.kuang2010.googleplay20.base.BaseFragment;
import com.kuang2010.googleplay20.bean.TabFragmentBean;
import com.kuang2010.googleplay20.fragment.AppFragment;
import com.kuang2010.googleplay20.fragment.CategoryFragment;
import com.kuang2010.googleplay20.fragment.GameFragment;
import com.kuang2010.googleplay20.fragment.HomeFragment;
import com.kuang2010.googleplay20.fragment.HotFragment;
import com.kuang2010.googleplay20.fragment.RecommendFragment;
import com.kuang2010.googleplay20.fragment.SubjectFragment;
import com.kuang2010.googleplay20.fragment.TestFragment;
import com.kuang2010.googleplay20.util.GsonUtil;

import org.xutils.x;

import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private TabLayout mTab_main;
    private ViewPager mVp_main;
    private ArrayList<TabFragmentBean> mTabFragmentBeans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initXutil();
        initView();
        initData();// 创建Fragement的过程需要时间(视图,loadingpager)
        initEvent();

    }

    private void initXutil() {
        x.Ext.init(getApplication());
    }

    private void initEvent() {
        //优化数据触发加载时机
        mVp_main.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mVp_main.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                BaseFragment baseFragment = mTabFragmentBeans.get(0).getBaseFragment();
                baseFragment.getMianPagerControl().triggerLoadData();//需要判断MianPagerControl()是否为空
            }
        });
        mVp_main.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                BaseFragment baseFragment = mTabFragmentBeans.get(position).getBaseFragment();
                baseFragment.getMianPagerControl().triggerLoadData();//需要判断MianPagerControl()是否为空
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mTab_main.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.d("tagtag","onTabSelected_tab:"+tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                Log.d("tagtag","onTabUnselected_tab:"+tab.getPosition());
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                Log.d("tagtag","onTabReselected_tab:"+tab.getPosition());
            }
        });
    }

    private void initData() {
        MainPagerAdapter pagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
        mVp_main.setAdapter(pagerAdapter);
        mTab_main.setupWithViewPager(mVp_main,true);

        final String[] tabTitles = getResources().getStringArray(R.array.tab_title);
        mTabFragmentBeans = new ArrayList<>();
        mTabFragmentBeans.add(new TabFragmentBean(new HomeFragment(), tabTitles[0]));
        mTabFragmentBeans.add(new TabFragmentBean(new AppFragment(), tabTitles[1]));
        mTabFragmentBeans.add(new TabFragmentBean(new GameFragment(), tabTitles[2]));
        mTabFragmentBeans.add(new TabFragmentBean(new SubjectFragment(), tabTitles[3]));
        mTabFragmentBeans.add(new TabFragmentBean(new RecommendFragment(), tabTitles[4]));
        mTabFragmentBeans.add(new TabFragmentBean(new CategoryFragment(), tabTitles[5]));
        mTabFragmentBeans.add(new TabFragmentBean(new HotFragment(), tabTitles[6]));
        mTabFragmentBeans.add(new TabFragmentBean(new TestFragment(), tabTitles[7]));

        pagerAdapter.setDatas(mTabFragmentBeans);

//        for (int i=0;i<tabTitles.length;i++){
//            TabLayout.Tab tab = mTab_main.newTab().setText(tabTitles[i]);
//            mTab_main.addTab(tab);
//        }

//        TabLayout.Tab tab0 = mTab_main.getTabAt(0);
//        mTab_main.getTabAt(0).setIcon(R.mipmap.ic_launcher);
//        ImageView imageView = new ImageView(this);
//        imageView.setImageResource(R.mipmap.ic_launcher);
//        mTab_main.getTabAt(0).setCustomView(imageView);

//        for (int i=0;i<tabTitles.length;i++){
//            TabLayout.TabView tabView = mTab_main.getTabAt(i).view;
//            TextView textView = (TextView) tabView.getChildAt(1);
//            textView.setTextColor(R.color.red);
//            textView.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,30,getResources().getDisplayMetrics()));
//        }
    }

    private void initView() {
        mTab_main = findViewById(R.id.tab_main);
        mVp_main = findViewById(R.id.vp_main);
    }
}
