package org.st;

public class Room  {
   // public enum RoomState { INIT, OPEN, LOCKED,CLOSED };
    /** Java version of RoomListener. */
    public static interface RoomListener {
        public void onJoin(int result);
        public void onLeave(int result);
        public void onConnectionChange(int state);
		//user notify
		public void onUserJoin(User user);
		public void onUserLeave(User user);
		public void onUserUpdate(User user);
		public void onUpdateRole(int nodeId, User.Role role);
		public void onUpdateStatus(int nodeId, User.Status status);
		
    }
    
    
    private final long nativeRoom;
    private final long nativeListener;
    
    
    public Room(long nativeRoom, long nativeListener) {
        this.nativeRoom = nativeRoom;
        this.nativeListener = nativeListener;
        
    }
	
    public User getSelf()
	{
		return nativeGetSelf();
	}
	private native User nativeGetSelf();
	
	public User getUser(int nodeId)
	{
		return nativeGetUser(nodeId);
	}
	private native User nativeGetUser(int nodeId);
	
    public boolean join(String userId, String userName, String password) {
		return nativeJoin(userId, userName, password);
    }
    private native boolean nativeJoin(String userId, String userName, String password);
    
    public boolean leave() {
		return nativeLeave();
    }
	
	public void dispose() {
		nativeFreeRoom(nativeRoom);
		nativeFreeRoomListener(nativeListener);
  }
  
    private native boolean nativeLeave();
    
    public String getRoomID() {
        return nativeGetRoomID();
    }
    private native String nativeGetRoomID();
    
    public String getRoomName() {
        return nativeGetRoomName();
    }
    private native String nativeGetRoomName();
    
    public RoomInfo getRoomInfo() {
        return nativeGetRoomInfo();
    }
    private native RoomInfo nativeGetRoomInfo();
    
    public Chat getChat() {
        return new Chat(nativeGetNativeChat());
    }
    private native long nativeGetNativeChat();
    
    public Audio getAudio() {
        return new Audio(nativeGetNativeAudio());
    }
    private native long nativeGetNativeAudio();

    public Video getVideo() {
        return new Video(nativeGetNativeVideo());
    }
    private native long nativeGetNativeVideo();
	
	private static native void nativeFreeRoom(long nativeRoom);
    private static native void nativeFreeRoomListener(long nativeRoomListener);
}
