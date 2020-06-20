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
        //以下两种方式添加mView,会使mView的父控件不是同一个
//        mView = LayoutInflater.from(getContext()).inflate(R.layout.view_download_progress, this);//方式一添加会使得该自定义view<==>mview即自定义view=mView成为同一个控件且属性叠加，mView的父控件即自定义view的父控件。
        mView = LayoutInflater.from(getContext()).inflate(R.layout.view_download_progress, null);//方式二添加会使mView作为自定义view的子控件添加进自定义view中，自定义view!=mView，mView的宽高、margin属性会被设置无效，变为wrap_content，mView的父控件则是该自定义view。
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


    /**注意view.getLeft()是控件view 相对 于view的 父 控件的左边距，要找准mView的父控件**/
    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);// (图标,和文字)
        int left = this.getLeft();//自定义view的相对（自定义view的父控件，很可能是屏幕）位置
        Log.d("dispatchDraw","left:"+left);
        int left1 = mView.getLeft();//内根mView相对于 自定义view 或 自定义view的父控件 的位置，当以方式一添加时内根view就是自定义view，此时的父控件是自定义view的父控件。
        Log.d("dispatchDraw","left1:"+left1);
        //在自定义中RectF等区域里的left... 位置 也是个相对值，是相对于该自定义控件的位置， 坐标原点在自定义控件的左上角。
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
