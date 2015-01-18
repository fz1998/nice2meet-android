package com.threebody.sdk.http.entity;

/**
 * Created by xiaxin on 15-1-18.
 */
public class LoginResponse extends BaseResponse {
    String sid;
    String access_token;
    String room_uri;
    int ret;
    String msg;

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getRoom_uri() {
        return room_uri;
    }

    public void setRoom_uri(String room_uri) {
        this.room_uri = room_uri;
    }

    public int getRet() {
        return ret;
    }

    public void setRet(int ret) {
        this.ret = ret;
    }

    @Override
    public String getMsg() {
        return msg;
    }

    @Override
    public void setMsg(String msg) {
        this.msg = msg;
    }
}
