package com.kuang2010.googleplay20.base;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * author: kuangzeyu2019
 * date: 2020/6/9
 * time: 15:03
 * desc:
 */
public abstract class BaseFragment extends Fragment {

    private LoadingPager mLoadingPager;
    public Context mContext;

    public LoadingPager getLoadingPager() {
        return mLoadingPager;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mLoadingPager ==null){
            mLoadingPager = new LoadingPager(mContext) {
                @Override
                protected View initSuccessView() {
                    return BaseFragment.this.initSuccessView();
                }

                @Override
                protected void initData(ILoadDataFinishPageStateCallBack callBack) {
                    BaseFragment.this.initData(callBack);
                }

            };
        }
        if (mLoadingPager.getParent()!=null){
            ViewParent parent = mLoadingPager.getParent();
            if (parent instanceof ViewGroup){
                ((ViewGroup) parent).removeView(mLoadingPager);
            }
        }
//        mMianPagerControl.triggerLoadData();  //优化数据触发加载时机 selectPosition
        return mLoadingPager;
    }

    protected abstract void initData(LoadingPager.ILoadDataFinishPageStateCallBack callBack);

    protected abstract View initSuccessView();
}
