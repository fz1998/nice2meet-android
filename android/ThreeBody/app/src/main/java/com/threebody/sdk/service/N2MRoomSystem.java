package com.threebody.sdk.service;

import org.st.Room;
import org.st.RoomSystem;

/**
 * Created by xiaxin on 15-2-5.
 */
public class N2MRoomSystem {

    private static N2MRoomSystem instance ;

    private RoomSystem roomSystem;

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
    }

    public RoomService obtainRoom(String roomNumber) {
        this.roomService = new RoomService(roomNumber);
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
