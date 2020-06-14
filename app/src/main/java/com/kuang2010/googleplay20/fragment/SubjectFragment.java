package com.kuang2010.googleplay20.fragment;

import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.kuang2010.googleplay20.base.BaseFragment;
import com.kuang2010.googleplay20.base.MianPagerControl;

/**
 * author: kuangzeyu2019
 * date: 2020/6/9
 * time: 15:08
 * desc: 专题
 */
public class SubjectFragment extends BaseFragment {
    @Override
    protected void initData(final MianPagerControl.ILoadDataFinishPageStateCallBack callBack) {

    }

    @Override
    protected View initSuccessView() {
        TextView tv = new TextView(mContext);
        tv.setText(getClass().getSimpleName());
        tv.setGravity(Gravity.CENTER);
        return tv;
    }
}
