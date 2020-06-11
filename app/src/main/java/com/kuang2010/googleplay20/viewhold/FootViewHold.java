package com.kuang2010.googleplay20.viewhold;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.kuang2010.googleplay20.R;
import com.kuang2010.googleplay20.adapter.HomeRvAdapter.LoadState;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import androidx.annotation.NonNull;


/**
 * author: kuangzeyu2019
 * date: 2020/6/11
 * time: 16:15
 * desc:
 */
public class FootViewHold extends BaseViewHold<LoadState>{

    @ViewInject(R.id.item_loadmore_container_loading)
    LinearLayout mContainerLoading;

    @ViewInject(R.id.item_loadmore_container_retry)
    LinearLayout			mContainerRetry;

    public FootViewHold(Context context, @NonNull View itemView) {
        super(itemView);
        x.view().inject(this,itemView);
    }

    @Override
    public void setData(LoadState loadState) {
        // 隐藏所有
        mContainerLoading.setVisibility(8);
        mContainerRetry.setVisibility(8);

        switch (loadState) {
            case STATE_LOADING:// 显示加载视图
                mContainerLoading.setVisibility(0);
                break;
            case STATE_LOAD_ERROR:// 显示重试视图
                mContainerRetry.setVisibility(0);
                break;
            case STATE_GONE:// 啥也不显示
                break;

            default:
                break;
        }
    }
}
