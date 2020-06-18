package com.kuang2010.googleplay20.bean;

import com.kuang2010.googleplay20.manager.DownLoadManager;

/**
 * author: kuangzeyu2019
 * date: 2020/6/18
 * time: 22:34
 * desc:
 */
public class DownloadInfo {
    public String	downloadUrl;	// 下载的接口地址
    public String  downloadUrlParmaName;//请求参数1-name下载的应用名称
//    public String  downloadUrlParmaRange;//请求参数2-range从什么位置开始下载

    public String	packageName;	// 应用的包名
    public long		size;			// 应用的总大小
    public String version		;   //		1.1.0605.0

    public String savePath;//保存路径
    public long curProgress; //当前的下载进度,请求参数2-range从什么位置开始下载
    public int mCurState;//当前下载的状态

}
