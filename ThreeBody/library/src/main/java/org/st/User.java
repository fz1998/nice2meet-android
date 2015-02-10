package org.st;
import java.lang.String;
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
	
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setNodeId(int nodeId) {
        this.nodeId = nodeId;
    }

    public void setRole(org.st.User.Role role) {
        this.role = role;
    }

    public void setState(org.st.User.Status state) {
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

    public org.st.User.Role getRole() {
        return role;
    }

    public org.st.User.Status getState() {
        return state;
    }


	
}