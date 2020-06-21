package com.kuang2010.googleplay20.fragment;

import android.view.View;

import com.kuang2010.googleplay20.adapter.HomeRvAdapter;
import com.kuang2010.googleplay20.base.BaseFragment;
import com.kuang2010.googleplay20.base.BaseProtocol;
import com.kuang2010.googleplay20.base.BaseViewHold;
import com.kuang2010.googleplay20.base.LoadingPager;
import com.kuang2010.googleplay20.base.SuperLoadBaseProtocol;
import com.kuang2010.googleplay20.bean.AppInfoBean;
import com.kuang2010.googleplay20.factory.RecyclerViewFactory;
import com.kuang2010.googleplay20.manager.DownLoadManager;
import com.kuang2010.googleplay20.protocol.HomeLoadProtocol;
import com.kuang2010.googleplay20.viewhold.AppInfoViewHold;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/**
 * author: kuangzeyu2019
 * date: 2020/6/9
 * time: 15:07
 * desc: 首页
 */
public class HomeFragment extends BaseFragment {

    List<AppInfoBean> mAppInfoBeans;
    List<String>		mPictures;
    private boolean mHasMoreData;
    private HomeRvAdapter mAdapter;

    @Override
    protected void initData(final LoadingPager.ILoadDataFinishPageStateCallBack callBack) {
/*
        RequestParams requestParams = new RequestParams();
        String url = Constant.URlS.BASEURL + "home";
        requestParams.setUri(url);
        requestParams.addQueryStringParameter("index", "0");
        requestParams.setConnectTimeout(3000);
        x.http().get(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                HomeBean homeBean = gson.fromJson(result, HomeBean.class);
                if (homeBean==null){
                    mHasMoreData = false;
                    callBack.setLoadingFinishPageStateAndRefreshUi(MianPagerControl.PageState.STATE_EMPTY);
                    return;
                }
                List<AppInfoBean> appInfoBeans = homeBean.list;
                if (appInfoBeans==null || appInfoBeans.size()==0){
                    mHasMoreData = false;
                    callBack.setLoadingFinishPageStateAndRefreshUi(MianPagerControl.PageState.STATE_EMPTY);
                    return;
                }

                mAppInfoBeans = appInfoBeans;
                mPictures = homeBean.picture;
                mHasMoreData = true;//首次还有更多数据
                callBack.setLoadingFinishPageStateAndRefreshUi(MianPagerControl.PageState.STATE_SUCCESS);

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                //mHasMoreData保持初始的状态就行
                callBack.setLoadingFinishPageStateAndRefreshUi(MianPagerControl.PageState.STATE_ERROR);
                //JAVA.NET.SOCKETEXCEPTION: SOCKET FAILED: EPERM (OPERATION NOT PERMITTED) 解决方法
                //卸载重装
            }

            @Override
            public void onCancelled(CancelledException cex) {
                //mHasMoreData保持初始的状态就行
                callBack.setLoadingFinishPageStateAndRefreshUi(MianPagerControl.PageState.STATE_ERROR);
            }

            @Override
            public void onFinished() {

            }
        });

  */

        HomeLoadProtocol homeLoadProtocol = new HomeLoadProtocol();
        homeLoadProtocol.loadListData(0, callBack, new SuperLoadBaseProtocol.OnLoadItemDataResultListener<AppInfoBean>() {
            @Override
            public void setItemBeans(List<AppInfoBean> appInfoBeans) {
                mAppInfoBeans = appInfoBeans;
            }

            @Override
            public void setLunboPics(List<String> pictures) {
                mPictures = pictures;
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
        mAdapter = new HomeRvAdapter(mContext,mHasMoreData);
        rv.setAdapter(mAdapter);
        mAdapter.setItemBeans(mAppInfoBeans);
        mAdapter.setLunboDatas(mPictures);
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
