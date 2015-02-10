package com.threebody.conference.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.threebody.conference.R;
import com.threebody.conference.ui.fragment.ChatFragment;
import com.threebody.conference.ui.fragment.SetFragment;
import com.threebody.conference.ui.fragment.VideoFragment;
import com.threebody.conference.ui.util.FragmentUtil;
import com.threebody.conference.ui.util.ToastUtil;
import com.threebody.sdk.common.AudioCommon;
import com.threebody.sdk.common.ChatCommon;
import com.threebody.sdk.common.RoomCommon;
import com.threebody.sdk.common.STSystem;
import com.threebody.sdk.common.VideoCommon;
import com.threebody.sdk.common.impl.AudioCommonImpl;
import com.threebody.sdk.common.impl.ChatCommonImpl;
import com.threebody.sdk.common.impl.RoomCommonImpl;
import com.threebody.sdk.common.impl.VideoCommonImpl;
import com.threebody.sdk.domain.VideoBean;

import org.st.User;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

/**
 * Created by xiaxin on 15-1-14.
 */
public class MeetingActivity extends BaseActivity {
    @InjectView(R.id.llContainer)LinearLayout llContainer;
    @InjectView(R.id.flMessage)FrameLayout flMessage;
    @InjectView(R.id.flVideo)FrameLayout flVideo;
    @InjectView(R.id.flSet)FrameLayout flSet;
    @InjectView(R.id.flExit)FrameLayout flExit;
    RoomCommonImpl roomCommon;
    ChatCommonImpl chatCommon;
    AudioCommonImpl audioCommon;
    VideoCommonImpl videoCommon;
//    @InjectView(R.id.localVideo)LocalVideoView localVideoView;
    ChatFragment mMessage;
    VideoFragment mVideo;
    SetFragment mSet;
    List<FrameLayout> tabs;
    List<Fragment> mFragments;
    int index = 1;
    @Override
    protected void initUI() {
        setContentView(R.layout.activity_meeting);
        super.initUI();
        getSupportActionBar().hide();
        flMessage.setOnClickListener(this);
        flVideo.setOnClickListener(this);
        flSet.setOnClickListener(this);
        flExit.setOnClickListener(this);
        tabs = new ArrayList<FrameLayout>();
        tabs.add(flMessage);
        tabs.add(flVideo);
        tabs.add(flSet);
        mMessage = new ChatFragment();
        mVideo = new VideoFragment();
        mSet = new SetFragment();
        mFragments = new ArrayList<Fragment>();
        mFragments.add(mMessage);
        mFragments.add(mVideo);
        mFragments.add(mSet);

        initData();
        getSupportFragmentManager().beginTransaction().add(R.id.llContainer, mVideo).commit();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.flMessage:
            case R.id.flVideo:
            case R.id.flSet:
                int oldIndex = index;
                tabs.get(index).setBackgroundResource(R.color.liquid);
                index = Integer.parseInt((String)v.getTag());
                if(oldIndex != index){
                    ToastUtil.showToast(this, "old = "+oldIndex +" new  ="+index);
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
            FragmentUtil.moveToLeftFragment(this, R.id.llContainer, mFragments.get(index));
        }else{
            FragmentUtil.moveToRightFragment(this, R.id.llContainer, mFragments.get(index));
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
        roomCommon.leave();
        finish();
    }
    private void initData(){
       roomCommon = (RoomCommonImpl)STSystem.getInstance().getRoomCommons().get(0);
        roomCommon.setCallback(new RoomCommon.RoomCallback() {
            @Override
            public void onJoin(int result) {

            }

            @Override
            public void onLeave(int reason) {
                ToastUtil.showToast(MeetingActivity.this, "退出会议");
            }

            @Override
            public void onConnectionChange(int state) {

            }

            @Override
            public void onUserJoin(User user) {

            }

            @Override
            public void onUserLeave(User user) {

            }

            @Override
            public void onUserUpdate(User user) {

            }

            @Override
            public void onUpdateRole(int nodeId, User.Role newRole) {

            }

            @Override
            public void onUpdateStatus(int nodeId, User.Status status) {

            }
        });
        chatCommon = new ChatCommonImpl(roomCommon, new ChatCommon.ChatCallback() {
            @Override
            public void onReceivePublicMessage(int nodeId, String message) {

            }

            @Override
            public void onReceivePrivateMessage(int nodeId, String message) {

            }

            @Override
            public void onReceiveData(int nodeId, String data) {

            }
        });
        audioCommon = new AudioCommonImpl(roomCommon, new AudioCommon.AudioCallback() {
            @Override
            public void onOpenMicrophone(int result, int nodeId) {
                if(nodeId == roomCommon.getMe().getNodeId()){
                    mSet.openLocalAudio();
                }
            }

            @Override
            public void onCloseMicrophone(int result, int nodeId) {
                if(nodeId == roomCommon.getMe().getNodeId()){
                    mSet.closeLocalAudio();
                }
            }

            @Override
            public void onRequestOpenMicrophone(int nodeId) {

            }
        });
        videoCommon = new VideoCommonImpl(roomCommon, new VideoCommon.VideoCallback() {
            @Override
            public void onOpenVideo(int result, int nodeId, String deviceId) {
                if(nodeId == roomCommon.getMe().getNodeId()){
                    mSet.openLocalVideo();
                }
            }

            @Override
            public void onCloseVideo(int result, int nodeId, String deviceId) {
                if(nodeId == roomCommon.getMe().getNodeId()){
                    mSet.closeLocalAudio();
                }
            }

            @Override
            public void onRequestOpenVideo(int nodeId, String deviceId) {

            }

            @Override
            public void onVideoData(VideoBean videoBean) {
                mVideo.receiVideoBean(videoBean);
            }
        });
    }

    public VideoCommonImpl getVideoCommon() {
        return videoCommon;
    }

    public ChatCommonImpl getChatCommon() {
        return chatCommon;
    }

    public void sendMessage(String message){
        chatCommon.sendPublicMessage(message);
    }

    public void refreshVideo(){
        mVideo.refresh(videoCommon.getDevices());
    }

    @Override
    public void onBackPressed() {
        showDialog();
    }
}
