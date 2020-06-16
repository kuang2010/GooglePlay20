package com.kuang2010.googleplay20.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.kuang2010.googleplay20.R;
import com.kuang2010.googleplay20.base.BaseProtocol;
import com.kuang2010.googleplay20.base.BaseRvAdapter;
import com.kuang2010.googleplay20.base.BaseViewHold;
import com.kuang2010.googleplay20.bean.AppInfoBean;
import com.kuang2010.googleplay20.protocol.AppLoadProtocol;
import com.kuang2010.googleplay20.viewhold.AppInfoViewHold;

/**
 * author: kuangzeyu2019
 * date: 2020/6/13
 * time: 21:14
 * desc:
 */
public class AppAdapter extends BaseRvAdapter<AppInfoBean> {

    private boolean mHasMoreData;
    public AppAdapter(Context context,boolean hasMoreData) {
        super(context);
        mHasMoreData = hasMoreData;
    }

    @Override
    protected BaseViewHold getItemInfoViewHold(ViewGroup parent) {
        return new AppInfoViewHold(mContext, LayoutInflater.from(mContext).inflate(R.layout.item_home_info,parent,false));

    }

    @Override
    protected boolean hasMoreData() {
        return mHasMoreData;
    }

    @Override
    protected void loadMoreData(int size, ILoadMoreDataAndStateCallBack callBack) {
        AppLoadProtocol appLoadMoreProtocol = new AppLoadProtocol();
        appLoadMoreProtocol.loadMoreListData( size,callBack, new BaseProtocol.OnHasMoreDataListener() {
            @Override
            public void setHasMoreData(boolean hasMoreData) {
                mHasMoreData = hasMoreData;
            }
        });
    }
}
