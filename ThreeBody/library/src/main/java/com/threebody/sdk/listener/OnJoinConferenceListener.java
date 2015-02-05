package com.threebody.sdk.listener;

import com.threebody.sdk.http.entity.LoginResponse;

/**
 * Created by xiaxin on 15-1-13.
 */
public interface OnJoinConferenceListener {
    void onJoinResult(LoginResponse result);
}
