package com.kuang2010.googleplay20;

import com.kuang2010.googleplay20.util.GsonUtil;

/**
 * author: kuangzeyu2019
 * date: 2020/6/21
 * time: 14:41
 * desc:
 */
public class Test<T> {

    public void test(){
        String js = "{'a':'1','b':['2','3','4']}";

        TestBean testBean = GsonUtil.json2Bean(js, TestBean.class);

        System.out.println(">>>>>>>>"+testBean.toString());


        TestBean testBean2 = GsonUtil.json2T(js, this);
        System.out.println(">>>>>>>>"+testBean2.toString());
    }


}
