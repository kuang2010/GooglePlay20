package com.kuang2010.googleplay20;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;

/**
 * author: kuangzeyu2019
 * date: 2020/6/15
 * time: 0:21
 * desc:
 */
public class BaseApplication extends Application {

    private static BaseApplication mApp;

    public static BaseApplication getInstance(){
        return mApp;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;
    }
}
