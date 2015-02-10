package com.threebody.sdk.common;

import com.threebody.sdk.util.LoggerUtil;

import org.st.User;
import org.st.Video;

/**
 * Created by xiaxin on 15-2-4.
 */
public class VideoCommon {
    String tag = getClass().getName();
    RoomCommon roomCommon;
    VideoCallback callback;
    Video.VideoListener listener;
    Video video;
    protected VideoCommon(RoomCommon roomCommon, VideoCallback callback) {
        this.roomCommon = roomCommon;
        this.callback = callback;

        init();
    }
    private void init(){
        video = roomCommon.getVideo();
        roomCommon.setVideoCommon(this);
        initListener();
    }
    private void initListener(){
        listener = new Video.VideoListener() {
            @Override
            public void onOpenVideo(int result, int nodeId, String deviceId) {
                LoggerUtil.info(tag, "onOpenVideo result = "+result+" nodeId = "+nodeId+" deviceId = "+deviceId);
                User user = roomCommon.findUserById(nodeId);
                if(user != null){
                    user.setVideoOn(true);
                }
                    if(checkCallback()){
                        callback.onOpenVideo(result, nodeId, deviceId);
                    }
            }

            @Override
            public void onCloseVideo(int result, int nodeId, String deviceId) {
                LoggerUtil.info(tag, "onCloseVideo result = "+result+" nodeId = "+nodeId+" deviceId = "+deviceId);
                User user = roomCommon.findUserById(nodeId);
                if(user != null){
                    user.setVideoOn(false);
                }
                if(checkCallback()){
                    callback.onCloseVideo(result, nodeId, deviceId);
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
            public void onVideoData(int nodeId, String deviceId, char[] data, int len, int width, int height) {
                LoggerUtil.info(tag, "onVideoData nodeId = "+nodeId+" deviceId = "+deviceId+" len = "+len+" width = "+width+" height = "+height);
                if(checkCallback()){
                    callback.onVideoData(nodeId, deviceId, data, len, width, height);
                }
            }
        };
    }
    //
    public int getMaxVideo(){
        return video.getMaxVideo();
    }
    public int getSurplusVideo(){
        return video.getSurplusVideo();
    }
    public boolean openVideo(int nodeId, String deviceId){
        return video.openVideo(nodeId, deviceId);
    }
    public boolean closeVideo(int nodeId, String deviceId){
        return video.closeVideo(nodeId, deviceId);
    }
    private boolean checkCallback(){
        if(callback == null){
            return false;
        }
        return true;
    }
    public interface VideoCallback{
         void onOpenVideo(int result, int nodeId, String deviceId);
         void onCloseVideo(int result, int nodeId, String deviceId);
         void onRequestOpenVideo(int nodeId, String deviceId);
         void onVideoData(int nodeId, String deviceId, char[] data, int len, int width, int height);
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
