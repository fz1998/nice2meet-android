package com.threebody.sdk.common;

import org.st.Audio;
import org.st.Chat;
import org.st.Room;

/**
 * Created by xiaxin on 15-2-4.
 */
public abstract class RoomCommon {
    protected Room room;
    RoomCallback callback;
    private Room.RoomListener listener;
    protected Chat chat;
    protected Audio audio;
    public RoomCommon (RoomCallback callback){
        this.callback = callback;
        initListener();
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
        return room.leave();
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
    public Room.RoomInfo getRoomInfo(){
        return room.getRoomInfo();
    }

    /**
     * 获取聊天模块
     * @return
     */
     Chat getChat(){
        return room.getChat();
    }

    /**
     * 获取音频模块
     * @return
     */
     Audio getAudio(){
        return room.getAudio();
    }
    private void initListener(){
        listener = new Room.RoomListener() {
            @Override
            public void onJoin(int result) {
                if(checkCallback()){
                    listener.onJoin(result);
                }
            }

            @Override
            public void onLeave(int reason) {
                if(checkCallback()){
                    callback.onLeave(reason);
                }
            }

            @Override
            public void onConnectionChange(int state) {
                if(checkCallback()){
                    callback.onConnectionChange(state);
                }
            }

            @Override
            public void onUserJoin(Room.User user) {
                if(checkCallback()){
                    callback.onUserJoin(user);
                }
            }

            @Override
            public void onUserLeave(Room.User user) {
                if(checkCallback()){
                    callback.onUserLeave(user);
                }
            }

            @Override
            public void onUserUpdate(Room.User user) {
                if(checkCallback()){
                    callback.onUserUpdate(user);
                }
            }

            @Override
            public void onUpdateRole(int nodeId, int newRole) {
                if(checkCallback()){
                    callback.onUpdateRole(nodeId, newRole);
                }
            }

            @Override
            public void onUpdateStatus(int nodeId, int status) {
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
        void onUserJoin(org.st.Room.User user);
        /**
         * 用户离开会议
         * @param user
         */
        void onUserLeave(org.st.Room.User user);
        /**
         * 用户状态改变
         * @param user
         */
        void onUserUpdate(org.st.Room.User user);
        /**
         * 本地角色改变
         * @param nodeId       用户id
         * @param newRole  user角色
         */
        void onUpdateRole(int nodeId, int newRole);
        /**
         * 用户状态改变
         * @param nodeId      用户id
         * @param status  用户状态
         */
        void onUpdateStatus(int nodeId, int status);
    }

}
