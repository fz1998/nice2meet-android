package com.threebody.sdk.function;

import com.infowarelab.conference.audio.jni.AudioJni;

public class AudioService {
	
	private static  AudioService instance = null;
	private AudioService(){}
	public static AudioService getInstance(){
		if(instance == null){
			instance = new AudioService();
		}
		return instance;
	}
	public void openMyAudio(int state){
		AudioJni.openAudio(state);
	}
	public void sendMyAudioData(short[] data, int length){
		AudioJni.sendAudioData(data, length);
	}
	public byte[] getAudioData(){
		return AudioJni.getAudioData();
	}
}
