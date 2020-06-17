package com.kuang2010.googleplay20.viewhold.detail;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.View;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.kuang2010.googleplay20.R;
import com.kuang2010.googleplay20.base.BaseHolder;
import com.kuang2010.googleplay20.bean.AppInfoBean;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * author: kuangzeyu2019
 * date: 2020/6/16
 * time: 19:20
 * desc: 应用的描述部分
 */
public class AppDetailDesHolder extends BaseHolder<AppInfoBean> implements View.OnClickListener {
    @ViewInject(R.id.app_detail_des_tv_author)
    TextView			mTvAuthor;
    @ViewInject(R.id.app_detail_des_tv_des)
    TextView			mTvDes;
    @ViewInject(R.id.app_detail_des_iv_arrow)
    ImageView mIvArrow;

    private boolean openState;
    private int mMeasuredTvDesHeight;//mTvDes 本身的高度
    private int mCloseHeight;//mTvDes 关闭时的高度
    private ObjectAnimator mTvDesOpenAnim;//mTvDes打开动画
    private ObjectAnimator mTvDesCloseAnim;//mTvDes关闭动画
    private AppInfoBean mAppInfoBean;
    private ObjectAnimator mMIvArrowOpenAnim;
    private ObjectAnimator mMIvArrorCloseAnim;

    public AppDetailDesHolder(Context context) {
        super(context);
    }

    @Override
    protected void bindDataToView(AppInfoBean data) {
        if (data==null){return;}
        mAppInfoBean = data;
        mTvAuthor.setText(data.author);
        mTvDes.setText(data.des);

        mTvDes.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                mTvDes.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                int lineCount = mTvDes.getLineCount();
                int height = mTvDes.getHeight();
                mMeasuredTvDesHeight = mTvDes.getMeasuredHeight();
                int line7 = mMeasuredTvDesHeight/lineCount * 7;

                mCloseHeight = lineCount>7? line7:mMeasuredTvDesHeight;//getShorHeight(7, mAppInfoBean.des);

                //初始化使mTvDes处于关闭状态,mIvArrow默认是向下处于关闭的
//                mTvDes.setHeight(mCloseHeight);
//                openState = false;

                //初始化使mTvDes处于打开状态,mTvDes默认处于打开状态
                mIvArrow.setRotation(-180);
                openState = true;

                initAnimator();
            }
        });


    }

    @Override
    protected View initView() {
        View view = View.inflate(mContext, R.layout.item_detail_des, null);
        x.view().inject(this,view);
        view.setOnClickListener(this);
        return view;
    }

    private void initAnimator() {
        //初始化mTvDes的打开动画：从7行关闭时的高度，到mTvDes本身的高度（如果本身高度大于7行则有动画效果，否则没有动画效果）
        //textview没有设置行数时在点击事件里是拿不到它实际的高度的,要到onGlobalLayout里获取
        //textview是有setHeight()方法的即有height属性，可以用ObjectAnimator设置动画效果
        //属性动画默认时长400ms
        mTvDesOpenAnim = ObjectAnimator.ofInt(
                mTvDes,
                "height",//高度折叠动画
                mCloseHeight,
                mMeasuredTvDesHeight
        );

        mTvDesCloseAnim = ObjectAnimator.ofInt(
                mTvDes,
                "height",//高度折叠动画
                mMeasuredTvDesHeight,
                mCloseHeight
        );

        /*mTvDes.setPivotY(0);
        mTvDes.setScaleY(0.5f);//缩放-->变挤,变松,可视的内容不会变多或变少
        int measuredHeight = mTvDes.getMeasuredHeight();
        int height = mTvDes.getHeight();
        mTvDesOpenAnim = ObjectAnimator.ofFloat(
                mTvDes,
                "scaleY",
                0.5f,
                1.0f
        );
        mTvDesCloseAnim = ObjectAnimator.ofFloat(
                mTvDes,
                "scaleY",
                1.0f,
                0.5f
        );*/

        //监听属性动画的执行过程
        Animator.AnimatorListener listener = new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                int measuredHeight = mTvDes.getMeasuredHeight();
                int height = mTvDes.getHeight();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                int measuredHeight = mTvDes.getMeasuredHeight();
                int height = mTvDes.getHeight();

                //找爹
                ViewParent parent = mTvDes.getParent();
                if (parent!=null){
                    while (true){
                        parent = parent.getParent();
                        if (parent==null){
                            //找完了没找到ScrollView
                            break;
                        }
                        if (parent instanceof ScrollView){
                            // 滚动  ScrollView 到最底部
                            ((ScrollView)parent).fullScroll(View.FOCUS_DOWN);
                        }
                    }
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        };

        mTvDesOpenAnim.addListener(listener);
        mTvDesCloseAnim.addListener(listener);

        //初始化箭头的打开动画：旋转180度
        mMIvArrorCloseAnim = ObjectAnimator.ofFloat(
                mIvArrow,
                "rotation",
                180,
                0
        );
        mMIvArrowOpenAnim = ObjectAnimator.ofFloat(
                mIvArrow,
                "rotation",
                0,
                180
        );

    }

    @Override
    public void onClick(View v) {
        openState = !openState;
        if (openState){
            mTvDesOpenAnim.start();
            mMIvArrowOpenAnim.start();
        }else {
            mTvDesCloseAnim.start();
            mMIvArrorCloseAnim.start();
        }
    }


    //获取行数对应的高度
    private int getShorHeight(int line, String des) {
        TextView mTempTextView = new TextView(mContext);
        mTempTextView.setText(des);
        mTempTextView.setLines(line);// 需要设置行高才可以拿到具体的高度
        mTempTextView.measure(0, 0);
        int measuredHeight = mTempTextView.getMeasuredHeight();

        return measuredHeight;
    }
}
