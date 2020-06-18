package com.kuang2010.googleplay20.base;

import android.content.Context;
import android.view.View;

/**
 * author: kuangzeyu2019
 * date: 2020/6/16
 * time: 19:06
 * desc: view + data
 * 提供视图 + 接收数据T + 绑定数据(刷新UI)
 */
public abstract class BaseHolder<T> {

    private final View mView;
    protected final Context mContext;

    public BaseHolder(Context context){
        mContext = context;
        mView = initView();
        if (mView!=null){
            mView.setTag(this);//给listview做缓存复用
        }
    }

    /**
     * 外界调用获取holder的视图
     * @return
     */
    public View getView(){
        return mView;
    }


    /**
     * 外界调用，传递数据和绑定数据
     * @param data
     */
    public void setData(T data){
        bindDataToView(data);
    }

    /**
     * 子类实现，数据和视图绑定(刷新UI)
     * @param data
     */
    protected abstract void bindDataToView(T data);

    /**
     * 子类实现，初始化持有的视图
     * @return
     */
    protected abstract View initView();
}
