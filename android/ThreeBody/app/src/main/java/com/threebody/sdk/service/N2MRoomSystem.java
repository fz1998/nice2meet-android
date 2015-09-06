package com.threebody.sdk.service;

import org.st.Room;
import org.st.RoomSystem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaxin on 15-2-5.
 */
public class N2MRoomSystem {

    private static N2MRoomSystem instance ;

    private RoomSystem roomSystem;

    private List<RoomService> roomList;

    // TODO: 2015/9/6 allows only one conference room exist.
    private RoomService roomService;

    public static N2MRoomSystem instance(){
       if(instance == null){
           instance = new N2MRoomSystem();
       }
        return instance;
    }
    private N2MRoomSystem(){
        roomSystem = new RoomSystem();
        roomList = new ArrayList<>();
    }

    public RoomService obtainRoom(String roomNumber) {
        this.roomService = new RoomService(roomNumber);
        roomList.add(roomService);
        Room room = roomSystem.createRoom(roomService.getRoomListener(), roomNumber);
        this.roomService.setRoomModule(room);
        return roomService;
    }

    public RoomService getRoomService() {
        return roomService;
    }

    public RoomSystem getRoomSystem() {
        return roomSystem;
    }
}
