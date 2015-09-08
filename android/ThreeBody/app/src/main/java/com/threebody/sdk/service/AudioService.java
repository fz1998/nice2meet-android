package com.threebody.sdk.service;


import com.threebody.sdk.util.LoggerUtil;

import org.st.Audio;
import org.st.User;

public class AudioService {
    String tag = getClass().getName();

    private Audio audioModule;

    AudioCallback callback;
    RoomService roomService;

    private boolean isMicOn = false;
    private boolean isHandFree = true;

    public AudioService(RoomService roomService, AudioCallback callbak) {
        audioModule = roomService.getRoomModule().getAudio();
        this.callback = callbak;
        roomService.setAudioService(this);
        this.roomService = roomService;
        initListener();
    }

    private boolean checkMe(int nodeId) {
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
                isMicOn = false;
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
                User user = roomService.findUserById(nodeId);
                if (user != null) {
                    user.setAudioOn(true);
                }
                if (result == 0) {
                    if (checkMe(nodeId)) {
                        isMicOn = true;
                    }
                }
                if (callback != null) {
                    callback.onOpenMicrophone(result, nodeId);
                }
            }

            @Override
            synchronized public void onCloseMicrophone(int result, int nodeId) {
                LoggerUtil.info(tag, "onCloseMic result = " + result + " nodeId = " + nodeId);
                User user = roomService.findUserById(nodeId);
                if (user != null) {
                    user.setAudioOn(false);
                }
                if (result == 0) {
                    if (checkMe(nodeId)) {
                        isMicOn = false;
                    }
                }
                if (callback != null) {
                    callback.onCloseMicrophone(result, nodeId);
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

    public boolean isMicOn() {
        return isMicOn;
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
        void onOpenMicrophone(int result, int nodeId);

        /**
         * 关闭MIC结果
         *
         * @param result
         * @param nodeId
         */
        void onCloseMicrophone(int result, int nodeId);

        /**
         * 请求打开音频设备，房间音频路数用完，主持人会接受到此请求，用于音频控制
         *
         * @param nodeId
         */
        void onRequestOpenMicrophone(int nodeId);
    }
}
