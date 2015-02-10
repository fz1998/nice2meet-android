package org.st;

public class Audio {
    private final long nativeAudio;
    private long nativeAudioListener;
    
    public static interface AudioListener {
        public void onOpenMicrophone(int result, int nodeId);
        public void onCloseMicrophone(int result, int nodeId);
        public void onRequestOpenMicrophone(int openId);
    }
    
    public Audio(long nativeAudio) {
        this.nativeAudio = nativeAudio;
    }
    
    public boolean setListener(AudioListener listener) {
        nativeAudioListener = nativeCreateAudioListener(listener);
        return nativeSetListener( nativeAudioListener);
    }
    private static native long nativeCreateAudioListener(AudioListener listener);
    private native boolean nativeSetListener(long listener);
    
    
    public int getMaxAudio() {
        return nativeGetMaxAudio();
    }
    private native int nativeGetMaxAudio();
    
    public int getSurplusAudio() {
        return nativeGetSurplusAudio();
    }
    private native int nativeGetSurplusAudio();
    
	public boolean getLocalMicphoneCount() {
        return nativeGetLocalMicphoneCount();
    }
    private native boolean nativeGetLocalMicphoneCount();
	
	public boolean getUserMicphoneCount(int nodeId) {
        return nativeGetUserMicphoneCount(nodeId);
    }
    private native boolean nativeGetUserMicphoneCount(int nodeId);
	
    public boolean openMicrophone(int nodeId) {
        return nativeOpenMicrophone(nodeId);
    }
    private native boolean nativeOpenMicrophone(int nodeId);
    
    public boolean closeMicrophone(int nodeId) {
        return nativeCloseMicrophone(nodeId);
    }
    private native boolean nativeCloseMicrophone(int nodeId);
}