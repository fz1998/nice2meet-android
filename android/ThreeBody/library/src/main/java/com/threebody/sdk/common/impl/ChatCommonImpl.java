package com.threebody.sdk.common.impl;

import com.threebody.sdk.common.ChatCommon;
import com.threebody.sdk.common.RoomCommon;

/**
 * Created by xiaxin on 15-2-5.
 */
public class ChatCommonImpl extends ChatCommon{
    public ChatCommonImpl(RoomCommon roomCommon, ChatCallback callback) {
        super(roomCommon, callback);
    }
}
