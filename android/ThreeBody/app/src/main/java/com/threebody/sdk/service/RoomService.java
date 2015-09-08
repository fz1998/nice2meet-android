package com.threebody.sdk.service;

import android.util.Log;

import com.threebody.sdk.util.LoggerUtil;

import org.st.Room;
import org.st.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaxin on 15-2-4.
 */
public class RoomService {

    String tag = getClass().getName();
    private Room roomModule;
    private User me;
    private List<User> users;

    private JoinResultCallback joinResultCallback;
    private Room.RoomListener roomListener;

    protected ChatService chatService;
    protected AudioService audioService;
    protected VideoService videoService;
    protected String roomId;

    protected RoomService(String roomId){
        this.roomId = roomId;

        if(users == null){
            users = new ArrayList<>();
        }
        initListener();
    }

    public User getMe() {
        return me;
    }

    public Room getRoomModule(){
        return this.roomModule;
    }
    public void setRoomModule(Room roomModule) {
        this.roomModule = roomModule;
    }

    public void setChatService(ChatService chatService) {
        this.chatService = chatService;
    }

    public AudioService getAudioService() {
        return audioService;
    }

    public void setAudioService(AudioService audioService) {
        this.audioService = audioService;
    }

    public VideoService getVideoService() {
        return videoService;
    }

    public void setVideoService(VideoService videoService) {
        this.videoService = videoService;
    }

    /**
     * 加入会议
     * @param userId   用户id
     * @param userName 用户显示名
     * @param password 密码
     * @return
     */
    public boolean join(String userId, String userName, String password){
        return roomModule.join(userId, userName, password);
    }

    /**
     * 离开会议
     * @return
     */
    public  boolean leave(){
        if(roomModule.leave()){
            dispose();
        }

        return false;
    }

    /**
     * 销毁房间 最后一个调用函数
     */
    public void dispose(){
        long begin= System.currentTimeMillis();
        Log.d("dispose begin time", "ms = " +begin);
        roomModule.dispose();
        long end = System.currentTimeMillis();
        Log.d("dispose end time", "ms = " +end);
        Log.d("dispose end time", "ms = " +(end-begin));
    }


    /**
     * 获取自己
     * @return
     */
    User getCurrentUser(){
       return roomModule.getSelf();
    }

    public User findUserById(int nodeId){
        return roomModule.getUser(nodeId);
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
