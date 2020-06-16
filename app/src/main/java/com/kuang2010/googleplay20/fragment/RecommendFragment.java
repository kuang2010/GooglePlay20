package com.kuang2010.googleplay20.fragment;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.kuang2010.googleplay20.base.BaseFragment;
import com.kuang2010.googleplay20.base.LoadingPager;
import com.kuang2010.googleplay20.base.SuperLoadBaseProtocol;
import com.kuang2010.googleplay20.protocol.RecommendProtocol;
import com.kuang2010.googleplay20.util.DensityUtil;
import com.kuang2010.googleplay20.view.flyinout.ShakeListener;
import com.kuang2010.googleplay20.view.flyinout.StellarMap;

import java.util.List;
import java.util.Random;

/**
 * author: kuangzeyu2019
 * date: 2020/6/9
 * time: 15:08
 * desc: 推荐
 * 飞入飞出布局
 */
public class RecommendFragment extends BaseFragment {

    ShakeListener mShakeListener;

    List<String> mDatas;

    @Override
    protected void initData(final LoadingPager.ILoadDataFinishPageStateCallBack callBack) {
        RecommendProtocol recommendProtocol = new RecommendProtocol();
        recommendProtocol.loadListData(0, callBack, new SuperLoadBaseProtocol.OnLoadItemDataResultListener<String>() {
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


        final StellarMap stellarMap = new StellarMap(mContext);//飞入飞出

        final RecommendAdapter adapter = new RecommendAdapter();
        stellarMap.setAdapter(adapter);

        // 设置拆分规则
        stellarMap.setRegularity(15, 20);

        // 设置首页选中
        stellarMap.setGroup(0, true);

        // 加入摇一摇
        mShakeListener = new ShakeListener(mContext);

        mShakeListener.setOnShakeListener(new ShakeListener.OnShakeListener() {
            @Override
            public void onShake() {
                int currentGroup = stellarMap.getCurrentGroup();
                if (currentGroup == adapter.getGroupCount() - 1) {
                    currentGroup = 0;
                } else {
                    currentGroup++;
                }
                // 切换
                stellarMap.setGroup(currentGroup, true);
            }
        });
        return stellarMap;
    }

    @Override
    public void onPause() {
        if (mShakeListener != null) {
            mShakeListener.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        if (mShakeListener != null) {
            mShakeListener.resume();
        }
        super.onResume();
    }

    class RecommendAdapter implements StellarMap.Adapter {

        private static final int	PAGESIZE	= 15;//每组的孩子个数

        @Override
        public int getGroupCount() {// 返回有几组
            // 33/15 = 3
            if (mDatas.size() % PAGESIZE != 0) {// 有余数
                return mDatas.size() / PAGESIZE + 1;
            }
            return mDatas.size() / PAGESIZE;
        }

        @Override
        public int getCount(int group) {// 每组有多少个
            // 15 15 3
            if (group == getGroupCount() - 1) {// 最后一页
                // 最后一页
                if (mDatas.size() % PAGESIZE != 0) {
                    return mDatas.size() % PAGESIZE;
                }
            }
            return PAGESIZE;
        }

        @Override
        public View getView(int group, int position, View convertView) {// 具体view
            int location = group * PAGESIZE + position;

            TextView tv = new TextView(mContext);
            tv.setText(mDatas.get(location));
            int padding = DensityUtil.dip2px(mContext,4);
            tv.setPadding(padding, padding, padding, padding);
            Random random = new Random();
            // 随机大小
            tv.setTextSize(random.nextInt(4) + 12);// 12-16
            // 随机颜色
            int alpha = 255;
            int red = random.nextInt(190) + 30;// 30-220,没到0和255是为了色值柔和一点
            int green = random.nextInt(190) + 30;// 30-220
            int blue = random.nextInt(190) + 30;// 30-220
            int color = Color.argb(alpha, red, green, blue);
            tv.setTextColor(color);
            return tv;
        }

        @Override
        public int getNextGroupOnPan(int group, float degree) {
            // TODO
            return 0;
        }

        @Override
        public int getNextGroupOnZoom(int group, boolean isZoomIn) {
            // TODO
            return 0;
        }

    }

}
