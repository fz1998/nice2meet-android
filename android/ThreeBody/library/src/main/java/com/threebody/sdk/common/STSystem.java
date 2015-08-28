package com.threebody.sdk.common;

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

    public static STSystem getInstance(){
       if(instance == null){
           instance = new STSystem();
       }
        return instance;
    }
    private STSystem(){
        roomSystem = new RoomSystem();
        roomCommons = new ArrayList<>();
    }

//    public void createRoom(RoomCommon roomCommon){
//        this.roomCommon = roomCommon;
//        roomCommons.add(roomCommon);
//        Room room = roomSystem.createRoom(roomCommon.getRoomListener(), roomCommon.getRoomId());
//        roomCommon.setRoom(room);
//    }

    public RoomCommon obtainRoom(String roomNumber) {
        this.roomCommon = new RoomCommon(roomNumber);
        roomCommons.add(roomCommon);
        Room room = roomSystem.createRoom(roomCommon.getRoomListener(), roomNumber);
        this.roomCommon.setRoom(room);
        return roomCommon;
    }



    public  List<RoomCommon> getRoomCommons() {
        return roomCommons;
    }

    public RoomCommon getRoomCommon() {
        return roomCommon;
    }

}
