package com.kuang2010.googleplay20.base;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kuang2010.googleplay20.R;
import com.kuang2010.googleplay20.viewhold.FootViewHold;
import com.kuang2010.googleplay20.viewhold.LunboViewHold;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * author: kuangzeyu2019
 * date: 2020/6/11
 * time: 15:13
 * desc: 可以接收轮播图数据、一般条目数据、下拉加载更多的RecyclerView.Adapter
 * ItemBean是接口返回的所有数据里的一种bean数据类型
 */
public abstract class BaseRvAdapter<ItemBean> extends RecyclerView.Adapter<BaseViewHold<ItemBean>> {

    static final int ITEMTYPE_LUNBO = 0;
    static final int ITEMTYPE_ITEMINFO = 1;
    static final int ITEMTYPE_FOOT = 2;
    private List<ItemBean> mItemBeans = new ArrayList<>();
    private List<String> mPictures = new ArrayList<>();
    private LoadingState mLoadState = LoadingState.STATE_FINISH_GONE;
    protected Context mContext;
    private int mLunboItemCount;
    private int mFootItemCount;


    public BaseRvAdapter(Context context) {
        mContext = context;
    }

    /**
     * 设置一般的条目数据和更新UI
     * @param itemBeans
     */
    public void setItemBeans(List<ItemBean> itemBeans){
        mItemBeans.clear();
        if (itemBeans!=null && itemBeans.size()>0){
            mItemBeans.addAll(itemBeans);
        }
        notifyDataSetChanged();
    }

    /**
     * 获取一般的条目数据
     * @return
     */
    public List<ItemBean> getItemBeans() {
        if (mItemBeans == null) {
            return new ArrayList<>();
        }
        return mItemBeans;
    }

    /**
     * 设置轮播图数据和更新轮播图UI
     * @param pictures
     */
    public void setLunboDatas(List<String> pictures) {
        mPictures.clear();
        if (pictures!=null&& pictures.size()>0){
            mPictures = pictures;
        }
        notifyItemChanged(0);
    }

    public List<String> getLunboDatas() {
        return mPictures;
    }

    //上拉加载更多,view的状态
    public enum LoadingState {
        STATE_FINISH_GONE,//隐藏(没有更多数据了和加载更多完成)
        STATE_LOADING,//正在加载更多(还有更多数据)
        STATE_LOAD_ERROR;//加载出错，点击重试 (还有更多数据)
    }


    @NonNull
    @Override
    public BaseViewHold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BaseViewHold viewHold = null;
        if (viewType == ITEMTYPE_LUNBO){
            viewHold = new LunboViewHold(mContext,LayoutInflater.from(mContext).inflate(R.layout.item_lunbo,parent,false));
        }else if (viewType == ITEMTYPE_ITEMINFO){
            viewHold = getItemInfoViewHold(parent);//new AppInfoViewHold(mContext,LayoutInflater.from(mContext).inflate(R.layout.item_home_info,parent,false));
        }else if (viewType == ITEMTYPE_FOOT){
            viewHold = new FootViewHold(mContext,LayoutInflater.from(mContext).inflate(R.layout.item_load_more,parent,false));
        }
        return viewHold;
    }

    @Override
    public void onBindViewHolder(@NonNull final BaseViewHold holder, int position) {
        if (getItemViewType(position) == ITEMTYPE_LUNBO){

            holder.setData(mPictures);

        }else if (getItemViewType(position) == ITEMTYPE_ITEMINFO){

            holder.setData(mItemBeans.get(getItemBeanPos(position)));

        }else if (getItemViewType(position) == ITEMTYPE_FOOT){
            //这种形式处理加载更多缺点：加载出错时，在footview和footview的上一个item之间滑动是不会调用onBindViewHolder的。这不同于listview
            //所以当没有点击重试时不能用这种形式
            if (hasMoreData() && mLoadState != LoadingState.STATE_LOADING){
                triggerLoadMoreData(holder);
            }else {
                mLoadState = LoadingState.STATE_FINISH_GONE;
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
        mLoadState = LoadingState.STATE_LOADING;
        holder.setData(mLoadState);
        loadMoreData(mItemBeans.size(),new ILoadMoreDataAndStateCallBack<ItemBean>() {
            @Override
            public void setLoadingState(LoadingState loadState) {
                mLoadState = loadState;
                if (Looper.myLooper() == Looper.getMainLooper()){
                    holder.setData(mLoadState);
                }else {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            holder.setData(mLoadState);
                            mHandler.removeCallbacks(this);
                        }
                    });
                }

            }

            @Override
            public void setMoreDatas(List<ItemBean> moreDatas) {
                if (moreDatas!=null && moreDatas.size()>0){
                    mItemBeans.addAll(moreDatas);
                    if (Looper.myLooper() == Looper.getMainLooper()){
                        notifyDataSetChanged();
                    }else {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                notifyDataSetChanged();
                                mHandler.removeCallbacks(this);
                            }
                        });
                    }
                }
            }
        });
    }


    private Handler mHandler = new Handler();

    /**
     * 下拉加载更多数据的回调
     * @param <T>
     */
    public interface ILoadMoreDataAndStateCallBack<T>{
        /**
         * 设置加载更多的footview的UI显示状态
         * @param loadState
         */
        void setLoadingState(LoadingState loadState);

        /**
         * 添加加载更多的数据和刷新UI
         * @param moreDatas
         */
        void setMoreDatas(List<T> moreDatas);
    }


    @Override
    public int getItemCount() {
        mLunboItemCount = mPictures.size()==0?0:1;
        mFootItemCount = mItemBeans.size()==0?0:1;
        return mItemBeans.size()+mLunboItemCount+mFootItemCount;
    }

    @Override
    public int getItemViewType(int position) {
        if (position==0){

            if (mPictures.size()>0){
                return ITEMTYPE_LUNBO;
            }else {
                return ITEMTYPE_ITEMINFO;
            }

        }else if (position==getItemCount()-1){

            return ITEMTYPE_FOOT;

        }else {
            return ITEMTYPE_ITEMINFO;
        }
    }

    private int getItemBeanPos(int position) {
        return position-mLunboItemCount;
    }

    /**
     * 子类实现，获取一般条目的viewHold
     * @return
     */
    protected abstract BaseViewHold getItemInfoViewHold(ViewGroup parent);

    /**
     * 子类复写，判断是否有加载更多的数据
     * @return true还有更多数据，会展示加载更多的footview
     *          false没有更多数据，会隐藏加载更多的footview
     */
    protected  boolean hasMoreData(){
        return false;
    };

    /**
     * 子类复写，当有更多数据时子类复写此方法，真正去加载更多的数据
     * @param size  已加载数据的条目个数
     * @param iLoadMoreDataAndStateCallBack 加载更多数据的回调
     * @return
     */
    protected void loadMoreData(int size, ILoadMoreDataAndStateCallBack iLoadMoreDataAndStateCallBack){
        //空实现
    };

}
