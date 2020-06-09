package com.kuang2010.googleplay20.adapter;

import com.kuang2010.googleplay20.bean.TabFragmentBean;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

/**
 * author: kuangzeyu2019
 * date: 2020/6/9
 * time: 14:58
 * desc:
 */
public class MainPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<TabFragmentBean> mTabFragmentBeans;
    public void setDatas(ArrayList<TabFragmentBean> tabFragmentBeans) {
        mTabFragmentBeans = tabFragmentBeans;
        notifyDataSetChanged();
    }

    public MainPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return mTabFragmentBeans.get(position).getBaseFragment();
    }

    @Override
    public int getCount() {
        if (mTabFragmentBeans!=null){
            return mTabFragmentBeans.size();
        }
        return 0;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mTabFragmentBeans.get(position).getTabTitle();
    }
}
