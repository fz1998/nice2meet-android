package cn.tee3.n2m.biz.http;

/**
 * Created by xiaxin on 15-1-18.
 */
public class UrlUtil {
    public static final String SERVICE_HOST = "http://121.41.119.216:8081/";
    public static final String LOGIN = "nice2meet/user/login";//登录

    public static String getUrl(String text){
        return SERVICE_HOST+text;
    }
}
