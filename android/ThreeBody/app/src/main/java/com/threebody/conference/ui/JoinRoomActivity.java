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
import com.threebody.sdk.util.LoggerUtil;

import java.util.UUID;

import butterknife.InjectView;
import cn.tee3.n2m.Constants;

public class JoinRoomActivity extends BaseActivity implements RoomCommon.JoinResultCallback {

    @InjectView(R.id.etNum)
    EditText etNum;
    @InjectView(R.id.etName)
    EditText etName;
    @InjectView(R.id.etPassword)
    EditText etPassword;
    @InjectView(R.id.btnAddIn)
    Button btnAddIn;

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

        //todo for testing, need to delete.
        joinConference();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btnAddIn:

                if (!TextViewUtil.isNullOrEmpty(etNum)) {
                    ToastUtil.showToast(this, R.string.noMeetNum);
                    return;
                }
                if (!TextViewUtil.isNullOrEmpty(etName)) {
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


        //todo for testing, need to recover.
//        final String num = etNum.getText().toString();
        final String num = Constants.ROOM_NUMBER;

        final String userId = UUID.randomUUID().toString();
        final String name = etName.getText().toString();
        final String password = etPassword.getText().toString();

        // obtain a room
        RoomCommon roomCommon = STSystem.getInstance().obtainRoom(num);

        // set join result listener
        roomCommon.setJoinResultCallback(this);

        // join
        roomCommon.join(userId, name, password);
    }


    @Override
    public void onJoinResult(int result) {
        if (0 == result) {
            LoggerUtil.info(getClass().getName(), "join result = " + result);
            Intent intent = new Intent();
            intent.setClass(JoinRoomActivity.this, MeetingActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
