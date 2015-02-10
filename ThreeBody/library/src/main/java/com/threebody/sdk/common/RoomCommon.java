package com.threebody.sdk.common;

import com.threebody.sdk.util.LoggerUtil;

import org.st.Audio;
import org.st.Chat;
import org.st.Room;
import org.st.RoomInfo;
import org.st.User;
import org.st.Video;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaxin on 15-2-4.
 */
public abstract class RoomCommon {
    String tag = getClass().getName();
    protected Room room;
    User me;
    List<User> users;
    RoomCallback callback;
    JoinResultListener joinListener;
    private Room.RoomListener listener;
    protected ChatCommon chatCommon;
    protected AudioCommon audioCommon;
    protected VideoCommon videoCommon;
    protected String roomId;


    protected RoomCommon (RoomCallback callback, String roomId){
        this.callback = callback;
        this.roomId = roomId;
        init();
    }
    protected RoomCommon(JoinResultListener joinListener, String roomId){
        this.joinListener = joinListener;
        this.roomId = roomId;
        init();
    }
    private void init(){
        if(users == null){
            users = new ArrayList<>();
        }
        initListener();
    }

    public User getMe() {
        return me;
    }

    public void setMe(User me) {
        this.me = me;
        users.add(me);
    }

    public void setRoom(Room room) {
        this.room = room;
    }
    public void setCallback(RoomCallback callback) {
        this.callback = callback;
        joinListener = null;
    }

    public ChatCommon getChatCommon() {
        return chatCommon;
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
        room.dispose();
    }

    /**
     * 获取房间信息
     * @return
     */
    public RoomInfo getRoomInfo(){
        return room.getRoomInfo();
    }

    /**
     * 获取聊天模块
     * @return
     */
     Chat getChat(){
        return room.getChat();
    }
    Video getVideo(){
        return room.getVideo();
    }
    /**
     * 获取音频模块
     * @return
     */
     Audio getAudio(){
        return room.getAudio();
    }

    /**
     * 获取自己
     * @return
     */
    User getSelf(){
       return room.getSelf();
    }

    public User findUserById(int nodeId){
        return room.getUser(nodeId);
    }
    private void initListener(){
        listener = new Room.RoomListener() {
            @Override
            public void onJoin(int result) {
                LoggerUtil.info(tag, "onJoin result = "+result);
                me = getSelf();
                if(me != null){
                    users.add(me);
                }
                if(checkCallback()){
                    callback.onJoin(result);
                }
                if(joinListener != null){
                    joinListener.onJoinResult(result);
                }
            }

            @Override
            public void onLeave(int reason) {
//                LoggerUtil.info(tag, "onLeave result = "+ reason);
                if(checkCallback()){
                    callback.onLeave(reason);
                }
            }

            @Override
            public void onConnectionChange(int state) {
                LoggerUtil.info(tag, "onConnectionChange state = "+state);
                if(checkCallback()){
                    callback.onConnectionChange(state);
                }
            }

            @Override
            public void onUserJoin(User user) {
                LoggerUtil.info(tag, "onUserJoin nodeId = "+user.getNodeId()+" name = "+user.getUserName());
                users.add(user);
                if(checkCallback()){
                    callback.onUserJoin(user);
                }
            }

            @Override
            public void onUserLeave(User user) {
                LoggerUtil.info(tag, "onUserLeave nodeId = "+user.getNodeId()+" name = "+user.getUserName());
                if(users != null && !users.isEmpty()){
                    for (User u : users){
                        if(u.getNodeId() == user.getNodeId()){
                            users.remove(u);
                        }
                    }
                }
                if(checkCallback()){
                    callback.onUserLeave(user);
                }
            }

            @Override
            public void onUserUpdate(User user) {
                LoggerUtil.info(tag, "onUserUpdate nodeId = "+user.getNodeId()+" name = "+user.getUserName());
                if(users != null && !users.isEmpty()){
                    for(User u : users){
                        if(u.getNodeId() == user.getNodeId()){
                            users.remove(u);
                            users.add(user);
                        }
                    }
                }
                if(checkCallback()){
                    callback.onUserUpdate(user);
                }
            }

            @Override
            public void onUpdateRole(int nodeId, User.Role newRole) {
                LoggerUtil.info(tag, "onUpdateRole nodeId = "+nodeId+" newRole = "+newRole.toString());
                if(users != null && !users.isEmpty()){
                    for(User user : users){
                        if(nodeId == user.getNodeId()){
                            user.setRole(newRole);
                        }
                    }
                }
                if(checkCallback()){
                    callback.onUpdateRole(nodeId, newRole);
                }
            }

            @Override
            public void onUpdateStatus(int nodeId, User.Status status) {
                LoggerUtil.info(tag, "onUpdatestatus nodeId = "+nodeId+" status = "+status.toString());
                if(users != null && !users.isEmpty()){
                    for(User user : users){
                        if(nodeId == user.getNodeId()){
                          user.setState(status);
                        }
                    }
                }
                if(checkCallback()){
                    callback.onUpdateStatus(nodeId, status);
                }
            }
        };
    }
    Room.RoomListener getListener(){
        return listener;
    }
    private boolean checkCallback(){
        if(callback == null){
            return false;
        }
        return true;
    }
    public interface JoinResultListener{
        void onJoinResult(int result);
    }
    public interface RoomCallback{
        /**
         * 加入会议结果
         * @param result
         */
        void onJoin(int result);
        /**
         * 退出会议结果
         * @param reason
         */
        void onLeave(int reason);
        /**
         * 连接状态改变
         * @param state
         */
        void onConnectionChange(int state);
        /**
         * 新用户加入
         * @param user
         */
        void onUserJoin(User user);
        /**
         * 用户离开会议
         * @param user
         */
        void onUserLeave(User user);
        /**
         * 用户状态改变
         * @param user
         */
        void onUserUpdate(User user);
        /**
         * 本地角色改变
         * @param nodeId       用户id
         * @param newRole  user角色
         */
        void onUpdateRole(int nodeId, User.Role newRole);
        /**
         * 用户状态改变
         * @param nodeId      用户id
         * @param status  用户状态
         */
        void onUpdateStatus(int nodeId, User.Status status);
    }

}
