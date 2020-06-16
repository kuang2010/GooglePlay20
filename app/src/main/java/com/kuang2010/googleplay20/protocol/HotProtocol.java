package com.kuang2010.googleplay20.protocol;

import com.kuang2010.googleplay20.base.SuperLoadBaseProtocol;
import com.kuang2010.googleplay20.util.GsonUtil;

import java.util.List;

/**
 * author: kuangzeyu2019
 * date: 2020/6/16
 * time: 8:35
 * desc:
 */
public class HotProtocol extends SuperLoadBaseProtocol<List<String>,String> {


    @Override
    protected List<String> getItemBeans(List<String> strings) {
        return strings;
    }

    @Override
    protected List<String> parasJsonString(String result) {
        return GsonUtil.json2List(result,String.class);
    }

    @Override
    protected String getInterceKey() {
        return "hot";
    }
}
