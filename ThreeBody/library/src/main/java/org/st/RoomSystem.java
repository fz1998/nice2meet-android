package org.st;

public class RoomSystem {
    static {
        System.loadLibrary("mcu1.3_sdk_so");
    }
    
    private final long nativeSystem;
    private long nativeListener;
	
	public static interface RoomSystemListener {
        public void onInit(int result);
    }
	
    public RoomSystem() {
        nativeSystem = nativeCreateRoomSystem();
        if (nativeSystem == 0) {
            throw new RuntimeException("Failed to initialize RoomSystem!");
        }
    }
    private static native long nativeCreateRoomSystem();
    
	public static native boolean initializeAndroidGlobals(Object context, boolean initializeAudio, boolean initializeVideo);
	
	public void init(RoomSystem.RoomSystemListener listener, String serverurl, String accessToken)
	{
        nativeListener = nativeCreateRoomSystemListener(listener);
		nativeInit(nativeListener, serverurl, accessToken);
	}
    private static native long nativeCreateRoomSystemListener(RoomSystem.RoomSystemListener listener);
    private native void nativeInit(long nativeListener, String serverurl, String accessToken);
    
    public void unInit() {
        nativeUninit();
        nativeFreeSystem(nativeSystem);
        nativeFreeListener(nativeListener);
    }
    private native void nativeUninit();
    private static native void nativeFreeSystem(long nativeSystem);
    private static native void nativeFreeListener(long nativeListener);
    
    public Room createRoom(Room.RoomListener roomListener, String roomId) 
    {
        long nativeRoomListener = nativeCreateRoomListener(roomListener);
        long nativeRoom = nativeCreateRoom(nativeRoomListener, roomId);
        return new Room(nativeRoom, nativeRoomListener);
    }
	
    private static native long nativeCreateRoom(long nativeRoomListener, String roomID);
    private static native long nativeCreateRoomListener(Room.RoomListener listener);
	
}
