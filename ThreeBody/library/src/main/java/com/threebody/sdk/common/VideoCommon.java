package com.threebody.sdk.common;

/**
 * Created by xiaxin on 15-2-4.
 */
public class VideoCommon {
    private static VideoCommon instance = new VideoCommon();
    private VideoCommon(){}
    public static VideoCommon getInstance(){

        return instance;
    }
}
