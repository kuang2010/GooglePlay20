package com.kuang2010.googleplay20.factory;

import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * author: kuangzeyu2019
 * date: 2020/6/13
 * time: 21:12
 * desc:
 */
public class RecyclerViewFactory {

    public static RecyclerView createRecyclerView(Context context){
        RecyclerView rv = new RecyclerView(context);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        rv.setLayoutManager(layoutManager);
        return rv;
    }
}
