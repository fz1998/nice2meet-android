package com.threebody.sdk.http.entity;

/**
 * Created by xiaxin on 15-1-18.
 */
public class LoginRequest {
    String name;
    String password;

    public LoginRequest(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
