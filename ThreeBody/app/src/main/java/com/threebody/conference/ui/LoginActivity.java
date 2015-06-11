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
import com.threebody.sdk.http.LoginHandle;
import com.threebody.sdk.http.entity.LoginRequest;
import com.threebody.sdk.http.entity.LoginResponse;
import com.threebody.sdk.listener.LoginListener;
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
//                if(!TextViewUtil.isNullOrEmpty(etPassword)){
//                    ToastUtil.showToast(this, R.string.noPswd);
//                    return;
//                }
                joinConference();
                break;
            default:
                break;
        }
    }

    private void joinConference(){
        if(dialog == null){
            dialog = new HttpProgressDialog("");
        }
        getSupportFragmentManager().beginTransaction().add(dialog,"").commit();
//        final String num = etNum.getText().toString().trim();
//        String name = etName.getText().toString().trim();
//        String password = etPassword.getText().toString().trim();
        final String num = etNum.getText().toString();

        final String name = etName.getText().toString();
        final String password = etPassword.getText().toString();
        RoomSystem.initializeAndroidGlobals(this, true, true);
        RoomSystem.setVideoOptions(640,480,10);
        RoomSystem.logEnable(true);
//        STSystem.getInstance().initializeAndroidGlobals(this);
        final LoginRequest request = new LoginRequest(name, password);
        new LoginHandle(new LoginListener() {
            @Override
            public void onLoginResult(LoginResponse result) {
                if(result.getRet() == 0){

//                }
                    String url = result.getRoom_uri().substring(0, result.getRoom_uri().indexOf("/"));

                    LoggerUtil.info(getClass().getName(), "url = "+url);
//                    String url = "192.168.1.108:20009";
//                    String token = result.getAccess_tocken();
//                    String url = result.getRoom_uri();
                    String token = result.getAccess_tocken();
                    if(!IS_SYSTEM_INIT){
                        STSystem.getInstance().init(new STSystem.ConferenceSystemCallback() {
                            @Override
                            public void onInitResult(int result) {
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

                                //STSystem.getInstance().getRoomSystem().ArrangeRoom(info);
                                // STSystem.getInstance().getRoomSystem().QueryRoomByExtendId("3213r5255");
                                // STSystem.getInstance().getRoomSystem().CancelRoom("r593");
                                // STSystem.getInstance().getRoomSystem().QueryRoom("r595");
                                LoggerUtil.info(getClass().getName(), "result = " + result);
                                IS_SYSTEM_INIT = true;
                                roomCommon = new RoomCommonImpl(new RoomCommon.JoinResultListener() {
                                    @Override
                                    public void onJoinResult(int result) {
                                        LoggerUtil.info(getClass().getName(), "join result = " + result);
                                        Intent intent = new Intent();
                                        intent.setClass(LoginActivity.this, MeetingActivity.class);
                                        startActivity(intent);
                                        finish();
                                        org.st.User user =  roomCommon.getRoom().getUser("12221");
                                        roomCommon.getRoom().getRoomID();
                                        roomCommon.getRoom().getRoomName();
                                    }
                                }, num);

                                STSystem.getInstance().createRoom(roomCommon);
                                UUID id = UUID.randomUUID();
                                //roomCommon.join("12221", name, password);
                                roomCommon.join(id.toString(), name, password);
                            }
                       //}, "192.168.2.2:8080", "demo_access","demo_secret");
                            //},"60.191.94.115:8090" , "demo_access","demo_secret");

                         },url, "demo_access","demo_secret");

                    }
//                    else{
//                        roomCommon = (RoomCommonImpl)STSystem.getInstance().findCommonById(num);
//                        if(roomCommon == null){
//                            roomCommon = new RoomCommonImpl(new RoomCommon.JoinResultListener() {
//                                @Override
//                                public void onJoinResult(int result) {
//                                    LoggerUtil.info(getClass().getName(), "join result = "+result);
//                                    Intent intent = new Intent();
//                                    intent.setClass(LoginActivity.this, MeetingActivity.class);
//                                    startActivity(intent);
//                                    finish();
//                                }
//
//                 }, num);
//                        }
//                        STSystem.getInstance().createRoom(roomCommon);
//                        roomCommon.join("11221", name, password);
//                    }

                }
            }
        }).login(request);
    }
    //    @Ovde
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_login, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}
