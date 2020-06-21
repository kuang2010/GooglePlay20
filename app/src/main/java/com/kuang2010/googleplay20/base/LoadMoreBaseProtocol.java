package com.kuang2010.googleplay20.base;

import org.xutils.common.Callback;

import java.util.List;

/**
 * author: kuangzeyu2019
 * date: 2020/6/13
 * time: 13:30
 * desc: 加载更多数据的协议 ， 作废
 * T是接口返回的所有数据类型，有可能是集合，有可能是bean。
 * ItemBean是接口返回的所有数据里的一种bean数据类型
 */
public abstract  class LoadMoreBaseProtocol<T, ItemBean> extends BaseProtocol {

    /**
     * 下拉加载更多数据的回调
     */
    BaseRvAdapter.ILoadMoreDataAndStateCallBack moreCallBack;


    @Override
    protected void onCancelled(Callback.CancelledException cex) {
        moreCallBack.setLoadingState(BaseRvAdapter.LoadingState.STATE_LOAD_ERROR);
    }

    @Override
    protected void onError(Throwable ex, boolean isOnCallback) {
        moreCallBack.setLoadingState(BaseRvAdapter.LoadingState.STATE_LOAD_ERROR);
    }

    @Override
    protected void onSuccess(String result) {
//        HomeBean homeBean = gson.fromJson(result, HomeBean.class);
        T t = parasJsonString(result);
        if (t == null) {
            setHasMoreData(false);
            moreCallBack.setLoadingState(BaseRvAdapter.LoadingState.STATE_FINISH_GONE);
            return;
        }

//        List<AppInfoBean> appInfoBeans = homeBean.list;
        List<ItemBean> list = getItemBeans(t);
        if (list == null || list.size() == 0) {
            setHasMoreData(false);
            moreCallBack.setLoadingState(BaseRvAdapter.LoadingState.STATE_FINISH_GONE);
            return;
        }
        setHasMoreData(true);
        moreCallBack.setLoadingState(BaseRvAdapter.LoadingState.STATE_FINISH_GONE);
        moreCallBack.setMoreDatas(list);
    }


    /**
     * 子类复写该方法，获取加载更多时的一般的条目数据
     * @param t 网络加载到的所有源数据
     * @return 一般的条目数据
     */
    protected abstract List<ItemBean> getItemBeans(T t);

    /**
     * 子类复写该方法，解析网络加载到的json字符串数据
     * @param result json字符串数据
     * @return 解析后的数据 可能是bean，也可能是集合
     */
    protected abstract T parasJsonString(String result);

    /**
     * 外界调用，加载更多数据
     * @param callBack 下拉加载更多数据的回调
     * @param index 分页加载数据的下标参数，即已加载完的数据个数
     * @param onHasMoreDataListener 有更多数据的回调监听,用于判断是否要加载更多数据
     */
    public void loadMoreData(BaseRvAdapter.ILoadMoreDataAndStateCallBack callBack, int index, OnHasMoreDataListener onHasMoreDataListener){
        super.loadData(index);
        this.moreCallBack = callBack;
        mOnHasMoreDataListener = onHasMoreDataListener;
    }
}
