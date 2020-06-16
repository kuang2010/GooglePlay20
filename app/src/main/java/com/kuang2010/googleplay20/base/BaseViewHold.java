package com.kuang2010.googleplay20.base;

import android.view.View;

import com.kuang2010.googleplay20.bean.AppInfoBean;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * author: kuangzeyu2019
 * date: 2020/6/11
 * time: 16:12
 * desc: 提供视图 + 接收数据 + 绑定数据
 * ItemBean是接口返回的所有数据里的一种bean数据类型
 */
public abstract class BaseViewHold<ItemBean> extends RecyclerView.ViewHolder {
    public BaseViewHold(@NonNull View itemView) {
        super(itemView);
    }

    public abstract void setData(ItemBean t);
}
