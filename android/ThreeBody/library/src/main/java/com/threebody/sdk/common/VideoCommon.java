package com.threebody.sdk.common;

import com.threebody.sdk.domain.DeviceBean;
import com.threebody.sdk.domain.VideoBean;
import com.threebody.sdk.util.LoggerUtil;

import org.st.Screen;
import org.st.User;
import org.st.Video;
import org.webrtc.VideoRenderer;

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
    public static  int IS_CAMERA_OPEN = CAMERA_OFF;
    public static final int VIDEO_OPEN= 40001;
    public static final int VIDEO_CLOSE= 40002;
    public static final int VIDEO_STATUS = 40003;
    public static final int SCREEN_OPEN= 40004;
    public static final int SCREEN_CLOSE= 40005;
    Video.CameraType currentCameraType;
    RoomCommon roomCommon;
    protected VideoCallback callback;

    Video video;
    Screen.ScreenListener screenListener;
    Screen screen;
    List<DeviceBean> devices;

    public VideoCommon(RoomCommon roomCommon, VideoCallback callback) {
        this.roomCommon = roomCommon;
        this.callback = callback;
        video = roomCommon.getRoom().getVideo();
        screen = roomCommon.getRoom().getScreen();
        init();
    }

    public RoomCommon getRoomCommon() {
        return roomCommon;
    }

    private void init(){
        if(devices == null){
            devices = new ArrayList<>();
        }
        video = roomCommon.getRoom().getVideo();
        video.setAutoRotation(false);
        roomCommon.setVideoCommon(this);
        initListener();
    }
    private synchronized DeviceBean findDeviceById(int nodeid ,String deviceId){
        if(devices != null && !devices.isEmpty()){
            for (DeviceBean deviceBean : devices){
                if(deviceId.equals(deviceBean.getDeviceId()) && deviceBean.getNodeId() == nodeid){
                    return deviceBean;
                }
            }
        }
        return null;
    }
    private int checkDeviceShowCount()
    {
        int count = 0;
        for (DeviceBean deviceBean: devices){
            if (deviceBean.isVideoChecked())
                count++;
        }
        return count;
    }

    public List<DeviceBean> getDevices() {
        return devices;
    }

    private void initListener(){
        if (video == null)
            return;

        Video.VideoListener listener = new Video.VideoListener() {
            @Override
            synchronized public void onOpenVideo(int result, int nodeId, String deviceId) {
                LoggerUtil.info(tag, "onOpenVideo result = "+result+" nodeId = "+nodeId+" deviceId = "+deviceId);

                if(result == 0){
                    User user = roomCommon.findUserById(nodeId);
                    if(user != null){
                        if(user.getNodeId() == roomCommon.getMe().getNodeId()){
                            IS_CAMERA_OPEN = CAMERA_ON;
                        }
                        user.setVideoOn(true);

                        DeviceBean deviceBean = new DeviceBean(nodeId, deviceId);
                        if (checkDeviceShowCount()<2){
                            deviceBean.setVideoChecked(true);
                        }

                        deviceBean.setUser(user);
                        devices.add(deviceBean);
                        if(checkCallback()){
                            callback.onOpenVideo(deviceBean);
                        }
                    }


                }


            }

            @Override
            synchronized public void onCloseVideo(int result, int nodeId, String deviceId) {
                LoggerUtil.info(tag, "onCloseVideo result = "+result+" nodeId = "+nodeId+" deviceId = "+deviceId);
                DeviceBean deviceBean = findDeviceById(nodeId,deviceId);
                if(deviceBean != null) {
                    deviceBean.setVideoChecked(false);
                }


                if(result == 0){
                    User user = roomCommon.findUserById(nodeId);
                    if(user != null){
                        if(user.getNodeId() == getRoomCommon().getMe().getNodeId()){
                            IS_CAMERA_OPEN = CAMERA_OFF;
                        }
                        user.setVideoOn(false);
                    }
                    if(deviceBean != null){
                        devices.remove(deviceBean);
                    }
                }

                if(checkCallback()){
                    callback.onCloseVideo(result, nodeId, deviceId);
                }
            }

            @Override
            synchronized public void onRequestOpenVideo(int nodeId, String deviceId) {
                LoggerUtil.info(tag, "onRequestOpenVideo nodeId = "+nodeId+" deviceId = "+deviceId);
                if(checkCallback()){
                    callback.onRequestOpenVideo(nodeId, deviceId);
                }
            }

            @Override
            synchronized public void onVideoData(int nodeId, String deviceId, byte[] data, int len, int width, int height) {
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

        screenListener = new Screen.ScreenListener(){
            @Override
            public void onShareScreen(int result, int nodeId, String screenId){
                LoggerUtil.info(tag, "onShareScreen result = "+result+" nodeId = "+nodeId+" deviceId = "+screenId);

                if(result == 0){
                    User user = roomCommon.findUserById(nodeId);
                    if(user != null){
                        if(user.getNodeId() == roomCommon.getMe().getNodeId()){
                            IS_CAMERA_OPEN = CAMERA_ON;
                        }
                        user.setVideoOn(true);

                        DeviceBean deviceBean = new DeviceBean(nodeId, screenId, true);
                        if (checkDeviceShowCount()<2){
                            deviceBean.setVideoChecked(true);
                        }

                        deviceBean.setUser(user);
                        devices.add(deviceBean);
                        if(checkCallback()){
                            callback.onShareScreen(deviceBean);
                        }
                    }


                }
            }
            @Override
            public void onCloseScreen(int result, int nodeId, String screenId){
                LoggerUtil.info(tag, "onCloseScreen result = "+result+" nodeId = "+nodeId+" deviceId = "+screenId);
                DeviceBean deviceBean = findDeviceById(nodeId,screenId);
                deviceBean.setVideoChecked(false);
                if(checkCallback()){
                    callback.onCloseScreen(result, nodeId, screenId);
                }
                if(result == 0){
                    User user = roomCommon.findUserById(nodeId);
                    if(user != null){
                        if(user.getNodeId() == getRoomCommon().getMe().getNodeId()){
                            IS_CAMERA_OPEN = CAMERA_OFF;
                        }
                        user.setVideoOn(false);
                    }
                    if(deviceBean != null){
                        devices.remove(deviceBean);
                    }
                }
            }
        };
        if (screen == null)
            return;
        screen.setListener(screenListener);

    }
    //
    public int getMaxVideo(){
        return video.getMaxVideo();
    }
    public int getSurplusVideo(){
        return video.getSurplusVideo();
    }
    public boolean openVideo(int nodeId){
//        if(video.openVideo(nodeId)){
//            IS_CAMERA_OPEN = CAMERA_ON;
//            return true;
//        }
        if(video.openLocalVideo(Video.CameraType.Front)){
            currentCameraType  = Video.CameraType.Front;
            IS_CAMERA_OPEN = CAMERA_ON;
            return true;
        }else if(video.openLocalVideo(Video.CameraType.Back)){
            currentCameraType  = Video.CameraType.Back;
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

    public boolean setScreenRender(int nodeId, String screenId, VideoRenderer renderer){
        LoggerUtil.info(getClass().getName(), " nodeId = "+nodeId+" renderer = "+renderer.toString());
        return screen.setScreenRender(nodeId, screenId, renderer);
    }

    public boolean setVideoRender(int nodeId,  String deviceId, VideoRenderer renderer){
        LoggerUtil.info(getClass().getName(), " nodeId = "+nodeId+" renderer = "+renderer.toString());
        return video.setVideoRender(nodeId,deviceId, renderer);
    }

    public  boolean removeSreenRender(int nodeId, String screenId, VideoRenderer renderer){
        return screen.removeScreenRender(nodeId, screenId,renderer);
    }

    public  boolean removeVideoRender(int nodeId,String deviceId, VideoRenderer renderer){
//        return video.removeVideoRender(nodeId, deviceId, renderer);
          return video.removeVideoRender(nodeId,renderer);
    }

    public  boolean switchVideoRender( VideoRenderer renderer1, VideoRenderer renderer2){
//        return video.removeVideoRender(nodeId, deviceId, renderer);
        return video.switchRender(renderer1, renderer2);
    }
    public  boolean switchVideo( ){
        Video.CameraType now = (currentCameraType== Video.CameraType.Back ? Video.CameraType.Front: Video.CameraType.Back);
//        return video.removeVideoRender(nodeId, deviceId, renderer);
        if (video.switchLocalVideo(now)){
            currentCameraType = now;
            return true;
        }
        return false;
    }
    protected boolean checkCallback(){
        if(callback == null){
            return false;
        }
        return true;
    }
    public interface VideoCallback{
         void onShareScreen(DeviceBean deviceBean);
         void onCloseScreen(int result, int nodeId, String deviceId);
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
