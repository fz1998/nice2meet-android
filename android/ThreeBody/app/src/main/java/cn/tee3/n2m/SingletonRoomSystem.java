package cn.tee3.n2m;

import org.st.RoomSystem;

/**
 * Created by uyu on 2015/8/22.
 */
public class SingletonRoomSystem {

    private static RoomSystem singleInstance;

    private SingletonRoomSystem() {}

    public static RoomSystem getInstance() {
        if(singleInstance == null) {
            singleInstance = new RoomSystem();
        }
        return singleInstance;
    }
}
