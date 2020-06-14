package com.kuang2010.googleplay20.fragment;

import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.kuang2010.googleplay20.adapter.SubjectAdapter;
import com.kuang2010.googleplay20.base.BaseFragment;
import com.kuang2010.googleplay20.base.MianPagerControl;
import com.kuang2010.googleplay20.base.SuperLoadBaseProtocol;
import com.kuang2010.googleplay20.bean.SubjectBean;
import com.kuang2010.googleplay20.factory.RecyclerViewFactory;
import com.kuang2010.googleplay20.protocol.SubJectProtocol;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/**
 * author: kuangzeyu2019
 * date: 2020/6/9
 * time: 15:08
 * desc: 专题
 */
public class SubjectFragment extends BaseFragment {
    List<SubjectBean> mSubjectBeans;
    @Override
    protected void initData(final MianPagerControl.ILoadDataFinishPageStateCallBack callBack) {
        SubJectProtocol subJectProtocol = new SubJectProtocol();
        subJectProtocol.loadData(0, callBack, new SuperLoadBaseProtocol.OnLoadDataResultListener<SubjectBean>() {
            @Override
            public void setItemBeans(List<SubjectBean> subjectBeans) {
                mSubjectBeans = subjectBeans;
            }

            @Override
            public void setLunboPics(List<String> mPictures) {

            }
        }, null);
    }

    @Override
    protected View initSuccessView() {
        RecyclerView rv = RecyclerViewFactory.createRecyclerView(mContext);
        SubjectAdapter subjectAdapter = new SubjectAdapter(mContext);
        rv.setAdapter(subjectAdapter);
        subjectAdapter.setItemBeans(mSubjectBeans);
        return rv;
    }
}
