package com.kuang2010.googleplay20.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kuang2010.googleplay20.R;

import androidx.annotation.Nullable;

/**
 * author: kuangzeyu2019
 * date: 2020/6/20
 * time: 9:04
 * desc:
 */
public class DownloadProgressView extends RelativeLayout {

    private ImageView mIv_download_progress;
    private TextView mTv_download_progress;
    private Paint mPaint;
    private View mView;
    private long mProgress;
    private long mMax = 100;

    public DownloadProgressView(Context context) {
        this(context,null);
    }

    public DownloadProgressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mView = LayoutInflater.from(getContext()).inflate(R.layout.view_download_progress, null);
        RelativeLayout.LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(CENTER_IN_PARENT);
        addView(mView,layoutParams);
        mIv_download_progress = mView.findViewById(R.id.iv_download_progress);
        mTv_download_progress = mView.findViewById(R.id.tv_download_progress);

        mPaint = new Paint();
        mPaint.setColor(getResources().getColor(R.color.progress_color));
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(getResources().getDimension(R.dimen.dp_2));
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec),MeasureSpec.getSize(heightMeasureSpec));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);// 绘制背景
        //要在使用该自定义view时给view设置个背景才能绘制 android:background="@color/translate"

    }


    /**getLeft()时控件相对于父控件的左边距位置**/
    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);// (图标,和文字)
        //在mIv_download_progress的区域里画弧：
        RectF oval = new RectF(mView.getLeft()+mIv_download_progress.getLeft(),mView.getTop()+mIv_download_progress.getTop(),mView.getLeft()+mIv_download_progress.getLeft()+mIv_download_progress.getWidth(),mView.getTop()+mIv_download_progress.getTop()+mIv_download_progress.getHeight());
        float startAngle = -90;//起始弧度
        float sweepAngle = 360 * mProgress/mMax;//扫过的弧度
        canvas.drawArc(oval,startAngle,sweepAngle,false,mPaint);

    }


    public void setProgress(long progress){
        mProgress = progress;
        invalidate();
    }

    public void setMax(long max){
        mMax = max;
    }

    public void setIcon(int resId){
        mIv_download_progress.setImageResource(resId);
    }

    public void setNote(String text){
        mTv_download_progress.setText(text);
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mOnTachedWindownListener!=null){
            mOnTachedWindownListener.onAttachedToWindow();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mOnTachedWindownListener!=null){
            mOnTachedWindownListener.onDetachedFromWindow();
        }
    }


    public interface OnTachedWindownListener{
        void onAttachedToWindow();
        void onDetachedFromWindow();
    }

    private OnTachedWindownListener mOnTachedWindownListener;

    public void setOnTachedWindownListener(OnTachedWindownListener onTachedWindownListener) {
        mOnTachedWindownListener = onTachedWindownListener;
    }
}
