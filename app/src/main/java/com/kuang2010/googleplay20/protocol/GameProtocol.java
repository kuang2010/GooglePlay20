package com.kuang2010.googleplay20.protocol;

import com.kuang2010.googleplay20.base.SuperLoadBaseProtocol;
import com.kuang2010.googleplay20.bean.AppInfoBean;
import com.kuang2010.googleplay20.util.GsonUtil;

import java.util.List;

/**
 * author: kuangzeyu2019
 * date: 2020/6/14
 * time: 19:35
 * desc:
 */
public class GameProtocol extends SuperLoadBaseProtocol<List<AppInfoBean>,AppInfoBean> {


    @Override
    protected String getInterceKey() {
        return "game";
    }

    @Override
    protected List<AppInfoBean> getItemBeans(List<AppInfoBean> appInfoBeans) {
        return appInfoBeans;
    }

    @Override
    protected List<AppInfoBean> parasJsonString(String result) {
        return GsonUtil.json2List(result,AppInfoBean.class);
    }
}
