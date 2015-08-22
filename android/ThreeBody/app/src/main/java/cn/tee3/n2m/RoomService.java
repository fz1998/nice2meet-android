package cn.tee3.n2m;

import com.threebody.sdk.common.RoomCommon;
import com.threebody.sdk.common.STSystem;
import com.threebody.sdk.common.impl.RoomCommonImpl;

/**
 * Created by uyu on 2015/8/21.
 */
public class RoomService {

    private RoomCommon.JoinResultListener joinResultListener;

    public RoomService(RoomCommon.JoinResultListener joinResultListener) {
        this.joinResultListener = joinResultListener;
    }

    public void joinRoom(String num, String userId, String name, String password) {
        RoomCommonImpl roomCommon = new RoomCommonImpl(joinResultListener, num);
        STSystem.getInstance().createRoom(roomCommon);
        roomCommon.join(userId, name, password);
    }
}
