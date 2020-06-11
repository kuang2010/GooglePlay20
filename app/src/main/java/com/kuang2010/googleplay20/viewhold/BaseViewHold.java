package com.kuang2010.googleplay20.viewhold;

import android.view.View;

import com.kuang2010.googleplay20.bean.AppInfoBean;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * author: kuangzeyu2019
 * date: 2020/6/11
 * time: 16:12
 * desc:
 */
public abstract class BaseViewHold<T> extends RecyclerView.ViewHolder {
    public BaseViewHold(@NonNull View itemView) {
        super(itemView);
    }

    public abstract void setData(T t);
}
