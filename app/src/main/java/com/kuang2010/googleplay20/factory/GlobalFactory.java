package com.kuang2010.googleplay20.factory;

import com.kuang2010.googleplay20.manager.ThreadPoolExecutorProxy;

import java.util.HashMap;

/**
 * author: kuangzeyu2019
 * date: 2020/6/14
 * time: 23:20
 * desc:
 */
public class GlobalFactory {

    /**************************strHashMap*******************************/
    static HashMap<String,String> strHashMap = null;
    public static  HashMap<String,String> getStrHashMap(){
        if (strHashMap == null){
            synchronized (GlobalFactory.class){
                if (strHashMap==null){
                    strHashMap = new HashMap<>();
                }
            }
        }
        return strHashMap;
    }
    /**************************strHashMap*******************************/


    /**************************线程池*******************************/
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
    /**************************线程池*******************************/
}
