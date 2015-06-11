package com.threebody.sdk.common.impl;

import com.threebody.sdk.common.AudioCommon;
import com.threebody.sdk.common.RoomCommon;

/**
 * Created by xiaxin on 15-2-5.
 */
public class AudioCommonImpl extends AudioCommon {
    public AudioCommonImpl(RoomCommon roomCommon, AudioCallback callbak) {
        super(roomCommon, callbak);
    }
}
