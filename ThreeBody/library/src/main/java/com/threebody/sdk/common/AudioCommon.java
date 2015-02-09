package com.threebody.sdk.common;


import org.st.Audio;

public abstract class AudioCommon {
    public static boolean IS_MIC_ON = false;
    private Audio audio;
    Audio.AudioListener listener;
    AudioCallback callback;
    public AudioCommon(RoomCommon roomCommon, AudioCallback callbak){
        audio = roomCommon.getAudio();
        this.callback = callbak;
        roomCommon.setAudioCommon(this);
        initListener();
    }
    public boolean openMic(int id){
        if(audio != null){
            return audio.openMicrophone(id);
        }
        return false;
    }
    public boolean closeMic(int id){
        if(audio != null){
            return audio.closeMicrophone(id);
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
                 if(checkCallback()){
                     callback.onOpenMicrophone(result, nodeId);
                 }
            }

            @Override
            public void onCloseMicrophone(int result, int nodeId) {
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
