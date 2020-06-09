package com.kuang2010.googleplay20.bean;

import com.kuang2010.googleplay20.base.BaseFragment;

/**
 * author: kuangzeyu2019
 * date: 2020/6/9
 * time: 15:11
 * desc:
 */
public class TabFragmentBean {
    private BaseFragment mBaseFragment;
    private String tabTitle;

    public TabFragmentBean(BaseFragment baseFragment, String tabTitle) {
        mBaseFragment = baseFragment;
        this.tabTitle = tabTitle;
    }

    public BaseFragment getBaseFragment() {
        return mBaseFragment;

    }

    public String getTabTitle() {
        return tabTitle == null ? "" : tabTitle;

    }
}
