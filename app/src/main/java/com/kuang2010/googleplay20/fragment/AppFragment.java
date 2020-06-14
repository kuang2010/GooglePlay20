package com.kuang2010.googleplay20.fragment;

import android.view.View;

import com.kuang2010.googleplay20.adapter.AppAdapter;
import com.kuang2010.googleplay20.base.BaseFragment;
import com.kuang2010.googleplay20.base.BaseProtocol;
import com.kuang2010.googleplay20.base.MianPagerControl;
import com.kuang2010.googleplay20.base.SuperLoadBaseProtocol;
import com.kuang2010.googleplay20.bean.AppInfoBean;
import com.kuang2010.googleplay20.factory.RecyclerViewFactory;
import com.kuang2010.googleplay20.protocol.AppLoadProtocol;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/**
 * author: kuangzeyu2019
 * date: 2020/6/9
 * time: 15:06
 * desc: 应用
 */
public class AppFragment extends BaseFragment {
    List<AppInfoBean> mAppInfoBeans;
    boolean mHasMoreData;
    @Override
    protected void initData(final MianPagerControl.ILoadDataFinishPageStateCallBack callBack) {
        AppLoadProtocol appLoadProtocol = new AppLoadProtocol();
        appLoadProtocol.loadData(0, callBack, new SuperLoadBaseProtocol.OnLoadDataResultListener<AppInfoBean>() {
            @Override
            public void setItemBeans(List<AppInfoBean> appInfoBeans) {
                mAppInfoBeans = appInfoBeans;
            }

            @Override
            public void setLunboPics(List<String> mPictures) {

            }
        }, new BaseProtocol.OnHasMoreDataListener() {
            @Override
            public void setHasMoreData(boolean hasMoreData) {
                mHasMoreData = hasMoreData;
            }
        });
    }

    @Override
    protected View initSuccessView() {
        RecyclerView rv = RecyclerViewFactory.createRecyclerView(mContext);
        AppAdapter adapter = new AppAdapter(mContext,mHasMoreData);
        rv.setAdapter(adapter);
        adapter.setItemBeans(mAppInfoBeans);
        return rv;
    }
}
