package com.kuang2010.googleplay20.viewhold;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kuang2010.googleplay20.R;
import com.kuang2010.googleplay20.base.BaseViewHold;
import com.kuang2010.googleplay20.bean.SubjectBean;
import com.kuang2010.googleplay20.conf.Constant;
import com.kuang2010.googleplay20.util.BitmapUtil;
import com.kuang2010.googleplay20.util.BitmapUtilFactory;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import androidx.annotation.NonNull;

import static com.kuang2010.googleplay20.util.BitmapUtilFactory.*;

/**
 * author: kuangzeyu2019
 * date: 2020/6/14
 * time: 21:27
 * desc:
 */
public class SubjectViewHold extends BaseViewHold<SubjectBean> {
    @ViewInject(R.id.item_subject_iv_icon)
    ImageView mIvIcon;
    @ViewInject(R.id.item_subject_tv_title)
    TextView mTvTitle;
    private Context mContext;
    public SubjectViewHold(Context context, @NonNull View itemView) {
        super(itemView);
        mContext = context;
        x.view().inject(this,itemView);
    }

    @Override
    public void setData(SubjectBean data) {
        mTvTitle.setText(data.des);
        getBitmapUtils(mContext).display(mIvIcon, Constant.URlS.IMAGEBASEURL + data.url);
    }

}
