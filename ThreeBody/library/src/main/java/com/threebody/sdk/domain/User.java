package com.threebody.sdk.domain;

/**
 * Created by xiaxin on 15-1-14.
 */
public class User {
    String uid;
    String name;
    boolean isVideoChecked;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isVideoChecked() {
        return isVideoChecked;
    }

    public void setVideoChecked(boolean isVideoChecked) {
        this.isVideoChecked = isVideoChecked;
    }
}
