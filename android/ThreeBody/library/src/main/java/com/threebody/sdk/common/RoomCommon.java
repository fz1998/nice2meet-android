package com.threebody.sdk.common;

import android.util.Log;

import com.threebody.sdk.util.LoggerUtil;

import org.st.Room;
import org.st.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaxin on 15-2-4.
 */
public class RoomCommon {

    String tag = getClass().getName();
    protected Room room;
    User me;
    List<User> users;

    JoinResultCallback joinResultCallback;
    private Room.RoomListener roomListener;

    protected ChatCommon chatCommon;
    protected AudioCommon audioCommon;
    protected VideoCommon videoCommon;
    protected String roomId;

    protected RoomCommon(String roomId){
        this.roomId = roomId;

        if(users == null){
            users = new ArrayList<>();
        }
        initListener();
    }

    public User getMe() {
        return me;
    }

    public Room getRoom(){
        return this.room;
    }
    public void setRoom(Room room) {
        this.room = room;
    }

    public void setChatCommon(ChatCommon chatCommon) {
        this.chatCommon = chatCommon;
    }

    public AudioCommon getAudioCommon() {
        return audioCommon;
    }

    public void setAudioCommon(AudioCommon audioCommon) {
        this.audioCommon = audioCommon;
    }

    public VideoCommon getVideoCommon() {
        return videoCommon;
    }

    public void setVideoCommon(VideoCommon videoCommon) {
        this.videoCommon = videoCommon;
    }

    public String getRoomId() {
        return roomId;
    }

    public List<User> getUsers() {
        return users;
    }

    /**
     * 加入会议
     * @param userId   用户id
     * @param userName 用户显示名
     * @param password 密码
     * @return
     */
    public boolean join(String userId, String userName, String password){
        return room.join(userId, userName, password);
    }

    /**
     * 离开会议
     * @return
     */
    public  boolean leave(){
        if(room.leave()){
            dispose();
            STSystem.getInstance().getRoomCommons().remove(this);
        }

        return false;
    }

    /**
     * 销毁房间 最后一个调用函数
     */
    public void dispose(){
        long begin= System.currentTimeMillis();
        Log.d("dispose begin time", "ms = " +begin);
        room.dispose();
        long end = System.currentTimeMillis();
        Log.d("dispose end time", "ms = " +end);
        Log.d("dispose end time", "ms = " +(end-begin));


    }


    /**
     * 获取自己
     * @return
     */
    User getCurrentUser(){
       return room.getSelf();
    }

    public User findUserById(int nodeId){
        return room.getUser(nodeId);
    }

    private void initListener(){
        roomListener = new Room.RoomListener() {
            @Override
            public void onJoin(int result) {
                LoggerUtil.info(tag, "onJoin result = "+result);
                me = getCurrentUser();
                if(me != null){
                    users.add(me);
                }

                if(joinResultCallback != null){
                    joinResultCallback.onJoinResult(result);
                }
            }

            @Override
            synchronized public void onLeave(int reason) {
            }

            @Override
            synchronized public void onConnectionChange(Room.ConnectionStatus state) {
                LoggerUtil.info(tag, "onConnectionChange state = "+state);
            }

            @Override
            synchronized public void onUserJoin(User user) {
                Log.e(tag, "onUserJoin nodeId = "+user.getNodeId()+" name = "+user.getUserName() + " role = "+user.getRole() + " usrID = "+user.getUserId());
                users.add(user);
            }

            @Override
            synchronized public void onUserLeave(User user) {
                Log.i(tag, "onUserLeave nodeId = "+user.getNodeId()+" name = "+user.getUserName());
                if(users != null && !users.isEmpty()){
                    for(User u : users){
                        if(u.getNodeId() == user.getNodeId()){
                            users.remove(u);
                            break;
                        }
                    }
//                    users.remove(user);
                }
            }

            @Override
            synchronized public void onUserUpdate(User user) {
                LoggerUtil.info(tag, "onUserUpdate nodeId = "+user.getNodeId()+" name = "+user.getUserName());
                if(users != null && !users.isEmpty()){
                    for(User u : users){
                        if(u.getNodeId() == user.getNodeId()){
                            users.remove(u);
                            users.add(user);
                        }
                    }
                }
            }

            @Override
            synchronized public void onUpdateRole(int nodeId, User.Role newRole) {
                LoggerUtil.info(tag, "onUpdateRole nodeId = "+nodeId+" newRole = "+newRole.toString());
                if(users != null && !users.isEmpty()){
                    for(User user : users){
                        if(nodeId == user.getNodeId()){
                            user.setRole(newRole);
                        }
                    }
                }
            }

            @Override
            synchronized public void onUpdateStatus(int nodeId, User.Status status) {
                LoggerUtil.info(tag, "onUpdatestatus nodeId = "+nodeId+" status = "+status.toString());
                if(users != null && !users.isEmpty()){
                    for(User user : users){
                        if(nodeId == user.getNodeId()){
                          user.setState(status);
                        }
                    }
                }
            }

            @Override
            synchronized public void onUpdateUserData(int nodeId, String data) {
                LoggerUtil.info(tag, "onUpdateUserData nodeId = " + nodeId + " data = " + data);
            }
        };
    }

    Room.RoomListener getRoomListener(){
        return roomListener;
    }

    public void setJoinResultCallback(JoinResultCallback joinResultCallback) {
        this.joinResultCallback = joinResultCallback;
    }

    /**
     * 给JoinActiviry的回调接口
     */
    public interface JoinResultCallback {
        void onJoinResult(int result);
    }


}
