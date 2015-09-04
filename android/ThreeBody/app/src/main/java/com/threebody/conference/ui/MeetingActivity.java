package com.threebody.conference.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.threebody.conference.R;
import com.threebody.conference.ui.fragment.ChatFragment;
import com.threebody.conference.ui.fragment.SetupFragment;
import com.threebody.conference.ui.fragment.VideoFragment;
import com.threebody.conference.ui.fragment.VideoSelectFragment;
import com.threebody.conference.ui.util.FragmentUtil;
import com.threebody.sdk.common.AudioCommon;
import com.threebody.sdk.common.ChatCommon;
import com.threebody.sdk.common.RoomCommon;
import com.threebody.sdk.common.STSystem;
import com.threebody.sdk.common.VideoCommon;
import com.threebody.sdk.domain.N2MVideo;
import com.threebody.sdk.util.LoggerUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

/**
 * Created by xiaxin on 15-1-14.
 */
public class MeetingActivity extends BaseActivity {
    @InjectView(R.id.mainScreenLinearLayout)LinearLayout llContainer;
    @InjectView(R.id.flChat)FrameLayout flChatBtn;
    @InjectView(R.id.flVideo_btn)FrameLayout flVideoBtn;
    @InjectView(R.id.flSet)FrameLayout flSetupBtn;
    @InjectView(R.id.flExit)FrameLayout flExitBtn;

    RoomCommon roomCommon;
    ChatCommon chatCommon;
    AudioCommon audioCommon;
    VideoCommon videoCommon;
    ChatFragment chatFragment;
    VideoFragment videoFragment;
    SetupFragment setupFragment;
    VideoSelectFragment videoSelectFragment;
    List<FrameLayout> btnList;
    List<Fragment> fragmentList;
    int index = 1;

    public VideoFragment getVideoFragment() {
        return videoFragment;
    }

    @Override
    protected void initUI() {
        setContentView(R.layout.activity_meeting);
        super.initUI();

        getSupportActionBar().hide();
        flChatBtn.setOnClickListener(this);
        flVideoBtn.setOnClickListener(this);
        flSetupBtn.setOnClickListener(this);
        flExitBtn.setOnClickListener(this);
        btnList = new ArrayList<FrameLayout>();
        btnList.add(flChatBtn);
        btnList.add(flVideoBtn);
        btnList.add(flSetupBtn);
        chatFragment = new ChatFragment();
        videoFragment = new VideoFragment();
        setupFragment = new SetupFragment();
        videoSelectFragment = new VideoSelectFragment();
        fragmentList = new ArrayList<Fragment>();
        fragmentList.add(chatFragment);
        fragmentList.add(videoFragment);
        fragmentList.add(setupFragment);

        // display video Fragment
        getSupportFragmentManager().beginTransaction().add(R.id.mainScreenLinearLayout, videoFragment).commit();

        // init biz objects
        initBizObjects();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.flChat:
            case R.id.flVideo_btn:
            case R.id.flSet:
                int oldIndex = index;
                btnList.get(index).setBackgroundResource(R.color.liquid);
                index = Integer.parseInt((String)v.getTag());
                if(oldIndex != index){
//                    ToastUtil.showToast(this, "old = "+oldIndex +" new  ="+index);
                    v.setBackgroundResource(R.drawable.topbg);
                    changeFragment(oldIndex,index);
                }
                break;
            case R.id.flExit:
               showDialog();
                break;
            default:
                break;
        }
    }
    private void changeFragment(int old, int index){
        if(old > index){
            FragmentUtil.moveToLeftFragment(this, R.id.mainScreenLinearLayout, fragmentList.get(index));
        }else{
            FragmentUtil.moveToRightFragment(this, R.id.mainScreenLinearLayout, fragmentList.get(index));
        }
    }
    private void showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.tip)
                .setIcon(R.drawable.ic_launcher)
                .setMessage(R.string.exitTip)
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                leaveConference();
            }
        }).create().show();
    }
    private void leaveConference(){
        videoFragment.closeAll();
        roomCommon.leave();
        finish();
    }

    private void initBizObjects(){
        // roomCommon
        roomCommon = STSystem.getInstance().getRoomCommon();
        // chat common
        chatCommon = new ChatCommon(roomCommon, new ChatCommon.ChatCallback() {
            @Override
            public void onReceivePublicMessage(int nodeId, String message) {
                LoggerUtil.info(getClass().getName(), " receive public message nodeId = "+nodeId +" message = "+message);
                    chatFragment.receivePublicMessage();
            }

            @Override
            public void onReceivePrivateMessage(int nodeId, String message) {

            }

            @Override
            public void onReceiveData(int nodeId, String data) {

            }
        });
        // audio common
        audioCommon = new AudioCommon(roomCommon, new AudioCommon.AudioCallback() {
            @Override
            public void onOpenMicrophone(int result, int nodeId) {

                if(result == 0){
                    Message msg = new Message();
                    msg.what = VideoCommon.VIDEO_STATUS;
                    msg.obj = true;
                    msg.arg1 = nodeId;
                    handler.sendMessage(msg);
                }
            }

            @Override
            public void onCloseMicrophone(int result, int nodeId) {

                if(result == 0){
                    Message msg = new Message();
                    msg.what = VideoCommon.VIDEO_STATUS;
                    msg.obj = true;
                    msg.arg1 = nodeId;
                    handler.sendMessage(msg);
                }
            }

            @Override
            public void onRequestOpenMicrophone(int nodeId) {

            }
        });
        videoCommon = new VideoCommon(roomCommon, new VideoCommon.VideoCallback() {
            @Override
            public void onOpenVideo(N2MVideo n2MVideo) {
                Message msg = new Message();
                msg.what = VideoCommon.VIDEO_OPEN;
                msg.obj = n2MVideo;
                handler.sendMessage(msg);
            }

            @Override
            public void onCloseVideo(int result, int nodeId, String deviceId) {

                N2MVideo n2MVideo = new N2MVideo(nodeId,deviceId);
                Message msg = new Message();
                msg.what = VideoCommon.VIDEO_CLOSE;
                msg.obj = n2MVideo;

                handler.sendMessage(msg);

            }
            @Override
            public void onShareScreen(N2MVideo n2MVideo)
            {
                Message msg = new Message();
                msg.what = VideoCommon.SCREEN_OPEN;
                msg.obj = n2MVideo;
                handler.sendMessage(msg);
            }
            @Override
            public void onCloseScreen(int result, int nodeId, String deviceId)
            {
                N2MVideo n2MVideo = new N2MVideo(nodeId,deviceId, true);
                Message msg = new Message();
                msg.what = VideoCommon.SCREEN_CLOSE;
                msg.obj = n2MVideo;
                handler.sendMessage(msg);
            }
            @Override
            public void onRequestOpenVideo(int nodeId, String deviceId) {

            }
        });
    }

    public VideoCommon getVideoCommon() {
        return videoCommon;
    }

    public ChatCommon getChatCommon() {
        return chatCommon;
    }

    public RoomCommon getRoomCommon() {
        return roomCommon;
    }



    public static final int FLAG_FROM_SETUP_FRAGMENT = 0;
    public static final int FLAG_FROM_VIDEO_FRAGMENT = 1;

    public void refreshVideoFragmentUI(){

//        FragmentUtil.moveToLeftFragment(this, R.id.mainScreenLinearLayout, videoFragment);
//        videoFragment.refreshVideoWindows();
        videoFragment.refreshVideoWindows();

        onBackPressed();


//        if (flag == FLAG_FROM_SETUP_FRAGMENT) {
//            FragmentUtil.moveToLeftFragment(this, R.id.mainScreenLinearLayout, setupFragment);
//        }else if (flag == FLAG_FROM_VIDEO_FRAGMENT) {
//            FragmentUtil.moveToLeftFragment(this, R.id.mainScreenLinearLayout, videoFragment);
//        }

//        videoFragment.refreshVideoWindows(videoCommon.getDevices());
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    @Override
    public void onBackPressed() {
        if (videoSelectFragment.isVisible()) {
            super.onBackPressed();
            return;
        }
        showDialog();

    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what){
                case VideoCommon.VIDEO_OPEN:
                    N2MVideo n2MVideo = (N2MVideo)msg.obj;
                    videoFragment.refreshVideoWindows();
                    // update Setup Fragment UI
                    if(n2MVideo.getNodeId() == roomCommon.getMe().getNodeId()){
                        setupFragment.showCloseLocalVideoOnVideoSwitch();
                    }

                    break;
                case VideoCommon.VIDEO_CLOSE:
                    n2MVideo = (N2MVideo)msg.obj;
                    videoFragment.refreshVideoWindows();;
                    if(n2MVideo.getNodeId() == roomCommon.getMe().getNodeId()){
                        setupFragment.showOpenLocalVideoOnVideoSwitch();
                    }
                    break;
                case VideoCommon.SCREEN_OPEN:
                    n2MVideo = (N2MVideo)msg.obj;
                    videoFragment.refreshVideoWindows();;
                    if(n2MVideo.getNodeId() == roomCommon.getMe().getNodeId()){
                        setupFragment.showCloseLocalVideoOnVideoSwitch();
                    }

                    break;
                case VideoCommon.SCREEN_CLOSE:
                    n2MVideo = (N2MVideo)msg.obj;
                    videoFragment.refreshVideoWindows();;
                    if(n2MVideo.getNodeId() == roomCommon.getMe().getNodeId()){
                        setupFragment.showOpenLocalVideoOnVideoSwitch();
                    }
                    break;
                case VideoCommon.VIDEO_STATUS:
                    boolean isOpen = (Boolean)msg.obj;
                    videoFragment.setAudioStatus(isOpen, msg.arg1);
                    break;
            }
        }
    };

    public void changeToVideoSet() {
        FragmentUtil.moveToRightFragment(this, R.id.mainScreenLinearLayout, videoSelectFragment);
    }


//    private void initBizObjects(){
//        roomCommon = (RoomCommon)STSystem.getInstance().getRoomCommon();
//        roomCommon.setCallback(new RoomCommon.RoomCallback() {
//            @Override
//            public void onJoin(int result) {
//
//            }
//
//            @Override
//            public void onLeave(int reason) {
//                ToastUtil.showToast(MeetingActivity.this, "退出会议");
//
//            }
//
//            @Override
//            public void onConnectionChange(Room.ConnectionStatus state) {
//
//            }
//
//            @Override
//            public void onUserJoin(User user) {
//
//            }
//
//            @Override
//            public void onUserLeave(User user) {
//
//            }
//
//            @Override
//            public void onUserUpdate(User user) {
//
//            }
//
//            @Override
//            public void onUpdateRole(int nodeId, User.Role newRole) {
//
//            }
//
//            @Override
//            public void onUpdateStatus(int nodeId, User.Status status) {
//
//            }
//        });
//        chatCommon = new ChatCommonImpl(roomCommon, new ChatCommon.ChatCallback() {
//            @Override
//            public void onReceivePublicMessage(int nodeId, String message) {
//                LoggerUtil.info(getClass().getName(), " receive public message nodeId = "+nodeId +" message = "+message);
//                chatFragment.receivePublicMessage();
//            }
//
//            @Override
//            public void onReceivePrivateMessage(int nodeId, String message) {
//
//            }
//
//            @Override
//            public void onReceiveData(int nodeId, String data) {
//
//            }
//        });
//        audioCommon = new AudioCommonImpl(roomCommon, new AudioCommon.AudioCallback() {
//            @Override
//            public void onOpenMicrophone(int result, int nodeId) {
//
//                if(result == 0){
//                    Message msg = new Message();
//                    msg.what = VideoCommon.VIDEO_STATUS;
//                    msg.obj = true;
//                    msg.arg1 = nodeId;
//                    handler.sendMessage(msg);
//                }
//            }
//
//            @Override
//            public void onCloseMicrophone(int result, int nodeId) {
//
//                if(result == 0){
//                    Message msg = new Message();
//                    msg.what = VideoCommon.VIDEO_STATUS;
//                    msg.obj = true;
//                    msg.arg1 = nodeId;
//                    handler.sendMessage(msg);
//                }
//            }
//
//            @Override
//            public void onRequestOpenMicrophone(int nodeId) {
//
//            }
//        });
//        videoCommon = new VideoCommonImpl(roomCommon, new VideoCommon.VideoCallback() {
//            @Override
//            public void onOpenVideo(DeviceBean deviceBean) {
//                Message msg = new Message();
//                msg.what = VideoCommon.VIDEO_OPEN;
//                msg.obj = deviceBean;
//                handler.sendMessage(msg);
//            }
//
//            @Override
//            public void onCloseVideo(int result, int nodeId, String deviceId) {
//
//                DeviceBean deviceBean = new DeviceBean(nodeId,deviceId);
//                Message msg = new Message();
//                msg.what = VideoCommon.VIDEO_CLOSE;
//                msg.obj = deviceBean;
//                handler.sendMessage(msg);
//
//            }
//            @Override
//            public void onShareScreen(DeviceBean deviceBean)
//            {
//                Message msg = new Message();
//                msg.what = VideoCommon.SCREEN_OPEN;
//                msg.obj = deviceBean;
//                handler.sendMessage(msg);
//            }
//            @Override
//            public void onCloseScreen(int result, int nodeId, String deviceId)
//            {
//                DeviceBean deviceBean = new DeviceBean(nodeId,deviceId, true);
//                Message msg = new Message();
//                msg.what = VideoCommon.SCREEN_CLOSE;
//                msg.obj = deviceBean;
//                handler.sendMessage(msg);
//            }
//            @Override
//            public void onRequestOpenVideo(int nodeId, String deviceId) {
//
//            }
//
//            @Override
//            public void onVideoData(VideoBean videoBean) {
////                videoFragment.receiVideoBean(videoBean);
//            }
//        });
////        initDevice();
//    }



}
