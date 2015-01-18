package com.threebody.sdk.function;


public interface AudioCommon {
	/**
	 * 打开MIC
	 */
	public static final int MIC_ON = 1;

	/**
	 * 关闭MIC
	 */
	public static final int MIC_OFF = 0;

	/**
	 * 禁用MIC
	 */
	public static final int MIC_DISABLE = 50;

	/**
	 * 启用MIC
	 */
	public static final int MIC_ENABLE = 51;
	/**
	 * 音频编码格式
	 * 
	 */
	public static final int AUDIO_CODEC_G729A			= 40;
	public static final int AUDIO_CODEC_RSWC_8K			= 41;
	public static final int AUDIO_CODEC_RSWC_16K20MS	= 42;
	public static final int AUDIO_CODEC_RSWC_32K20MS	= 44;
	public static final int AUDIO_CODEC_PCMA			= 45;
	public static final int AUDIO_CODEC_PCMU			= 46;
	/**
	 * 发送本音频数据 
	 * @param data     音频数据 
	 * @param length   数据长度
	 */
	public void sendMyAudioData(short[] data, int length);
	/**
	 * 获取音频数据 
	 * @return  音频数据
	 */
	public byte[] getAudioData();
	/**
	  * 打开或关闭音频
	  * @param state (1:打开 0:关闭)
	  */
	public void openMyAudio(int state);
	/**
	 * 改变MIC显示图标
	 * 打开/关闭本地音频
	 * @param state  (true: 打开   false: 关闭)
	 */
	public void  onOpenAudioConfirm(boolean state);
//	/**
//	 * 接收音频数据
//	 * @param data
//	 */
//	 void onReceiveAudioData(byte[] data);
//	/**
//	 * 启动音频服务
//	 */
//	 void startAudioService();
//	 /**
//	  * 关闭音频服务
//	  */
//	 void stopAudioService();
//	 
//	/**
//	 * 设置MIC开关状态
//	 * 
//	 * @param isMICWork
//	 */
//	void setMICWork(boolean isMICWork);
//
//	
//	
//	/**
//	 * 打开MIC
//	 */
//	public void micOn();
//
//	/**
//	 * 关闭MIC
//	 */
//	public void micOff();
//	/**
//	 * 检查麦是否可用
//	 * @return
//	 */
//	public boolean isMICWork();
//	public void setHandler(Handler handler);
//	public void destroy();
	/**
	 * 音频设备状态
	 * @param userID      用户ID
	 * @param isHaveDev   是否连接音频设备
	 * @param isOpenDev   音频设备是否打开
	 */
	public void onUpdateDeviceStatus(int userID, boolean isHaveDev,
                                     boolean isOpenDev);
	/**
	 * 切换音频编码格式
	 * 
	 * @param codecType
	 */	
	public void onSyncVoipCodec(int codecType);
}
