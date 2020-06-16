package com.kuang2010.googleplay20.fragment;

import android.view.View;

import com.kuang2010.googleplay20.adapter.CategoryAdapter;
import com.kuang2010.googleplay20.base.BaseFragment;
import com.kuang2010.googleplay20.base.BaseProtocol;
import com.kuang2010.googleplay20.base.LoadingPager;
import com.kuang2010.googleplay20.base.SuperLoadBaseProtocol;
import com.kuang2010.googleplay20.bean.CategoryBean;
import com.kuang2010.googleplay20.bean.CategoryInfoBean;
import com.kuang2010.googleplay20.factory.RecyclerViewFactory;
import com.kuang2010.googleplay20.protocol.CategoryProtocol;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/**
 * author: kuangzeyu2019
 * date: 2020/6/9
 * time: 15:06
 * desc: 分类
 */
public class CategoryFragment extends BaseFragment {
    List<CategoryInfoBean> mCategoryInfoBeans;
    @Override
    protected void initData(final LoadingPager.ILoadDataFinishPageStateCallBack callBack) {
        CategoryProtocol categoryProtocol = new CategoryProtocol();
        categoryProtocol.loadListData(0, callBack, new SuperLoadBaseProtocol.OnLoadItemDataResultListener<CategoryBean>() {
            @Override
            public void setItemBeans(List<CategoryBean> categoryBeans) {
                mCategoryInfoBeans = new ArrayList<>();
                for (CategoryBean categoryBean : categoryBeans){
                    List<CategoryInfoBean> infos = categoryBean.infos;
                    String title = categoryBean.title;
                    //手动添加titleXXXBean:
                    CategoryInfoBean titleCategoryInfoBean = new CategoryInfoBean();
                    titleCategoryInfoBean.isTitle = true;
                    titleCategoryInfoBean.title = title;
                    mCategoryInfoBeans.add(titleCategoryInfoBean);
                    //添加itemXXXBean:
                    for (CategoryInfoBean categoryInfoBean:infos){
                        categoryInfoBean.isTitle = false;
                        mCategoryInfoBeans.add(categoryInfoBean);
                    }

                }
            }

            @Override
            public void setLunboPics(List<String> mPictures) {

            }
        }, new BaseProtocol.OnHasMoreDataListener() {
            @Override
            public void setHasMoreData(boolean hasMoreData) {

            }
        });
    }

    @Override
    protected View initSuccessView() {
        RecyclerView rv = RecyclerViewFactory.createRecyclerView(mContext);
        CategoryAdapter adapter = new CategoryAdapter(mContext);
        rv.setAdapter(adapter);
        adapter.setData(mCategoryInfoBeans);
        return rv;
    }
}
