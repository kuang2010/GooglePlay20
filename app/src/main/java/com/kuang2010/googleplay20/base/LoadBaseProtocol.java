package com.kuang2010.googleplay20.base;

import com.google.gson.Gson;

import org.xutils.common.Callback;

import java.util.List;

/**
 * author: kuangzeyu2019
 * date: 2020/6/13
 * time: 13:30
 * desc: 首次加载数据的协议
 * T是接口返回的所有数据类型，有可能是集合，有可能是bean。
 * ItemBean是接口返回的所有数据里的一种bean数据类型
 */
public abstract class LoadBaseProtocol<T,ItemBean> extends BaseProtocol{

    /** 加载数据的结果回调,用于获取数据*/
    private OnLoadDataResultListener<ItemBean> mOnLoadDataResultListener;
    public interface OnLoadDataResultListener<ItemBean>{
        /**
         * 外界获取一般条目数据
         * @param itemBeans 一般条目数据
         */
        void setItemBeans(List<ItemBean> itemBeans);
        /**
         * 外界获取轮播图数据
         * @param mPictures 轮播图数据
         */
        void setLunboPics(List<String> mPictures);
    }

    /**
     * 加载数据完成后的回调，用于通知刷新UI
     * */
    private LoadingPager.ILoadDataFinishPageStateCallBack callBack;


    @Override
    protected void onCancelled(Callback.CancelledException cex) {
        callBack.setLoadingFinishPageStateAndRefreshUi(LoadingPager.PageState.STATE_ERROR);

    }

    @Override
    protected void onError(Throwable ex, boolean isOnCallback) {
        callBack.setLoadingFinishPageStateAndRefreshUi(LoadingPager.PageState.STATE_ERROR);
    }

    @Override
    protected void onSuccess(String result) {
        Gson gson = new Gson();
//        HomeBean homeBean = gson.fromJson(result, HomeBean.class);
        T t = parasJsonString(result);
        if (t==null){
            setHasMoreData(false);
            callBack.setLoadingFinishPageStateAndRefreshUi(LoadingPager.PageState.STATE_EMPTY);
            return;
        }
//        List<AppInfoBean> appInfoBeans = homeBean.list;
        List<ItemBean> itemBeans = getItemBeans(t);
        if (itemBeans==null || itemBeans.size()==0){
            setHasMoreData(false);
            callBack.setLoadingFinishPageStateAndRefreshUi(LoadingPager.PageState.STATE_EMPTY);
            return;
        }

//        mPictures = homeBean.picture;
        List<String> mPictures = getLunboPictures(t);
        if (mOnLoadDataResultListener!=null){
            mOnLoadDataResultListener.setItemBeans(itemBeans);
            mOnLoadDataResultListener.setLunboPics(mPictures);
        }
        /*****************走到这里认为还有数据没加载完********************/
        setHasMoreData(true);//首次还有更多数据
        callBack.setLoadingFinishPageStateAndRefreshUi(LoadingPager.PageState.STATE_SUCCESS);
    }


    /**
     * 子类复写该方法，返回轮播图数据
     * @param t 网络加载到的所有源数据
     * @return 轮播图数据
     */
    protected List<String> getLunboPictures(T t) {
        return null;
    }

    /**
     * 子类复写该方法，返回一般的条目数据
     * @param t 网络加载到的所有源数据
     * @return 一般的条目数据
     */
    protected abstract List<ItemBean> getItemBeans(T t);

    /**
     * 子类复写该方法，解析网络加载到的json字符串数据
     * @param result json字符串数据
     * @return 解析后的数据 可能是bean，也可能是集合
     */
    protected abstract T parasJsonString(String result) ;

    /**
     * 外界调用，首次加载数据
     * @param index 分页加载数据的下标
     * @param callBack 加载数据完成后的回调，用于刷新UI
     * @param onLoadDataResultListener 加载数据的结果回调,用于获取数据
     * @param onHasMoreDataListener  有更多数据的回调监听,用于判断是否要加载更多数据
     */
    public void loadData(int index, LoadingPager.ILoadDataFinishPageStateCallBack callBack, OnLoadDataResultListener<ItemBean> onLoadDataResultListener, OnHasMoreDataListener onHasMoreDataListener) {
        super.loadData(index);//0
        this.callBack = callBack;
        mOnLoadDataResultListener = onLoadDataResultListener;
        mOnHasMoreDataListener = onHasMoreDataListener;
    }
}
