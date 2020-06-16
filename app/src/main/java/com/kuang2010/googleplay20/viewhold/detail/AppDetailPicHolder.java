package com.kuang2010.googleplay20.viewhold.detail;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.kuang2010.googleplay20.R;
import com.kuang2010.googleplay20.base.BaseHolder;
import com.kuang2010.googleplay20.bean.AppInfoBean;
import com.kuang2010.googleplay20.conf.Constant;
import com.kuang2010.googleplay20.util.BitmapUtilFactory;
import com.kuang2010.googleplay20.view.RatioLayout;

import java.util.List;

/**
 * author: kuangzeyu2019
 * date: 2020/6/16
 * time: 19:20
 * desc: 应用的截图部分
 */
public class AppDetailPicHolder extends BaseHolder<AppInfoBean> {

    private LinearLayout mLinearLayout;

    public AppDetailPicHolder(Context context) {
        super(context);
    }

    @Override
    protected void bindDataToView(AppInfoBean data) {
        if (data==null)return;
        int screenWidth = mContext.getResources().getDisplayMetrics().widthPixels;
        float left_margin_dp = mContext.getResources().getDimension(R.dimen.dp_5);
        int vaildScreenWidth = (int) (screenWidth - 3* left_margin_dp);
        int picWidth = vaildScreenWidth/3;//要求无论在任何屏幕分辩率的手机上都展示出3张图片（宽度已知，动态设置高度）
        List<String> screens = data.screen;
        for (String pic:screens){
            RatioLayout ratioLayout = new RatioLayout(mContext);//宽度已知，动态设置高度
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(picWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.leftMargin = (int) left_margin_dp;
            ratioLayout.setLayoutParams(layoutParams);
            ratioLayout.setPicRatio(0.6f);
            ratioLayout.setRelative(ratioLayout.RELATIVE_WIDTH);
            ImageView iv = new ImageView(mContext);
            ViewGroup.LayoutParams ivparam = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            iv.setLayoutParams(ivparam);
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            BitmapUtilFactory.getBitmapUtils(mContext).display(iv, Constant.URlS.IMAGEBASEURL+pic);
            ratioLayout.addView(iv);
            mLinearLayout.addView(ratioLayout);
        }

    }

    @Override
    protected View initView() {
        HorizontalScrollView scrollView = new HorizontalScrollView(mContext);
        mLinearLayout = new LinearLayout(mContext);
        mLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        scrollView.setHorizontalScrollBarEnabled(false);
        scrollView.addView(mLinearLayout);
        return scrollView;
    }
}
