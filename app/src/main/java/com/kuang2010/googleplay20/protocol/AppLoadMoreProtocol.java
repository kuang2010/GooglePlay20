package com.kuang2010.googleplay20.protocol;

import com.kuang2010.googleplay20.base.LoadMoreBaseProtocol;
import com.kuang2010.googleplay20.bean.AppInfoBean;
import com.kuang2010.googleplay20.bean.HomeBean;
import com.kuang2010.googleplay20.util.GsonUtil;

import java.util.List;

/**
 * author: kuangzeyu2019
 * date: 2020/6/14
 * time: 17:27
 * desc: 作废
 */
public class AppLoadMoreProtocol extends LoadMoreBaseProtocol<List<AppInfoBean>, AppInfoBean> {


    @Override
    protected String getInterceKey() {
        return "app";
    }


    @Override
    protected List<AppInfoBean> getItemBeans(List<AppInfoBean> appInfoBeans) {
        return appInfoBeans;
    }

    @Override
    protected List<AppInfoBean> parasJsonString(String result) {
//        return GsonUtil.json2List(result,AppInfoBean.class);
        return GsonUtil.json2List(result,AppInfoBean.class);
    }
}
