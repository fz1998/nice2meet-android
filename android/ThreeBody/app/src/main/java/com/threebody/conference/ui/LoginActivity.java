package com.threebody.conference.ui;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.threebody.conference.R;
import com.threebody.conference.ui.util.TextViewUtil;
import com.threebody.conference.ui.util.ToastUtil;
import com.threebody.conference.ui.view.HttpProgressDialog;
import com.threebody.sdk.common.RoomCommon;
import com.threebody.sdk.common.STSystem;
import com.threebody.sdk.common.impl.RoomCommonImpl;
import com.threebody.sdk.util.LoggerUtil;

import org.st.RoomInfo;
import org.st.RoomSystem;

import java.util.UUID;

import butterknife.InjectView;


public class LoginActivity extends BaseActivity {
    public static boolean IS_SYSTEM_INIT = false;
    @InjectView(R.id.etNum)EditText etNum;
    @InjectView(R.id.etName)EditText etName;
    @InjectView(R.id.etPassword)EditText etPassword;
    @InjectView(R.id.btnAddIn)Button btnAddIn;
    RoomCommonImpl roomCommon ;
    RoomCommonImpl roomCommon2 ;
    HttpProgressDialog dialog;
    @Override
    protected void initUI() {
        setContentView(R.layout.activity_login);
        super.initUI();
        getSupportActionBar().hide();
        btnAddIn.setOnClickListener(this);
        String defaultName = android.os.Build.MODEL + ":"
                + android.os.Build.VERSION.SDK + "_"
                + android.os.Build.VERSION.RELEASE;
        etNum.setText("");
        etName.setText(defaultName);
        etPassword.setText("admin");
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.btnAddIn:

                if(!TextViewUtil.isNullOrEmpty(etNum)){
                    ToastUtil.showToast(this, R.string.noMeetNum);
                    return;
                }
                if(!TextViewUtil.isNullOrEmpty(etName)){
                    ToastUtil.showToast(this, R.string.noUser);
                    return;
                }

                joinConference();
                break;
            default:
                break;
        }
    }

    private void joinConference() {
        if (dialog == null) {
            dialog = new HttpProgressDialog("");
        }
        getSupportFragmentManager().beginTransaction().add(dialog, "").commit();
//        final String num = etNum.getText().toString().trim();
//        String name = etName.getText().toString().trim();
//        String password = etPassword.getText().toString().trim();
        final String num = etNum.getText().toString();
        final String userId = UUID.randomUUID().toString();
        final String name = etName.getText().toString();
        final String password = etPassword.getText().toString();
        RoomSystem.initializeAndroidGlobals(this, true, true);
        RoomSystem.setVideoOptions(640, 480, 10);
        RoomSystem.logEnable(true);


        if (true) {
            STSystem.getInstance().init(new STSystem.ConferenceSystemCallback() {
                @Override
                public void onInitResult(int result) {
                    if (result != 0) {
                        return;
                    }

                    org.st.RoomInfo info = new RoomInfo();
                    //free 1, host 2, p2pfree3,
                    info.setRoomMode(1);
                    //info.setHostID("oxx");
                    info.setOwnerID("1002");
                    info.setExtendID("3213r5255");
                    info.setRoomName("woxx");
                    info.setMaxAttendee(1000);
                    info.setMaxAudio(5);
                    info.setMaxVideo(5);

                    STSystem.getInstance().getRoomSystem().ArrangeRoom(info);

                    STSystem.getInstance().getRoomSystem().QueryRoomByExtendId("3213r5255");
                    // STSystem.getInstance().getRoomSystem().CancelRoom("r593");
                    // STSystem.getInstance().getRoomSystem().QueryRoom("r595");
                    LoggerUtil.info(getClass().getName(), "result = " + result);
                    IS_SYSTEM_INIT = true;
                    roomCommon = new RoomCommonImpl(new RoomCommon.JoinResultListener() {
                        @Override
                        public void onJoinResult(int result) {
                            if (0 == result) {
                                LoggerUtil.info(getClass().getName(), "join result = " + result);
                                Intent intent = new Intent();
                                intent.setClass(LoginActivity.this, MeetingActivity.class);
                                startActivity(intent);
                                finish();

                                roomCommon.getRoom().getRoomID();
                                roomCommon.getRoom().getRoomName();
                            }
                        }
                    }, num);

                    STSystem.getInstance().createRoom(roomCommon);
                    //STSystem.getInstance().unInit();
                    //roomCommon.join("12221", name, password);
                    roomCommon.join(userId, name, password);

                    //}, "192.168.2.2:8080", "demo_access","demo_secret");
                    // },"60.191.94.115:9080" , "demo_access","demo_secret");
                    //}, " 121.201.103.241:8080", "YzJlMzIyYWViNTEwYTlkOTY1Y2FkMmVlYzM0YmQyNWVkZTIzNDgzOA==");
                }
                }, "121.41.119.216:8080", "demo_access", "demo_secret");
//            },"60.191.94.115:8090"  , "demo_access", "demo_secret");
        }

    }
}
