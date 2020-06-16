package com.kuang2010.googleplay20.protocol;

import com.google.gson.reflect.TypeToken;
import com.kuang2010.googleplay20.base.LoadBaseProtocol;
import com.kuang2010.googleplay20.base.SuperLoadBaseProtocol;
import com.kuang2010.googleplay20.bean.AppInfoBean;
import com.kuang2010.googleplay20.bean.HomeBean;
import com.kuang2010.googleplay20.util.GsonUtil;

import java.util.List;

/**
 * author: kuangzeyu2019
 * date: 2020/6/13
 * time: 21:21
 * desc:
 */
public class AppLoadProtocol extends SuperLoadBaseProtocol<List<AppInfoBean>, AppInfoBean> {


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
        return GsonUtil.jsonType2List(result,new TypeToken<List<AppInfoBean>>(){}.getType());
    }
}
