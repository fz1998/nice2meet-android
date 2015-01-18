package com.threebody.conference.ui;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.threebody.conference.R;
import com.threebody.conference.ui.util.TextViewUtil;
import com.threebody.conference.ui.util.ToastUtil;
import com.threebody.sdk.http.entity.LoginRequest;

import butterknife.InjectView;


public class LoginActivity extends BaseActivity {
    @InjectView(R.id.etNum)EditText etNum;
    @InjectView(R.id.etName)EditText etName;
    @InjectView(R.id.etPassword)EditText etPassword;
    @InjectView(R.id.btnAddIn)Button btnAddIn;

    @Override
    protected void initUI() {
        setContentView(R.layout.activity_login);
        super.initUI();
        getSupportActionBar().hide();
        btnAddIn.setOnClickListener(this);
        etNum.setText("123456");
        etName.setText("admin");
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

        String num = etNum.getText().toString().trim();
//        String name = etName.getText().toString().trim();
//        String password = etPassword.getText().toString().trim();
        String name = "admin";
        String password = "admin";
        LoginRequest request = new LoginRequest(name, password);
//        new LoginHandle(new OnJoinConferenceListener() {
//            @Override
//            public void onJoinResult(int result) {
//                if(result == 0){
                    Intent intent = new Intent();
                    intent.setClass(LoginActivity.this, MeetingActivity.class);
                    startActivity(intent);
//                }
//            }
//        }).joinConference(request);
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
