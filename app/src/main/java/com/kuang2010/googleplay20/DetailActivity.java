package com.kuang2010.googleplay20;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.kuang2010.googleplay20.base.LoadingPager;
import com.kuang2010.googleplay20.base.SuperLoadBaseProtocol;
import com.kuang2010.googleplay20.bean.AppInfoBean;
import com.kuang2010.googleplay20.conf.Constant;
import com.kuang2010.googleplay20.manager.DownLoadManager;
import com.kuang2010.googleplay20.protocol.DetailPageProtocol;
import com.kuang2010.googleplay20.viewhold.detail.AppDetailBottomHolder;
import com.kuang2010.googleplay20.viewhold.detail.AppDetailDesHolder;
import com.kuang2010.googleplay20.viewhold.detail.AppDetailInfoHolder;
import com.kuang2010.googleplay20.viewhold.detail.AppDetailPicHolder;
import com.kuang2010.googleplay20.viewhold.detail.AppDetailSafeHolder;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

public class DetailActivity extends AppCompatActivity {

    private String mPackageName;
    private LoadingPager mLoadingPager;
    private AppInfoBean mAppInfoBean;
    private AppDetailBottomHolder mAppDetailBottomHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
        initEvent();
    }

    private void initEvent() {

    }

    private void initData() {
        mPackageName = getIntent().getStringExtra(Constant.APPINFO_PACKAGENAME);
        mLoadingPager.triggerLoadData();
    }

    private void initView() {
        mLoadingPager = new LoadingPager(this) {
            @Override
            protected View initSuccessView() {
                return DetailActivity.this.initSuccessView();
            }

            @Override
            protected void initData(ILoadDataFinishPageStateCallBack callBack) {
                DetailActivity.this.startLoadData(callBack);
            }
        };
        setContentView(mLoadingPager);
    }


    private void startLoadData(LoadingPager.ILoadDataFinishPageStateCallBack callBack) {

        DetailPageProtocol detailPageProtocol = new DetailPageProtocol(mPackageName);
        detailPageProtocol.loadCommonLoad(callBack, new SuperLoadBaseProtocol.OnCommonLoadDataResultListener<AppInfoBean>() {
            @Override
            public void setResultData(AppInfoBean appInfoBean) {
                mAppInfoBean = appInfoBean;
            }
        });
    }

    @ViewInject(R.id.fl_down_detail)
    FrameLayout                     mContainerBottom;

    @ViewInject(R.id.detail_fl_des)
    FrameLayout						mContainerDes;

    @ViewInject(R.id.detail_fl_info)
    FrameLayout						mContainerInfo;

    @ViewInject(R.id.detail_fl_pic)
    FrameLayout						mContainerPic;

    @ViewInject(R.id.detail_fl_safe)
    FrameLayout						mContainerSafe;
    private View initSuccessView() {
        View view = LayoutInflater.from(this).inflate(R.layout.activity_detail, null);
        x.view().inject(this,view);

        AppDetailInfoHolder appDetailInfoHolder = new AppDetailInfoHolder(this);
        mContainerInfo.addView(appDetailInfoHolder.getView());
        appDetailInfoHolder.setData(mAppInfoBean);

        AppDetailSafeHolder appDetailSafeHolder = new AppDetailSafeHolder(this);
        mContainerSafe.addView(appDetailSafeHolder.getView());
        appDetailSafeHolder.setData(mAppInfoBean);

        AppDetailPicHolder appDetailPicHolder = new AppDetailPicHolder(this);
        mContainerPic.addView(appDetailPicHolder.getView());
        appDetailPicHolder.setData(mAppInfoBean);

        AppDetailDesHolder appDetailDesHolder = new AppDetailDesHolder(this);
        mContainerDes.addView(appDetailDesHolder.getView());
        appDetailDesHolder.setData(mAppInfoBean);

        mAppDetailBottomHolder = new AppDetailBottomHolder(this);
        mContainerBottom.addView(mAppDetailBottomHolder.getView());
        mAppDetailBottomHolder.setData(mAppInfoBean);

        return view;
    }

    /**有权限请求会时onResume()会被调用两次，因为权限请求会弹框(onPause)，只是太快了没看到这个框而已(onResume())*/
    @Override
    protected void onResume() {
        if(mAppDetailBottomHolder!=null){//网络慢时，进入该页面mAppDetailBottomHolder是空的，只有离开这个页面跳到了其他APP页面再回到这个页面网络加载完成后才会不为空
            // 添加观察者,继续监听
            DownLoadManager.getDownLoadManagerInstance().addDownLoadObserve(mAppDetailBottomHolder);
            //手动发布一次通知,让观察者获取到当前的downloadInfo，并刷新UI
            DownLoadManager.getDownLoadManagerInstance().notifyUpdateUi(DownLoadManager.getDownLoadManagerInstance().getDownLoadInfo(this,mAppInfoBean));
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        if(mAppDetailBottomHolder!=null){
            //移除观察者，取消监听
            DownLoadManager.getDownLoadManagerInstance().removeObserve(mAppDetailBottomHolder);
        }
        super.onPause();
    }
}
