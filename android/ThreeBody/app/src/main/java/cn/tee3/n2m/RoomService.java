package cn.tee3.n2m;

import org.st.Room;

/**
 * Created by uyu on 2015/8/21.
 */
public class RoomService {

    private Room.RoomListener roomListener;

    public RoomService(Room.RoomListener roomListener) {
        this.roomListener = roomListener;
    }

    public void joinRoom(String num, String userId, String name, String password) {

        Room room = SingletonRoomSystem.getInstance().createRoom(this.roomListener, num);
        room.join(userId, name, password);

    }
}
