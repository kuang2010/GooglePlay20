package com.kuang2010.googleplay20.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kuang2010.googleplay20.R;
import com.kuang2010.googleplay20.base.BaseViewHold;
import com.kuang2010.googleplay20.bean.AppInfoBean;
import com.kuang2010.googleplay20.viewhold.FootViewHold;
import com.kuang2010.googleplay20.viewhold.AppInfoViewHold;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * author: kuangzeyu2019
 * date: 2020/6/11
 * time: 15:13
 * desc:
 */
public abstract class HomeRvAdapterCopy extends RecyclerView.Adapter<BaseViewHold> {

    static final int ITEMTYPE_LUNBO = 0;
    static final int ITEMTYPE_APPINFO = 1;
    static final int ITEMTYPE_FOOT = 2;
    private List<AppInfoBean> mAppInfoBeans = new ArrayList<>();
    private List<String> mPictures = new ArrayList<>();
    private LoadState mLoadState = LoadState.STATE_GONE;
    private Context mContext;
    private int mLunboItemCount;
    private int mFootItemCount;


    public HomeRvAdapterCopy(Context context) {
        mContext = context;
    }

    public void setAppInfoBeans(List<AppInfoBean> appInfoBeans){
        mAppInfoBeans.clear();
        if (appInfoBeans!=null && appInfoBeans.size()>0){
            mAppInfoBeans.addAll(appInfoBeans);
        }
        notifyDataSetChanged();
    }

    public List<AppInfoBean> getAppInfoBeans() {
        return mAppInfoBeans;
    }

    public void setPictures(List<String> pictures) {
        mPictures.clear();
        if (pictures!=null&& pictures.size()>0){
            mPictures = pictures;
        }
        notifyItemChanged(0);
    }

    public List<String> getPictures() {
        return mPictures;
    }

    //上拉加载更多,view的状态
    public enum LoadState{
        STATE_GONE,//隐藏(没有更多数据了)
        STATE_LOADING,//正在加载更多(还有有更多数据了)
        STATE_LOAD_ERROR;//加载出错，点击重试 (还有有更多数据了)
    }


    @NonNull
    @Override
    public BaseViewHold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BaseViewHold viewHold = null;
        if (viewType == ITEMTYPE_LUNBO){
            //viewHold = new LunboViewHold(LayoutInflater.from(mContext).inflate())
        }else if (viewType == ITEMTYPE_APPINFO){
            viewHold = new AppInfoViewHold(mContext,LayoutInflater.from(mContext).inflate(R.layout.item_home_info,parent,false));
        }else if (viewType == ITEMTYPE_FOOT){
            viewHold = new FootViewHold(mContext,LayoutInflater.from(mContext).inflate(R.layout.item_load_more,parent,false));
        }
        return viewHold;
    }

    @Override
    public void onBindViewHolder(@NonNull final BaseViewHold holder, int position) {
        if (getItemViewType(position) == ITEMTYPE_LUNBO){

            holder.setData(mPictures);

        }else if (getItemViewType(position) == ITEMTYPE_APPINFO){

            holder.setData(mAppInfoBeans.get(getAppInfoBeanPos(position)));

        }else if (getItemViewType(position) == ITEMTYPE_FOOT){
            //这种形式处理加载更多缺点：加载出错时，在footview和footview的上一个item之间滑动是不会调用onBindViewHolder的。这不同于listview
            //所以当没有点击重试时不能用这种形式
            if (hasMoreData() && mLoadState != LoadState.STATE_LOADING){
                triggerLoadMoreData(holder);
            }else {
                mLoadState = LoadState.STATE_GONE;
                holder.setData(mLoadState);
            }
            holder.itemView.findViewById(R.id.item_loadmore_container_retry).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    triggerLoadMoreData(holder);
                }
            });
        }
    }

    /**
     * @des 触发加载更多的数据
     * @call 滑动底,而且有更多的数据；点击重新加载数据
     */
    private void triggerLoadMoreData(@NonNull final BaseViewHold holder) {
        mLoadState = LoadState.STATE_LOADING;
        holder.setData(mLoadState);
        loadMoreData(new ILoadMoreDataCallBack() {
            @Override
            public void setLoadState(LoadState loadState) {
                mLoadState = loadState;
                if (Looper.myLooper() == Looper.getMainLooper()){
                    holder.setData(mLoadState);
                }else {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            holder.setData(mLoadState);
                        }
                    });
                }

            }
        });
    }


    private Handler mHandler = new Handler();
    public interface ILoadMoreDataCallBack{
        void setLoadState(LoadState loadState);
    }

    public abstract void loadMoreData(ILoadMoreDataCallBack iLoadMoreDataCallBack);

    protected abstract boolean hasMoreData();

    @Override
    public int getItemCount() {
        mLunboItemCount = mPictures.size()==0?0:1;
        mFootItemCount = mAppInfoBeans.size()==0?0:1;
        return mAppInfoBeans.size()+mLunboItemCount+mFootItemCount;
    }

    @Override
    public int getItemViewType(int position) {
        if (position==0){

            if (mPictures.size()>0){
                return ITEMTYPE_LUNBO;
            }else {
                return ITEMTYPE_APPINFO;
            }

        }else if (position==getItemCount()-1){

            return ITEMTYPE_FOOT;

        }else {
            return ITEMTYPE_APPINFO;
        }
    }

    private int getAppInfoBeanPos(int position) {
        return position-mLunboItemCount;
    }
}
