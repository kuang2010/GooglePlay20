package com.kuang2010.googleplay20.viewhold.detail;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.kuang2010.googleplay20.R;
import com.kuang2010.googleplay20.base.BaseHolder;
import com.kuang2010.googleplay20.bean.AppInfoBean;
import com.kuang2010.googleplay20.conf.Constant;
import com.kuang2010.googleplay20.util.BitmapUtilFactory;
import com.kuang2010.googleplay20.util.StringUtils;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * author: kuangzeyu2019
 * date: 2020/6/16
 * time: 19:20
 * desc: 应用的信息部分
 */
public class AppDetailInfoHolder extends BaseHolder<AppInfoBean> {

    @ViewInject(R.id.app_detail_info_rb_star)
    RatingBar mRbStar;

    @ViewInject(R.id.app_detail_info_iv_icon)
    ImageView mIvIcon;

    @ViewInject(R.id.app_detail_info_tv_downloadnum)
    TextView	mTvDownLoadNum;

    @ViewInject(R.id.app_detail_info_tv_name)
    TextView	mTvName;

    @ViewInject(R.id.app_detail_info_tv_size)
    TextView	mTvSize;

    @ViewInject(R.id.app_detail_info_tv_time)
    TextView	mTvTime;

    @ViewInject(R.id.app_detail_info_tv_version)
    TextView	mTvVersion;

    public AppDetailInfoHolder(Context context) {
        super(context);
    }

    @Override
    protected void bindDataToView(AppInfoBean data) {
        if (data == null)return;
        mTvName.setText(data.name);

        String date = mContext.getResources().getString(R.string.detail_date, data.date);
        String downloadNum = mContext.getResources().getString(R.string.detail_downloadnum, data.downloadNum);
        String size = mContext.getResources().getString(R.string.detail_size, StringUtils.formatFileSize(data.size));
        String version = mContext.getResources().getString(R.string.detail_version, data.version);

        mTvDownLoadNum.setText(downloadNum);
        mTvSize.setText(size);
        mTvTime.setText(date);
        mTvVersion.setText(version);

        mRbStar.setRating(data.stars);

        mIvIcon.setImageResource(R.drawable.ic_default);
        BitmapUtilFactory.getBitmapUtils(mContext).display(mIvIcon, Constant.URlS.IMAGEBASEURL + data.iconUrl);
    }

    @Override
    protected View initView() {
        View view = View.inflate(mContext, R.layout.item_detail_info,null);
        x.view().inject(this,view);
        return view;
    }
}
