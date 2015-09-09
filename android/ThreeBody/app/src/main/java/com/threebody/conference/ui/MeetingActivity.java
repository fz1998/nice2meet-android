package com.threebody.conference.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.threebody.conference.R;
import com.threebody.conference.ui.fragment.ChatFragment;
import com.threebody.conference.ui.fragment.SetupFragment;
import com.threebody.conference.ui.fragment.VideoFragment;
import com.threebody.conference.ui.fragment.VideoSelectFragment;
import com.threebody.conference.ui.fragment.VideoWindow;
import com.threebody.conference.ui.util.FragmentUtil;
import com.threebody.sdk.service.AudioService;
import com.threebody.sdk.service.ChatService;
import com.threebody.sdk.service.RoomService;
import com.threebody.sdk.service.N2MRoomSystem;
import com.threebody.sdk.service.VideoService;
import com.threebody.sdk.domain.N2MVideo;
import com.threebody.sdk.util.LoggerUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

/**
 * Created by xiaxin on 15-1-14.
 */
public class MeetingActivity extends BaseActivity {

    public static final int SCREEN_CLOSE= 40005;
    public static final int SCREEN_OPEN= 40004;
    public static final int VIDEO_STATUS = 40003;
    public static final int VIDEO_CLOSE= 40002;
    public static final int VIDEO_OPEN= 40001;

    @InjectView(R.id.flChat_btn)FrameLayout flChatBtn;
    @InjectView(R.id.flVideo_btn)FrameLayout flVideoBtn;
    @InjectView(R.id.flSetup_btn)FrameLayout flSetupBtn;
    @InjectView(R.id.flExit_btn)FrameLayout flExitBtn;

    ChatFragment chatFragment; FrameLayout chatNumberLayout; TextView chatNumberText; int chatNumber = 0;
    VideoFragment videoFragment;
    SetupFragment setupFragment;
    VideoSelectFragment videoSelectFragment;
    List<FrameLayout> btnList;
    List<Fragment> fragmentList;

    // biz objects
    RoomService roomService;
    ChatService chatService;
    AudioService audioService;
    VideoService videoService;


    public VideoFragment getVideoFragment() {
        return videoFragment;
    }

    @Override
    protected void initUI() {
        setContentView(R.layout.activity_meeting);
        super.initUI();

        getSupportActionBar().hide();
        flChatBtn.setOnClickListener(this); chatNumberLayout = (FrameLayout) findViewById(R.id.layout_chat_number_id);
        flVideoBtn.setOnClickListener(this); chatNumberText = (TextView) findViewById(R.id.text_chat_number_id);
        flSetupBtn.setOnClickListener(this);
        flExitBtn.setOnClickListener(this);
        btnList = new ArrayList<>();
        btnList.add(flChatBtn);
        btnList.add(flVideoBtn);
        btnList.add(flSetupBtn);
        chatFragment = new ChatFragment();
        videoFragment = new VideoFragment();
        setupFragment = new SetupFragment();
        videoSelectFragment = new VideoSelectFragment();
        fragmentList = new ArrayList<>();
        fragmentList.add(chatFragment);
        fragmentList.add(videoFragment);
        fragmentList.add(setupFragment);

        // display video Fragment
        getSupportFragmentManager().beginTransaction().add(R.id.mainScreenLinearLayout, videoFragment).commit();

        // init biz objects
        initBizObjects();
    }
    int oldIndex = 1;
    int fragmentIndex = 1;
    @Override
    public void onClick(View v) {
        super.onClick(v);
        int viewIndex = Integer.parseInt((String)v.getTag());
        oldIndex = fragmentIndex;
        fragmentIndex = viewIndex;
        //do nothing if it's the same button as previous one
        if (oldIndex == viewIndex){
            return;
        }

        //flExit_btn
        if (v.getId() == R.id.flExit_btn){
            showDialog();
            return;
        }

        //else
        btnList.get(oldIndex).setBackgroundResource(R.color.liquid);
        if(oldIndex != fragmentIndex){
            v.setBackgroundResource(R.drawable.topbg);
            changeFragment(oldIndex, fragmentIndex);
        }
        if (Integer.parseInt((String)v.getTag()) == 0){
            //chatfragment
            chatNumberLayout.setVisibility(View.INVISIBLE);
            chatNumber = 0;
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
        //// TODO: 2015/9/5 any other cleaning work needed ?
        videoFragment.closeAll();
        roomService.leave();
        finish();
    }

    private void initBizObjects(){
        // roomCommon
        // FIXME: 2015/9/5 to use SingletonRoomSystem
        roomService = N2MRoomSystem.instance().getRoomService();
        // chatModule common
        chatService = new ChatService(roomService, new ChatService.ChatCallback() {
            @Override
            public void onReceivePublicMessage(int nodeId, String message) {
                LoggerUtil.info(getClass().getName(), " receive public message nodeId = " + nodeId + " message = " + message);
                chatFragment.receivePublicMessage();

                if (fragmentIndex == 0){
                    return;
                }

                ChatNumberRefreshTask task = new ChatNumberRefreshTask();
                task.execute();

            }

            @Override
            public void onReceivePrivateMessage(int nodeId, String message) {

            }

            @Override
            public void onReceiveData(int nodeId, String data) {

            }
        });
        // audio common
        audioService = new AudioService(roomService, new AudioService.AudioCallback() {
            @Override
            public void onOpenMicrophone(int result, int nodeId) {

                if(result == 0){
                    Message msg = new Message();
                    msg.what = VIDEO_STATUS;
                    msg.obj = true;
                    msg.arg1 = nodeId;
                    handler.sendMessage(msg);
                }
            }

            @Override
            public void onCloseMicrophone(int result, int nodeId) {

                if(result == 0){
                    Message msg = new Message();
                    msg.what = VIDEO_STATUS;
                    msg.obj = false;
                    msg.arg1 = nodeId;
                    handler.sendMessage(msg);
                }
            }

            @Override
            public void onRequestOpenMicrophone(int nodeId) {

            }
        });
        videoService = new VideoService(roomService, new VideoService.VideoCallback() {
            @Override
            public void onOpenVideo(N2MVideo n2MVideo) {
                Message msg = new Message();
                msg.what = VIDEO_OPEN;
                msg.obj = n2MVideo;
                handler.sendMessage(msg);
            }

            @Override
            public void onCloseVideo(int result, int nodeId, String deviceId) {

                N2MVideo n2MVideo = new N2MVideo(nodeId,deviceId);
                Message msg = new Message();
                msg.what = VIDEO_CLOSE;
                msg.obj = n2MVideo;

                handler.sendMessage(msg);

            }
            @Override
            public void onShareScreen(N2MVideo n2MVideo)
            {
                Message msg = new Message();
                msg.what = SCREEN_OPEN;
                msg.obj = n2MVideo;
                handler.sendMessage(msg);
            }
            @Override
            public void onCloseScreen(int result, int nodeId, String deviceId)
            {
                N2MVideo n2MVideo = new N2MVideo(nodeId,deviceId, true);
                Message msg = new Message();
                msg.what = SCREEN_CLOSE;
                msg.obj = n2MVideo;
                handler.sendMessage(msg);
            }
            @Override
            public void onRequestOpenVideo(int nodeId, String deviceId) {

            }
        });
    }

    public VideoService getVideoService() {
        return videoService;
    }

    public ChatService getChatService() {
        return chatService;
    }

    public RoomService getRoomService() {
        return roomService;
    }

    public void refreshVideoFragmentUI(){
        videoFragment.refreshVideoWindows();
        // back to latest UI
        onBackPressed();
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
            N2MVideo n2MVideo;
            switch (msg.what){
                case VIDEO_OPEN:
                    n2MVideo = (N2MVideo)msg.obj;
                    videoFragment.refreshVideoWindows();
                    // update Setup Fragment UI
                    if(n2MVideo.getNodeId() == roomService.getMe().getNodeId()){
                        setupFragment.showCloseLocalVideoOnVideoSwitch();
                    }

                    break;
                case VIDEO_CLOSE:
                    n2MVideo = (N2MVideo)msg.obj;
                    videoFragment.refreshVideoWindows();
                    if(n2MVideo.getNodeId() == roomService.getMe().getNodeId()){
                        setupFragment.showOpenLocalVideoOnVideoSwitch();
                    }
                    break;
                case SCREEN_OPEN:
                    n2MVideo = (N2MVideo)msg.obj;
                    videoFragment.refreshVideoWindows();;
                    if(n2MVideo.getNodeId() == roomService.getMe().getNodeId()){
                        setupFragment.showCloseLocalVideoOnVideoSwitch();
                    }

                    break;
                case SCREEN_CLOSE:
                    n2MVideo = (N2MVideo)msg.obj;
                    videoFragment.refreshVideoWindows();;
                    if(n2MVideo.getNodeId() == roomService.getMe().getNodeId()){
                        setupFragment.showOpenLocalVideoOnVideoSwitch();
                    }
                    break;
                case VIDEO_STATUS:
                    boolean isOpen = (Boolean)msg.obj;
                    videoFragment.setAudioStatus(isOpen, msg.arg1);
                    break;
            }
        }
    };

    public void showVideoSelectFragment() {
        FragmentUtil.moveToRightFragment(this, R.id.mainScreenLinearLayout, videoSelectFragment);
    }

    private class ChatNumberRefreshTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... params) {
            chatNumber++;
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
//            super.onPostExecute(aVoid);
            chatNumberText.setText(chatNumber + "");
            chatNumberLayout.setVisibility(View.VISIBLE);
        }
    }
}
