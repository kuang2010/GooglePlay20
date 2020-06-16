package com.kuang2010.googleplay20.fragment;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

//import com.google.android.material.internal.FlowLayout;
import com.kuang2010.googleplay20.base.BaseFragment;
import com.kuang2010.googleplay20.base.LoadingPager;
import com.kuang2010.googleplay20.base.SuperLoadBaseProtocol;
import com.kuang2010.googleplay20.protocol.HotProtocol;
import com.kuang2010.googleplay20.util.DensityUtil;
import com.kuang2010.googleplay20.view.FlowLayout;

import java.util.List;
import java.util.Random;

/**
 * author: kuangzeyu2019
 * date: 2020/6/9
 * time: 15:07
 * desc: 排行
 * 流式布局+代码设置控件背景所需的drawable资源，替换xml写的，可以随机改变颜色值
 */
public class HotFragment extends BaseFragment {
    List<String> mDatas;
    @Override
    protected void initData(final LoadingPager.ILoadDataFinishPageStateCallBack callBack) {
        HotProtocol hotProtocol = new HotProtocol();
        hotProtocol.loadListData(0, callBack, new SuperLoadBaseProtocol.OnLoadItemDataResultListener<String>() {
            @Override
            public void setItemBeans(List<String> strings) {
                mDatas = strings;
            }

            @Override
            public void setLunboPics(List<String> mPictures) {

            }
        }, null);
    }

    @Override
    protected View initSuccessView() {

        ScrollView sv = new ScrollView(mContext);

        FlowLayout flowLayout = new FlowLayout(mContext);
//        LinearLayout flowLayout = new LinearLayout(mContext);
        com.google.android.material.internal.FlowLayout fl = new com.google.android.material.internal.FlowLayout(mContext);
        /*<item name="itemSpacing">15dp</item>*/
       /* <item name="lineSpacing">15dp</item>*/


        for (final String info : mDatas) {
            TextView tv = new TextView(mContext);
            tv.setText(info);
            int padding = DensityUtil.dip2px(mContext,5);
            tv.setPadding(padding, padding, padding, padding);
            tv.setGravity(Gravity.CENTER);
            tv.setTextColor(Color.WHITE);

            // 设置圆角的背景
            //tv.setBackgroundResource(R.drawable.shape_hot_tv);

            //代码设置控件tv背景所需的drawable资源，替换xml写的，可以随机改变颜色值
            // 默认效果
            GradientDrawable normalBg = new GradientDrawable();
            normalBg.setCornerRadius(10);

            Random random = new Random();
            int alpha = 255;
            int red = random.nextInt(170) + 30;// 30-200,没到0和255是为了色值柔和一点
            int green = random.nextInt(170) + 30;// 30-200
            int blue = random.nextInt(170) + 30;// 30-200
            int argb = Color.argb(alpha, red, green, blue);
            normalBg.setColor(argb);

            // 按下效果
            GradientDrawable pressedBg = new GradientDrawable();
            pressedBg.setCornerRadius(10);
            pressedBg.setColor(Color.DKGRAY);

            //代码设置状态选择器
            StateListDrawable selectorBg = new StateListDrawable();
            selectorBg.addState(new int[] { android.R.attr.state_pressed }, pressedBg);// 按下的状态
            selectorBg.addState(new int[] {}, normalBg);// 默认状态

            tv.setBackground(selectorBg);

            tv.setClickable(true);

            tv.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, info, 0).show();
                }
            });

//            flowLayout.setSpace(30,30);
            fl.addView(tv);
        }

        sv.addView(fl);

        return sv;
    }
}
