package com.threebody.conference.ui.util.http;

/**
 * Created by xiaxin on 15-1-18.
 */
public class UrlUtil {
    public static final String SERVICE_HOST = "http://192.168.1.108:8080/";
    public static final String LOGIN = "nice2meet/user/login";//登录

    public static String getUrl(String text){
        return SERVICE_HOST+text;
    }
}
