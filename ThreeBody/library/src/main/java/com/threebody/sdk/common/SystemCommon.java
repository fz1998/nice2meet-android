package com.threebody.sdk.common;

import org.st.RoomSystem;

/**
 * Created by xiaxin on 15-2-4.
 */
public abstract class SystemCommon implements RoomSystem.RoomSystemListener{
    protected RoomSystem roomSystem;
    protected RoomCommon roomCommon;
    public static boolean isInit = false;
    public SystemCommon(RoomCommon room){
        roomSystem = new RoomSystem();
        this.roomCommon = room;
    }

    @Override
    public void onInit(int i) {
        initResult(i);
    }
    protected abstract void initResult(int result);

    protected void init(String url, String token){
         roomSystem.init(this, url, token);
    }
    protected void unInit(){
        roomSystem.unInit();
    }
    protected void setLogLevel(String path, String level){
//        roomSystem.
    }
    protected void createRoom(String roomId){
        roomSystem.createRoom(roomCommon, roomId);
    }

    public RoomSystem getRoomSystem() {
        return roomSystem;
    }

    public void setRoomSystem(RoomSystem roomSystem) {
        this.roomSystem = roomSystem;
    }
}
