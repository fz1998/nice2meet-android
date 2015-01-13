package com.threebody.conference.handle;

import com.threebody.conference.listener.OnJoinConferenceListener;

/**
 * Created by xiaxin on 15-1-13.
 */
public class LoginHandle {
    OnJoinConferenceListener listener;

    public LoginHandle(OnJoinConferenceListener listener) {
        this.listener = listener;
    }

    public void joinConference(String num, String name, String password){

    }
}
