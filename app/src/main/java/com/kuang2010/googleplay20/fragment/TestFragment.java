package com.kuang2010.googleplay20.fragment;

import android.os.SystemClock;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.kuang2010.googleplay20.base.BaseFragment;
import com.kuang2010.googleplay20.base.LoadingPager;

import java.util.Random;

/**
 * author: kuangzeyu2019
 * date: 2020/6/9
 * time: 15:07
 * desc: 首页
 */
public class TestFragment extends BaseFragment {

    @Override
    protected void initData(final LoadingPager.ILoadDataFinishPageStateCallBack callBack) {
        new Thread(){
            @Override
            public void run() {
                super.run();
                SystemClock.sleep(2000);

                LoadingPager.PageState[] pageStates = new LoadingPager.PageState[]{
                        LoadingPager.PageState.STATE_SUCCESS,
                        LoadingPager.PageState.STATE_ERROR,
                        LoadingPager.PageState.STATE_EMPTY,
                };
                Random random = new Random();
                int index = random.nextInt(pageStates.length) ;
                callBack.setLoadingFinishPageStateAndRefreshUi(pageStates[index]);

            }
        }.start();
    }

    @Override
    protected View initSuccessView() {
        TextView tv = new TextView(mContext);
        tv.setText(this.getClass().getSimpleName());
        tv.setGravity(Gravity.TOP);
        return tv;
    }
}
