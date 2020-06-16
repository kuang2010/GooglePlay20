package com.kuang2010.googleplay20.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kuang2010.googleplay20.R;
import com.kuang2010.googleplay20.base.BaseViewHold;
import com.kuang2010.googleplay20.bean.CategoryInfoBean;
import com.kuang2010.googleplay20.viewhold.CategoryItemViewHold;
import com.kuang2010.googleplay20.viewhold.CategoryTitleViewHold;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * author: kuangzeyu2019
 * date: 2020/6/16
 * time: 11:36
 * desc:
 */
public class CategoryAdapter extends RecyclerView.Adapter<BaseViewHold> {

    public static final int VIEWTYPE_TITLE = 0;
    public static final int VIEWTYPE_ITEM = 1;

    private List<CategoryInfoBean> mCategoryInfoBeans = new ArrayList<>();

    private Context mContext;
    public CategoryAdapter(Context context){
        mContext = context;
    }

    public void setData(List<CategoryInfoBean> categoryInfoBeans){
        mCategoryInfoBeans.clear();
        if (categoryInfoBeans!=null&&categoryInfoBeans.size()>0){
            mCategoryInfoBeans.addAll(categoryInfoBeans);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BaseViewHold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BaseViewHold viewHold = null;
        if (viewType == VIEWTYPE_TITLE){
            TextView tv = new TextView(mContext);
            tv.setTextSize(18);
            tv.setPadding(10,10,10,10);
            viewHold = new CategoryTitleViewHold(tv);
        }else if (viewType == VIEWTYPE_ITEM){
            viewHold = new CategoryItemViewHold(mContext, LayoutInflater.from(mContext).inflate(R.layout.item_category_normal,parent,false));
        }
        return viewHold;
    }


    @Override
    public void onBindViewHolder(@NonNull BaseViewHold holder, int position) {
        holder.setData(mCategoryInfoBeans.get(position));
    }

    @Override
    public int getItemCount() {
        return mCategoryInfoBeans.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mCategoryInfoBeans.get(position).isTitle){
            return VIEWTYPE_TITLE;
        }else {
            return VIEWTYPE_ITEM;
        }

    }
}
