package com.threebody.sdk.domain;

import org.st.User;

/**
 * Created by xiaxin on 15-2-10.
 */
public class N2MVideo {
    int nodeId;
    String deviceId;
    boolean isScreen = false;
    User user;
    boolean isVideoChecked;
    public N2MVideo(int nodeId, String deviceId) {
        this.nodeId = nodeId;
        this.deviceId = deviceId;
    }

    public N2MVideo(int nodeId, String deviceId, boolean isScreen) {
        this.isScreen = isScreen;
        this.nodeId = nodeId;
        this.deviceId = deviceId;
    }
    public int getNodeId() {
        return nodeId;
    }

    public void setNodeId(int nodeId) {
        this.nodeId = nodeId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isVideoChecked() {
        return isVideoChecked;
    }

    public void setVideoChecked(boolean isVideoChecked) {
        this.isVideoChecked = isVideoChecked;
    }

    public boolean isScreen() {
        return isScreen;
    }

    public boolean equals(Object device1){
        N2MVideo device = (N2MVideo) device1;
        if (device1 == null)
            return false;
        return device.getDeviceId() == this.getDeviceId()
                &&
                device.getNodeId() == this.getNodeId();
    }
}
