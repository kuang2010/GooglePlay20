package com.kuang2010.googleplay20.util;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;


/**
 * @创建者	 Administrator
 * @创时间 	 2015-10-21 下午3:22:40
 * @描述	     TODO
 *
 * @版本       $Rev: 20 $
 * @更新者     $Author: admin $
 * @更新时间    $Date: 2015-10-21 15:26:19 +0800 (星期三, 21 十月 2015) $
 * @更新描述    TODO
 */
public class BitmapUtilFactory {

	private static BitmapUtil	mBitmapUtils;

	public static BitmapUtil getBitmapUtils(Context context){
		if (mBitmapUtils == null){
			synchronized (BitmapUtilFactory.class){
				if (mBitmapUtils == null){
					mBitmapUtils = new BitmapUtil(context);
				}
			}
		}
		return mBitmapUtils;
	}

}
