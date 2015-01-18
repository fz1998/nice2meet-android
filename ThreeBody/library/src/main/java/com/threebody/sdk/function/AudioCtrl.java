package com.threebody.sdk.function;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.SystemClock;

import com.threebody.sdk.util.LoggerUtil;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;


public class AudioCtrl {
//	private Logger log = Logger.getLogger(getClass());
	public static int WAITING_TIME = 10;
	private static int SAMEPLE_RATE = 16000;
	private static int LENGTH = 640;
	private static int recordNum = 0;
	private int codecType = AudioCommon.AUDIO_CODEC_RSWC_16K20MS;
	private short[] bytes_pkg = new short[LENGTH];
	private int playBufferSize = 0, recordBufferSize = 0 ;
	private AudioTrack audioTrack ;
	private AudioRecord audioRecord;
	private boolean isPlaying, isRecording;
	public Thread playThread, recordThread ;
	File file ;
	RandomAccessFile raf ;
	private static AudioCtrl instance ;
	
	public static AudioCtrl getInstance(){
		if(instance == null){
			instance = new AudioCtrl();
		}
		return instance;
	}
	private AudioCtrl(){
//		initFile();
		initRecorder();
	}
	int index ;
	private void initFile(){
		file = new File("mnt/sdcard/infowarelab/audio.pcm");
		if(file.exists()){
			file.delete();
		}
		try {
			file.createNewFile();
			
			index = 0;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
//	long start = 0;
	public void startAudioTrack(){
//		LoggerUtil.info(getClass().getName(), "startAudioTrack11311");
//		try {
//			raf = new RandomAccessFile(file, "rw");
//		} catch (FileNotFoundException e1) {
//			// 
//			e1.printStackTrace();
//		}
		initPlayer();
		playThread = new Thread(){
			@Override
			public void run() {
//				Process.setThreadPriority(-2);
				super.run();
//				LoggerUtil.info(getClass().getName(), "play priority1 = "+getPriority());
				
				audioTrack.play();
				isPlaying = true;
				while(isPlaying){
					
						byte[] bytes = null;
						bytes = CommonFactory.getInstance().getAudioCommon().getAudioData();

						
						if(bytes.length > 0){
							if(audioTrack != null){
//								LoggerUtil.info(getClass().getName(), "audiotrack length = "+bytes.length);
								audioTrack.write(bytes, 0, bytes.length);
								SystemClock.sleep(5);
							}
						}
					
				}
			}
		};
		playThread.start();
	}
	
	public static byte[] shortToByteArray(short[] sh) {  
        byte[] targets = new byte[sh.length * 2];  
       for(int j = 0; j < sh.length; j++){
    	   short s = sh[j];
    	   for (int i = 0; i < 2; i++) {  
               int offset = (2 - 1 - i) * 8;  
               targets[2 * j + i] = (byte) ((s >>> offset) & 0xff);  
           }  
       }
        return targets;  
    }  
	
	public void initCodec(int codecType) {
		this.codecType = codecType;
//		LoggerUtil.info(getClass().getName(), "initCodec = "+codecType);
		switch (codecType) {
		case AudioCommon.AUDIO_CODEC_RSWC_16K20MS:
			LENGTH = 640;
			SAMEPLE_RATE = 16000;
			break;
		case AudioCommon.AUDIO_CODEC_PCMA:
		case AudioCommon.AUDIO_CODEC_PCMU:
		//8K SampleRate  20ms
		case AudioCommon.AUDIO_CODEC_G729A:
			LENGTH = 320;
			SAMEPLE_RATE = 8000;
			break;
		default:
			break;
		}
		initRecorder();
	}
	public void startAudioRecorder(){
		LoggerUtil.info(getClass().getName(), "startAudioRecorder()");
		
		try {
			audioRecord.startRecording();
			isRecording = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		recordThread = new Thread(){
			@Override
			public void run() {
				super.run();
				LoggerUtil.info(getClass().getName(), "recorder priority = "+getPriority());
//				Process.setThreadPriority(0);
				while(isRecording){
//					LoggerUtil.info(getClass().getName(), "audioRecordstate = "+audioRecord.getRecordingState());
					if(audioRecord == null){
						LoggerUtil.info(getClass().getName(), "audiorecorder is null!!!");
					}
						if(audioRecord != null && audioRecord.getRecordingState() == AudioRecord.RECORDSTATE_RECORDING){
							recordNum = audioRecord.read(bytes_pkg, 0, LENGTH);
//							LoggerUtil.info(getClass().getName(), "recordNum = "+recordNum);
							if(recordNum <= 0)
								continue ;
//							byte[] record = shortToByteArray(bytes_pkg);
//							try {
//								raf.seek(index);
//								raf.write(record);
//								index += record.length;
//							} catch (IOException e) {
//								// 
//								e.printStackTrace();
//							}
//							for(int i=0;i<bytes_pkg.length;i++)     
//		                    {  
//								bytes_pkg[i]= (short) (bytes_pkg[i]*5);  
//		                    } 
//							LoggerUtil.info(getClass().getName(), "data:"+getData(bytes_pkg));
							CommonFactory.getInstance().getAudioCommon().sendMyAudioData(bytes_pkg, LENGTH);
//							LoggerUtil.info(getClass().getName(), "sendAudioData length = "+bytes_pkg.length);
//							audioTrack.write(bytes_pkg, 0, LENGTH);
							//LoggerUtil.info(getClass().getName(), "sendAudioData length="+bytes_pkg.length);
//							SystemClock.sleep(WAITING_TIME);
						}else{
//							LoggerUtil.info(getClass().getName(), "is not record");
							SystemClock.sleep(WAITING_TIME);
						}
					}
				}
			
		};
		recordThread.start();
	}
//	public void play(byte[] audioData){
//		LoggerUtil.info(getClass().getName(), "audio play length="+audioData.length);
//		if(audioData != null && audioData.length > 0){
//			audioTrack.write(audioData, 0, audioData.length);
//		}
//	}

	
	private void initPlayer() {
		isPlaying = true;
		try {
			playBufferSize  = AudioTrack.getMinBufferSize(SAMEPLE_RATE, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT);
			LoggerUtil.info(getClass().getName(), "playBufferSize="+playBufferSize);
			if(playBufferSize < 6000)
				playBufferSize *= 2;

			LoggerUtil.info(getClass().getName(), "audioTrack");
			

			audioTrack = new AudioTrack(AudioManager.STREAM_VOICE_CALL, SAMEPLE_RATE, AudioFormat.CHANNEL_OUT_MONO, 

					AudioFormat.ENCODING_PCM_16BIT, playBufferSize, AudioTrack.MODE_STREAM);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	 
	}
	public void initRecorder(){
		LoggerUtil.info(getClass().getName(), "initRecorder");
		bytes_pkg = new short[LENGTH];
		if(audioRecord != null){
			isRecording = false;
			audioRecord.stop();
			audioRecord.release();
			audioRecord = null;
		}
		recordBufferSize = AudioRecord.getMinBufferSize(SAMEPLE_RATE, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
		recordBufferSize = recordBufferSize * 2;
		
		try {
			LoggerUtil.info(getClass().getName(), "audioRecorder0");
			audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, SAMEPLE_RATE, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, recordBufferSize * 2);
		} catch (Exception e) {
			e.printStackTrace();
			LoggerUtil.info(getClass().getName(), "audioRecorder1");
			audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, SAMEPLE_RATE, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, 12000);
		}
		
	}
	
	
	public void stop(){
		stopAudioTrack();
//		destroyAudioRecorder();
		stopAudioRecorder();
	}

	public void stopAudioRecorder() {
		if(isRecording){
			isRecording = false;
			LoggerUtil.info(getClass().getName(), "isRecording = "+isRecording);
			try {
				audioRecord.stop();
			} catch (Exception e) {
				LoggerUtil.error(getClass().getName(), e.getMessage());
			}
			
			recordThread = null;
		}
	}
	
	public void stopAudioTrack() {
		isPlaying = false;
		LoggerUtil.info(getClass().getName(), "stopAudioTrack");
		if(audioTrack != null){
			audioTrack.stop();
			audioTrack.release();
		}
		playThread = null;
		audioTrack = null;
//		try {
//			if(raf != null){
//				raf.close();
//			}
//		} catch (IOException e) {
//			// 
//			e.printStackTrace();
//		}
//		raf = null;
	}
//	public void openAudio(int state){
//		LoggerUtil.info(getClass().getName(), "openAudio :"+state);
//		AudioJni.openAudio(state);
//	}
	private String getData(short[] data){
		StringBuffer sb=new StringBuffer();
		sb.append("length:"+data.length+" ");
		for(int i=0;i<data.length;i++){
			sb.append(data[i]).append(",");
		}
		return sb.toString();
	}
	public boolean isRecording() {
		return isRecording;
	}
	public boolean isPlaying() {
		return isPlaying;
	}
	public void setSampleRate(int rate){
		SAMEPLE_RATE = rate;
	}
	public void destroyAudioRecorder() {
		try {
			LoggerUtil.info(getClass().getName(), "destroyAudioRecorder");
			isRecording = false;
			audioRecord.stop();
			audioRecord.release();
			audioRecord = null;
		} catch (Exception e) {
			LoggerUtil.error(getClass().getName(), e.getMessage());
		}
	}
}
