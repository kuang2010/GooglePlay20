package com.kuang2010.googleplay20.fragment;

import android.view.View;

import com.kuang2010.googleplay20.adapter.GameAdapter;
import com.kuang2010.googleplay20.base.BaseFragment;
import com.kuang2010.googleplay20.base.BaseViewHold;
import com.kuang2010.googleplay20.base.LoadingPager;
import com.kuang2010.googleplay20.base.SuperLoadBaseProtocol;
import com.kuang2010.googleplay20.bean.AppInfoBean;
import com.kuang2010.googleplay20.factory.RecyclerViewFactory;
import com.kuang2010.googleplay20.manager.DownLoadManager;
import com.kuang2010.googleplay20.protocol.GameProtocol;
import com.kuang2010.googleplay20.viewhold.AppInfoViewHold;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/**
 * author: kuangzeyu2019
 * date: 2020/6/9
 * time: 15:07
 * desc: 游戏
 */
public class GameFragment extends BaseFragment {
    List<AppInfoBean> mAppInfoBeans;
    private GameAdapter mAdapter;

    @Override
    protected void initData(final LoadingPager.ILoadDataFinishPageStateCallBack callBack) {
        GameProtocol gameProtocol = new GameProtocol();
        gameProtocol.loadListData(0, callBack, new SuperLoadBaseProtocol.OnLoadItemDataResultListener<AppInfoBean>() {
            @Override
            public void setItemBeans(List<AppInfoBean> appInfoBeans) {
                mAppInfoBeans = appInfoBeans;
            }

            @Override
            public void setLunboPics(List<String> mPictures) {

            }
        },null);
    }

    @Override
    protected View initSuccessView() {
        RecyclerView rv = RecyclerViewFactory.createRecyclerView(mContext);
        mAdapter = new GameAdapter(mContext);
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
