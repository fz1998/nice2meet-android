package com.threebody.sdk.service;

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
    static List<RoomService> roomServices;
    private RoomService roomService;

    public static STSystem getInstance(){
       if(instance == null){
           instance = new STSystem();
       }
        return instance;
    }
    private STSystem(){
        roomSystem = new RoomSystem();
        roomServices = new ArrayList<>();
    }

//    public void createRoom(RoomCommon roomCommon){
//        this.roomCommon = roomCommon;
//        roomCommons.add(roomCommon);
//        Room room = roomSystem.createRoom(roomCommon.getRoomListener(), roomCommon.getRoomId());
//        roomCommon.setRoom(room);
//    }

    public RoomService obtainRoom(String roomNumber) {
        this.roomService = new RoomService(roomNumber);
        roomServices.add(roomService);
        Room room = roomSystem.createRoom(roomService.getRoomListener(), roomNumber);
        this.roomService.setRoom(room);
        return roomService;
    }



    public  List<RoomService> getRoomCommons() {
        return roomServices;
    }

    public RoomService getRoomService() {
        return roomService;
    }

}
