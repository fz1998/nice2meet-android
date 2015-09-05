package com.threebody.sdk.http;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.threebody.sdk.http.entity.LoginRequest;
import com.threebody.sdk.http.listener.MyParser;

public class HttpHelper {

    AsyncHttpClient client = new AsyncHttpClient();
    private static HttpHelper instance = new HttpHelper();

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

}
