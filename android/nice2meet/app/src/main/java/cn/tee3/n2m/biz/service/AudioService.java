package cn.tee3.n2m.biz.service;


import org.st.Audio;
import org.st.User;

import java.util.List;

import cn.tee3.n2m.biz.domain.N2MVideo;
import cn.tee3.n2m.biz.util.LoggerUtil;
import cn.tee3.n2m.ui.VideoDisplayController;

public class AudioService {
    String tag = getClass().getName();

    private Audio audioModule;

    AudioCallback callback;
    RoomService roomService;

    private VideoDisplayController videoDisplayController;

    public void setVideoDisplayController(VideoDisplayController videoDisplayController) {
        this.videoDisplayController = videoDisplayController;
    }

    private boolean isLocalAudioOn = false;
    private boolean isHandFree = true;

    public AudioService(RoomService roomService, AudioCallback callbak) {
        audioModule = roomService.getRoomModule().getAudio();
        this.callback = callbak;
        roomService.setAudioService(this);
        this.roomService = roomService;
        initListener();
    }

    private boolean audioBelongToLocalUser(int nodeId) {
        User me = roomService.getCurrentUser();
        if (me != null) {
            if (me.getNodeId() == nodeId) {
                return true;
            }
        }
        return false;
    }


    public boolean openMic(int nodeId) {
        if (audioModule != null) {
            return audioModule.openMicrophone(nodeId);
        } else {
            return false;
        }
    }

    public boolean closeMic(int id) {
        if (audioModule != null) {
            boolean isSucceed = audioModule.closeMicrophone(id);
            if (isSucceed) {
                isLocalAudioOn = false;
            }
            return isSucceed;
        }
        return false;
    }

    public boolean muteMic(int id, boolean mute) {
        if (audioModule != null) {
            return audioModule.muteMicrophone(id, mute);
        } else {
            return false;
        }
    }


    private void initListener() {
        if (audioModule == null) {
            return;
        }

        Audio.AudioListener listener = new Audio.AudioListener() {
            @Override
            synchronized public void onOpenMicrophone(int result, int nodeId) {
                LoggerUtil.info(tag, "onOpenMic result = " + result + " nodeId = " + nodeId);
                if (result == 0) {
                    User user = roomService.findUserById(nodeId);
                    if (user != null) {
                        if (audioBelongToLocalUser(nodeId)) {
                            isLocalAudioOn = true;
                        }

                        Boolean needToRefreshWindow;

                        List<N2MVideo> videoList = videoDisplayController.getVideoListById(nodeId);
                        if (videoList.size() == 0) {
                            N2MVideo video = new N2MVideo(nodeId, "");
                            user.setAudioOn(true);
                            video.setUser(user);
                            videoDisplayController.addVideo(video);
                            needToRefreshWindow = true;
                        }else {
                            for (N2MVideo video: videoList) {
                                video.getUser().setAudioOn(true); ////////////////////////
                            }
                            needToRefreshWindow = false;
                        }
//                        N2MVideo video = videoDisplayController.getVideoById(nodeId);
//                        if (video == null) {
//                            video = new N2MVideo(nodeId);
//                        }

                        if (callback != null) {
                            callback.onOpenMicrophone(result, nodeId, needToRefreshWindow);
                        }
                    }
                }
            }

            @Override
            synchronized public void onCloseMicrophone(int result, int nodeId) {
                LoggerUtil.info(tag, "onCloseMic result = " + result + " nodeId = " + nodeId);
                if (result == 0) {
                    User user = roomService.findUserById(nodeId);
                    if (user != null) {
                        if (audioBelongToLocalUser(nodeId)) {
                            isLocalAudioOn = false;
                        }
//                        user.setAudioOn(false);
                        Boolean needToRefreshWindow = false;

                        List<N2MVideo> videoList = videoDisplayController.getVideoListById(nodeId);
                        for (N2MVideo video: videoList) {
                            if(video != null && !video.getUser().isVideoOn()){
                                videoDisplayController.deleteVideo(video);
                                needToRefreshWindow = true;
                            }
                            video.getUser().setAudioOn(false);
                        }

//                        if (videoList.size() == 0) {
//                            N2MVideo video = new N2MVideo(nodeId);
//                            videoList.set(0, new N2MVideo(nodeId));
//                            video.setUser(user);
//                            videoDisplayController.addVideo(video);
//                        }else {
//                            for (N2MVideo video: videoList) {
//                                video.setUser(user);
//                            }
//                        }


//                        N2MVideo video = videoDisplayController.getVideoById(nodeId);
//
//                        if(video != null && !video.getUser().isVideoOn()){
//                            videoDisplayController.deleteVideo(video);
//                        }

                        if (callback != null) {
                            callback.onCloseMicrophone(result, nodeId, needToRefreshWindow);
                        }
                    }
                }
            }

            @Override
            synchronized public void onRequestOpenMicrophone(int nodeId) {
                if (callback != null) {
                    callback.onRequestOpenMicrophone(nodeId);
                }
            }
        };
        audioModule.setListener(listener);
    }

    private boolean videoBelongToCurrentUser(User user) {
        return user.getNodeId() == roomService.getMe().getNodeId();
    }

    public boolean isLocalAudioOn() {
        return isLocalAudioOn;
    }

    public void setHandFree(boolean handFree) {
        this.isHandFree = handFree;
    }

    public boolean isHandFree() {
        return isHandFree;
    }

    public interface AudioCallback {
        /**
         * 打开MIC结果
         *
         * @param result
         * @param nodeId
         */
        void onOpenMicrophone(int result, int nodeId, Boolean needToRefreshWindow);

        /**
         * 关闭MIC结果
         *
         * @param result
         * @param nodeId
         */
        void onCloseMicrophone(int result, int nodeId, Boolean needToRefreshWindow);

        /**
         * 请求打开音频设备，房间音频路数用完，主持人会接受到此请求，用于音频控制
         *
         * @param nodeId
         */
        void onRequestOpenMicrophone(int nodeId);
    }
}
