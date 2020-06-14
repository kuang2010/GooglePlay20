package com.kuang2010.googleplay20.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.kuang2010.googleplay20.R;
import com.kuang2010.googleplay20.base.BaseRvAdapter;
import com.kuang2010.googleplay20.base.BaseViewHold;
import com.kuang2010.googleplay20.bean.SubjectBean;
import com.kuang2010.googleplay20.viewhold.SubjectViewHold;

import java.util.List;

/**
 * author: kuangzeyu2019
 * date: 2020/6/14
 * time: 21:20
 * desc:
 */
public class SubjectAdapter extends BaseRvAdapter<SubjectBean> {

    public SubjectAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseViewHold getItemInfoViewHold(ViewGroup parent) {
        return new SubjectViewHold(mContext,LayoutInflater.from(mContext).inflate(R.layout.item_subject,parent,false));
    }
}
