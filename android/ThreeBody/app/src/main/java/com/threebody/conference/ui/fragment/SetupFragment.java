package com.threebody.conference.ui.fragment;

import android.media.AudioManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.threebody.conference.R;
import com.threebody.conference.ui.MeetingActivity;
import com.threebody.conference.ui.util.ToastUtil;
import com.threebody.sdk.service.AudioService;
import com.threebody.sdk.service.N2MRoomSystem;
import com.threebody.sdk.service.RoomService;

import butterknife.InjectView;

/**
 * Created by xiaxin on 15-1-14.
 */
public class SetupFragment extends BaseFragment {

    public static final String FRONT_CAMERA = "FRONT_CAMERA";
    public static final String BACK_CAMERA = "BACK_CAMERA";

    @InjectView(R.id.btn_video_switch)Button btnVideoSwitch;
    @InjectView(R.id.btn_select_video)Button btnSelectVideo;
    @InjectView(R.id.btn_audio_switch)Button btnAudioSwitch;
    @InjectView(R.id.btn_speaker_hand_free_switch)Button btnSpeakerHandFreeSwitch;
    @InjectView(R.id.btn_switch_front_back_camera)Button btnSwitchFrontBackCamera;
    @InjectView(R.id.button_switchvideo_id)Button buttonSwitchVideo;
    @InjectView(R.id.llHelp)LinearLayout llHelp;

    View setupFragmentView;

    // camera type
    //// FIXME: 2015/9/1 dirty
    private static String TO_DISPLAY_CARMERA_TYPE = BACK_CAMERA;


    RoomService roomService;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //// TODO: 2015/9/1 some werid issues, do it some time later.
//        if (setupFragmentView != null) {
//            // release current view from its parent, otherwise, this view will be added to 2 different containers, which is not allowed.
//            ViewGroup vg = (ViewGroup) setupFragmentView.getParent();
//            if(vg != null){
//                vg.removeView(setupFragmentView);
//            }
//            setupFragmentView.refreshDrawableState();
//            return setupFragmentView;
//        } else {
            setupFragmentView = inflater.inflate(R.layout.fragment_setup, null);
            initView(setupFragmentView);
            return setupFragmentView;
//        }
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        roomService = N2MRoomSystem.instance().getRoomService();
        btnVideoSwitch.setOnClickListener(this);
        btnAudioSwitch.setOnClickListener(this);
        llHelp.setOnClickListener(this);
        btnSpeakerHandFreeSwitch.setOnClickListener(this);
        btnSelectVideo.setOnClickListener(this);
        btnSwitchFrontBackCamera.setOnClickListener(this);

        buttonSwitchVideo.setOnClickListener(this);

        if(isMicOn()){
            btnAudioSwitch.setText(R.string.closeAudio);
        }

        if(isVideoOn()){
            btnVideoSwitch.setText(R.string.closeVideo);
        }

        AudioManager audioManager =
                ((AudioManager) getActivity().getSystemService(getActivity().AUDIO_SERVICE));
        AudioService audioService = roomService.getAudioService();
        if (audioManager.isSpeakerphoneOn()){
            btnSpeakerHandFreeSwitch.setText(R.string.closeSpeaker);
            audioService.setHandFree(true);

        }else {
            btnSpeakerHandFreeSwitch.setText(R.string.openSpeaker);
            audioService.setHandFree(false);
        }

        // select camera type button
        if(TO_DISPLAY_CARMERA_TYPE.equals(BACK_CAMERA)) {
            btnSwitchFrontBackCamera.setText(R.string.useBackCamera);
        } else {
            btnSwitchFrontBackCamera.setText(R.string.useFrontCamera);
        }
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.btn_audio_switch:
                btnAudioSwitch.setEnabled(false);
                if(!isMicOn()){
                    if(openAudio()){
                        btnAudioSwitch.setText(R.string.closeAudio);
                    }
                }else {
                    if(closeAudio()){
                        btnAudioSwitch.setText(R.string.openAudio);
                    }else{
                        ToastUtil.showToast(getActivity(), R.string.closefailed);
                    }
                }
                btnAudioSwitch.setEnabled(true);
                break;
            case R.id.btn_video_switch:
                btnVideoSwitch.setEnabled(false);
                if(!isVideoOn()){
                    if(openLocalVideo()){
                        btnVideoSwitch.setText(R.string.closeVideo);
                    }
                }else {
                    if(closeVideo()){
                        btnVideoSwitch.setText(R.string.openVideo);
                        btnSwitchFrontBackCamera.setText(R.string.useBackCamera); // FIXME: 2015/9/8 Why this ?
                    }else{
                        ToastUtil.showToast(getActivity(), R.string.closefailed);
                    }
                }
                btnVideoSwitch.setEnabled(true);
                break;
            case R.id.btn_speaker_hand_free_switch:
                btnSpeakerHandFreeSwitch.setEnabled(false);
                AudioService audioService = roomService.getAudioService();
                if(!isHandFree()){
                    if(setPlayoutSpeaker(true)){
                        btnSpeakerHandFreeSwitch.setText(R.string.closeSpeaker);
                        audioService.setHandFree(true);
                    }
                }else {
                    if(setPlayoutSpeaker(false)){
                        audioService.setHandFree(false);
                        btnSpeakerHandFreeSwitch.setText(R.string.openSpeaker);
                    }
                }

                btnSpeakerHandFreeSwitch.setEnabled(true);

                break;
            case R.id.btn_select_video:
                ((MeetingActivity)getActivity()).changeToVideoSet();
                break;
            case R.id.btn_switch_front_back_camera:
                String tag = (String) btnSwitchFrontBackCamera.getTag();
                if(TO_DISPLAY_CARMERA_TYPE.equals(FRONT_CAMERA)) {
                    TO_DISPLAY_CARMERA_TYPE = BACK_CAMERA;
                    btnSwitchFrontBackCamera.setText(R.string.useBackCamera);
                }  else {
                    TO_DISPLAY_CARMERA_TYPE = FRONT_CAMERA;
                    btnSwitchFrontBackCamera.setText(R.string.useFrontCamera);
                }
                roomService.getVideoService().switchCamera();
                break;
            case R.id.button_switchvideo_id:
                VideoFragment videoFragment = ((MeetingActivity)getActivity()).getVideoFragment();
                videoFragment.switchVideo();
                break;
        }
    }

    private boolean isHandFree() {
        return roomService.getAudioService().isHandFree();
    }

    private boolean isMicOn() {
        return roomService.getAudioService().isMicOn();

    }

    private boolean openAudio(){
        return roomService.getAudioService().openMic(roomService.getMe().getNodeId());
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

    private boolean muteAudio(boolean mute){
        return roomService.getAudioService().muteMic(roomService.getMe().getNodeId(), mute);
    }

    private boolean closeAudio(){
        return roomService.getAudioService().closeMic(roomService.getMe().getNodeId());
    }

    private boolean openLocalVideo(){
        return roomService.getVideoService().openLocalVideo(roomService.getMe().getNodeId());
    }
    private boolean closeVideo(){
        return roomService.getVideoService().closeVideo(roomService.getMe().getNodeId());
    }
    public void showCloseLocalVideoOnVideoSwitch(){
        if(btnVideoSwitch != null){
            btnVideoSwitch.setText(R.string.closeVideo);
        }
    }
    public void showOpenLocalVideoOnVideoSwitch(){
        if(btnVideoSwitch != null){
            btnVideoSwitch.setText(R.string.openVideo);
        }
    }

    private boolean isVideoOn() {
        return roomService.getVideoService().isVideoOn();
    }
}
