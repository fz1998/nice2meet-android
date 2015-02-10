package org.st;
public class Room  {
    public static class RoomInfo {
        String roomID;
        String roomName;
        String	ownerID;
        String	hostID;

        public String getRoomID() {
            return roomID;
        }

        public String getRoomName() {
            return roomName;
        }

        public String getOwnerID() {
            return ownerID;
        }

        public String getHostID() {
            return hostID;
        }

        public String getPassword() {
            return password;
        }

        public String getHostPassword() {
            return hostPassword;
        }

        public int getTimezone() {
            return timezone;
        }

        public int getStatTime() {
            return statTime;
        }

        public int getEndTime() {
            return endTime;
        }

        public int getRoomMode() {
            return roomMode;
        }

        public int getMaxAttendee() {
            return maxAttendee;
        }

        public int getBandwidth() {
            return bandwidth;
        }

        public int getMaxAudio() {
            return maxAudio;
        }

        public int getMaxVideo() {
            return maxVideo;
        }

        public boolean isHasPassword() {
            return hasPassword;
        }

        public String getExtension() {
            return extension;
        }

        public void setRoomID(String roomID) {

            this.roomID = roomID;
        }

        public void setRoomName(String roomName) {
            this.roomName = roomName;
        }

        public void setOwnerID(String ownerID) {
            this.ownerID = ownerID;
        }

        public void setHostID(String hostID) {
            this.hostID = hostID;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public void setHostPassword(String hostPassword) {
            this.hostPassword = hostPassword;
        }

        public void setTimezone(int timezone) {
            this.timezone = timezone;
        }

        public void setStatTime(int statTime) {
            this.statTime = statTime;
        }

        public void setRoomMode(int roomMode) {
            this.roomMode = roomMode;
        }

        public void setEndTime(int endTime) {
            this.endTime = endTime;
        }

        public void setMaxAttendee(int maxAttendee) {
            this.maxAttendee = maxAttendee;
        }

        public void setBandwidth(int bandwidth) {
            this.bandwidth = bandwidth;
        }

        public void setMaxAudio(int maxAudio) {
            this.maxAudio = maxAudio;
        }

        public void setMaxVideo(int maxVideo) {
            this.maxVideo = maxVideo;
        }

        public void setHasPassword(boolean hasPassword) {
            this.hasPassword = hasPassword;
        }

        public void setExtension(String extension) {
            this.extension = extension;
        }

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
	
	public static class User {
        public void setUserId(String userId) {
            this.userId = userId;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public void setNodeId(int nodeId) {
            this.nodeId = nodeId;
        }

        public void setRole(Role role) {
            this.role = role;
        }

        public void setState(Status state) {
            this.state = state;
        }

        public String getUserId() {

            return userId;
        }

        public String getUserName() {
            return userName;
        }

        public int getNodeId() {
            return nodeId;
        }

        public Role getRole() {
            return role;
        }

        public Status getState() {
            return state;
        }

        public boolean isVideoChecked() {
            return isVideoChecked;
        }

        public void setVideoChecked(boolean isVideoChecked) {
            this.isVideoChecked = isVideoChecked;
        }

        public enum Role
        {
            host,
            presenter,
            attendee,
        };
       public  enum Status
        {
            offline,
            online,
            leaving,
        };

		String	userId;
        String	userName;
		int		nodeId;
        Role	role;
        Status	state;
        boolean isVideoChecked;
		
        
        public User(String userId, String userName, int nodeId, int role,int state) {
            this.userId = userId;
            this.userName = userName;
            this.nodeId = nodeId;
			switch(role) 
			{ 
			case 0: 
				this.role = Role.host;
				break; 
			case 1: 
				this.role = Role.presenter;
				break; 
			case 2: 
				this.role = Role.attendee;
				break; 
			default: 
				break; 
			}
			switch(state) 
			{ 
			case 0: 
				this.state = Status.offline;
				break; 
			case 1: 
				this.state = Status.online;
				break; 
			case 2: 
				this.state = Status.leaving;
				break; 
			default: 
				break; 
			} 			
        }
    }
    
    public enum RoomState { INIT, OPEN, LOCKED,CLOSED };
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
/*
    public Video getVideo() {
        return new Video(nativeGetNativeVideo());
    }
    private native long nativeGetNativeVideo();
	*/
	private static native void nativeFreeRoom(long nativeRoom);
    private static native void nativeFreeRoomListener(long nativeRoomListener);
}
