package com.kuang2010.googleplay20.protocol;

import com.kuang2010.googleplay20.DetailActivity;
import com.kuang2010.googleplay20.base.SuperLoadBaseProtocol;
import com.kuang2010.googleplay20.bean.AppInfoBean;
import com.kuang2010.googleplay20.conf.Constant;
import com.kuang2010.googleplay20.util.GsonUtil;

import java.util.HashMap;
import java.util.List;

/**
 * author: kuangzeyu2019
 * date: 2020/6/16
 * time: 19:29
 * desc:
 */
public class DetailPageProtocol extends SuperLoadBaseProtocol<AppInfoBean,AppInfoBean> {

    private String mPackageName;
    public DetailPageProtocol(String packageName){
        mPackageName = packageName;
    }

    @Override
    protected AppInfoBean parasJsonString(String result) {
        return GsonUtil.json2Bean(result,AppInfoBean.class);
    }

    @Override
    protected String getInterceKey() {
        return "detail";
    }

    @Override
    protected void setParams(HashMap<String, String> params) {
        params.clear();
        params.put(Constant.APPINFO_PACKAGENAME,mPackageName);
    }
}
