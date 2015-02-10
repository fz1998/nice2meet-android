package com.threebody.sdk.common;


import com.threebody.sdk.util.LoggerUtil;

import org.st.Audio;
import org.st.User;

public abstract class AudioCommon {
    String tag = getClass().getName();
    public static final int MIC_OFF = 0;
    public static final int MIC_ON = 1;
    public static final int MIC_HANDS_UP = 2;
    public static int IS_MIC_ON = 0;
    private Audio audio;
    Audio.AudioListener listener;
    AudioCallback callback;
    RoomCommon roomCommon;

    public AudioCommon(RoomCommon roomCommon, AudioCallback callbak){
        audio = roomCommon.getAudio();
        this.callback = callbak;
        roomCommon.setAudioCommon(this);
        this.roomCommon = roomCommon;
        initListener();
    }

    public RoomCommon getRoomCommon() {
        return roomCommon;
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



    //
    public boolean openMic(int nodeId){
        if(checkMe(nodeId)){
            IS_MIC_ON = MIC_HANDS_UP;
        }
        if(audio != null){
            return audio.openMicrophone(nodeId);
        }
        return false;
    }
    public boolean closeMic(int id){
        if(audio != null){
            boolean isSucceed = audio.closeMicrophone(id);
            if(isSucceed){
                IS_MIC_ON = MIC_OFF;
            }
            return isSucceed;
        }
        return false;
    }
    public int getMaxAudio(){
        if(audio != null){
            return audio.getMaxAudio();
        }
        return -1;
    }

    /**
     * 获取剩余路数
     * @return
     */
    protected int getSurplusAudio(){
        if(audio != null){
            return audio.getSurplusAudio();
        }
        return -1;
    }
    private void initListener(){
        listener = new Audio.AudioListener() {
            @Override
            public void onOpenMicrophone(int result, int nodeId) {
                LoggerUtil.info(tag, "onOpenMic result = "+result+" nodeId = "+nodeId);
                User user = roomCommon.findUserById(nodeId);
                if(user != null){
                    user.setAudioOn(true);
                }
                if(result == 0){
                    if(checkMe(nodeId)){
                        IS_MIC_ON = MIC_ON;
                    }
                }
                 if(checkCallback()){
                     callback.onOpenMicrophone(result, nodeId);
                 }
            }

            @Override
            public void onCloseMicrophone(int result, int nodeId) {
                LoggerUtil.info(tag, "onCloseMic result = "+result+" nodeId = "+nodeId);
                User user = roomCommon.findUserById(nodeId);
                if(user != null){
                    user.setAudioOn(false);
                }
                if(result == 0){
                    if(checkMe(nodeId)){
                        IS_MIC_ON = MIC_OFF;
                    }
                }
                if(checkCallback()){
                    callback.onCloseMicrophone(result, nodeId);
                }
            }

            @Override
            public void onRequestOpenMicrophone(int nodeId) {
                if(checkCallback()){
                    callback.onRequestOpenMicrophone(nodeId);
                }
            }
        };
    }
    private boolean checkCallback(){
        if(callback == null){
            return false;
        }
        return true;
    }
    public interface AudioCallback{
        /**
         * 打开MIC结果
         * @param result
         * @param nodeId
         */
        void onOpenMicrophone(int result, int nodeId);

        /**
         * 关闭MIC结果
         * @param result
         * @param nodeId
         */
        void onCloseMicrophone(int result, int nodeId);
        /**
         * 请求打开音频设备，房间音频路数用完，主持人会接受到此请求，用于音频控制
         * @param nodeId
         */
        void onRequestOpenMicrophone(int nodeId);
    }
}
