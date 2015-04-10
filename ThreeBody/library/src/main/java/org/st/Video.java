package org.st;
import org.webrtc.VideoRenderer;
import java.util.LinkedList;
public class Video{
	private final long nativeVideo;
    private long nativeVideoListener;
    private final LinkedList<VideoRenderer> renderers =
      new LinkedList<VideoRenderer>();
	  
    public static interface VideoListener {
        public void onOpenVideo(int result, int nodeId, String deviceId);
        public void onCloseVideo(int result, int nodeId, String deviceId);
        public void onRequestOpenVideo(int nodeId, String deviceId);
		public void onVideoData(int nodeId, String deviceId, byte[] data, int len, int width, int height);
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
    
	public int getLocalCameraCount() {
        return nativeGetLocalCameraCount();
    }
	private native int nativeGetLocalCameraCount();
	
	public int getUserCameraCount(int nodeId) {
        return nativeGetUserCameraCount(nodeId);
    }
	private native int nativeGetUserCameraCount(int nodeId);
	
    public boolean openVideo(int nodeId, String deviceId) {
        return nativeOpenVideo(nodeId, deviceId);
    }
    private native boolean nativeOpenVideo(int nodeId, String deviceId);
    
	public boolean openVideo(int nodeId) {
        return nativeOpenUserVideo(nodeId);
    }
    private native boolean nativeOpenUserVideo(int nodeId);
	
    public boolean closeVideo(int nodeId, String deviceId) {
        return nativeCloseVideo(nodeId, deviceId);
    }
    private native boolean nativeCloseVideo(int nodeId, String deviceId);
	
	public boolean closeVideo(int nodeId) {
        return nativeCloseUserVideo(nodeId);
    }
    private native boolean nativeCloseUserVideo(int nodeId);
    
	public boolean setVideoRender(int nodeId, VideoRenderer renderer) {
		renderers.add(renderer);
        return nativeRegistVideoRender(nodeId, renderer.getNativeVideoRenderer());
    }
    private native boolean nativeRegistVideoRender(int nodeId, long nativeVideoRenderer);
	
	public boolean removeVideoRender(int nodeId, VideoRenderer renderer) {
		if (!renderers.remove(renderer)) {
		return false;
		}
		if (!nativeRegistVideoRender(nodeId, 0)){
			return false;
		}
        return true;
    }
}
