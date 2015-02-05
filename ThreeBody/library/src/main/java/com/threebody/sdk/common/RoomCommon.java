package com.threebody.sdk.common;

import org.st.Audio;
import org.st.Chat;
import org.st.Room;

/**
 * Created by xiaxin on 15-2-4.
 */
public abstract class RoomCommon implements Room.RoomListener{
    protected Room room;
    protected Chat chat;
    protected Audio audio;
    protected RoomCommon(){
    }
    @Override
    public void onJoin(int i) {

    }

    @Override
    public void onLeave(int i) {

    }

    @Override
    public void onConnectionChange(int i) {

    }

    @Override
    public void onUserJoin(Room.User user) {

    }

    @Override
    public void onUserLeave(Room.User user) {

    }

    @Override
    public void onUserUpdate(Room.User user) {

    }

    @Override
    public void onUpdateRole(int i, int i2) {

    }

    @Override
    public void onUpdateStatus(int i, int i2) {

    }

    /**
     * 加入会议结果
     * @param result
     */
    protected abstract void joinResult(int result);

    /**
     * 退出会议结果
     * @param reason
     */
    protected abstract void leaveResult(int reason);

    /**
     * 连接状态改变
     * @param state
     */
    protected abstract void connectionChanged(int state);

    /**
     * 新用户加入
     * @param user
     */
    protected abstract void userJoin(Room.User user);

    /**
     * 用户离开会议
     * @param user
     */
    protected abstract void userLeave(Room.User user);

    /**
     * 用户状态改变
     * @param user
     */
    protected abstract void userUpdate(Room.User user);

    /**
     * 本地角色改变
     * @param id       用户id
     * @param newRole  user角色
     */
    protected abstract void updateRole(int id, int newRole);

    /**
     * 用户状态改变
     * @param id      用户id
     * @param status  用户状态
     */
    protected abstract void updateStatus(int id, int status);

    /**
     * 加入会议
     * @param userId   用户id
     * @param userName 用户显示名
     * @param password 密码
     * @return
     */
    protected boolean join(String userId, String userName, String password){
        return room.join(userId, userName, password);
    }

    /**
     * 离开会议
     * @return
     */
    protected boolean leave(){
        return room.leave();
    }

    /**
     * 销毁房间 最后一个调用函数
     */
    protected void dispose(){
        room.dispose();
    }

    /**
     * 获取房间信息
     * @return
     */
    protected Room.RoomInfo getRoomInfo(){
        return room.getRoomInfo();
    }

    /**
     * 获取聊天模块
     * @return
     */
    protected Chat getChat(){
        return room.getChat();
    }

    /**
     * 获取音频模块
     * @return
     */
    protected Audio getAudio(){
        return room.getAudio();
    }

}
