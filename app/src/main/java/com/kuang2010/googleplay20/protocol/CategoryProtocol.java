package com.kuang2010.googleplay20.protocol;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kuang2010.googleplay20.base.SuperLoadBaseProtocol;
import com.kuang2010.googleplay20.bean.CategoryBean;
import com.kuang2010.googleplay20.util.GsonUtil;

import java.util.List;

/**
 * author: kuangzeyu2019
 * date: 2020/6/16
 * time: 11:12
 * desc:
 */
public class CategoryProtocol extends SuperLoadBaseProtocol<List<CategoryBean>,CategoryBean> {
    @Override
    protected List<CategoryBean> getItemBeans(List<CategoryBean> categoryBeans) {
        return categoryBeans;
    }

    @Override
    protected List<CategoryBean> parasJsonString(String result) {
//        Gson gson = new Gson();
//        List<CategoryBean> o = gson.fromJson(result, new TypeToken<List<CategoryBean>>() {
//        }.getType());

        return GsonUtil.jsonType2List(result,new TypeToken<List<CategoryBean>>(){}.getType());
    }

    @Override
    protected String getInterceKey() {
        return "category";
    }
}
