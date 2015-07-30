package com.threebody.sdk.http;


import com.threebody.sdk.http.entity.BaseResponse;
import com.threebody.sdk.http.entity.LoginRequest;
import com.threebody.sdk.http.entity.LoginResponse;
import com.threebody.sdk.http.listener.MyParser;
import com.threebody.sdk.listener.LoginListener;

/**
 * Created by xiaxin on 15-1-13.
 */
public class LoginHandle {
    LoginListener listener;

    public LoginHandle(LoginListener listener) {
        this.listener = listener;
    }

    public void login(final LoginRequest request){
        HttpHelper.getInstance().login(request, new MyParser() {
            @Override
            public void onFinish(BaseResponse result) {
                if(result != null){
                    LoginResponse response = (LoginResponse)result;
                    listener.onLoginResult(response);
                }

            }
        }, LoginResponse.class);

    }
}