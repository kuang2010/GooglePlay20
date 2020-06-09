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

    private MianPagerControl mMianPagerControl;
    public Context mContext;

    public MianPagerControl getMianPagerControl() {
        return mMianPagerControl;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mMianPagerControl==null){
            mMianPagerControl = new MianPagerControl(mContext) {
                @Override
                protected View initSuccessView() {
                    return BaseFragment.this.initSuccessView();
                }

                @Override
                protected void initData(ILoadDataFinishCallBack callBack) {
                    BaseFragment.this.initData(callBack);
                }

            };
        }
        if (mMianPagerControl.getParent()!=null){
            ViewParent parent = mMianPagerControl.getParent();
            if (parent instanceof ViewGroup){
                ((ViewGroup) parent).removeView(mMianPagerControl);
            }
        }
//        mMianPagerControl.triggerLoadData();  //优化数据触发加载时机 selectPosition
        return mMianPagerControl;
    }

    protected abstract void initData(MianPagerControl.ILoadDataFinishCallBack callBack);

    protected abstract View initSuccessView();
}
