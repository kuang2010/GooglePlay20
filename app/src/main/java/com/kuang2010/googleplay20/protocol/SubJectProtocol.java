package com.kuang2010.googleplay20.protocol;

import com.kuang2010.googleplay20.base.SuperLoadBaseProtocol;
import com.kuang2010.googleplay20.bean.SubjectBean;
import com.kuang2010.googleplay20.util.GsonUtil;

import java.util.List;

/**
 * author: kuangzeyu2019
 * date: 2020/6/14
 * time: 21:55
 * desc:
 */
public class SubJectProtocol extends SuperLoadBaseProtocol<List<SubjectBean>,SubjectBean> {


    @Override
    protected String getInterceKey() {
        return "subject";
    }

    @Override
    protected List<SubjectBean> getItemBeans(List<SubjectBean> subjectBeans) {
        return subjectBeans;
    }

    @Override
    protected List<SubjectBean> parasJsonString(String result) {
        return GsonUtil.json2List(result,SubjectBean.class);
    }
}
