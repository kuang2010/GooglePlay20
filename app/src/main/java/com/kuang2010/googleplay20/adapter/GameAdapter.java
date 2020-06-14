package com.kuang2010.googleplay20.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.kuang2010.googleplay20.R;
import com.kuang2010.googleplay20.base.BaseRvAdapter;
import com.kuang2010.googleplay20.base.BaseViewHold;
import com.kuang2010.googleplay20.bean.AppInfoBean;
import com.kuang2010.googleplay20.viewhold.AppInfoViewHold;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * author: kuangzeyu2019
 * date: 2020/6/14
 * time: 19:32
 * desc:
 */
public class GameAdapter extends BaseRvAdapter<AppInfoBean> {
    public GameAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseViewHold getItemInfoViewHold(ViewGroup parent) {
        return new AppInfoViewHold(mContext, LayoutInflater.from(mContext).inflate(R.layout.item_home_info,parent,false));
    }

}
