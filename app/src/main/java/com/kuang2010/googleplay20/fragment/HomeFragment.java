package com.kuang2010.googleplay20.fragment;

import android.os.SystemClock;
import android.util.Log;
import android.view.Gravity;
import android.view.View;

import com.google.gson.Gson;
import com.kuang2010.googleplay20.adapter.HomeRvAdapter;
import com.kuang2010.googleplay20.base.BaseFragment;
import com.kuang2010.googleplay20.base.MianPagerControl;
import com.kuang2010.googleplay20.bean.AppInfoBean;
import com.kuang2010.googleplay20.bean.HomeBean;
import com.kuang2010.googleplay20.conf.Constant;
import com.kuang2010.googleplay20.factory.ThreadPoolExecutorProxyFactory;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;
import java.util.Random;

import androidx.recyclerview.widget.LinearLayoutManager;
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

    @Override
    protected void initData(final MianPagerControl.ILoadDataFinishCallBack callBack) {

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
                    callBack.onPageStateResult(MianPagerControl.PageState.STATE_EMPTY);
                    mHasMoreData = false;
                    return;
                }
                List<AppInfoBean> appInfoBeans = homeBean.list;
                if (appInfoBeans==null || appInfoBeans.size()==0){
                    callBack.onPageStateResult(MianPagerControl.PageState.STATE_EMPTY);
                    mHasMoreData = false;
                    return;
                }

                mAppInfoBeans = appInfoBeans;
                mPictures = homeBean.picture;
                callBack.onPageStateResult(MianPagerControl.PageState.STATE_SUCCESS);
                mHasMoreData = true;//首次还有更多数据

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                //mHasMoreData保持初始的状态就行
                callBack.onPageStateResult(MianPagerControl.PageState.STATE_ERROR);
                //JAVA.NET.SOCKETEXCEPTION: SOCKET FAILED: EPERM (OPERATION NOT PERMITTED) 解决方法
                //卸载重装
            }

            @Override
            public void onCancelled(CancelledException cex) {
                //mHasMoreData保持初始的状态就行
                callBack.onPageStateResult(MianPagerControl.PageState.STATE_ERROR);
            }

            @Override
            public void onFinished() {

            }
        });
    }

    @Override
    protected View initSuccessView() {
        RecyclerView rv = new RecyclerView(mContext);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        rv.setLayoutManager(layoutManager);
        HomeRvAdapter adapter = new HomeRvAdapter(getContext()) {
            @Override
            public void loadMoreData(final ILoadMoreDataCallBack callBack) {
                Log.d("tagtag",">>>>>>>>>>>triggerLoadMoreData");
                ThreadPoolExecutorProxyFactory.createNormalThreadPoolExecutorProxy().submit(new Runnable() {
                    @Override
                    public void run() {
                        int size = mAppInfoBeans.size();
                        Log.d("tagtag","mAppInfoBeans_size:"+size);
                        SystemClock.sleep(2000);
                        String url = Constant.URlS.BASEURL + "home";
                        RequestParams params = new RequestParams();
                        params.setUri(url);

                        params.addQueryStringParameter("index", size + "");
                        params.setConnectTimeout(3000);
                        x.http().get(params, new Callback.CommonCallback<String>() {
                            @Override
                            public void onSuccess(String result) {
                                Gson gson = new Gson();
                                HomeBean homeBean = gson.fromJson(result, HomeBean.class);
                                if (homeBean==null){
                                    mHasMoreData = false;
                                    callBack.setLoadState(LoadState.STATE_GONE);
                                    return;
                                }
                                List<AppInfoBean> appInfoBeans = homeBean.list;
                                if (appInfoBeans==null || appInfoBeans.size()==0){
                                    mHasMoreData = false;
                                    callBack.setLoadState(LoadState.STATE_GONE);
                                    return;
                                }
                                mAppInfoBeans.addAll(appInfoBeans);
                                setAppInfoBeans(mAppInfoBeans);
                                mHasMoreData = true;
                                callBack.setLoadState(LoadState.STATE_LOAD_ERROR);
                            }

                            @Override
                            public void onError(Throwable ex, boolean isOnCallback) {
                                //mHasMoreData保持上一次的状态就行
                                callBack.setLoadState(LoadState.STATE_LOAD_ERROR);
                            }

                            @Override
                            public void onCancelled(CancelledException cex) {
                                //mHasMoreData保持上一次的状态就行
                                callBack.setLoadState(LoadState.STATE_LOAD_ERROR);
                            }

                            @Override
                            public void onFinished() {

                            }
                        });
                    }
                });
            }

            @Override
            protected boolean hasMoreData() {
                Log.d("tagtag",">>>>>>>>>>>>>>>>hasMoreData");
                return mHasMoreData;//
            }
        };
        rv.setAdapter(adapter);
        adapter.setAppInfoBeans(mAppInfoBeans);
        return rv;
    }
}
