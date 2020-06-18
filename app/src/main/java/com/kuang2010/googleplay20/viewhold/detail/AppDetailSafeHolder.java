package com.kuang2010.googleplay20.viewhold.detail;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewAnimator;

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
public class AppDetailSafeHolder extends BaseHolder<AppInfoBean> implements View.OnClickListener {
    @ViewInject(R.id.app_detail_safe_iv_arrow)
    ImageView mIvArrow;
    @ViewInject(R.id.app_detail_safe_des_container)
    LinearLayout mContainerDes;
    @ViewInject(R.id.app_detail_safe_pic_container)
    LinearLayout	mContainerPic;
    private ValueAnimator mDesOpenAnimator;
    private ValueAnimator mDeesCloseAnimator;
    private ObjectAnimator mIvArrowOpenAnima;
    private ObjectAnimator mIvArrowCloseAnima;
    private boolean isContainerDesOpen  = true;

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

        mContainerDes.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mContainerDes.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int desHeight = mContainerDes.getMeasuredHeight();
                initAnimator(desHeight);

                //初始化mContainerDes为关闭状态，箭头默认向下处于关闭的，mContainerDes默认是打开状态
                ViewGroup.LayoutParams layoutParams = mContainerDes.getLayoutParams();
                layoutParams.height = 0;
                mContainerDes.setLayoutParams(layoutParams);
                isContainerDesOpen = false;

                //初始化mContainerDes为打开状态，mContainerDes默认是打开状态，箭头默认向下处于关闭的
//                mIvArrow.setRotation(180);
//                isContainerDesOpen = true;

            }
        });
    }

    private void initAnimator(int desHeight) {
        //mContainerDes打开的动画：从height=0到height=desHeight的动画
        //由于mContainerDes没有直接设置高度的方法setHeight()，所以不能用ObjectAnimator
        //要用ValueAnimator,监听动画的渐变过程,得到渐变值

        mDesOpenAnimator = ValueAnimator.ofInt(0,desHeight);
        mDeesCloseAnimator = ValueAnimator.ofInt(desHeight,0);

        // 1.监听属性动画的渐变过程，得到渐变值，手动完成动画
        ValueAnimator.AnimatorUpdateListener listener = new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //在动画时间内这个方法会被多次调用！
                int height = (int) animation.getAnimatedValue();//得到渐变值
                ViewGroup.LayoutParams layoutParams = mContainerDes.getLayoutParams();
                layoutParams.height = height;//利用渐变值完成动的过程(高度折叠动画)
                mContainerDes.setLayoutParams(layoutParams);
            }
        };
        mDesOpenAnimator.addUpdateListener(listener);
        mDeesCloseAnimator.addUpdateListener(listener);

        //箭头的旋转动画  mIvArrow.setRotation();
        mIvArrowOpenAnima = ObjectAnimator.ofFloat(
                mIvArrow,
                "rotation",
                0,
                180
        );
        mIvArrowCloseAnima = ObjectAnimator.ofFloat(
                mIvArrow,
                "rotation",
                180,
                360
        );
    }

    @Override
    protected View initView() {
        View view = View.inflate(mContext, R.layout.item_detail_safe, null);
        x.view().inject(this,view);
        view.setOnClickListener(this);
        return view;
    }


    @Override
    public void onClick(View v) {
        isContainerDesOpen = !isContainerDesOpen;
        if (isContainerDesOpen){
            mDesOpenAnimator.start();
            mIvArrowOpenAnima.start();
        }else {
            mDeesCloseAnimator.start();
            mIvArrowCloseAnima.start();
        }
    }
}
