package com.kuang2010.googleplay20.base;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.FrameLayout;

import com.kuang2010.googleplay20.R;

import androidx.annotation.NonNull;

/**
 * author: kuangzeyu2019
 * date: 2020/6/9
 * time: 19:24
 * desc:
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
    public void triggerLoadData(){
        if (mPageState != PageState.STATE_SUCCESS && mPageState != PageState.STATE_LOADING){
            mPageState = PageState.STATE_LOADING;
            refreshUiByState();

            initData(new ILoadDataFinishCallBack() {
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

}
