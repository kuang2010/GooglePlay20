package com.kuang2010.googleplay20.factory;

import com.kuang2010.googleplay20.manager.ThreadPoolExecutorProxy;

/**
 * author: kuangzeyu2019
 * date: 2020/6/11
 * time: 10:28
 * desc: 创建不同的线程池代理工厂
 */
public class ThreadPoolExecutorProxyFactory {
    /**普通的线程池代理*/
    static ThreadPoolExecutorProxy mNormalThreadPoolExecutorProxy;
    /**下载的线程池代理*/
    static ThreadPoolExecutorProxy mDownloadThreadPoolExecutorProxy;

    /**
     * 得到普通的线程池代理
     * @return
     */
    public static ThreadPoolExecutorProxy createNormalThreadPoolExecutorProxy(){
        if (mNormalThreadPoolExecutorProxy==null){
            synchronized (ThreadPoolExecutorProxyFactory.class){
                if (mNormalThreadPoolExecutorProxy==null){
                    mNormalThreadPoolExecutorProxy = new ThreadPoolExecutorProxy(5);
                }
            }
        }
        return mNormalThreadPoolExecutorProxy;
    }

    /**
     * 得到下载的线程池代理
     * @return
     */
    public static  ThreadPoolExecutorProxy createDownLoadThreadPoolExecutorProxy(){
        if (mDownloadThreadPoolExecutorProxy == null){
            synchronized (ThreadPoolExecutorProxyFactory.class){
                if (mDownloadThreadPoolExecutorProxy==null){
                    mDownloadThreadPoolExecutorProxy = new ThreadPoolExecutorProxy(3);
                }
            }
        }
        return mDownloadThreadPoolExecutorProxy;
    }
}
