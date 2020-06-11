package com.kuang2010.googleplay20.manager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * author: kuangzeyu2019
 * date: 2020/6/11
 * time: 10:29
 * desc: 使用ThreadPoolExecutor更加方面,关心真正关心的即可
 *       提交任务,执行任务,移除任务
 *       没有static
 */
public class ThreadPoolExecutorProxy {
    /**
     * 单例
     * 1.一个类只有一个实例 (适用于全局只有唯一的本类实例)
     * 2.一个类里面的成员变量只有一个实例 +工厂(适用于全局有确定个数的本类实例,每个本类实例只有一个成员实例)
     */

    int nThreads = 1;//线程池里预定的线程个数，在构造方法里传过来
    public ThreadPoolExecutorProxy(int nThreads){
        super();
        if (nThreads>1){
            this.nThreads = nThreads;
        }
        //mThreadPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(nThreads);
    }

    /**提交任务*/
    /**可以通过Future里面定义的get方法得到执行结果,还可以try到执行过程中的异常信息*/
    public Future<?> submit(Runnable task){
        initProxyMemberInstance();
        return mThreadPool.submit(task);
    }

    /**执行任务*/
    public void execute(Runnable task){
        initProxyMemberInstance();
        mThreadPool.execute(task);
    }

    /**移除任务*/
    public void remove(Runnable task) {
        initProxyMemberInstance();
        mThreadPool.remove(task);
    }

    private ThreadPoolExecutor mThreadPool;
    private void initProxyMemberInstance() {//不同于类的单例有返回Instance
        if (mThreadPool==null ||mThreadPool.isShutdown() ||mThreadPool.isTerminated()){
            synchronized (ThreadPoolExecutorProxy.class){
                if (mThreadPool==null ||mThreadPool.isShutdown() ||mThreadPool.isTerminated()){
                    mThreadPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(nThreads);
                }
            }
        }
    }
}
