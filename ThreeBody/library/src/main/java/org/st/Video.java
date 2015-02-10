package org.st;
public class Video{
	private final long nativeVideo;
    private long nativeVideoListener;
    
    public static interface VideoListener {
        public void onOpenVideo(int result, int nodeId, String deviceId);
        public void onCloseVideo(int result, int nodeId, String deviceId);
        public void onRequestOpenVideo(int nodeId, String deviceId);
		public void onVideoData(int nodeId, String deviceId, char[] data, int len, int width, int height);
    }
    
    public Video(long nativeVideo) {
        this.nativeVideo = nativeVideo;
    }
    
    public boolean setListener(VideoListener listener) {
        nativeVideoListener = nativeCreateVideoListener(listener);
        return nativeSetListener( nativeVideoListener);
    }
    private static native long nativeCreateVideoListener(VideoListener listener);
    private native boolean nativeSetListener(long listener);
    
    
    public int getMaxVideo() {
        return nativeGetMaxVideo();
    }
    private native int nativeGetMaxVideo();
    
    public int getSurplusVideo() {
        return nativeGetSurplusVideo();
    }
    private native int nativeGetSurplusVideo();
    
    public boolean openVideo(int nodeId, String deviceId) {
        return nativeOpenVideo(nodeId, deviceId);
    }
    private native boolean nativeOpenVideo(int nodeId, String deviceId);
    
    public boolean closeVideo(int nodeId, String deviceId) {
        return nativeCloseVideo(nodeId, deviceId);
    }
    private native boolean nativeCloseVideo(int nodeId, String deviceId);
}
