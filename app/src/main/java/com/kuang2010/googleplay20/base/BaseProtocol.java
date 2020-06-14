package com.kuang2010.googleplay20.base;

import com.kuang2010.googleplay20.conf.Constant;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;


/**
 * author: kuangzeyu2019
 * date: 2020/6/13
 * time: 13:30
 * desc: MainActivity的接口协议
 */
public abstract class BaseProtocol {

    /**有更多数据的回调监听,用于判断是否要加载更多数据*/
    protected OnHasMoreDataListener mOnHasMoreDataListener;
    public interface OnHasMoreDataListener {
        void setHasMoreData(boolean hasMoreData);
    }

    /**
     * 加载数据
     * @param index 分页加载的下标参数，即已加载完的数据个数
     */
    protected void loadData(int index) {// final BaseRvAdapter.ILoadMoreDataCallBack callBack
        String url = Constant.URlS.BASEURL + getInterceKey();//"home"
        RequestParams params = new RequestParams();
        params.setUri(url);

        params.addQueryStringParameter("index", index + "");
        params.setConnectTimeout(3000);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                BaseProtocol.this.onSuccess(result);
               /* Gson gson = new Gson();
                HomeBean homeBean = gson.fromJson(result, HomeBean.class);
                if (homeBean==null){
//                    mHasMoreData = false;
                    setHasMoreData(false);
                    callBack.setLoadingState(BaseRvAdapter.LoadingState.STATE_GONE);
                    return;
                }
                List<AppInfoBean> appInfoBeans = homeBean.list;
                if (appInfoBeans==null || appInfoBeans.size()==0){
//                    mHasMoreData = false;
                    setHasMoreData(false);
                    callBack.setLoadingState(BaseRvAdapter.LoadingState.STATE_GONE);
                    return;
                }
//                mHasMoreData = true;
                setHasMoreData(true);
                callBack.setLoadingState(BaseRvAdapter.LoadingState.STATE_LOAD_ERROR);
                callBack.setMoreDatas(appInfoBeans);*/
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                //mHasMoreData保持上一次的状态就行
//                callBack.setLoadingState(BaseRvAdapter.LoadingState.STATE_LOAD_ERROR);
                BaseProtocol.this.onError(ex,isOnCallback);
            }

            @Override
            public void onCancelled(CancelledException cex) {
                //mHasMoreData保持上一次的状态就行
//                callBack.setLoadingState(BaseRvAdapter.LoadingState.STATE_LOAD_ERROR);
                BaseProtocol.this.onCancelled(cex);
            }

            @Override
            public void onFinished() {

            }
        });
    }

    /**
     * 子类调用，设置是否还可以加载更多数据
     * @param hasMoreData 是否还有更多数据
     */
    protected void setHasMoreData(boolean hasMoreData) {
        if (mOnHasMoreDataListener !=null){
            mOnHasMoreDataListener.setHasMoreData(hasMoreData);
        }
    }


    /**
     * 获取区分协议接口url的关键字
     * @return 区分协议接口url的关键字
     */
    protected abstract String getInterceKey();

    protected abstract void onCancelled(Callback.CancelledException cex);

    protected abstract void onError(Throwable ex, boolean isOnCallback);

    protected abstract void onSuccess(String result);
}
