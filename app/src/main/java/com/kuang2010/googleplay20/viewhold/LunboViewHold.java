package com.kuang2010.googleplay20.viewhold;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.kuang2010.bannerview.BannerAdapter;
import com.kuang2010.bannerview.Bannerview;
import com.kuang2010.bannerview.PointIndicatorView;
import com.kuang2010.googleplay20.R;
import com.kuang2010.googleplay20.base.BaseViewHold;
import com.kuang2010.googleplay20.conf.Constant;
import com.kuang2010.googleplay20.util.BitmapUtilFactory;
import com.kuang2010.googleplay20.util.ListCompareUtil;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

/**
 * author: kuangzeyu2019
 * date: 2020/6/11
 * time: 16:13
 * desc:
 */
public class LunboViewHold extends BaseViewHold<List<String>> {

    private  Bannerview mBannerview;
    private  PointIndicatorView mPv_main;
    private Context mContext;
    private List<String> mPictures;

    public LunboViewHold(Context context, @NonNull View itemView) {
        super(itemView);
        mContext = context;
        mBannerview = itemView.findViewById(R.id.banner_view);
        mPv_main = itemView.findViewById(R.id.pv_main);
        mBannerview.setIndicator(mPv_main);

    }

    @Override
    public void setData(List<String> pictures) {
        if (ListCompareUtil.compareList(mPictures,pictures)){
            return;
        }
        mPictures = pictures;
        mBannerview.initDatasAndItem(mPictures, new BannerAdapter.OnSetItemViewListener<String>() {
            @Override
            public View instantiateItem(int position, String data) {
                ImageView iv = new ImageView(mContext);
                iv.setScaleType(ImageView.ScaleType.FIT_XY);
                BitmapUtilFactory.getBitmapUtils(mContext).display(iv, Constant.URlS.IMAGEBASEURL + data);
                return iv;
            }
        });

        mBannerview.startAutoPlay(2000);
    }
}
