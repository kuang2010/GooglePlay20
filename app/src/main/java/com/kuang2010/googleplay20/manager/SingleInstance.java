package com.kuang2010.googleplay20.manager;

/**
 * author: kuangzeyu2019
 * date: 2020/6/11
 * time: 11:05
 * desc: 单例（懒汉式）
 * 单例类必须自己创建自己的唯一实例。
 * 单例类必须给所有其他对象提供获取这一实例的方法。
 */
public class SingleInstance {

    private SingleInstance(){}

    private static SingleInstance mSingleInstance;

    public static SingleInstance getInstance(){
        //双重判断加锁
        if (mSingleInstance==null){
            synchronized (SingleInstance.class){
                if (mSingleInstance==null){
                    mSingleInstance = new SingleInstance();
                }
            }
        }
        return mSingleInstance;
    }
}
