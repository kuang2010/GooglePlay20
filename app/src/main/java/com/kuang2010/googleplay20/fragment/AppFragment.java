package com.kuang2010.googleplay20.fragment;

import android.os.SystemClock;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.kuang2010.googleplay20.base.BaseFragment;
import com.kuang2010.googleplay20.base.MianPagerControl;

import java.util.Random;

/**
 * author: kuangzeyu2019
 * date: 2020/6/9
 * time: 15:06
 * desc: 应用
 */
public class AppFragment extends BaseFragment {
    @Override
    protected void initData(final MianPagerControl.ILoadDataFinishCallBack callBack) {
        new Thread(){
            @Override
            public void run() {
                super.run();
                SystemClock.sleep(2000);

                MianPagerControl.PageState[] pageStates = new MianPagerControl.PageState[]{
                        MianPagerControl.PageState.STATE_SUCCESS,
                        MianPagerControl.PageState.STATE_ERROR,
                        MianPagerControl.PageState.STATE_EMPTY,
                };
                Random random = new Random();
                int index = random.nextInt(pageStates.length) ;
                callBack.onPageStateResult(pageStates[index]);

            }
        }.start();
    }

    @Override
    protected View initSuccessView() {
        TextView tv = new TextView(mContext);
        tv.setText(this.getClass().getSimpleName());
        tv.setGravity(Gravity.CENTER);
        return tv;
    }
}
