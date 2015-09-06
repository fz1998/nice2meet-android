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
import com.threebody.sdk.service.AudioService;
import com.threebody.sdk.service.RoomService;
import com.threebody.sdk.service.N2MRoomSystem;
import com.threebody.sdk.service.VideoService;

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

        if(AudioService.IS_MIC_ON == AudioService.MIC_ON){
            btnAudioSwitch.setText(R.string.closeAudio);
        }else if(AudioService.IS_MIC_ON == AudioService.MIC_HANDS_UP){
            btnAudioSwitch.setText(R.string.handsup);
        }
        if(VideoService.IS_CAMERA_OPEN == VideoService.CAMERA_ON){
            btnVideoSwitch.setText(R.string.closeVideo);
        }else if(VideoService.IS_CAMERA_OPEN == VideoService.CAMERA_HOLD){
            btnAudioSwitch.setText(R.string.handsup);
        }
        AudioManager audioManager =
                ((AudioManager) getActivity().getSystemService(getActivity().AUDIO_SERVICE));
        if (audioManager.isSpeakerphoneOn()){
            btnSpeakerHandFreeSwitch.setText(R.string.closeSpeaker);
            AudioService.IS_SPEAKER_ON = AudioService.SPEAKER_ON;
        }else {
            btnSpeakerHandFreeSwitch.setText(R.string.openSpeaker);
            AudioService.IS_SPEAKER_ON = AudioService.SPEAKER_OFF;
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
                if(AudioService.IS_MIC_ON == AudioService.MIC_OFF){
                    if(openAudio()){
                        btnAudioSwitch.setText(R.string.closeAudio);

                    }else{
                        AudioService.IS_MIC_ON = AudioService.MIC_HANDS_UP;
                        btnAudioSwitch.setText(R.string.handsup);

                    }
                }else if(AudioService.IS_MIC_ON == AudioService.MIC_ON){
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
                if(VideoService.IS_CAMERA_OPEN == VideoService.CAMERA_OFF){
                    if(openVideo()){
                        btnVideoSwitch.setText(R.string.closeVideo);
                    }else{
                        VideoService.IS_CAMERA_OPEN = VideoService.CAMERA_HOLD;
                        btnVideoSwitch.setText(R.string.handsup);

                    }
                }else if(VideoService.IS_CAMERA_OPEN == VideoService.CAMERA_ON){
                    if(closeVideo()){
                        btnVideoSwitch.setText(R.string.openVideo);
                        btnSwitchFrontBackCamera.setText(R.string.useBackCamera);
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
                if(AudioService.IS_SPEAKER_ON== AudioService.SPEAKER_OFF){
                    if(setPlayoutSpeaker(true)){
                        btnSpeakerHandFreeSwitch.setText(R.string.closeSpeaker);
                        AudioService.IS_SPEAKER_ON = AudioService.SPEAKER_ON;
                    }
                }else if(AudioService.IS_SPEAKER_ON == AudioService.SPEAKER_ON){
                    if(setPlayoutSpeaker(false)){
                        AudioService.IS_SPEAKER_ON = AudioService.SPEAKER_OFF;
                        btnSpeakerHandFreeSwitch.setText(R.string.openSpeaker);
                    }
                }else{
                    ToastUtil.showToast(getActivity(), R.string.handsTip);
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
                roomService.getVideoService().switchVideo();
                break;
            case R.id.button_switchvideo_id:
                VideoFragment videoFragment = ((MeetingActivity)getActivity()).getVideoFragment();
                videoFragment.switchVideo();
                break;
        }
    }
    private boolean openAudio(){
        return roomService.getAudioService().openMic(roomService.getMe().getNodeId());
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
        return roomService.getAudioService().muteMic(roomService.getMe().getNodeId(), mute);
    }

    private boolean closeAudio(){
        return roomService.getAudioService().closeMic(roomService.getMe().getNodeId());
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
        return roomService.getVideoService().openVideo(roomService.getMe().getNodeId());
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
//    private boolean openVideo(){
//
//    }
//    private boolean closeVideo(){
//
//    }
}
