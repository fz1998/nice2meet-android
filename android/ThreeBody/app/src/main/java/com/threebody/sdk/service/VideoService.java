package com.threebody.sdk.service;

import com.threebody.sdk.domain.N2MVideo;
import com.threebody.sdk.util.LoggerUtil;

import org.st.Screen;
import org.st.User;
import org.st.Video;
import org.webrtc.VideoRenderer;

import java.util.List;

import cn.tee3.n2m.VideoDisplayController;

/**
 * Created by xiaxin on 15-2-4.
 */
public class VideoService {

    protected String tag = getClass().getName();
    public static final int CAMERA_OFF = 0;
    public static final int CAMERA_ON = 1;
    public static final int CAMERA_HOLD = 2;
    public static final int VIDEO_OPEN= 40001;
    public static final int VIDEO_CLOSE= 40002;
    public static final int VIDEO_STATUS = 40003;
    public static final int SCREEN_OPEN= 40004;
    public static final int SCREEN_CLOSE= 40005;

    private VideoDisplayController videoDisplayController;

    //// FIXME: 2015/9/5 no use static flag
    public static  int IS_CAMERA_OPEN = CAMERA_OFF;
    Video.CameraType currentCameraType;
    RoomService roomService;
    protected VideoCallback callback;
    Video videoModule;
    Screen.ScreenListener screenListener;

    Screen screenModule;

    public void setVideoDisplayController(VideoDisplayController videoDisplayController) {
        this.videoDisplayController = videoDisplayController;
    }

    public VideoService(RoomService roomService, VideoCallback callback) {
        this.roomService = roomService;
        this.callback = callback;
        videoModule = roomService.getRoomModule().getVideo();
        screenModule = roomService.getRoomModule().getScreen();
        init();
    }

    public RoomService getRoomService() {
        return roomService;
    }

    private void init(){

        videoModule = roomService.getRoomModule().getVideo();
        videoModule.setAutoRotation(false);
        roomService.setVideoService(this);
        initListener();
    }

    private synchronized N2MVideo findDeviceById(int nodeid ,String deviceId){
        List<N2MVideo> videos = videoDisplayController.getVideoList();
        if(videos != null && !videos.isEmpty()){
            for (N2MVideo n2MVideo : videos){
                if(deviceId.equals(n2MVideo.getDeviceId()) && n2MVideo.getNodeId() == nodeid){
                    return n2MVideo;
                }
            }
        }
        return null;
    }

    //// FIXME: 2015/9/5 used by videoModule selection list adaptor, need be careful.
    public List<N2MVideo> getDevices() {
        return videoDisplayController.getVideoList();
    }

    private void initListener(){
        if (videoModule == null)
            return;

        Video.VideoListener listener = new Video.VideoListener() {
            @Override
            synchronized public void onOpenVideo(int result, int nodeId, String deviceId) {
                LoggerUtil.info(tag, "onOpenVideo result = "+result+" nodeId = "+nodeId+" deviceId = "+deviceId);

                if(result == 0){
                    User user = roomService.findUserById(nodeId);
                    if(user != null){
                        if(videoBelongToCurrentUser(user)){
                            IS_CAMERA_OPEN = CAMERA_ON;
                        }
                        user.setVideoOn(true);
                        N2MVideo video = new N2MVideo(nodeId, deviceId);
                        video.setUser(user);

                        //// TODO: 2015/9/1 should display videoModule here ,rather than come all the way from VideoFragment to do this
                        // add new device to video display controller, attaching it to VideoWindow if possible
                        videoDisplayController.addVideo(video);

                        // call back for UI update
                        // just ask UI to update, no device passed here ?
                        if(checkCallback()){
                            callback.onOpenVideo(video);
                        }
                    }
                }
            }

            private boolean videoBelongToCurrentUser(User user) {
                return user.getNodeId() == roomService.getMe().getNodeId();
            }

            @Override
            synchronized public void onCloseVideo(int result, int nodeId, String deviceId) {
                LoggerUtil.info(tag, "onCloseVideo result = "+result+" nodeId = "+nodeId+" deviceId = "+deviceId);
                N2MVideo n2MVideo = findDeviceById(nodeId,deviceId);
                if(n2MVideo != null) {
                    n2MVideo.setVideoChecked(false);
                }

                if(result == 0){
                    User user = roomService.findUserById(nodeId);
                    if(user != null){
                        if(user.getNodeId() == getRoomService().getMe().getNodeId()){
                            IS_CAMERA_OPEN = CAMERA_OFF;
                        }
                        user.setVideoOn(false);
                    }

                    if(n2MVideo != null){
                        videoDisplayController.deleteVideo(n2MVideo);
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
                // TODO: 2015/9/8 implement this if need to take care of raw videoModule data, in the future.
            }
        };
        videoModule.setListener(listener);

        screenListener = new Screen.ScreenListener(){
            @Override
            public void onShareScreen(int result, int nodeId, String screenId){

                LoggerUtil.info(tag, "onShareScreen result = "+result+" nodeId = "+nodeId+" deviceId = "+screenId);

                if(result == 0){
                    User user = roomService.findUserById(nodeId);
                    if(user != null){
                        if(videoBelongToCurrentUser(user)){
                            IS_CAMERA_OPEN = CAMERA_ON;
                        }
                        user.setVideoOn(true);
                        user.setUserName(user.getUserName() + " Screen"); // TODO: 2015/9/8 dirty. set user name as name + "screen"
                        N2MVideo video = new N2MVideo(nodeId, screenId, true);
                        video.setUser(user);

                        //// TODO: 2015/9/1 should display videoModule here ,rather than come all the way from VideoFragment to do this
                        // add new device to video display controller, attaching it to VideoWindow if possible
                        videoDisplayController.addVideo(video);

                        // call back for UI update
                        // just ask UI to update, no device passed here ?
                        if(checkCallback()){
                            callback.onShareScreen(video);
                        }
                    }
                }
            }

            // TODO: 2015/9/8 duplicated code here.
            private boolean videoBelongToCurrentUser(User user) {
                return user.getNodeId() == roomService.getMe().getNodeId();
            }

            @Override
            public void onCloseScreen(int result, int nodeId, String screenId){

                LoggerUtil.info(tag, "onCloseScreen result = "+result+" nodeId = "+nodeId+" deviceId = "+screenId);
                N2MVideo video = findDeviceById(nodeId,screenId);
                if (video != null) {
                    video.setVideoChecked(false);
                }
                if(result == 0){
                    User user = roomService.findUserById(nodeId);
                    if(user != null){
                        if(user.getNodeId() == getRoomService().getMe().getNodeId()){
                            IS_CAMERA_OPEN = CAMERA_OFF;
                        }
                        user.setVideoOn(false);
                    }
                    if(video != null){
                        videoDisplayController.deleteVideo(video);
                    }
                }
                if(checkCallback()){
                    callback.onCloseScreen(result, nodeId, screenId);
                }
            }
        };

        if (screenModule != null) {
            screenModule.setListener(screenListener);
        }

    }

    public boolean openVideo(int nodeId){
        if(videoModule.openLocalVideo(Video.CameraType.Front)){
            currentCameraType  = Video.CameraType.Front;
            IS_CAMERA_OPEN = CAMERA_ON;
            return true;
        }else if(videoModule.openLocalVideo(Video.CameraType.Back)){
            currentCameraType  = Video.CameraType.Back;
            IS_CAMERA_OPEN = CAMERA_ON;
            return true;
        }
        return false;
    }

    public boolean closeVideo(int nodeId){
        if(videoModule.closeVideo(nodeId)){
            IS_CAMERA_OPEN = CAMERA_OFF;
            return true;
        }
        return false;
    }

    public boolean attachRenderToVideo(int nodeId, String deviceId, VideoRenderer renderer){
        LoggerUtil.info(getClass().getName(), " nodeId = " + nodeId + " renderer = " + renderer.toString());
        return videoModule.setVideoRender(nodeId, deviceId, renderer);
    }

    public  boolean removeScreenRender(int nodeId, String screenId, VideoRenderer renderer){
        return screenModule.removeScreenRender(nodeId, screenId, renderer);
    }

    public  boolean removeVideoRender(int nodeId,String deviceId, VideoRenderer renderer){
//        return videoModule.removeVideoRender(nodeId, deviceId, renderer);
          return videoModule.removeVideoRender(nodeId, renderer);
    }

    public  boolean switchCamera(){
        Video.CameraType now = (currentCameraType== Video.CameraType.Back ? Video.CameraType.Front: Video.CameraType.Back);
//        return videoModule.removeVideoRender(nodeId, deviceId, renderer);
        if (videoModule.switchLocalVideo(now)){
            currentCameraType = now;
            return true;
        }
        return false;
    }

    protected boolean checkCallback(){
        return callback != null;
    }

    public interface VideoCallback{
         void onShareScreen(N2MVideo n2MVideo);
         void onCloseScreen(int result, int nodeId, String deviceId);
         void onOpenVideo(N2MVideo n2MVideo);
         void onCloseVideo(int result, int nodeId, String deviceId);
         void onRequestOpenVideo(int nodeId, String deviceId);
    }
}
