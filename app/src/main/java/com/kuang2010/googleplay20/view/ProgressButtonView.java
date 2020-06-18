package com.kuang2010.googleplay20.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.widget.Button;

import com.kuang2010.googleplay20.R;


/**
 * author: kuangzeyu2019
 * date: 2020/6/18
 * time: 21:26
 * desc: 可以设置进度条的button按钮
 */
public class ProgressButtonView extends Button {

    private int mWidth;
    private int mHeight;
    private Paint mPaint;

    public ProgressButtonView(Context context) {
        this(context,null);
    }

    public ProgressButtonView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mWidth = (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,40,getContext().getResources().getDisplayMetrics())+0.5);
        mHeight = (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,20,getContext().getResources().getDisplayMetrics())+0.5);


        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(getResources().getColor(R.color.progress_color));
        mPaint.setStrokeWidth(getResources().getDimension(R.dimen.dp_5));
        mPaint.setAntiAlias(true);

//        setBackground(new ColorDrawable(getResources().getColor(R.color.progress_bg_color)));
        setBackgroundResource(R.drawable.selector_bg_bottom_down);


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int mode_width = MeasureSpec.getMode(widthMeasureSpec);
        int mode_height = MeasureSpec.getMode(heightMeasureSpec);
        int w = MeasureSpec.getSize(widthMeasureSpec);
        int h = MeasureSpec.getSize(heightMeasureSpec);
        if (mode_width == MeasureSpec.UNSPECIFIED ){
            w = mWidth;
        }
        if (mode_height == MeasureSpec.UNSPECIFIED){
            h = mHeight;
        }

        mWidth = w;
        mHeight = h;
        setMeasuredDimension(mWidth,mHeight);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        float left = 0;
        float top = 0;
        float right = mWidth * mProgress/mMax;
        float bottom = mHeight;
        canvas.drawRect(left,top,right,bottom,mPaint);

        super.onDraw(canvas);
    }


    private long mProgress;
    private long mMax = 100;

    public void setProgress(long progress){
        mProgress = progress ;
//        Log.d("tagtag","mProgress:"+mProgress*100/mMax);
        postInvalidate();
    }

    public void setMax(long max){
        mMax = max;
    }

    public long getProgress() {
        return mProgress*100/mMax;
    }
}
