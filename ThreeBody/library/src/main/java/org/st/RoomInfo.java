package org.st;
import java.lang.String;

public class RoomInfo {
	String	roomID;
    String	roomName;
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

    
}