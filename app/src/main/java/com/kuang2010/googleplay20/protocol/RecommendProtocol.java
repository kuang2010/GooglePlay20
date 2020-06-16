package com.kuang2010.googleplay20.protocol;

import com.google.gson.reflect.TypeToken;
import com.kuang2010.googleplay20.base.SuperLoadBaseProtocol;
import com.kuang2010.googleplay20.util.GsonUtil;

import java.util.List;

/**
 * author: kuangzeyu2019
 * date: 2020/6/16
 * time: 10:40
 * desc:
 */
public class RecommendProtocol extends SuperLoadBaseProtocol<List<String>,String> {

    @Override
    protected List<String> getItemBeans(List<String> strings) {
        return strings;
    }

    @Override
    protected List<String> parasJsonString(String result) {
        return GsonUtil.jsonType2List(result,new TypeToken<List<String>>(){}.getType());
    }


    @Override
    protected String getInterceKey() {
        return "recommend";
    }
}
