package com.kuang2010.googleplay20.viewhold.detail;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.kuang2010.googleplay20.base.BaseHolder;
import com.kuang2010.googleplay20.bean.AppInfoBean;

/**
 * author: kuangzeyu2019
 * date: 2020/6/16
 * time: 19:20
 * desc: 应用的信息部分
 */
public class AppDetailInfoHolder extends BaseHolder<AppInfoBean> {

    public AppDetailInfoHolder(Context context) {
        super(context);
    }

    @Override
    protected void bindDataToView(AppInfoBean data) {

    }

    @Override
    protected View initView() {
        TextView tv = new TextView(mContext);
        tv.setText(this.getClass().getSimpleName());
        return tv;
    }
}
