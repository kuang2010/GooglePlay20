package com.kuang2010.googleplay20.base;

import com.google.gson.Gson;

import org.xutils.common.Callback;

import java.util.List;

/**
 * author: kuangzeyu2019
 * date: 2020/6/14
 * time: 17:56
 * desc: 整合LoadMoreBaseProtocol与 LoadBaseProtocol
 * 完成首次加载数据和加载更多的数据的网络协议
 *
 * T是接口返回的所有数据类型，有可能是集合，有可能是bean。
 * ItemBean是接口返回的所有数据里的一种bean数据类型
 */
public abstract class SuperLoadBaseProtocol<T,ItemBean> extends BaseProtocol{

    /** 首次加载数据的结果回调,用于获取数据*/
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
     * 首次加载数据完成后的页面状态回调，用于通知更新页面UI
     * */
    private MianPagerControl.ILoadDataFinishPageStateCallBack pageStateCallBack;


    /**
     * 下拉加载更多数据的回调
     */
    BaseRvAdapter.ILoadMoreDataAndStateCallBack moreDataAndStateCallBack;


    @Override
    protected void onCancelled(Callback.CancelledException cex) {
        if (pageStateCallBack!=null)  pageStateCallBack.setLoadingFinishPageStateAndRefreshUi(MianPagerControl.PageState.STATE_ERROR);
        if(moreDataAndStateCallBack!=null)  moreDataAndStateCallBack.setLoadingState(BaseRvAdapter.LoadingState.STATE_LOAD_ERROR);
    }

    @Override
    protected void onError(Throwable ex, boolean isOnCallback) {
        if (pageStateCallBack!=null)pageStateCallBack.setLoadingFinishPageStateAndRefreshUi(MianPagerControl.PageState.STATE_ERROR);
        if (moreDataAndStateCallBack!=null)moreDataAndStateCallBack.setLoadingState(BaseRvAdapter.LoadingState.STATE_LOAD_ERROR);
    }

    @Override
    protected void onSuccess(String result) {
        Gson gson = new Gson();
//        HomeBean homeBean = gson.fromJson(result, HomeBean.class);
        T t = parasJsonString(result);
        if (t==null){
            setHasMoreData(false);
            if (pageStateCallBack!=null)pageStateCallBack.setLoadingFinishPageStateAndRefreshUi(MianPagerControl.PageState.STATE_EMPTY);
            if (moreDataAndStateCallBack!=null)moreDataAndStateCallBack.setLoadingState(BaseRvAdapter.LoadingState.STATE_FINISH_GONE);
            return;
        }


//        List<AppInfoBean> appInfoBeans = homeBean.list;
        List<ItemBean> itemBeans = getItemBeans(t);
        if (itemBeans==null || itemBeans.size()==0){
            setHasMoreData(false);
            if (pageStateCallBack!=null)pageStateCallBack.setLoadingFinishPageStateAndRefreshUi(MianPagerControl.PageState.STATE_EMPTY);
            if (moreDataAndStateCallBack!=null)moreDataAndStateCallBack.setLoadingState(BaseRvAdapter.LoadingState.STATE_FINISH_GONE);
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
        if (moreDataAndStateCallBack!=null)moreDataAndStateCallBack.setLoadingState(BaseRvAdapter.LoadingState.STATE_FINISH_GONE);
        if (moreDataAndStateCallBack!=null)moreDataAndStateCallBack.setMoreDatas(itemBeans);

        if (pageStateCallBack!=null)pageStateCallBack.setLoadingFinishPageStateAndRefreshUi(MianPagerControl.PageState.STATE_SUCCESS);


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
     * @param pageStateCallBack 加载数据完成后的回调，用于刷新UI
     * @param onLoadDataResultListener 加载数据的结果回调,用于获取首次的结果数据
     * @param onHasMoreDataListener  有更多数据的回调监听,用于判断是否要加载更多数据
     */
    public void loadData(int index, MianPagerControl.ILoadDataFinishPageStateCallBack pageStateCallBack, OnLoadDataResultListener<ItemBean> onLoadDataResultListener,  OnHasMoreDataListener onHasMoreDataListener) {
        this.pageStateCallBack = pageStateCallBack;
        mOnLoadDataResultListener = onLoadDataResultListener;
        mOnHasMoreDataListener = onHasMoreDataListener;
        super.loadData(index);
    }

    /**
     * 外界调用，加载更多数据
     * @param index 分页加载数据的下标参数，即已加载完的数据个数
     * @param moreDataAndStateCallBack 下拉加载更多数据的回调,用于获取更多的结果数据
     * @param onHasMoreDataListener  有更多数据的回调监听,用于判断是否要加载更多数据
     */
    public void loadMoreData(int index,BaseRvAdapter.ILoadMoreDataAndStateCallBack moreDataAndStateCallBack,  OnHasMoreDataListener onHasMoreDataListener){
        this.moreDataAndStateCallBack = moreDataAndStateCallBack;
        mOnHasMoreDataListener = onHasMoreDataListener;
        super.loadData(index);
    }
}
