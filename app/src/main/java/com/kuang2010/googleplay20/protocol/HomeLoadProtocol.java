package com.kuang2010.googleplay20.protocol;

import com.kuang2010.googleplay20.base.SuperLoadBaseProtocol;
import com.kuang2010.googleplay20.bean.AppInfoBean;
import com.kuang2010.googleplay20.bean.HomeBean;
import com.kuang2010.googleplay20.util.GsonUtil;

import java.util.List;

/**
 * author: kuangzeyu2019
 * date: 2020/6/13
 * time: 19:08
 * desc:
 */
public class HomeLoadProtocol extends SuperLoadBaseProtocol<HomeBean, AppInfoBean> {

    @Override
    protected String getInterceKey() {
        return "home";
    }

    @Override
    protected List<AppInfoBean> getItemBeans(HomeBean homeBean) {
        return homeBean.list;
    }

//    @Override
//    protected HomeBean parasJsonString(String result) {
//        return GsonUtil.json2Bean(result,HomeBean.class);
//    }

    @Override
    protected List<String> getLunboPictures(HomeBean homeBean) {
        return homeBean.picture;
    }
}
