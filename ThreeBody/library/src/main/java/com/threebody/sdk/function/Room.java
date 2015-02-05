package com.threebody.sdk.function;
public class Room  {
    public class RoomInfo {
        String roomID;
        String roomName;
        String	ownerID;
        String	hostID;
        String  password;
        String  hostPassword;
        int		timezone;
        int		statTime;
        int		endTime;
        int     roomMode;
        int		maxAttendee;
        int		bandwidth;
        int		maxAudio;
        int		maxVideo;
        boolean	hasPassword;
		
        String extension;
        
        public RoomInfo(String roomID, String roomName, String ownerID, String hostID,
                        String password, String hostPassword, String extension,
                        int timezone, int statTime, int endTime, int roomMode, int maxAttendee,
                        int bandwidth, int maxAudio, int maxVideo, boolean hasPassword) {
            this.roomID = roomID;
            this.roomName = roomName;
            this.ownerID = ownerID;
            this.hostID = hostID;
            this.password = password;
            this.hostPassword = hostPassword;
			this.extension = extension;
            this.timezone = timezone;
            this.statTime = statTime;
            this.endTime = endTime;
            this.roomMode = roomMode;
            this.maxAttendee = maxAttendee;
            this.bandwidth = bandwidth;
            this.maxAudio = maxAudio;
            this.maxVideo = maxVideo;
            this.hasPassword = hasPassword;

        }
    }
	
	public class User {
		/*enum Role
		{
			host,
			presenter,
			attendee,
		};
		enum Status
		{
			offline,
			online,
			leaving,
		};*/
		String	userId;
        String	userName;
		int		nodeId;
        int		role;
		int		state;

        
        public User(String userId, String userName, int nodeId, int role,int state) {
            this.userId = userId;
            this.userName = userName;
            this.nodeId = nodeId;
            this.role = role;
            this.state = state;
        }
    }
    
    public enum RoomState { INIT, OPEN, LOCKED,CLOSED };
    /** Java version of RoomObserver. */
    public static interface RoomObserver {
        public void onJoin(int result);
        public void onLeave(int result);
        public void onConnectionChange(int state);
		//user notify
		public void onUserJoin(User user);
		public void onUserLeave(User user);
		public void onUserUpdate(User user);
		public void onUpdateRole(int nodeId, int role);
		public void onUpdateStatus(int nodeId, int status);
		
    }
    
    
    private final long nativeRoom;
    private final long nativeObserver;
    
    
    public Room(long nativeRoom, long nativeObserver) {
        this.nativeRoom = nativeRoom;
        this.nativeObserver = nativeObserver;
        
    }
    
    public boolean join(String userId, String userName, String password) {
		return nativeJoin(userId, userName, password);
    }
    private native boolean nativeJoin(String userId, String userName, String password);
    
    public boolean leave() {
		return nativeLeave();
    }
	
	public void dispose() {
		nativeFreeRoom(nativeRoom);
		nativeFreeRoomObserver(nativeObserver);
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
/*
    public Video getVideo() {
        return new Video(nativeGetNativeVideo());
    }
    private native long nativeGetNativeVideo();
	*/
	private static native void nativeFreeRoom(long nativeRoom);
    private static native void nativeFreeRoomObserver(long nativeRoomObserver);
}
