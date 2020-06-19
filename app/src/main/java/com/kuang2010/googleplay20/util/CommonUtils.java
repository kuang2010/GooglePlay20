package com.kuang2010.googleplay20.util;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Build;

import androidx.core.content.FileProvider;

/**
 * app工具类
 */
public class CommonUtils
{

	/**
	 * 判断包是否安装
	 * 
	 * @param context
	 * @param packageName
	 * @return
	 */
	public static boolean isInstalled(Context context, String packageName)
	{
		PackageManager manager = context.getPackageManager();
		try
		{
			manager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);

			return true;
		}
		catch (NameNotFoundException e)
		{
			return false;
		}
	}

	/**
	 * 安装应用程序
	 *
	 * apkFile不能在私有文件夹下
	 * @param context
	 * @param apkFile
	 * @param authority  清单文件中内容提供者 FileProvider的主机名
	 */
	public static void installApp(Context context, File apkFile,String authority)
	{
//		Intent intent = new Intent(Intent.ACTION_VIEW);
//		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
//		context.startActivity(intent);

		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
			Uri uri = FileProvider.getUriForFile(context, authority, apkFile);
			intent.setDataAndType(uri, "application/vnd.android.package-archive");
		} else {
			intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
		}
		context.startActivity(intent);
	}

	/**
	 * 打开应用程序
	 * 
	 * @param context
	 * @param packageName
	 */
	public static void openApp(Context context, String packageName)
	{
		Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
		context.startActivity(intent);
	}
}
