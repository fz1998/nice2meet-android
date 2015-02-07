package com.threebody.sdk.common;

import org.st.RoomSystem;

/**
 * Created by xiaxin on 15-2-5.
 */
public class STSystem {
    private static STSystem instance = new STSystem();
    protected RoomSystem roomSystem;
    private RoomSystem.RoomSystemListener listener;
    ConferenceSystemCallback callback;
    public static boolean isInit = false;
    public static STSystem getInstance(){
        return instance;
    }
    private STSystem(){
        roomSystem = new RoomSystem();
        initListener();
    }


    public void init(ConferenceSystemCallback callback, String url, String token){
        this.callback = callback;
        roomSystem.init(listener, url, token);
    }
    public void unInit(){
        roomSystem.unInit();
    }
    public void setLogLevel(String path, String level){
//        roomSystem.
    }
    public void createRoom(RoomCommon roomCommon, String roomId){
        roomSystem.createRoom(roomCommon.getListener(), roomId);
    }

    public RoomSystem getRoomSystem() {
        return roomSystem;
    }
    public interface ConferenceSystemCallback{
        void onInitResult(int result);
    }
    private void initListener(){
        listener = new RoomSystem.RoomSystemListener() {
            @Override
            public void onInit(int i) {
                if(callback != null){
                    callback.onInitResult(i);
                }
            }
        };
    }

}
