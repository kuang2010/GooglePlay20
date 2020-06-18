package com.kuang2010.googleplay20.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.kuang2010.googleplay20.R;

/**
 * author: kuangzeyu2019
 * date: 2020/6/14
 * time: 21:28
 * desc: 在不同分辨率的手机上图片展示变形，是由于控件ImageView的
 * 宽高比与图片本身的宽高比不一样造成的，使用该类能使子控件ImageView等的宽高按已知图片的宽高比例进行不同手机的适配。
 * 0.已知宽度,能够动态计算设置高度
 * 1.已知高度,能够动态计算设置宽度
 */
public class RatioLayout extends FrameLayout {

    // 图片的宽高比 ==RatioLayout宽/RatioLayout高
    private float			mPicRatio		= 0.0f;

    /**1.已知宽度,能够动态计算高度*/
    public static final int	RELATIVE_WIDTH	= 0;
    /** 2.已知高度,能够动态计算宽度*/
    public static final int	RELATIVE_HEIGHT	= 1;

    private int				relative		= RELATIVE_WIDTH;	// 默认是,已知宽度,能够动态计算高度


    public void setPicRatio(float picRatio) {
        mPicRatio = picRatio;
    }

    public void setRelative(int relative) {
        this.relative = relative;
    }

    public RatioLayout(Context context) {
        this(context, null);
    }

    public RatioLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 取出自定义的属性

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RatioLayout);

        mPicRatio = typedArray.getFloat(R.styleable.RatioLayout_picRatio, 1.0f);// xml-->2.43

        relative = typedArray.getInt(R.styleable.RatioLayout_relative, RELATIVE_WIDTH);

        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        /**
         android.view.View.MeasureSpec.UNSPECIFIED:  wrap_content
         android.view.View.MeasureSpec.EXACTLY:  match_parent 100px 100dp
         android.view.View.MeasureSpec.AT_MOST
         */
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (widthMode == MeasureSpec.EXACTLY && relative == RELATIVE_WIDTH) {// 宽度已知的情况-->动态计算高度
            // 获取自身的宽度
            int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
            int parentHeight = (int) (parentWidth / mPicRatio + .5f);

            // 孩子的宽度和高度
            int childWidth = parentWidth - getPaddingLeft() - getPaddingRight();
            int childHeigth = parentHeight - getPaddingBottom() - getPaddingTop();

            // 保存测量结果
            setMeasuredDimension(parentWidth, parentHeight);

            // 告知孩子测绘方式,请求孩子测绘自身
            int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY);
            int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(childHeigth, MeasureSpec.EXACTLY);
            measureChildren(childWidthMeasureSpec, childHeightMeasureSpec);
        } else if (heightMode == MeasureSpec.EXACTLY && relative == RELATIVE_HEIGHT) {// 高度已知的情况-->动态计算宽度
            // 获取自身的高度
            // 图片的宽高比==RatioLayout宽/RatioLayout高
            int parentHeight = MeasureSpec.getSize(heightMeasureSpec);
            int parentWidth = (int) (mPicRatio * parentHeight + .5f);

            // 孩子的宽度和高度
            int childWidth = parentWidth - getPaddingLeft() - getPaddingRight();
            int childHeigth = parentHeight - getPaddingBottom() - getPaddingTop();

            // 保存测量结果
            setMeasuredDimension(parentWidth, parentHeight);

            // 告知孩子测绘方式,请求孩子测绘自身
            int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY);
            int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(childHeigth, MeasureSpec.EXACTLY);
            measureChildren(childWidthMeasureSpec, childHeightMeasureSpec);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }

    }
}

