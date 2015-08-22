package com.threebody.sdk.common.impl;

import com.threebody.sdk.common.RoomCommon;

/**
 * Created by xiaxin on 15-2-5.
 */
public class RoomCommonImpl extends RoomCommon {
//    public RoomCommonImpl(RoomCallback callback, String roomId) {
//        super(callback, roomId);
//    }
    public RoomCommonImpl(JoinResultListener joinResultListener, String roomId){
        super(joinResultListener, roomId);

    }
}
