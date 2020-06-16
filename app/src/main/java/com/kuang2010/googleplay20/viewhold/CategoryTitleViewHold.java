package com.kuang2010.googleplay20.viewhold;

import android.view.View;
import android.widget.TextView;

import com.kuang2010.googleplay20.base.BaseViewHold;
import com.kuang2010.googleplay20.bean.CategoryInfoBean;

import androidx.annotation.NonNull;

/**
 * author: kuangzeyu2019
 * date: 2020/6/16
 * time: 15:12
 * desc:
 */
public class CategoryTitleViewHold extends BaseViewHold<CategoryInfoBean> {

    private final TextView mTv;

    public CategoryTitleViewHold(@NonNull View itemView) {
        super(itemView);
        mTv = (TextView) itemView;
    }

    @Override
    public void setData(CategoryInfoBean t) {
        mTv.setText(t.title);
    }
}
