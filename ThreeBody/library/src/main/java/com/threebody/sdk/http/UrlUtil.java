package com.threebody.sdk.http;

/**
 * Created by xiaxin on 15-1-18.
 */
public class UrlUtil {
    public static final String SERVICE_HOST = "http://3tee.com.cn:8081/";
    public static final String LOGIN = "nice2meet/user/login";//登录

    public static String getUrl(String text){
        return SERVICE_HOST+text;
    }
}
