package com.threebody.sdk.common;

import com.threebody.sdk.domain.DeviceBean;
import com.threebody.sdk.domain.VideoBean;
import com.threebody.sdk.util.LoggerUtil;

import org.st.User;
import org.st.Video;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaxin on 15-2-4.
 */
public class VideoCommon {
    protected String tag = getClass().getName();
    public static final int CAMERA_OFF = 0;
    public static final int CAMERA_ON = 1;
    public static final int CAMERA_HOLD = 2;
    public static  int IS_CAMERA_OPEN ;
    public static final int VIDEO_OPEN= 40001;
    public static final int VIDEO_CLOSE= 40002;
    public static final int VIDEO_STATUS = 40003;
    RoomCommon roomCommon;
    protected VideoCallback callback;
    Video.VideoListener listener;
    Video video;
    List<DeviceBean> devices;
    protected VideoCommon(RoomCommon roomCommon, VideoCallback callback) {
        this.roomCommon = roomCommon;
        this.callback = callback;
        video = roomCommon.getVideo();
        init();
    }

    public RoomCommon getRoomCommon() {
        return roomCommon;
    }

    private void init(){
        if(devices == null){
            devices = new ArrayList<>();
        }
        video = roomCommon.getVideo();
        roomCommon.setVideoCommon(this);
        initListener();
    }
    private DeviceBean findDeviceById(String deviceId){
        if(devices != null && !devices.isEmpty()){
            for (DeviceBean deviceBean : devices){
                if(deviceId.equals(deviceBean.getDeviceId())){
                    return deviceBean;
                }
            }
        }
        return null;
    }

    public List<DeviceBean> getDevices() {
        return devices;
    }

    protected void initListener(){
        listener = new Video.VideoListener() {
            @Override
            public void onOpenVideo(int result, int nodeId, String deviceId) {
                LoggerUtil.info(tag, "onOpenVideo result = "+result+" nodeId = "+nodeId+" deviceId = "+deviceId);

                if(result == 0){
                    User user = roomCommon.findUserById(nodeId);
                    if(user != null){
                        if(user.getNodeId() == roomCommon.getMe().getNodeId()){
                            IS_CAMERA_OPEN = CAMERA_ON;
                        }
                        user.setVideoOn(true);
                        DeviceBean deviceBean = new DeviceBean(nodeId, deviceId);
                        deviceBean.setUser(user);
                        devices.add(deviceBean);
                        if(checkCallback()){
                            callback.onOpenVideo(deviceBean);
                        }
                    }


                }


            }

            @Override
            public void onCloseVideo(int result, int nodeId, String deviceId) {
                LoggerUtil.info(tag, "onCloseVideo result = "+result+" nodeId = "+nodeId+" deviceId = "+deviceId);
                if(checkCallback()){
                    callback.onCloseVideo(result, nodeId, deviceId);
                }
                if(result == 0){
                    User user = roomCommon.findUserById(nodeId);
                    if(user != null){
                        if(user.getNodeId() == getRoomCommon().getMe().getNodeId()){
                            IS_CAMERA_OPEN = CAMERA_OFF;
                        }
                        user.setVideoOn(false);
                    }
                    DeviceBean deviceBean = findDeviceById(deviceId);
                    if(deviceBean != null){
                        devices.remove(deviceBean);
                    }
                }

            }

            @Override
            public void onRequestOpenVideo(int nodeId, String deviceId) {
                LoggerUtil.info(tag, "onRequestOpenVideo nodeId = "+nodeId+" deviceId = "+deviceId);
                if(checkCallback()){
                    callback.onRequestOpenVideo(nodeId, deviceId);
                }
            }

            @Override
            public void onVideoData(int nodeId, String deviceId, byte[] data, int len, int width, int height) {
                VideoBean videoBean = new VideoBean();
                videoBean.setNodeId(nodeId);
                videoBean.setDeviceId(deviceId);
                videoBean.setVideoData(data);
                videoBean.setLength(len);
                videoBean.setWidth(width);
                videoBean.setHeight(height);
                if(checkCallback()){
                    callback.onVideoData(videoBean);
                }
            }
        };
        video.setListener(listener);
    }
    //
    public int getMaxVideo(){
        return video.getMaxVideo();
    }
    public int getSurplusVideo(){
        return video.getSurplusVideo();
    }
    public boolean openVideo(int nodeId){
        if(video.openVideo(nodeId)){
            IS_CAMERA_OPEN = CAMERA_ON;
            return true;
        }
        return false;
    }
    public boolean closeVideo(int nodeId){
        if(video.closeVideo(nodeId)){
            IS_CAMERA_OPEN = CAMERA_OFF;
            return true;
        }
        return false;
    }
    public boolean openVideo(int nodeId, String deviceId){
        return video.openVideo(nodeId, deviceId);
    }
    public  boolean closeVideo(int nodeId, String deviceId){
        return video.closeVideo(nodeId, deviceId);
    }
    protected boolean checkCallback(){
        if(callback == null){
            return false;
        }
        return true;
    }
    public interface VideoCallback{
         void onOpenVideo(DeviceBean deviceBean);
         void onCloseVideo(int result, int nodeId, String deviceId);
         void onRequestOpenVideo(int nodeId, String deviceId);
         void onVideoData(VideoBean videoBean);
    }
    private boolean checkMe(int nodeId){
        User me = roomCommon.getSelf();
        if(me != null){
            if(me.getNodeId() == nodeId){
                return true;
            }
        }
        return false;
    }
}
