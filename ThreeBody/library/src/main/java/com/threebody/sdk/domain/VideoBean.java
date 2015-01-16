package com.threebody.sdk.domain;

import java.io.Serializable;

/**
 * 视频
 * 
 * @author Sean.xie
 * 
 */
public class VideoBean implements Serializable {
	private static final long serialVersionUID = 1L;

	/** 视频数据 */
	private byte[] videoData;
	/** 视频数据 */
	private int[] videoDataByInt;
	/** 是否放大 */
	private boolean largeFace;
	/** 视频高度 */
	private int height;
	/** 视频宽度 */
	private int width;
	/**
	 * 视频用户的id
	 */
	private int userId;

	public byte[] getVideoData() {
		return videoData;
	}

	public boolean isLargeFace() {
		return largeFace;
	}

	public void setVideoData(byte[] videoData) {
		this.videoData = videoData;
	}

	public void setLargeFace(boolean largeFace) {
		this.largeFace = largeFace;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int[] getVideoDataByInt() {
		return videoDataByInt;
	}

	public void setVideoDataByInt(int[] videoDataByInt) {
		this.videoDataByInt = videoDataByInt;
	}

}
