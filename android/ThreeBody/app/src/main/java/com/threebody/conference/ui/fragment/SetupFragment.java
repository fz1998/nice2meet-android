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
public class SetupFragment extends BaseFragment {
    @InjectView(R.id.btn_video_switch)Button btnVideoSwitch;
    @InjectView(R.id.btn_video_show)Button btnVideoShow;
    @InjectView(R.id.btn_audio_switch)Button btnAudioSwitch;
    @InjectView(R.id.btn_speaker_hand_free_switch)Button btnSpeakerHandFreeSwitch;
    @InjectView(R.id.btn_switch_front_back_camera)Button btnSwitchFrontBackCamera;
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
        btnVideoSwitch.setOnClickListener(this);
        btnAudioSwitch.setOnClickListener(this);
        llHelp.setOnClickListener(this);
        btnSpeakerHandFreeSwitch.setOnClickListener(this);
        btnVideoShow.setOnClickListener(this);
        btnSwitchFrontBackCamera.setOnClickListener(this);

        if(AudioCommon.IS_MIC_ON == AudioCommon.MIC_ON){
            btnAudioSwitch.setText(R.string.closeAudio);
        }else if(AudioCommon.IS_MIC_ON == AudioCommon.MIC_HANDS_UP){
            btnAudioSwitch.setText(R.string.handsup);
        }
        if(VideoCommon.IS_CAMERA_OPEN == VideoCommon.CAMERA_ON){
            btnVideoSwitch.setText(R.string.closeVideo);
        }else if(VideoCommon.IS_CAMERA_OPEN == VideoCommon.CAMERA_HOLD){
            btnAudioSwitch.setText(R.string.handsup);
        }
        AudioManager audioManager =
                ((AudioManager) getActivity().getSystemService(getActivity().AUDIO_SERVICE));
        if (audioManager.isSpeakerphoneOn()){
            btnSpeakerHandFreeSwitch.setText(R.string.closeSpeaker);
            AudioCommon.IS_SPEAKER_ON = AudioCommon.SPEAKER_ON;
        }else {
            btnSpeakerHandFreeSwitch.setText(R.string.openSpeaker);
            AudioCommon.IS_SPEAKER_ON = AudioCommon.SPEAKER_OFF;
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.btn_audio_switch:
                btnAudioSwitch.setEnabled(false);
                if(AudioCommon.IS_MIC_ON == AudioCommon.MIC_OFF){
                    if(openAudio()){
                        btnAudioSwitch.setText(R.string.closeAudio);

                    }else{
                        AudioCommon.IS_MIC_ON = AudioCommon.MIC_HANDS_UP;
                        btnAudioSwitch.setText(R.string.handsup);

                    }
                }else if(AudioCommon.IS_MIC_ON == AudioCommon.MIC_ON){
                    if(closeAudio()){
                        btnAudioSwitch.setText(R.string.openAudio);
                    }else{
                        ToastUtil.showToast(getActivity(), R.string.closefailed);
                    }
                }else{
                    ToastUtil.showToast(getActivity(), R.string.handsTip);
                }
                btnAudioSwitch.setEnabled(true);
                break;
            case R.id.btn_video_switch:
                btnVideoSwitch.setEnabled(false);
                if(VideoCommon.IS_CAMERA_OPEN == VideoCommon.CAMERA_OFF){
                    if(openVideo()){
                        btnVideoSwitch.setText(R.string.closeVideo);
                    }else{
                        VideoCommon.IS_CAMERA_OPEN = VideoCommon.CAMERA_HOLD;
                        btnVideoSwitch.setText(R.string.handsup);

                    }
                }else if(VideoCommon.IS_CAMERA_OPEN == VideoCommon.CAMERA_ON){
                    if(closeVideo()){
                        btnVideoSwitch.setText(R.string.openVideo);
                    }else{
                        ToastUtil.showToast(getActivity(), R.string.closefailed);
                    }
                }else{
                    ToastUtil.showToast(getActivity(), R.string.handsTip);
                }
                btnVideoSwitch.setEnabled(true);
                break;
            case R.id.btn_speaker_hand_free_switch:
                btnSpeakerHandFreeSwitch.setEnabled(false);
                if(AudioCommon.IS_SPEAKER_ON== AudioCommon.SPEAKER_OFF){
                    if(setPlayoutSpeaker(true)){
                        btnSpeakerHandFreeSwitch.setText(R.string.closeSpeaker);
                        AudioCommon.IS_SPEAKER_ON = AudioCommon.SPEAKER_ON;
                    }
                }else if(AudioCommon.IS_SPEAKER_ON == AudioCommon.SPEAKER_ON){
                    if(setPlayoutSpeaker(false)){
                        AudioCommon.IS_SPEAKER_ON = AudioCommon.SPEAKER_OFF;
                        btnSpeakerHandFreeSwitch.setText(R.string.openSpeaker);
                    }
                }else{
                    ToastUtil.showToast(getActivity(), R.string.handsTip);
                }
                btnSpeakerHandFreeSwitch.setEnabled(true);


                /*// test for muteMicrophone
                if(AudioCommon.IS_SPEAKER_ON== AudioCommon.SPEAKER_OFF){
                    if(muteAudio(true)){
                        btnSpeakerHandFreeSwitch.setText(R.string.closeSpeaker);
                        AudioCommon.IS_SPEAKER_ON = AudioCommon.SPEAKER_ON;
                    }
                }else if(AudioCommon.IS_SPEAKER_ON == AudioCommon.SPEAKER_ON){
                    if(muteAudio(false)){
                        AudioCommon.IS_SPEAKER_ON = AudioCommon.SPEAKER_OFF;
                        btnSpeakerHandFreeSwitch.setText(R.string.openSpeaker);
                    }
                }else{
                    ToastUtil.showToast(getActivity(), R.string.handsTip);
                }*/
                btnSpeakerHandFreeSwitch.setEnabled(true);
                break;
            case R.id.btn_video_show:
                ((MeetingActivity)getActivity()).changeToVideoSet();
                break;
            case R.id.btn_switch_front_back_camera:
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
        if(btnAudioSwitch != null){
            btnAudioSwitch.setText(R.string.closeAudio);
        }
    }
    public void closeLocalAudio(){
        if(btnAudioSwitch != null ){
            btnAudioSwitch.setText(R.string.openAudio);
        }

    }
    private boolean openVideo(){
        return roomCommon.getVideoCommon().openVideo(roomCommon.getMe().getNodeId());
    }
    private boolean closeVideo(){
        return roomCommon.getVideoCommon().closeVideo(roomCommon.getMe().getNodeId());
    }
    public void openLocalVideo(){
        if(btnVideoSwitch != null){
            btnVideoSwitch.setText(R.string.closeVideo);
        }
    }
    public void closeLoacalVideo(){
        if(btnVideoSwitch != null){
            btnVideoSwitch.setText(R.string.openVideo);
        }
    }
//    private boolean openVideo(){
//
//    }
//    private boolean closeVideo(){
//
//    }
}
