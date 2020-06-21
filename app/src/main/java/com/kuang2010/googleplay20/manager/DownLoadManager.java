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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * author: kuangzeyu2019
 * date: 2020/6/18
 * time: 22:29
 * desc: 下载管理
 * 单例+观察者模式
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
                                                                        //xx可升级

//    private static int mCurState;
    private Map<String,DownloadInfo> mDownloadInfos = new HashMap<>();//给外界根据包名获取到对应的上一次的下载信息
    private Map<String,Runnable> mRunnableMap = new HashMap<>();//所有下载任务的集合
    private boolean isStop;//终止所有下载任务

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

        String filePath = getSaveFilePath(appInfoBean.packageName);
        File saveFile = new File(filePath);
        downloadInfo.downloadUrl = Constant.URlS.BASEURL+"download";
        String downloadUrl = appInfoBean.downloadUrl;
        if (!downloadUrl.endsWith(".apk")){
            //解决报文中有些downloadUrl少了.apk导致不能下载的问题
            downloadUrl = downloadUrl+".apk";
        }
        downloadInfo.downloadUrlParmaName = downloadUrl;
        downloadInfo.curProgress = saveFile.exists()?saveFile.length():0 ;
        downloadInfo.packageName = appInfoBean.packageName;
        downloadInfo.savePath = filePath;
        downloadInfo.size = appInfoBean.size;
        downloadInfo.version = appInfoBean.version;

        downloadInfo.mCurState = STATE_UNDOWNLOAD;
        notifyUpdateUi(downloadInfo);

        /**
         * 预先把状态置为等待状态
         * 1.假如正在执行的下载任务<3个,需要执行的"下载任务",应该会放到"工作线程"中--->把状态变为"下载中"的状态
         * 2.假如正在执行的下载任务>=3个,需要执行的"下载任务",应该会放到"任务队列"中--->此时的状态还是"等待中"
         */
        downloadInfo.mCurState =  STATE_WAITINGDOWNLOAD;
        notifyUpdateUi(downloadInfo);

        /**
         * 启动有三个线的线程池去开启下载任务。
         * 因此最多可以有三个下载任务同时进行，多出的下载任务会在线程池里的队列中排队等待执行
         * **/
        DownLoadTask downLoadTask = new DownLoadTask(downloadInfo);
        mRunnableMap.put(downloadInfo.packageName,downLoadTask);
        ThreadPoolExecutorProxyFactory.createDownLoadThreadPoolExecutorProxy().submit(downLoadTask);
    }


    private String getSaveFilePath(String packageName) {
        return FileUtil.getPublicDir("app")+"/"+packageName+".apk";
    }


    /**
     * 暂停下载
     * @param downloadInfo  与 DownLoadTask里的mDownloadInfo是同一个对象
     */
    public void pauseLoad(DownloadInfo downloadInfo) {
        downloadInfo.mCurState = STATE_PAUSEDOWNLOAD;//-->while结束循环
        notifyUpdateUi(downloadInfo);
    }


    /**
     * 取消下载downloadInfo对应的任务
     * @param downloadInfo
     */
    public void cancelLoad(DownloadInfo downloadInfo){
//        Runnable runnable = mRunnableMap.get(downloadInfo.packageName);
//        ThreadPoolExecutorProxyFactory.createDownLoadThreadPoolExecutorProxy().remove(runnable);
        mRunnableMap.remove(downloadInfo.packageName);
        mDownloadInfos.remove(downloadInfo.packageName);
        downloadInfo.mCurState = STATE_UNDOWNLOAD;
        notifyUpdateUi(downloadInfo);
    }


    /**
     * 终止所有的下载,类似于杀进程的操作
     */
    public void stopDownload(){
        isStop = true;
        mRunnableMap.clear();
        mDownloadInfos.clear();
        mDownLoadObserves.clear();
        downLoadManager = null;
    }

    /**
     * 这里的逻辑有点复杂，可以调试处理
     * 主要给外界首次进入页面和Resume时获取downLoadInfo的信息
     * @param context
     * @param appInfoBean
     * @return
     */
    public DownloadInfo getDownLoadInfo(Context context,AppInfoBean appInfoBean) {
        if (appInfoBean == null)return null;

        DownloadInfo downloadInfo = mDownloadInfos.get(appInfoBean.packageName);
        String filePath = getSaveFilePath(appInfoBean.packageName);
        File saveFile = new File(filePath);

        Log.d("getDownLoadInfo","downloadInfo>>>>>>>>"+downloadInfo);
        if (downloadInfo == null){
            //第一次下载，或杀进程重启应用销毁了downloadManager
            downloadInfo = new DownloadInfo();
            downloadInfo.downloadUrl = Constant.URlS.BASEURL+"download";
            String downloadUrl = appInfoBean.downloadUrl;
            if (!downloadUrl.endsWith(".apk")){
                //解决报文中有些downloadUrl少了.apk导致不能下载的问题
                downloadUrl = downloadUrl +".apk";
            }
            downloadInfo.downloadUrlParmaName = downloadUrl;
            downloadInfo.curProgress = saveFile.exists()?saveFile.length():0 ;
            downloadInfo.packageName = appInfoBean.packageName;
            downloadInfo.savePath = filePath;
            downloadInfo.size = appInfoBean.size;
            downloadInfo.version = appInfoBean.version;

            if (CommonUtils.isInstalled(context,downloadInfo.packageName)){
                //已安装
                downloadInfo.mCurState = STATE_INSTALLED;
                return downloadInfo;
            }

            if (saveFile.exists() && saveFile.length()>0){
                //已下载
                if (saveFile.length()<downloadInfo.size){
                    //未下载完，之前暂停了下载。这里不可能是正在下载，因为downloadInfo == null
                    downloadInfo.mCurState = STATE_PAUSEDOWNLOAD;
                }else {
                    //已下载完成
                    downloadInfo.mCurState = STATE_DOWNLOADED;
                }
                return downloadInfo;
            }

            downloadInfo.mCurState = STATE_UNDOWNLOAD;//其他三种状态不可能出现在这里

        }else {

            if (CommonUtils.isInstalled(context,downloadInfo.packageName)){
                //(人为)已安装
                downloadInfo.mCurState = STATE_INSTALLED;
                return downloadInfo;
            }

            if (!saveFile.exists() ||saveFile.length()==0){
                //(人为)删除apk文件，或正在等待下载
                if (downloadInfo.curProgress>0){//则不是正在等待下载
                    //是(人为)删除了apk文件
                    downloadInfo.mCurState = STATE_UNDOWNLOAD;
                    return downloadInfo;
                }
            }

            if (saveFile.exists() && saveFile.length()==downloadInfo.size){
                //已下载并安装完成, 但是被(人为)卸载了
                downloadInfo.mCurState = STATE_DOWNLOADED;
                return downloadInfo;
            }
            if (saveFile.length()<downloadInfo.size){
                //未下载完，这里可能是暂停下载，或是正在下载中。保持原有状态就行
            }

            //否则，保持原有状态

        }


        return downloadInfo;
    }


    private class DownLoadTask implements Runnable {

        private DownloadInfo mDownloadInfo;

        public DownLoadTask(DownloadInfo downloadInfo) {
            mDownloadInfo = downloadInfo;
        }

        @Override
        public void run() {
            //子线程
            if (!mRunnableMap.containsValue(this)){
                //任务取消
                return;
            }

            mDownloadInfo.mCurState =  STATE_DOWNLOADING;
            notifyUpdateUi(mDownloadInfo);

            InputStream in = null;
            FileOutputStream out = null;
            try {

                String ul = mDownloadInfo.downloadUrl+"?name="+mDownloadInfo.downloadUrlParmaName+"&range="+mDownloadInfo.curProgress;
                URL url = new URL(ul);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//                conn.addRequestProperty();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(3000);
                conn.setReadTimeout(3000);

                int responseCode = conn.getResponseCode();

                if (responseCode == 200){

                    in = conn.getInputStream();

                    File file = new File(mDownloadInfo.savePath);
                    out = new FileOutputStream(file,true);
                    byte[] buf = new byte[1024];
                    int len = -1;
                    boolean isPause = false;
                    while ((len=in.read(buf))!=-1){
                        if (isStop){
                            break;
                        }
                        if (mDownloadInfo.mCurState == STATE_PAUSEDOWNLOAD){
                            isPause = true;
                            break;
                        }

                        out.write(buf,0,len);
                        out.flush();

                        mDownloadInfo.mCurState =  STATE_DOWNLOADING;
                        mDownloadInfo.curProgress += len;
                        notifyUpdateUi(mDownloadInfo);//由于本地读取数据很快，又使用了handler发消息，会延时收到数据，从而导致在收到最后几节数据前状态已改变为STATE_DOWNLOADED，导致更新进度条更新不到100%

                        Log.d("upDateDownloadUi","len1:"+len);
                        if (mDownloadInfo.curProgress == mDownloadInfo.size){
                            //java.net.SocketException: Socket closed
                            //解决暂停下载又继续下载，下载完跳不出while的服务器问题
                            break;
                        }
                    }
                    Log.d("upDateDownloadUi","len2:"+len);
                    //下载完成或暂停下载
                    if (isPause||isStop){
                        //暂停下载，noting to do
                    }else {
                        //下载完成
                        SystemClock.sleep(500);//解决由于使用了handler发消息，会延时收到数据，从而导致在收到最后几节数据前状态已改变为STATE_DOWNLOADED，更新进度条UI失败
                        mDownloadInfo.mCurState =  STATE_DOWNLOADED;
                        notifyUpdateUi(mDownloadInfo);
                        Log.d("upDateDownloadUi","downloadInfo>>>>>>STATE_DOWNLOADED");

                        mRunnableMap.remove(mDownloadInfo.packageName);
                    }
                }

            } catch (IOException e) {
                Log.e("IOException","e:"+e.getCause().toString());
                mDownloadInfo.mCurState =  STATE_DOWNLOADFAILED;
                notifyUpdateUi(mDownloadInfo);
            }finally {
                IOUtils.close(out);
                IOUtils.close(in);
            }

        }
    }

    private Handler mHandler = new Handler();

    /**被观察者this发布通知给所有的观察者,观察者刷新UI*/
    public void notifyUpdateUi(final DownloadInfo downloadInfo) {
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


    public interface DownLoadObserve{//观察者、监听器

        /**
         * @des 更新下载信息
         * 观察者和被观察者是多对一的关系 ，所以在观察者接收通知时要过滤一下通知的信息是否是自己想要的对应信息
         * @param downloadInfo 被观察者通知给观察者的下载信息
         */
        void onDownLoadUpdate(DownloadInfo downloadInfo);
    }

    private List<DownLoadObserve> mDownLoadObserves = new ArrayList<>();

    /**
     * @des 被观察者(被观察的对象this--DownloadManager) 订阅 观察者，让观察者监听下载过程
     * 观察者和被观察者是多对一的关系 ， 所以在观察者接收通知时要过滤一下通知的信息是否是自己想要的对应信息
     *
     * 不用set方法设置监听，是因为在首页中下载时，会存在多个itemHolder同时使用该单例DownLoadManager去下载的情况，就需要有各自的
     * 监听其同时存在了， 而单例中的成员是唯一的， 使用set方法会覆盖之前的itemHolder设置的监听器。
     * @param downLoadObserve
     */
    public void addDownLoadObserve(DownLoadObserve downLoadObserve){//观察者设计模式
        if (downLoadObserve!=null && !mDownLoadObserves.contains(downLoadObserve)){
            mDownLoadObserves.add(downLoadObserve);
        }

    }
    public void removeObserve(DownLoadObserve downLoadObserve){
        mDownLoadObserves.remove(downLoadObserve);
    }

}
