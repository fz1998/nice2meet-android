package com.threebody.sdk.function;
public class RoomSystem {
    static {
        System.loadLibrary("mcu1.3_sdk_so");
    }
    
    private final long nativeSystem;
    private long nativeObserver;
	
	public static interface Observer {
        public void onInit(int result);
    }
	
    public RoomSystem() {
        nativeSystem = nativeCreateRoomSystem();
        if (nativeSystem == 0) {
            throw new RuntimeException("Failed to initialize RoomSystem!");
        }
    }
    private static native long nativeCreateRoomSystem();
    
	public void init(Observer observer, String serverurl, String accessToken)
	{
        nativeObserver = nativeCreateRoomSystemObserver(observer);
		nativeInit(nativeObserver, serverurl, accessToken);
	}
    private static native long nativeCreateRoomSystemObserver(Observer observer);
    private native void nativeInit(long nativeObserver, String serverurl, String accessToken);
    
    public void unInit() {
        nativeUninit();
        nativeFreeSystem(nativeSystem);
        nativeFreeObserver(nativeObserver);
    }
    private static native void nativeUninit();
    private static native void nativeFreeSystem(long nativeSystem);
    private static native void nativeFreeObserver(long nativeObserver);
    
    public Room createRoom(Room.RoomObserver roomObserver, String roomId) 
    {
        long nativeRoomObserver = nativeCreateRoomObserver(roomObserver);
        long nativeRoom = nativeCreateRoom(nativeRoomObserver, roomId);
        return new Room(nativeRoom, nativeRoomObserver);
    }
	
    private static native long nativeCreateRoom(long nativeRoomObserver, String roomID);
    private static native long nativeCreateRoomObserver(Room.RoomObserver observer);
	
}
