package org.st;

public class User {
	
	public static enum Role
    {
        host,
        presenter,
        attendee,
    };
    public static enum Status
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
    boolean isVideoOn;
    boolean isAudioOn;

    public User(String userId, String userName, int nodeId, Role role,Status state) {
        this.userId = userId;
        this.userName = userName;
        this.nodeId = nodeId;
		this.role = role;
		this.state = state;
    }
	public User() {
        this.userId = "";
        this.userName = "";
        this.nodeId = 0;
    }

    public boolean isVideoOn() {
        return isVideoOn;
    }

    public void setVideoOn(boolean isVideoOn) {
        this.isVideoOn = isVideoOn;
    }

    public boolean isAudioOn() {
        return isAudioOn;
    }

    public void setAudioOn(boolean isAudioOn) {
        this.isAudioOn = isAudioOn;
    }


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


	
}