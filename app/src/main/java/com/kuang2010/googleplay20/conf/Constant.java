package com.kuang2010.googleplay20.conf;

/**
 * author: kuangzeyu2019
 * date: 2020/6/11
 * time: 15:30
 * desc:
 */
public class Constant {
    public static final long	PROTOCOLTIMEOUT	= 24 * 60 * 60 * 1000;//接口数据的本地保存时间长
    public static final String APPINFO_PACKAGENAME = "packageName";

    public static final class URlS {
        public static final String	BASEURL			= "http://192.168.2.140:8080/GooglePlayServer/";
        public static final String	IMAGEBASEURL	= BASEURL + "image?name=";
    }

    public static final class REQ {

    }

    public static final class RESPONSE {

    }

    public static final class PAY {
        public static final int	PAYTYPE_ZHIFUBAO	= 1;
        public static final int	PAYTYPE_UUPAY		= 2;
        public static final int	PAYTYPE_WEIXIN		= 3;
    }
}
