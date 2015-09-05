package com.threebody.sdk.service;


import com.threebody.sdk.util.LoggerUtil;

import org.st.Audio;
import org.st.User;

public class AudioService {
    String tag = getClass().getName();
    public static final int MIC_OFF = 0;
    public static final int MIC_ON = 1;
    public static final int MIC_HANDS_UP = 2;
    public static final int SPEAKER_OFF = 0;
    public static final int SPEAKER_ON = 1;

    //todo need to change this
    public static int IS_MIC_ON = MIC_OFF;
    public static int IS_SPEAKER_ON = MIC_OFF;

    private Audio audioModule;

    AudioCallback callback;
    RoomService roomService;

    public AudioService(RoomService roomService, AudioCallback callbak){
        audioModule = roomService.getRoom().getAudio();
        this.callback = callbak;
        roomService.setAudioService(this);
        this.roomService = roomService;
        initListener();
    }

    private boolean checkMe(int nodeId){
        User me = roomService.getCurrentUser();
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
        if(audioModule != null){
            return audioModule.openMicrophone(nodeId);
        }
        return false;
    }
    public boolean closeMic(int id){
        if(audioModule != null){
            boolean isSucceed = audioModule.closeMicrophone(id);
            if(isSucceed){
                IS_MIC_ON = MIC_OFF;
            }
            return isSucceed;
        }
        return false;
    }
    public boolean muteMic(int id, boolean mute){
        if(audioModule != null){
            boolean isSucceed = audioModule.muteMicrophone(id, mute);
            if(isSucceed){
               // IS_MIC_ON = MIC_OFF;
            }
            return isSucceed;
        }
        return false;
    }

    public int getMaxAudio(){
        if(audioModule != null){
            return audioModule.getMaxAudio();
        }
        return -1;
    }

    /**
     * 获取剩余路数
     * @return
     */
    protected int getSurplusAudio(){
        if(audioModule != null){
            return audioModule.getSurplusAudio();
        }
        return -1;
    }
    private void initListener(){
        if (audioModule == null)
            return;
        Audio.AudioListener listener = new Audio.AudioListener() {
            @Override
            synchronized public void onOpenMicrophone(int result, int nodeId) {
                LoggerUtil.info(tag, "onOpenMic result = "+result+" nodeId = "+nodeId);
                User user = roomService.findUserById(nodeId);
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
            synchronized public void onCloseMicrophone(int result, int nodeId) {
                LoggerUtil.info(tag, "onCloseMic result = "+result+" nodeId = "+nodeId);
                User user = roomService.findUserById(nodeId);
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
            synchronized public void onRequestOpenMicrophone(int nodeId) {
                if(checkCallback()){
                    callback.onRequestOpenMicrophone(nodeId);
                }
            }
        };
        audioModule.setListener(listener);
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
