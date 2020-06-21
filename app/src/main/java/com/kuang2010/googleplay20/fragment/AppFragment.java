package com.kuang2010.googleplay20.fragment;

import android.view.View;

import com.kuang2010.googleplay20.adapter.AppAdapter;
import com.kuang2010.googleplay20.base.BaseFragment;
import com.kuang2010.googleplay20.base.BaseProtocol;
import com.kuang2010.googleplay20.base.BaseViewHold;
import com.kuang2010.googleplay20.base.LoadingPager;
import com.kuang2010.googleplay20.base.SuperLoadBaseProtocol;
import com.kuang2010.googleplay20.bean.AppInfoBean;
import com.kuang2010.googleplay20.factory.RecyclerViewFactory;
import com.kuang2010.googleplay20.manager.DownLoadManager;
import com.kuang2010.googleplay20.protocol.AppLoadProtocol;
import com.kuang2010.googleplay20.viewhold.AppInfoViewHold;

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
    private AppAdapter mAdapter;

    @Override
    protected void initData(final LoadingPager.ILoadDataFinishPageStateCallBack callBack) {
        AppLoadProtocol appLoadProtocol = new AppLoadProtocol();
        appLoadProtocol.loadListData(0, callBack, new SuperLoadBaseProtocol.OnLoadItemDataResultListener<AppInfoBean>() {
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
        mAdapter = new AppAdapter(mContext,mHasMoreData);
        rv.setAdapter(mAdapter);
        mAdapter.setItemBeans(mAppInfoBeans);
        if (isResumed()){
            addDownloadObserve();
        }
        return rv;
    }

    @Override
    public void onResume() {
        //添加观察者监视下载,onResume时集合viewHolds里可能还没有添加完所有的viewhold
        addDownloadObserve();
        super.onResume();
    }

    private void addDownloadObserve() {
        if (mAdapter!=null){
            List<BaseViewHold> viewHolds = mAdapter.getViewHolds();
            for (BaseViewHold baseViewHold:viewHolds){
                if (baseViewHold instanceof AppInfoViewHold){
                    DownLoadManager.getDownLoadManagerInstance().addDownLoadObserve(((AppInfoViewHold) baseViewHold));
                    mAdapter.notifyDataSetChanged();//触发bindData-->onBindViewHolder-->setData-->发布通知
                }
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        //移除观察者
        if (mAdapter!=null){
            List<BaseViewHold> viewHolds = mAdapter.getViewHolds();
            for (BaseViewHold baseViewHold:viewHolds){
                if (baseViewHold instanceof AppInfoViewHold){
                    DownLoadManager.getDownLoadManagerInstance().removeObserve(((AppInfoViewHold) baseViewHold));
                }
            }
        }
    }
}
