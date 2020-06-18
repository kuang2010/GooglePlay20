package com.kuang2010.googleplay20.manager;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;

import com.kuang2010.googleplay20.bean.AppInfoBean;
import com.kuang2010.googleplay20.bean.DownloadInfo;
import com.kuang2010.googleplay20.conf.Constant;
import com.kuang2010.googleplay20.factory.ThreadPoolExecutorProxyFactory;
import com.kuang2010.googleplay20.util.CommonUtils;
import com.kuang2010.googleplay20.util.FileUtil;
import com.kuang2010.googleplay20.util.IOUtils;

import org.xutils.http.RequestParams;
import org.xutils.http.annotation.HttpResponse;
import org.xutils.x;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * author: kuangzeyu2019
 * date: 2020/6/18
 * time: 22:29
 * desc:
 */
public class DownLoadManager {

    private static DownLoadManager downLoadManager;

    public static final int				STATE_UNDOWNLOAD		= 0;	//未下载
    public static final int				STATE_DOWNLOADING		= 1;	//下载中
    public static final int				STATE_PAUSEDOWNLOAD		= 2;	//暂停下载
    public static final int				STATE_WAITINGDOWNLOAD	= 3;	//等待下载
    public static final int				STATE_DOWNLOADFAILED	= 4;	//下载失败
    public static final int				STATE_DOWNLOADED		= 5;	//已下载完成
    public static final int				STATE_INSTALLED			= 6;    //已安装

//    private static int mCurState;
    private Map<String,DownloadInfo> mDownloadInfos = new HashMap<>();//给外界根据包名获取到对应的上一次的下载信息

    private DownLoadManager(){};

    public static DownLoadManager getDownLoadManagerInstance(){
        if (downLoadManager==null){
            synchronized (DownLoadManager.class){
                if (downLoadManager == null){
                    downLoadManager = new DownLoadManager();
                }
            }
        }
        return downLoadManager;
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

*

 *
 * 去下载，外界要根据下载的状态调用该方法，避免下载中又去下载
 * @param context
 * @param appInfoBean  数据源
 *
 * */
    public void downLoad(Context context,AppInfoBean appInfoBean){

        DownloadInfo downloadInfo = new DownloadInfo();//单例里的局部变量不是唯一的
        mDownloadInfos.put(appInfoBean.packageName,downloadInfo);

        if (CommonUtils.isInstalled(context,appInfoBean.packageName)){
            //已安装
            downloadInfo.mCurState = STATE_INSTALLED;
            updateUi(downloadInfo);
            return;
        }

        String filePath = getSaveFilePath(appInfoBean.packageName);
        File saveFile = new File(filePath);
        if (saveFile.exists() && saveFile.length()>0){
            //已下载
            if (saveFile.length()<appInfoBean.size){
                //未下载完，之前暂停了下载
                downloadInfo.mCurState = STATE_PAUSEDOWNLOAD;
                updateUi(downloadInfo);
            }else {
                //已下载完成
                downloadInfo.mCurState = STATE_DOWNLOADED;
                updateUi(downloadInfo);
            }
            return;
        }

        downloadInfo.downloadUrl = Constant.URlS.BASEURL+"download";
        downloadInfo.downloadUrlParmaName = appInfoBean.downloadUrl;
        downloadInfo.curProgress = saveFile.exists()?saveFile.length():0 ;
        downloadInfo.packageName = appInfoBean.packageName;
        downloadInfo.savePath = filePath;
        downloadInfo.size = appInfoBean.size;
        downloadInfo.version = appInfoBean.version;

        downloadInfo.mCurState = STATE_UNDOWNLOAD;
        updateUi(downloadInfo);

        /**
         * 预先把状态置为等待状态
         * 1.假如正在执行的下载任务<3个,需要执行的"下载任务",应该会放到"工作线程"中--->把状态变为"下载中"的状态
         * 2.假如正在执行的下载任务>=3个,需要执行的"下载任务",应该会放到"任务队列"中--->此时的状态还是"等待中"
         */
        downloadInfo.mCurState =  STATE_WAITINGDOWNLOAD;
        updateUi(downloadInfo);

        /**
         * 启动有三个线的线程池去开启下载任务。
         * 因此最多可以有三个下载任务同时进行，多出的下载任务会在线程池里的队列中排队等待执行
         * **/
        ThreadPoolExecutorProxyFactory.createDownLoadThreadPoolExecutorProxy().submit(new DownLoadTask(downloadInfo));
    }


    private String getSaveFilePath(String packageName) {
        String appDir = FileUtil.getDir("app");
        return appDir+"/"+packageName+".apk";
    }


    /**
     * 暂停下载
     * @param downloadInfo  与 DownLoadTask里的mDownloadInfo是同一个对象
     */
    public void pauseLoad(DownloadInfo downloadInfo) {
        downloadInfo.mCurState = STATE_PAUSEDOWNLOAD;
        updateUi(downloadInfo);
    }

    /**
     * 主要获取downLoadInfo里的状态信息
     * @param context
     * @param appInfoBean
     * @return
     */
    public DownloadInfo getDownLoadInfo(Context context,AppInfoBean appInfoBean) {
        if (mDownloadInfos.get(appInfoBean.packageName) == null){
            //第一次下载，或应用重启
            DownloadInfo downloadInfo = new DownloadInfo();

            if (CommonUtils.isInstalled(context,appInfoBean.packageName)){
                //已安装
                downloadInfo.mCurState = STATE_INSTALLED;
                updateUi(downloadInfo);
                return downloadInfo;
            }

            String filePath = getSaveFilePath(appInfoBean.packageName);
            File saveFile = new File(filePath);
            if (saveFile.exists() && saveFile.length()>0){
                //已下载
                if (saveFile.length()<appInfoBean.size){
                    //未下载完，之前暂停了下载
                    downloadInfo.mCurState = STATE_PAUSEDOWNLOAD;
                    updateUi(downloadInfo);
                }else {
                    //已下载完成
                    downloadInfo.mCurState = STATE_DOWNLOADED;
                    updateUi(downloadInfo);
                }
                return downloadInfo;
            }

            //没下载过，第一次下载
            downloadInfo.mCurState = STATE_UNDOWNLOAD;
            return downloadInfo;

        }else {

           return mDownloadInfos.get(appInfoBean.packageName);
        }
    }


    private class DownLoadTask implements Runnable {

        private DownloadInfo mDownloadInfo;

        public DownLoadTask(DownloadInfo downloadInfo) {
            mDownloadInfo = downloadInfo;
        }

        @Override
        public void run() {
            //子线程
            InputStream in = null;
            FileOutputStream out = null;
            try {
//                RequestParams requestParams = new RequestParams();

//                HttpURLConnection conn = x.http().getSync(requestParams, HttpURLConnection.class);

                URL url = new URL(mDownloadInfo.downloadUrl+"?name="+mDownloadInfo.downloadUrlParmaName+"&range="+mDownloadInfo.curProgress);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//                conn.addRequestProperty();
                conn.setRequestMethod("GET");

                int responseCode = conn.getResponseCode();

                if (responseCode == 200){

                    in = conn.getInputStream();

                    File file = new File(mDownloadInfo.savePath);
                    out = new FileOutputStream(file,true);
                    byte[] buf = new byte[1024];
                    int len = -1;
                    boolean isPause = false;
                    while ((len=in.read(buf))>-1){

                        if (mDownloadInfo.mCurState == STATE_PAUSEDOWNLOAD){
                            isPause = true;
                        }

                        out.write(buf,0,len);
                        out.flush();

                        mDownloadInfo.mCurState =  STATE_DOWNLOADING;
                        mDownloadInfo.curProgress += len;
                        updateUi(mDownloadInfo);//由于本地读取数据很快，又使用了handler发消息，会延时收到数据，从而导致在收到最后几节数据前状态已改变为STATE_DOWNLOADED，导致更新进度条更新不到100%

                    }
                    //下载完成或暂停下载
                    if (isPause){
                        //暂停下载，noting to do
                    }else {
                        //下载完成
                        SystemClock.sleep(500);//解决由于使用了handler发消息，会延时收到数据，从而导致在收到最后几节数据前状态已改变为STATE_DOWNLOADED，更新进度条UI失败
                        mDownloadInfo.mCurState =  STATE_DOWNLOADED;
                        updateUi(mDownloadInfo);
                        //mDownloadInfos.remove(mDownloadInfo.packageName);
                    }
                }

            } catch (IOException throwable) {
                throwable.printStackTrace();
                mDownloadInfo.mCurState =  STATE_DOWNLOADFAILED;
                updateUi(mDownloadInfo);
            }finally {
                IOUtils.close(out);
                IOUtils.close(in);
            }

        }
    }

    private Handler mHandler = new Handler();
    private void updateUi(final DownloadInfo downloadInfo) {
        if (Looper.myLooper() == Looper.getMainLooper()){
            for (DownLoadObserve downLoadObserve:mDownLoadObserves){
                downLoadObserve.onDownLoadUpdate(downloadInfo);
            }
        }else {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    for (DownLoadObserve downLoadObserve:mDownLoadObserves){
                        downLoadObserve.onDownLoadUpdate(downloadInfo);

                    }
                }
            });

        }
    }


    public interface DownLoadObserve{//观察者
        void onDownLoadUpdate(DownloadInfo downloadInfo);
    }

    private List<DownLoadObserve> mDownLoadObserves = new ArrayList<>();

    /**
     * 监听下载过程
     * 不用set方法设置监听，是因为在首页中下载时，会存在多个itemHolder同时使用该单例DownLoadManager去下载的情况，就需要有各自的
     * 监听其同时存在了， 而单例中的成员是唯一的， 使用set方法会覆盖之前的itemHolder设置的监听器。
     * @param downLoadObserve
     */
    public void addDownLoadObserve(DownLoadObserve downLoadObserve){//观察者设计模式
        mDownLoadObserves.add(downLoadObserve);
    }
    public void removeObserve(DownLoadObserve downLoadObserve){
        mDownLoadObserves.remove(downLoadObserve);
    }

}
