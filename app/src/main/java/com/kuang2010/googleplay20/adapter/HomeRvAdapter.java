package com.kuang2010.googleplay20.adapter;

import android.content.Context;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.kuang2010.googleplay20.R;
import com.kuang2010.googleplay20.base.BaseProtocol;
import com.kuang2010.googleplay20.base.BaseRvAdapter;
import com.kuang2010.googleplay20.bean.AppInfoBean;
import com.kuang2010.googleplay20.base.BaseViewHold;
import com.kuang2010.googleplay20.factory.ThreadPoolExecutorProxyFactory;
import com.kuang2010.googleplay20.protocol.HomeLoadProtocol;
import com.kuang2010.googleplay20.viewhold.AppInfoViewHold;


/**
 * author: kuangzeyu2019
 * date: 2020/6/11
 * time: 15:13
 * desc:
 */
public  class HomeRvAdapter extends BaseRvAdapter<AppInfoBean>{

    private boolean mHasMoreData;
    public HomeRvAdapter(Context context, boolean hasMoreData) {
        super(context);
        mHasMoreData = hasMoreData;
    }

    @Override
    protected BaseViewHold getItemInfoViewHold(ViewGroup parent) {
        return new AppInfoViewHold(mContext,LayoutInflater.from(mContext).inflate(R.layout.item_home_info,parent,false));
    }

    @Override
    public void loadMoreData(final int size, final ILoadMoreDataAndStateCallBack callBack) {
        Log.d("tagtag",">>>>>>>>>>>triggerLoadMoreData");
        ThreadPoolExecutorProxyFactory.createNormalThreadPoolExecutorProxy().submit(new Runnable() {
            @Override
            public void run() {
                Log.d("tagtag","mAppInfoBeans_size:"+size);
                SystemClock.sleep(2000);
                HomeLoadProtocol homeLoadMoreProtocol = new HomeLoadProtocol();
                homeLoadMoreProtocol.loadMoreData(size, callBack, new BaseProtocol.OnHasMoreDataListener() {
                    @Override
                    public void setHasMoreData(boolean hasMoreData) {
                        mHasMoreData = hasMoreData;
                    }
                });

            }

        });
    }

    @Override
    protected boolean hasMoreData() {
        return mHasMoreData;
    }
}