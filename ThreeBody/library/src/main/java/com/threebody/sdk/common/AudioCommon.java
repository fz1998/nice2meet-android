package com.threebody.sdk.common;


import org.st.Audio;

public abstract class AudioCommon implements Audio.AudioListener{
    private Audio audio;
    @Override
    public void onOpenMicrophone(int i, int i2) {
        onOpenMic(i, i2);
    }

    @Override
    public void onCloseMicrophone(int i, int i2) {
        onCloseMic(i, i2);
    }

    @Override
    public void onRequestOpenMicrophone(int i) {
        onRequestMic(i);
    }

    /**
     * 打开MIC结果
     * @param result
     * @param id
     */
    protected abstract void onOpenMic(int result, int id);

    /**
     * 关闭MIC结果
     * @param resutl
     * @param id
     */
    protected abstract void onCloseMic(int resutl, int id);

    /**
     * 请求打开音频设备，房间音频路数用完，主持人会接受到此请求，用于音频控制
     * @param id
     */
    protected abstract void onRequestMic(int id);
    protected boolean openMic(int id){
        if(audio != null){
            return audio.openMicrophone(id);
        }
        return false;
    }
    protected boolean closeMic(int id){
        if(audio != null){
            return audio.closeMicrophone(id);
        }
        return false;
    }
    protected int getMaxAudio(){
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
//    protected
}
