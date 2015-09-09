package cn.tee3.n2m.biz.domain;

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
	private int nodeId;
    //设备id
    private String deviceId;
    //数据长度
    private int length;


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

    public int getNodeId() {
        return nodeId;
    }

    public void setNodeId(int nodeId) {
        this.nodeId = nodeId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public int[] getVideoDataByInt() {
		return videoDataByInt;
	}

	public void setVideoDataByInt(int[] videoDataByInt) {
		this.videoDataByInt = videoDataByInt;
	}

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
}
