package com.kuang2010.googleplay20.manager;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.kuang2010.googleplay20.base.BaseActivity;

import java.util.Stack;

import androidx.fragment.app.FragmentActivity;

/**
 * author: kuangzeyu2019
 * date: 2020/6/21
 * time: 9:28
 * desc:
 */
public class ActivityStack {
    private static  Stack<BaseActivity> mBaseActivityStack = new Stack<>();//此处用不用static都一样(在mActivityStack里Stack也只有一个实例mBaseActivityStack)，因为ActivityStack的实例只会被创建一次
    private static ActivityStack mActivityStack = new ActivityStack();//效果等同于下面
    /*static {
        if (mActivityStack==null){//有无判空都一样
            mActivityStack = new ActivityStack();
        }
    }*/
    private ActivityStack(){System.out.println("ActivityStack被实例化了1次");};
    public static ActivityStack getInastance(){
        return mActivityStack;
    }

    public void addActivity(BaseActivity activity){
        if (activity!=null){
            mBaseActivityStack.add(activity);
        }
    }

    /**
     * 并移除activityStack中 指定的BaseActivity
     * @param activity
     */
    public void removeActivity(BaseActivity activity){
        if (mBaseActivityStack.contains(activity)){
            mBaseActivityStack.remove(activity);
        }
    }

    /**
     * 获取activityStack中 栈顶BaseActivity
     */
    public BaseActivity topActivity() {
        if (mBaseActivityStack.isEmpty()) {
            return null;
        }
        BaseActivity activity = mBaseActivityStack.peek();//mBaseActivityStack.lastElement();
        return activity;
    }


    /**
     * 获取activityStack中cls对应的BaseActivity， 没有找到则返回null
     */
    public BaseActivity findActivity (Class<?> cls) {
        BaseActivity activity = null;
        for (BaseActivity aty : mBaseActivityStack) {
            if (aty.getClass().equals(cls)) {
                activity = aty;
                break;
            }
        }
        return activity;
    }


    /**
     * 关闭并移除activityStack中指定的BaseActivity，
     * remove并不能finish掉界面
     */
    public void finishActivity(BaseActivity activity) {
        if (activity != null) {
            removeActivity(activity);
            activity.finish();
        }
    }

    /**
     * 关闭并移除activityStack中指定的cls对应的BaseActivity，
     */
    public void finishActivity(Class<?> cls) {
        for (int i = mBaseActivityStack.size() - 1; i >= 0; i--) {
            if (mBaseActivityStack.get(i).getClass().equals(cls)) {
                finishActivity(mBaseActivityStack.get(i));
                break;
            }
        }
    }


    /**
     * 关闭并移除activityStack中栈顶BaseActivity
     */
    public void finishTopActivity() {
        /*BaseActivity activity = mBaseActivityStack.lastElement();
        finishActivity(activity);*/
        BaseActivity activity = mBaseActivityStack.pop();
        activity.finish();
    }

    /**
     * 关闭并移除 除 指定activity以外的  全部activity ，如果cls不存在于栈中，则栈全部清空
     */
    public void finishOthersActivity(Class<?> cls) {
        for (int i = mBaseActivityStack.size() - 1; i >= 0; i--) {
            if (!(mBaseActivityStack.get(i).getClass().equals(cls))) {
                finishActivity( mBaseActivityStack.get(i));
            }
        }
    }

    /**
     * 关闭并移除 除 指定cls1和cls2以外的全部activity
     * @param cls1
     * @param cls2
     */
    public void finishOthersActivity(Class<?> cls1,Class<?> cls2) {
        for (int i = mBaseActivityStack.size() - 1; i >= 0; i--) {
            if (!(mBaseActivityStack.get(i).getClass().equals(cls1)) && !(mBaseActivityStack.get(i).getClass().equals(cls2)) ) {
                finishActivity(mBaseActivityStack.get(i));
            }
        }
    }


    /**
     * 关闭并移除所有Activity，退出应用程序
     */
    public void finishAllActivity() {
        for (int i = mBaseActivityStack.size() - 1; i >= 0; i--) {
            mBaseActivityStack.get(i).finish();
        }
        mBaseActivityStack.clear();
    }


    /**
     * 应用程序完全退出
     */
    public void AppExit() {
        try {
            finishAllActivity();
            Runtime.getRuntime().exit(0);
            android.os.Process.killProcess(android.os.Process.myPid());
        } catch (Exception e) {
            Runtime.getRuntime().exit(-1);
        }
    }

}
