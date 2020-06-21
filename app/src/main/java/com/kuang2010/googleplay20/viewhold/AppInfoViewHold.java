package com.kuang2010.googleplay20.viewhold;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.kuang2010.googleplay20.DetailActivity;
import com.kuang2010.googleplay20.R;
import com.kuang2010.googleplay20.base.BaseViewHold;
import com.kuang2010.googleplay20.bean.AppInfoBean;
import com.kuang2010.googleplay20.bean.DownloadInfo;
import com.kuang2010.googleplay20.conf.Constant;
import com.kuang2010.googleplay20.manager.DownLoadManager;
import com.kuang2010.googleplay20.util.BitmapUtilFactory;
import com.kuang2010.googleplay20.util.CommonUtils;
import com.kuang2010.googleplay20.util.StringUtils;
import com.kuang2010.googleplay20.view.DownloadProgressView;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;

import androidx.annotation.NonNull;

/**
 * author: kuangzeyu2019
 * date: 2020/6/13
 * time: 11:50
 * desc: viewhold , 下载观察者
 */
public class AppInfoViewHold extends BaseViewHold<AppInfoBean> implements View.OnClickListener, DownLoadManager.DownLoadObserve, DownloadProgressView.OnTachedWindownListener {

    @ViewInject(R.id.item_appinfo_iv_icon)
    ImageView mIvIcon;

    @ViewInject(R.id.item_appinfo_rb_stars)
    RatingBar mRbStars;

    @ViewInject(R.id.item_appinfo_tv_des)
    TextView mTvDes;

    @ViewInject(R.id.item_appinfo_tv_size)
    TextView	mTvSize;

    @ViewInject(R.id.item_appinfo_tv_title)
    TextView	mTvTitle;

    @ViewInject(R.id.dpv_down_item)
    DownloadProgressView mProgressView;

    private Context mContext;
    private  View mItemView;
    private AppInfoBean mAppInfoBean;
    private DownloadInfo mDownloadInfo;
    public AppInfoViewHold(Context context, @NonNull View itemView) {
        super(itemView);
        mContext = context;
        mItemView = itemView;
        x.view().inject(this,itemView);
        mProgressView.setOnClickListener(this);
        DownLoadManager.getDownLoadManagerInstance().addDownLoadObserve(this);
        mProgressView.setOnTachedWindownListener(this);
    }

    @Override
    public void setData(final AppInfoBean data) {
        mAppInfoBean = data;
        mTvDes.setText(data.des);
        mTvSize.setText(StringUtils.formatFileSize(data.size));
        mTvTitle.setText(data.name);

        mRbStars.setRating(data.stars);

        //复原一下控件，避免复用造成视图错误
        mProgressView.setProgress(0);

        // 默认图片
        mIvIcon.setImageResource(R.drawable.ic_default);
        // 图片加载
        // BitmapUtils bitmapUtils = new BitmapUtils(UIUtils.getContext());
        // http://localhost:8080/GooglePlayServer/image?name=app/com.itheima.www/icon.jpg
        BitmapUtilFactory.getBitmapUtils(mContext).display(mIvIcon, Constant.URlS.IMAGEBASEURL + data.iconUrl);

        mItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DetailActivity.class);
                intent.putExtra(Constant.APPINFO_PACKAGENAME,data.packageName);
                mContext.startActivity(intent);
            }
        });


        //手动发布一次通知,让观察者获取到当前的downloadInfo，并刷新UI
        DownLoadManager.getDownLoadManagerInstance().notifyUpdateUi(DownLoadManager.getDownLoadManagerInstance().getDownLoadInfo(mContext,data));

    }


    @Override
    public void onClick(View v) {
        if (mDownloadInfo==null){
            doDownLoad();
        }else {

            switch (mDownloadInfo.mCurState){
                case DownLoadManager.STATE_UNDOWNLOAD://未下载
                    doDownLoad();
                    break;
                case DownLoadManager.STATE_DOWNLOADING:// 下载中
                    doPauseLoad();
                    break;
                case DownLoadManager.STATE_PAUSEDOWNLOAD:// 暂停下载
                    doDownLoad();
                    break;
                case DownLoadManager.STATE_WAITINGDOWNLOAD://等待下载
                    cancelLoad();
                    break;
                case DownLoadManager.STATE_DOWNLOADFAILED:// 下载失败
                    doDownLoad();
                    break;
                case DownLoadManager.STATE_DOWNLOADED://下载完成
                    installApp();
                    break;
                case DownLoadManager.STATE_INSTALLED:// 已安装
                    openApp();
                    break;

            }
        }
    }

    /**打开APP*/
    private void openApp() {
        CommonUtils.openApp(mContext,mDownloadInfo.packageName);
        //在onResume里改变状态
    }

    /**安装APP*/
    private void installApp() {
        CommonUtils.installApp(mContext,new File(mDownloadInfo.savePath),"com.kuang2010.googleplay20.insatller");
        //在onResume里改变状态
    }

    /**取消下载*/
    private void cancelLoad() {
        DownLoadManager.getDownLoadManagerInstance().cancelLoad(mDownloadInfo);
    }

    /**暂停下载*/
    private void doPauseLoad() {
        DownLoadManager.getDownLoadManagerInstance().pauseLoad(mDownloadInfo);
    }

    /**下载*/
    private void doDownLoad() {
        DownLoadManager.getDownLoadManagerInstance().downLoad(mContext,mAppInfoBean);
    }


    @Override
    public void onDownLoadUpdate(DownloadInfo downloadInfo) {
        // 过滤不属于本观察者的通知,因为存在另外的线程可能正在执行它的下载任务，它会发布通知，这个通知是所有观察者都能收到的,观察者和被观察者是多对一的关系
        if (downloadInfo==null || mAppInfoBean ==null ||!downloadInfo.packageName.equals(mAppInfoBean.packageName)) {
            return;
        }
        mDownloadInfo = downloadInfo;
        upDateDownloadUi(downloadInfo);
    }

    /**
     * 根据状态改变UI
     * @param downloadInfo
     */
    private void upDateDownloadUi(DownloadInfo downloadInfo) {
        if (downloadInfo==null) return;
        Log.d("upDateDownloadUi","downloadInfo>>>>>>"+downloadInfo.mCurState);
        switch (downloadInfo.mCurState){
            case DownLoadManager.STATE_UNDOWNLOAD://下载
                mProgressView.setNote("下载");
                mProgressView.setIcon(R.drawable.ic_download);
                break;
            case DownLoadManager.STATE_DOWNLOADING://正在下载
                Log.d("tagtag","downloadInfo.curProgress:"+downloadInfo.curProgress);
                Log.d("tagtag","downloadInfo.size:"+downloadInfo.size);
                mProgressView.setMax(downloadInfo.size);
                mProgressView.setProgress(downloadInfo.curProgress);
                int progress = (int) (downloadInfo.curProgress * 100.0f / downloadInfo.size + .5f);
                mProgressView.setNote(progress + "%");
                mProgressView.setIcon(R.drawable.ic_pause);
                break;
            case DownLoadManager.STATE_PAUSEDOWNLOAD://继续下载
                mProgressView.setNote("继续下载");
                mProgressView.setIcon(R.drawable.ic_resume);
                mProgressView.setProgress(downloadInfo.curProgress);
                mProgressView.setMax(downloadInfo.size);

                break;
            case DownLoadManager.STATE_WAITINGDOWNLOAD://等待中...
                mProgressView.setNote("等待中...");
                mProgressView.setIcon(R.drawable.ic_pause);
                break;
            case DownLoadManager.STATE_DOWNLOADFAILED://重试
                mProgressView.setNote("重试");
                mProgressView.setIcon(R.drawable.ic_redownload);
                break;
            case DownLoadManager.STATE_DOWNLOADED://安装
                mProgressView.setProgress(0);
                mProgressView.setMax(downloadInfo.size);
                mProgressView.setNote("安装");
                mProgressView.setIcon(R.drawable.ic_install);
                break;
            case DownLoadManager.STATE_INSTALLED://打开
                mProgressView.setNote("打开");
                mProgressView.setIcon(R.drawable.ic_install);
                break;

        }
    }


    //解决不了页面 onResume 和 onPause 要刷新的问题
    @Override
    public void onAttachedToWindow() {
        DownLoadManager.getDownLoadManagerInstance().addDownLoadObserve(this);
        //手动发布一次通知,让观察者获取到当前的downloadInfo，并刷新UI
        if (mAppInfoBean!=null)
        DownLoadManager.getDownLoadManagerInstance().notifyUpdateUi(DownLoadManager.getDownLoadManagerInstance().getDownLoadInfo(mContext,mAppInfoBean));

    }

    @Override
    public void onDetachedFromWindow() {
        DownLoadManager.getDownLoadManagerInstance().removeObserve(this);
    }
}
