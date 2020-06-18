package com.kuang2010.googleplay20.viewhold.detail;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.kuang2010.googleplay20.R;
import com.kuang2010.googleplay20.base.BaseHolder;
import com.kuang2010.googleplay20.bean.AppInfoBean;
import com.kuang2010.googleplay20.bean.DownloadInfo;
import com.kuang2010.googleplay20.manager.DownLoadManager;
import com.kuang2010.googleplay20.view.ProgressButtonView;

/**
 * author: kuangzeyu2019
 * date: 2020/6/16
 * time: 19:20
 * desc: 应用的下载部分
 */
public class AppDetailBottomHolder extends BaseHolder<AppInfoBean> implements View.OnClickListener, DownLoadManager.DownLoadObserve {

    private ProgressButtonView mPbtn_detail_bottom;

    public AppDetailBottomHolder(Context context) {
        super(context);
    }

    AppInfoBean mAppInfoBean;
    DownloadInfo mDownloadInfo;

    @Override
    protected void bindDataToView(AppInfoBean data) {
        mAppInfoBean = data;
        DownloadInfo downLoadInfo = DownLoadManager.getDownLoadManagerInstance().getDownLoadInfo(mContext, data);
        upDateDownloadUi(downLoadInfo);
    }

    @Override
    protected View initView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_detail_bottom, null);
        mPbtn_detail_bottom = view.findViewById(R.id.pbtn_detail_bottom);
        mPbtn_detail_bottom.setOnClickListener(this);
        return view;
    }

/**
 下载分析
 状态(编程记录)  	|  给用户的提示(ui展现)   | 用户行为(触发操作)
 ----------------|-----------------------| -----------------
 未下载			|下载				    | 去下载
 下载中			|显示进度条		   		| 暂停下载
 暂停下载			|继续下载				| 断点继续下载
 等待下载			|等待中...				| 取消下载
 下载失败 		|重试					| 重试下载
 下载完成 		|安装					| 安装应用
 已安装 			|打开					| 打开应用

 ***************************************************************
 记录状态--> 根据不同状态显示UI --> 操作事件 --> 改变状态 --> 显示UI

 */


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

    private void openApp() {

    }

    private void installApp() {

    }

    private void cancelLoad() {

    }

    private void doPauseLoad() {
        DownLoadManager.getDownLoadManagerInstance().pauseLoad(mDownloadInfo);
    }

    private void doDownLoad() {
        DownLoadManager.getDownLoadManagerInstance().downLoad(mContext,mAppInfoBean);
    }


    @Override
    public void onDownLoadUpdate(DownloadInfo downloadInfo) {
        mDownloadInfo = downloadInfo;
        upDateDownloadUi(downloadInfo);
    }

    private void upDateDownloadUi(DownloadInfo downloadInfo) {
        switch (downloadInfo.mCurState){
            case DownLoadManager.STATE_UNDOWNLOAD:
                mPbtn_detail_bottom.setText("下载");
            break;
            case DownLoadManager.STATE_DOWNLOADING:
                Log.d("tagtag","downloadInfo.curProgress:"+downloadInfo.curProgress);
                Log.d("tagtag","downloadInfo.size:"+downloadInfo.size);
                mPbtn_detail_bottom.setProgress(downloadInfo.curProgress);
                mPbtn_detail_bottom.setMax(downloadInfo.size);
                mPbtn_detail_bottom.setText(mPbtn_detail_bottom.getProgress()+"%");
                break;
            case DownLoadManager.STATE_PAUSEDOWNLOAD:
                mPbtn_detail_bottom.setText("继续下载");
                break;
            case DownLoadManager.STATE_WAITINGDOWNLOAD:
                mPbtn_detail_bottom.setText("等待中...");
                break;
            case DownLoadManager.STATE_DOWNLOADFAILED:
                mPbtn_detail_bottom.setText("重试");
                break;
            case DownLoadManager.STATE_DOWNLOADED:
                mPbtn_detail_bottom.setText("安装");
                break;
            case DownLoadManager.STATE_INSTALLED:
                mPbtn_detail_bottom.setText("打开");
                break;

        }
    }
}
