package com.threebody.sdk.http;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.threebody.sdk.http.listener.MyParser;
import com.threebody.sdk.http.entity.LoginRequest;

public class HttpHelper {

    AsyncHttpClient client = new AsyncHttpClient();
    private static HttpHelper instance = new HttpHelper();
    public static final int METHOD_POST = 0;
    public static final int METHOD_GET = 1;
    public static final int METHOD_DELETE = 2;
    public static final int METHOD_PUT = 3;

    private HttpHelper(){

    }
    public static HttpHelper getInstance(){
        return instance;
    }
    public void cancel(Context context) {
        if(client != null){
            client.cancelRequests(context, true);
        }
    }
    //登录
    public void login(LoginRequest request, MyParser parser, Class cls){
        HttpUtil.httpActionText(client, UrlUtil.getUrl(UrlUtil.LOGIN), request, parser, null, cls, METHOD_GET);
    }


}
