package com.threebody.sdk.http;


import com.threebody.sdk.http.entity.BaseResponse;
import com.threebody.sdk.http.entity.LoginRequest;
import com.threebody.sdk.http.entity.LoginResponse;
import com.threebody.sdk.http.listener.MyParser;
import com.threebody.sdk.listener.OnJoinConferenceListener;

/**
 * Created by xiaxin on 15-1-13.
 */
public class LoginHandle {
    OnJoinConferenceListener listener;

    public LoginHandle(OnJoinConferenceListener listener) {
        this.listener = listener;
    }

    public void joinConference(final LoginRequest request){
        HttpHelper.getInstance().login(request, new MyParser() {
            @Override
            public void onFinish(BaseResponse result) {
                if(result != null){
                    LoginResponse response = (LoginResponse)result;
                    listener.onJoinResult(response);
                }

            }
        }, LoginResponse.class);

    }
}
