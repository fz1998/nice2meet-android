package com.threebody.sdk.function;

public class Audio {
    private final long nativeAudio;
    private long nativeAudioObserver;
    
    public static interface AudioObserver {
        public void onOpenMicrophone(int result, int nodeId);
        public void onCloseMicrophone(int result, int nodeId);
        public void onRequestOpenMicrophone(int openId);
    }
    
    public Audio(long nativeAudio) {
        this.nativeAudio = nativeAudio;
    }
    
    public boolean setObserver(AudioObserver observer) {
        nativeAudioObserver = nativeCreateAudioObserver(observer);
        return nativeSetObserver( nativeAudioObserver);
    }
    private static native long nativeCreateAudioObserver(AudioObserver observer);
    private native boolean nativeSetObserver(long observer);
    
    
    public int getMaxAudio() {
        return nativeGetMaxAudio();
    }
    private native int nativeGetMaxAudio();
    
    public int getSurplusAudio() {
        return nativeGetSurplusAudio();
    }
    private native int nativeGetSurplusAudio();
    
    public boolean openMicrophone(int nodeId) {
        return nativeOpenMicrophone(nodeId);
    }
    private native boolean nativeOpenMicrophone(int nodeId);
    
    public boolean closeMicrophone(int nodeId) {
        return nativeCloseMicrophone(nodeId);
    }
    private native boolean nativeCloseMicrophone(int nodeId);
}