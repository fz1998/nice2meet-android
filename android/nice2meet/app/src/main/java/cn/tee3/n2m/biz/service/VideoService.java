package cn.tee3.n2m.biz.service;

import cn.tee3.n2m.biz.domain.N2MVideo;
import cn.tee3.n2m.biz.util.LoggerUtil;

import org.st.Screen;
import org.st.User;
import org.st.Video;
import org.webrtc.VideoRenderer;

import java.util.List;

import cn.tee3.n2m.ui.VideoDisplayController;

/**
 * Created by xiaxin on 15-2-4.
 */
public class VideoService {

    protected String tag = getClass().getName();

    private VideoDisplayController videoDisplayController;

    Video.CameraType currentCameraType;
    RoomService roomService;
    Video videoModule;
    private boolean isLocalVideoOn = false;

    Screen.ScreenListener screenListener;
    Screen screenModule;

    protected VideoCallback callback;

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

    private synchronized N2MVideo findVideoByIdAndDevice(int nodeid, String deviceId){
        List<N2MVideo> videos = videoDisplayController.getVideoList();
        if(videos != null && !videos.isEmpty()){
            for (N2MVideo n2MVideo : videos){
//                if(deviceId.equals(n2MVideo.getDeviceId()) && n2MVideo.getNodeId() == nodeid){
//                    return n2MVideo;
//                }
                if (n2MVideo.getNodeId() == nodeid){
                    if (deviceId.equals(n2MVideo.getDeviceId()) || n2MVideo.getDeviceId() == ""){
                        return  n2MVideo;
                    }
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
                        if(videoBelongToLocalUser(user)){
                            isLocalVideoOn = true;
                        }

//                        List<N2MVideo> videoList = videoDisplayController.getVideoListById(nodeId);
//                        if (videoList.size() == 0) {
//                            N2MVideo video = new N2MVideo(nodeId, deviceId);
//                            videoList.set(0, new N2MVideo(nodeId));
//                            video.setUser(user);
//                            videoDisplayController.addVideo(video);
//                        }else {
//                            for (N2MVideo video: videoList) {
//                                video.setUser(user);
//                            }
//                        }

                        N2MVideo video = findVideoByIdAndDevice(nodeId, deviceId);
                        if (video == null) {
                            video = new N2MVideo(nodeId, deviceId);
                            user.setVideoOn(true);
                            video.setUser(user);
                            videoDisplayController.addVideo(video);
                        } else {
                            //check if the device is null
                            video.getUser().setVideoOn(true);
                            if (video.getDeviceId() == null || video.getDeviceId() == ""){
                                video.setDeviceId(deviceId);
                            }
                        }


                        //// TODO: 2015/9/1 should display videoModule here ,rather than come all the way from VideoFragment to do this
                        // add new device to video display controller, attaching it to VideoWindow if possible

                        // call back for UI update
                        // just ask UI to update, no device passed here ?
                        if(callback != null){
                            callback.onOpenVideo(video);
                        }
                    }
                }
            }

            private boolean videoBelongToLocalUser(User user) {
                return user.getNodeId() == roomService.getMe().getNodeId();
            }

            @Override
            synchronized public void onCloseVideo(int result, int nodeId, String deviceId) {
                LoggerUtil.info(tag, "onCloseVideo result = "+result+" nodeId = "+nodeId+" deviceId = "+deviceId);

                if(result == 0){
                    User user = roomService.findUserById(nodeId);
                    if(user != null) {
                        if (user.getNodeId() == getRoomService().getMe().getNodeId()) {
                            isLocalVideoOn = false;
                        }
//                        user.setVideoOn(false);

                        N2MVideo video = findVideoByIdAndDevice(nodeId, deviceId);

                        if (video != null && !video.getUser().isAudioOn()) {
                            videoDisplayController.deleteVideo(video);
                            video.getUser().setVideoOn(false);
                        }
                    }
                }

                if(callback != null){
                    callback.onCloseVideo(result, nodeId, deviceId);
                }
            }

            @Override
            synchronized public void onRequestOpenVideo(int nodeId, String deviceId) {
                LoggerUtil.info(tag, "onRequestOpenVideo nodeId = "+nodeId+" deviceId = "+deviceId);
                if(callback != null){
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
                            isLocalVideoOn = true;
                        }
                        user.setVideoOn(true);
                        user.setUserName(user.getUserName() + " Screen"); // TODO: 2015/9/8 dirty. set user name as name + "screen"

                        N2MVideo video = videoDisplayController.getVideoById(nodeId);
                        if (video == null) {
                            video = new N2MVideo(nodeId, screenId, true);
                        } else {
                            //check if the device is null
//                            if (video.getDeviceId() == null || video.getDeviceId() == ""){
//                                video.setDeviceId(deviceId);
//                            }
                        }

                        video.setUser(user);

                        //// TODO: 2015/9/1 should display videoModule here ,rather than come all the way from VideoFragment to do this
                        // add new device to video display controller, attaching it to VideoWindow if possible
                        videoDisplayController.addVideo(video);

                        // call back for UI update
                        // just ask UI to update, no device passed here ?
                        if(callback != null){
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
                N2MVideo video = findVideoByIdAndDevice(nodeId, screenId);
                if (video != null) {
                    video.setVideoChecked(false);
                }
                if(result == 0){
                    User user = roomService.findUserById(nodeId);
                    if(user != null){
                        if(user.getNodeId() == getRoomService().getMe().getNodeId()){
                            isLocalVideoOn = false;
                        }
                        user.setVideoOn(false);
                    }
                    if(video != null){
                        videoDisplayController.deleteVideo(video);
                    }
                }
                if(callback != null){
                    callback.onCloseScreen(result, nodeId, screenId);
                }
            }
        };

        if (screenModule != null) {
            screenModule.setListener(screenListener);
        }

    }

    public boolean openLocalVideo(int nodeId){
        if(videoModule.openLocalVideo(Video.CameraType.Front)){
            currentCameraType  = Video.CameraType.Front;
            isLocalVideoOn = true;
            return true;
        }else if(videoModule.openLocalVideo(Video.CameraType.Back)){
            currentCameraType  = Video.CameraType.Back;
            isLocalVideoOn = true;
            return true;
        }
        return false;
    }

    public boolean closeVideo(int nodeId){
        if(videoModule.closeVideo(nodeId)){
            isLocalVideoOn = false;
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
        return videoModule.removeVideoRender(nodeId, renderer);
    }

    public  boolean switchCamera(){
        Video.CameraType now = (currentCameraType== Video.CameraType.Back ? Video.CameraType.Front: Video.CameraType.Back);
        if (videoModule.switchLocalVideo(now)) {
            currentCameraType = now;
            return true;
        } else {
            return false;
        }
    }

    public boolean isLocalVideoOn() {
        return isLocalVideoOn;
    }

    public interface VideoCallback{
        void onShareScreen(N2MVideo n2MVideo);
        void onCloseScreen(int result, int nodeId, String deviceId);
        void onOpenVideo(N2MVideo n2MVideo);
        void onCloseVideo(int result, int nodeId, String deviceId);
        void onRequestOpenVideo(int nodeId, String deviceId);
    }
}
