package com.kuang2010.googleplay20.base;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.kuang2010.googleplay20.R;
import com.kuang2010.googleplay20.factory.ThreadPoolExecutorProxyFactory;

import androidx.annotation.NonNull;

/**
 * author: kuangzeyu2019
 * date: 2020/6/9
 * time: 19:24
 * desc: 放置4个常见的页面
 * 	 ① 加载页面
 * 	 ② 错误页面
 * 	 ③ 空页面
 * 	 ④ 成功页面
 */
public abstract class MianPagerControl extends FrameLayout {

    private Context mContext;
    private View mLoadingView;
    private View mErrorView;
    private View mEmptyView;
    private View mSuccessView;

    private PageState mPageState = PageState.STATE_INIT;

    public enum PageState{
        STATE_INIT(0),STATE_LOADING(1),STATE_SUCCESS(2),STATE_ERROR(3),STATE_EMPTY(4);
        private int mState;
        private PageState(int state){
            mState = state;
        }
        public int getState() {
            return mState;
        }
    }

    public MianPagerControl(@NonNull Context context) {
        super(context);
        mContext = context;
        initCommonView();
//        setBackgroundColor(Color.RED);
    }

    private void initCommonView() {
        mLoadingView = View.inflate(mContext, R.layout.pager_loading, null);
        addView(mLoadingView);
        mErrorView = View.inflate(mContext, R.layout.pager_error, null);
        mErrorView.findViewById(R.id.error_btn_retry).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                triggerLoadData();
            }
        });
        addView(mErrorView);
        mEmptyView = View.inflate(mContext, R.layout.pager_empty, null);
        addView(mEmptyView);
        refreshUiByState();
    }

    private void refreshUiByState() {
        if (mPageState==PageState.STATE_LOADING){
            mLoadingView.setVisibility(VISIBLE);
        }else {
            mLoadingView.setVisibility(GONE);
        }

        if (mPageState == PageState.STATE_EMPTY){
            mEmptyView.setVisibility(VISIBLE);
        }else {
            mEmptyView.setVisibility(GONE);
        }

        if (mPageState == PageState.STATE_ERROR){
            mErrorView.setVisibility(VISIBLE);
        }else {
            mErrorView.setVisibility(GONE);
        }

        if (mSuccessView !=null){
            if (mPageState == PageState.STATE_SUCCESS ){
                mSuccessView.setVisibility(VISIBLE);
            }else {
                mSuccessView.setVisibility(GONE);
            }
        }else {
            if (mPageState == PageState.STATE_SUCCESS){
                mSuccessView = initSuccessView();
                addView(mSuccessView);
            }
        }

    }

    private Handler mHandler = new Handler();
    /**
     * @des 触发异步加载数据
     * @call 外界需要触发加载数据的时候调用该方法
     */
    public void triggerLoadData(){
        if (mPageState != PageState.STATE_SUCCESS && mPageState != PageState.STATE_LOADING){
            mPageState = PageState.STATE_LOADING;
            refreshUiByState();

            ThreadPoolExecutorProxyFactory.createNormalThreadPoolExecutorProxy().submit(new TestLoadDataTask());

            initData(new ILoadDataFinishCallBack() {//(线程+接口回调) 实现先数据再逻辑的异步加载
                @Override
                public void onPageStateResult(PageState pageState) {
                    mPageState = pageState;
                    if (Looper.myLooper() == Looper.getMainLooper()){
                        refreshUiByState();
                    }else {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                refreshUiByState();
                            }
                        });
                    }
                }
            });


        }
    }

    protected abstract View initSuccessView();
    protected abstract void initData(ILoadDataFinishCallBack callBack);

    /**
     * 加载数据完成后的回调，用于刷新UI
     * */
    public interface ILoadDataFinishCallBack {
        void onPageStateResult(PageState pageState);
    }

    private class TestLoadDataTask implements Runnable {
        @Override
        public void run() {
            SystemClock.sleep(2000);
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    Log.d("tagtag","测试测试加载数据完成，刷新UI");
                }
            });

        }
    }
}
