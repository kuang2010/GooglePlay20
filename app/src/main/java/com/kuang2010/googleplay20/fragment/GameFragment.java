package com.kuang2010.googleplay20.fragment;

import android.os.SystemClock;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.kuang2010.googleplay20.adapter.GameAdapter;
import com.kuang2010.googleplay20.base.BaseFragment;
import com.kuang2010.googleplay20.base.MianPagerControl;
import com.kuang2010.googleplay20.base.SuperLoadBaseProtocol;
import com.kuang2010.googleplay20.bean.AppInfoBean;
import com.kuang2010.googleplay20.factory.RecyclerViewFactory;
import com.kuang2010.googleplay20.protocol.GameProtocol;

import java.util.List;
import java.util.Random;

import androidx.recyclerview.widget.RecyclerView;

/**
 * author: kuangzeyu2019
 * date: 2020/6/9
 * time: 15:07
 * desc: 游戏
 */
public class GameFragment extends BaseFragment {
    List<AppInfoBean> mAppInfoBeans;
    @Override
    protected void initData(final MianPagerControl.ILoadDataFinishPageStateCallBack callBack) {
        GameProtocol gameProtocol = new GameProtocol();
        gameProtocol.loadData(0, callBack, new SuperLoadBaseProtocol.OnLoadDataResultListener<AppInfoBean>() {
            @Override
            public void setItemBeans(List<AppInfoBean> appInfoBeans) {
                mAppInfoBeans = appInfoBeans;
            }

            @Override
            public void setLunboPics(List<String> mPictures) {

            }
        },null);
    }

    @Override
    protected View initSuccessView() {
        RecyclerView rv = RecyclerViewFactory.createRecyclerView(mContext);
        GameAdapter adapter = new GameAdapter(mContext);
        rv.setAdapter(adapter);
        adapter.setItemBeans(mAppInfoBeans);
        return rv;
    }
}
