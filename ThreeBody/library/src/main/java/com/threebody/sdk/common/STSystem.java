package com.threebody.sdk.common;

import com.threebody.sdk.util.LoggerUtil;

import org.st.Room;
import org.st.RoomSystem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaxin on 15-2-5.
 */
public class STSystem {
    private static STSystem instance ;
    protected RoomSystem roomSystem;
    static List<RoomCommon> roomCommons;
    private RoomCommon roomCommon;
    private RoomSystem.RoomSystemListener listener;
    ConferenceSystemCallback callback;
    public static boolean isInit = false;
    private final String tag = "System";
    public static STSystem getInstance(){
       if(instance == null){
           instance = new STSystem();
       }
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
    public void init(ConferenceSystemCallback callback, String url, String accessKey, String secretKey){
        this.callback = callback;
        roomSystem.init(listener, url, accessKey, secretKey);
    }
    public void unInit(){
        roomSystem.unInit();
    }
    public void setLogLevel(String path, String level){
//        roomSystem.
    }
    public void createRoom(RoomCommon roomCommon){
        this.roomCommon = roomCommon;
        roomCommons.add(roomCommon);
        Room room = roomSystem.createRoom(roomCommon.getListener(), roomCommon.getRoomId());
        roomCommon.setRoom(room);
    }
    public void initializeAndroidGlobals(Object activity){
        RoomSystem.initializeAndroidGlobals(activity, true, true);
    }
    public void desroyRoom(){
       this.roomCommon.getRoom().dispose();
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
            @Override
            public void onArrangeRoom(int i, java.lang.String s) {
                LoggerUtil.info(tag, i+s);
            }
            @Override
            public void onCancelRoom(int i, java.lang.String s) {
                LoggerUtil.info(tag, i+s);
            }
            @Override
            public void onQueryRoom(int i, org.st.RoomInfo roomInfo) {
                LoggerUtil.info(tag, roomInfo.getRoomID());
            }
        };
    }

    public RoomCommon getRoomCommon() {
        return roomCommon;
    }
}
