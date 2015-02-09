package com.threebody.sdk.common;

import org.st.Room;
import org.st.RoomSystem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaxin on 15-2-5.
 */
public class STSystem {
    private static STSystem instance = new STSystem();
    protected RoomSystem roomSystem;
    static List<RoomCommon> roomCommons;
    private RoomSystem.RoomSystemListener listener;
    ConferenceSystemCallback callback;
    public static boolean isInit = false;
    public static STSystem getInstance(){
        return instance;
    }
    private STSystem(){
        roomSystem = new RoomSystem();
        roomCommons = new ArrayList<>();
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
    public void createRoom(RoomCommon roomCommon){
        Room room = roomSystem.createRoom(roomCommon.getListener(), roomCommon.getRoomId());
        roomCommon.setRoom(room);
        roomCommons.add(roomCommon);
    }
    public void initializeAndroidGlobals(Object activity){
        RoomSystem.initializeAndroidGlobals(activity, true, true);
    }

    public  List<RoomCommon> getRoomCommons() {
        return roomCommons;
    }

    public RoomSystem getRoomSystem() {
        return roomSystem;
    }
    public interface ConferenceSystemCallback{
        void onInitResult(int result);
    }
    public static RoomCommon findCommonById(String roomId){
          if(roomCommons != null && !roomCommons.isEmpty()){
              for (RoomCommon common : roomCommons){
                  if(roomId.equals(common.getRoomId())){
                      return common;
                  }
              }
          }
        return null;
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
