package com.kuang2010.googleplay20.viewhold;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.kuang2010.googleplay20.R;
import com.kuang2010.googleplay20.bean.AppInfoBean;
import com.kuang2010.googleplay20.conf.Constant;
import com.kuang2010.googleplay20.util.BitmapUtil;
import com.kuang2010.googleplay20.util.BitmapUtilFactory;
import com.kuang2010.googleplay20.util.StringUtils;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import androidx.annotation.NonNull;

/**
 * author: kuangzeyu2019
 * date: 2020/6/11
 * time: 15:15
 * desc:
 */
public class AppInfoViewHold extends BaseViewHold<AppInfoBean> {

    @ViewInject(R.id.item_appinfo_iv_icon)
    ImageView mIvIcon;

    @ViewInject(R.id.item_appinfo_rb_stars)
    RatingBar mRbStars;

    @ViewInject(R.id.item_appinfo_tv_des)
    TextView	mTvDes;

    @ViewInject(R.id.item_appinfo_tv_size)
    TextView	mTvSize;

    @ViewInject(R.id.item_appinfo_tv_title)
    TextView	mTvTitle;

    private Context mContext;
    public AppInfoViewHold(Context context, @NonNull View itemView) {
        super(itemView);
        mContext = context;
        x.view().inject(this,itemView);
    }

    @Override
    public void setData(AppInfoBean data) {
        mTvDes.setText(data.des);
        mTvSize.setText(StringUtils.formatFileSize(data.size));
        mTvTitle.setText(data.name);

        mRbStars.setRating(data.stars);

        // 默认图片
        mIvIcon.setImageResource(R.drawable.ic_default);
        // 图片加载
        // BitmapUtils bitmapUtils = new BitmapUtils(UIUtils.getContext());
        // http://localhost:8080/GooglePlayServer/image?name=app/com.itheima.www/icon.jpg
        BitmapUtilFactory.getBitmapUtils(mContext).display(mIvIcon, Constant.URlS.IMAGEBASEURL + data.iconUrl);
    }


}
