package com.threebody.sdk.function;




/**
 * 组件工厂类,单例
 * 
 * @author Sean.xie
 * 
 */
public class CommonFactory {

	private static CommonFactory instance = null;

//	/**
//	 * 用户组件
//	 */
//	private UserCommon userCommon;
//	/**
//	 * 文档组件
//	 */
//	private DocCommon docCommon;
//	/**
//	 * 会议组件
//	 */
//	private ConferenceCommon conferenceCommon;
////	/**
////	 * 多媒体组件
////	 */
////	private MediaCommon mediaCommon;
//	/**
//	 * 桌面共享组件
//	 */
//	private ShareDtCommon sdCommon;
//	/**
//	 * 音频组件
//	 */
	private AudioCommon audioCommon;
//	/**
//	 * 视频组件
//	 */
//	private VideoCommon videoCommon;
//	/**
//	 * 聊天组件
//	 */
//	private ChatCommom chatCommom;
//
//	public ChatCommom getChatCommom() {
//		return chatCommom;
//	}
//	public void setChatCommom(ChatCommom chatCommom) {
//		this.chatCommom = chatCommom;
//	}
	private CommonFactory() {
		
	}
//	public void initLocalCommon(){
//		userCommon = new UserCommonImpl();
//		userCommon.setHandler(null);
//		docCommon = new DocCommonImpl();
//		conferenceCommon = new ConferenceCommonImpl();
////		mediaCommon = new MediaCommonImpl();
//		sdCommon = new ShareDtCommonImpl();
//		audioCommon = new AudioCommonImpl();
//		videoCommon = new VideoCommonImpl();
//	}
//	public ConferenceCommon getConferenceCommon() {
//		return conferenceCommon;
//	}
//
//	public VideoCommon getVideoCommon() {
//		return videoCommon;
//	}

	public static CommonFactory getInstance() {
		if (instance == null) {
			instance = new CommonFactory();
		}
		return instance;
	}

//	public UserCommon getUserCommon() {
//		return userCommon;
//	}
//
//	public DocCommon getDocCommon() {
//		return docCommon;
//	}

//	public ConferenceCommon getConferenceCommonInstance() {
//		return conferenceCommon;
//	}

//	public MediaCommon getMediaCommon() {
//		if(mediaCommon == null)
//			mediaCommon = new MediaCommonImpl();
//		return mediaCommon;
//	}

//	public ShareDtCommon getSdCommon() {
//		return sdCommon;
//	}
//
//	public CommonFactory setSdCommon(ShareDtCommon sdCommon) {
//		this.sdCommon = sdCommon;
//		return this;
//	}
	public AudioCommon getAudioCommon() {
		return audioCommon;
	}

	public CommonFactory setAudioCommon(AudioCommon audioCommon) {
		this.audioCommon = audioCommon;
		return this;
	}
//	public CommonFactory setUserCommon(UserCommon userCommon){
//		this.userCommon = userCommon;
//		return this;
//	}
//	public CommonFactory setDocCommon(DocCommon docCommon){
//		this.docCommon = docCommon;
//		return this;
//	}
//	public CommonFactory setConferenceCommon(ConferenceCommon conferenceCommon){
//		this.conferenceCommon = conferenceCommon;
//		return this;
//	}
//	public CommonFactory setVideoCommon(VideoCommon videoCommon){
//		this.videoCommon = videoCommon;
//		return this;
//	}
}
