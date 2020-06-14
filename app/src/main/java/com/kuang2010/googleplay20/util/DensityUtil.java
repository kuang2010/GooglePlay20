package com.kuang2010.googleplay20.util;

import android.content.Context;

public class DensityUtil {
	 /** 
     * 根据手机的分辨率从 dip 的单位 转成为 px(像素) 
     */  
    public static int dip2px(Context context, float dpValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);  //float 转 int 加上0.5f 可以减少精度损失（四舍五入）
    }  
  
    /** 
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp 
     */  
    public static int px2dip(Context context, float pxValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (pxValue / scale + 0.5f);  //float 转 int 加上0.5f 可以减少精度损失（四舍五入）
    }  
}
