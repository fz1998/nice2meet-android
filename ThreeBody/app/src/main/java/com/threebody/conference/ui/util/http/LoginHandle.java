package com.threebody.conference.ui.util.http;


import android.content.Context;

import com.threebody.conference.ui.util.http.entity.BaseResponse;
import com.threebody.conference.ui.util.http.entity.LoginRequest;
import com.threebody.conference.ui.util.http.entity.LoginResponse;
import com.threebody.conference.ui.util.http.listener.MyParser;
import com.threebody.sdk.listener.OnJoinConferenceListener;

/**
 * Created by xiaxin on 15-1-13.
 */
public class LoginHandle {
    OnJoinConferenceListener listener;
    Context context;
    public LoginHandle(Context context, OnJoinConferenceListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void joinConference(final LoginRequest request){
        HttpHelper.getInstance().login(context, request, new MyParser() {
            @Override
            public void onFinish(BaseResponse result) {
                if(result != null){
                    LoginResponse response = (LoginResponse)result;
                    listener.onJoinResult(response.getRet());
                }

            }
        }, LoginResponse.class);

    }
}
