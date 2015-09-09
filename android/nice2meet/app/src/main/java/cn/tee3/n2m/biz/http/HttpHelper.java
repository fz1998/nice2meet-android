package cn.tee3.n2m.biz.http;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;

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
