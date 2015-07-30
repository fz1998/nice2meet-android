package com.threebody.conference.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.media.AudioManager;

import com.threebody.conference.R;
import com.threebody.conference.ui.MeetingActivity;
import com.threebody.conference.ui.util.ToastUtil;
import com.threebody.sdk.common.AudioCommon;
import com.threebody.sdk.common.RoomCommon;
import com.threebody.sdk.common.STSystem;
import com.threebody.sdk.common.VideoCommon;

import butterknife.InjectView;

/**
 * Created by xiaxin on 15-1-14.
 */
public class SetFragment extends BaseFragment {
    @InjectView(R.id.ivVideo)Button ivVideo;
    @InjectView(R.id.ivVideoShow)Button ivVideoShow;
    @InjectView(R.id.ivAudio)Button ivAudio;
    @InjectView(R.id.ivSpeaker)Button ivSpeaker;
    @InjectView(R.id.ivSwitchCamera)Button ivSwitchCamera;
    @InjectView(R.id.llHelp)LinearLayout llHelp;
    RoomCommon roomCommon;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_set, null);
        initView(view);
        return view;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        roomCommon = STSystem.getInstance().getRoomCommons().get(0);
        ivVideo.setOnClickListener(this);
        ivAudio.setOnClickListener(this);
        llHelp.setOnClickListener(this);
        ivSpeaker.setOnClickListener(this);
        ivVideoShow.setOnClickListener(this);
        ivSwitchCamera.setOnClickListener(this);
        if(AudioCommon.IS_MIC_ON == AudioCommon.MIC_ON){
            ivAudio.setText(R.string.closeAudio);
        }else if(AudioCommon.IS_MIC_ON == AudioCommon.MIC_HANDS_UP){
            ivAudio.setText(R.string.handsup);
        }
        if(VideoCommon.IS_CAMERA_OPEN == VideoCommon.CAMERA_ON){
            ivVideo.setText(R.string.closeVideo);
        }else if(VideoCommon.IS_CAMERA_OPEN == VideoCommon.CAMERA_HOLD){
            ivAudio.setText(R.string.handsup);
        }
        AudioManager audioManager =
                ((AudioManager) getActivity().getSystemService(getActivity().AUDIO_SERVICE));
        if (audioManager.isSpeakerphoneOn()){
            ivSpeaker.setText(R.string.closeSpeaker);
            AudioCommon.IS_SPEAKER_ON = AudioCommon.SPEAKER_ON;
        }else {
            ivSpeaker.setText(R.string.openSpeaker);
            AudioCommon.IS_SPEAKER_ON = AudioCommon.SPEAKER_OFF;
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.ivAudio:
                ivAudio.setEnabled(false);
                if(AudioCommon.IS_MIC_ON == AudioCommon.MIC_OFF){
                    if(openAudio()){
                        ivAudio.setText(R.string.closeAudio);

                    }else{
                        AudioCommon.IS_MIC_ON = AudioCommon.MIC_HANDS_UP;
                        ivAudio.setText(R.string.handsup);

                    }
                }else if(AudioCommon.IS_MIC_ON == AudioCommon.MIC_ON){
                    if(closeAudio()){
                        ivAudio.setText(R.string.openAudio);
                    }else{
                        ToastUtil.showToast(getActivity(), R.string.closefailed);
                    }
                }else{
                    ToastUtil.showToast(getActivity(), R.string.handsTip);
                }
                ivAudio.setEnabled(true);
                break;
            case R.id.ivVideo:
                ivVideo.setEnabled(false);
                if(VideoCommon.IS_CAMERA_OPEN == VideoCommon.CAMERA_OFF){
                    if(openVideo()){
                        ivVideo.setText(R.string.closeVideo);
                    }else{
                        VideoCommon.IS_CAMERA_OPEN = VideoCommon.CAMERA_HOLD;
                        ivVideo.setText(R.string.handsup);

                    }
                }else if(VideoCommon.IS_CAMERA_OPEN == VideoCommon.CAMERA_ON){
                    if(closeVideo()){
                        ivVideo.setText(R.string.openVideo);
                    }else{
                        ToastUtil.showToast(getActivity(), R.string.closefailed);
                    }
                }else{
                    ToastUtil.showToast(getActivity(), R.string.handsTip);
                }
                ivVideo.setEnabled(true);
                break;
            case R.id.ivSpeaker:
                ivSpeaker.setEnabled(false);
                if(AudioCommon.IS_SPEAKER_ON== AudioCommon.SPEAKER_OFF){
                    if(setPlayoutSpeaker(true)){
                        ivSpeaker.setText(R.string.closeSpeaker);
                        AudioCommon.IS_SPEAKER_ON = AudioCommon.SPEAKER_ON;
                    }
                }else if(AudioCommon.IS_SPEAKER_ON == AudioCommon.SPEAKER_ON){
                    if(setPlayoutSpeaker(false)){
                        AudioCommon.IS_SPEAKER_ON = AudioCommon.SPEAKER_OFF;
                        ivSpeaker.setText(R.string.openSpeaker);
                    }
                }else{
                    ToastUtil.showToast(getActivity(), R.string.handsTip);
                }
                ivSpeaker.setEnabled(true);


                /*// test for muteMicrophone
                if(AudioCommon.IS_SPEAKER_ON== AudioCommon.SPEAKER_OFF){
                    if(muteAudio(true)){
                        ivSpeaker.setText(R.string.closeSpeaker);
                        AudioCommon.IS_SPEAKER_ON = AudioCommon.SPEAKER_ON;
                    }
                }else if(AudioCommon.IS_SPEAKER_ON == AudioCommon.SPEAKER_ON){
                    if(muteAudio(false)){
                        AudioCommon.IS_SPEAKER_ON = AudioCommon.SPEAKER_OFF;
                        ivSpeaker.setText(R.string.openSpeaker);
                    }
                }else{
                    ToastUtil.showToast(getActivity(), R.string.handsTip);
                }*/
                ivSpeaker.setEnabled(true);
                break;
            case R.id.ivVideoShow:
                ((MeetingActivity)getActivity()).changeToVideoSet();
                break;
            case R.id.ivSwitchCamera:
                roomCommon.getVideoCommon().switchVideo();
                break;
        }
    }
    private boolean openAudio(){
        return roomCommon.getAudioCommon().openMic(roomCommon.getMe().getNodeId());
    }
    private boolean openSpeaker(){
        AudioManager audioManager =
                ((AudioManager) getActivity().getSystemService(getActivity().AUDIO_SERVICE));
        // TODO(fischman): figure out how to do this Right(tm) and remove the
        // suppression.
        //audioManager.setMode( AudioManager.MODE_IN_COMMUNICATION);
        audioManager.setSpeakerphoneOn(true);
        return  true;
    }
    private boolean setPlayoutSpeaker(boolean loudspeakerOn){
        int apiLevel = android.os.Build.VERSION.SDK_INT;
        AudioManager audioManager =
                ((AudioManager) getActivity().getSystemService(getActivity().AUDIO_SERVICE));
        if ((3 == apiLevel) || (4 == apiLevel)) {
            // 1.5 and 1.6 devices
            if (loudspeakerOn) {
                // route audio to back speaker
                audioManager.setMode(AudioManager.MODE_NORMAL);
            } else {
                // route audio to earpiece
                audioManager.setMode(AudioManager.MODE_IN_CALL);
            }
        } else {
            // 2.x devices
            if ((android.os.Build.BRAND.equals("Samsung") ||
                    android.os.Build.BRAND.equals("samsung")) &&
                    ((5 == apiLevel) || (6 == apiLevel) ||
                            (7 == apiLevel))) {
                // Samsung 2.0, 2.0.1 and 2.1 devices
                if (loudspeakerOn) {
                    // route audio to back speaker
                    audioManager.setMode(AudioManager.MODE_IN_CALL);
                    audioManager.setSpeakerphoneOn(loudspeakerOn);
                } else {
                    // route audio to earpiece
                    audioManager.setSpeakerphoneOn(loudspeakerOn);
                    audioManager.setMode(AudioManager.MODE_NORMAL);
                }
            } else {
                // Non-Samsung and Samsung 2.2 and up devices
                audioManager.setSpeakerphoneOn(loudspeakerOn);
            }
        }
        return true;
    }
    private boolean closeSpeaker(){
        AudioManager audioManager =
                ((AudioManager) getActivity().getSystemService(getActivity().AUDIO_SERVICE));
        // TODO(fischman): figure out how to do this Right(tm) and remove the
        // suppression.

       // boolean isWiredHeadsetOn = audioManager.isWiredHeadsetOn();
        //audioManager.setMode(AudioManager.MODE_IN_CALL );
        audioManager.setSpeakerphoneOn(false);
        return  true;
    }

    private boolean muteAudio(boolean mute){
        return roomCommon.getAudioCommon().muteMic(roomCommon.getMe().getNodeId(), mute);
    }

    private boolean closeAudio(){
        return roomCommon.getAudioCommon().closeMic(roomCommon.getMe().getNodeId());
    }
    public void openLocalAudio(){
        if(ivAudio != null){
            ivAudio.setText(R.string.closeAudio);
        }
    }
    public void closeLocalAudio(){
        if(ivAudio != null ){
            ivAudio.setText(R.string.openAudio);
        }

    }
    private boolean openVideo(){
        return roomCommon.getVideoCommon().openVideo(roomCommon.getMe().getNodeId());
    }
    private boolean closeVideo(){
        return roomCommon.getVideoCommon().closeVideo(roomCommon.getMe().getNodeId());
    }
    public void openLocalVideo(){
        if(ivVideo != null){
            ivVideo.setText(R.string.closeVideo);
        }
    }
    public void closeLoacalVideo(){
        if(ivVideo != null){
            ivVideo.setText(R.string.openVideo);
        }
    }
//    private boolean openVideo(){
//
//    }
//    private boolean closeVideo(){
//
//    }
}
