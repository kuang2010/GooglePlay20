package com.kuang2010.googleplay20.viewhold.detail;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kuang2010.googleplay20.R;
import com.kuang2010.googleplay20.base.BaseHolder;
import com.kuang2010.googleplay20.bean.AppInfoBean;
import com.kuang2010.googleplay20.bean.SafeBean;
import com.kuang2010.googleplay20.conf.Constant;
import com.kuang2010.googleplay20.util.BitmapUtil;
import com.kuang2010.googleplay20.util.BitmapUtilFactory;
import com.kuang2010.googleplay20.util.DensityUtil;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

/**
 * author: kuangzeyu2019
 * date: 2020/6/16
 * time: 19:20
 * desc: 应用的安全部分
 */
public class AppDetailSafeHolder extends BaseHolder<AppInfoBean> {
    @ViewInject(R.id.app_detail_safe_iv_arrow)
    ImageView mIvArrow;
    @ViewInject(R.id.app_detail_safe_des_container)
    LinearLayout mContainerDes;
    @ViewInject(R.id.app_detail_safe_pic_container)
    LinearLayout	mContainerPic;

    public AppDetailSafeHolder(Context context) {
        super(context);
    }

    @Override
    protected void bindDataToView(AppInfoBean data) {
        if (data==null)return;
        List<SafeBean> safes = data.safe;
        for (SafeBean safeBean:safes){
            ImageView iv_line1 = new ImageView(mContext);
            String safeUrl = safeBean.safeUrl;
            BitmapUtilFactory.getBitmapUtils(mContext).display(iv_line1, Constant.URlS.IMAGEBASEURL +safeUrl);
            mContainerPic.addView(iv_line1);

            LinearLayout line = new LinearLayout(mContext);
            int padding = DensityUtil.dip2px(mContext,4);
            line.setPadding(padding, padding, padding, padding);
            line.setOrientation(LinearLayout.HORIZONTAL);
            ImageView iv_line2 = new ImageView(mContext);
            TextView tv_line2 = new TextView(mContext);
            String safeDesUrl = safeBean.safeDesUrl;
            BitmapUtilFactory.getBitmapUtils(mContext).display(iv_line2, Constant.URlS.IMAGEBASEURL +safeDesUrl);
            tv_line2.setText(safeBean.safeDes);
            String safeDesColor = safeBean.safeDesColor;
            if ("0".equals(safeDesColor)){
                tv_line2.setTextColor(mContext.getResources().getColor(R.color.app_detail_safe_normal));
            }else {
                tv_line2.setTextColor(mContext.getResources().getColor(R.color.app_detail_safe_warning));
            }
            line.addView(iv_line2);
            line.addView(tv_line2);
            mContainerDes.addView(line);
        }
    }

    @Override
    protected View initView() {
        View view = View.inflate(mContext, R.layout.item_detail_safe, null);
        x.view().inject(this,view);
        return view;
    }
}
