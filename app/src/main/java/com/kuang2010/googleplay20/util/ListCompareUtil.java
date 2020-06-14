package com.kuang2010.googleplay20.util;

import java.util.List;

/**
 * author: kuangzeyu2019
 * date: 2020/5/26
 * time: 20:55
 * desc: 比较两个list是否相同，T要实现equals方法
 */
public class ListCompareUtil {

    public static <T> boolean compareList(List<T> list1, List<T> list2){
        if (list1==null || list2==null){
            return false;
        }

        if (list1.size() != list2.size() || list1.size()==0){
            return false;
        }

        for (int i=0;i<list1.size();i++){

            if (!list1.get(i).equals(list2.get(i))){
                return false;
            }

        }

        return true;

    }
}
