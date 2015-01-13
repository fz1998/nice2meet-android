package com.threebody.conference;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.threebody.conference.handle.LoginHandle;
import com.threebody.conference.listener.OnJoinConferenceListener;
import com.threebody.conference.util.TextViewUtil;

import butterknife.InjectView;


public class LoginActivity extends BaseActivity {
    @InjectView(R.id.etNum)EditText etNum;
    @InjectView(R.id.etName)EditText etName;
    @InjectView(R.id.etPassword)EditText etPassword;
    @InjectView(R.id.tvAddIn)TextView tvAddIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        tvAddIn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.tvAddIn:
                if(!TextViewUtil.isNullOrEmpty(etNum)){

                    return;
                }
                if(!TextViewUtil.isNullOrEmpty(etName)){

                    return;
                }
                if(!TextViewUtil.isNullOrEmpty(etPassword)){

                    return;
                }
                joinConference();
                break;
            default:
                break;
        }
    }
    private void joinConference(){
        String num = etNum.getText().toString().trim();
        String name = etName.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        new LoginHandle(new OnJoinConferenceListener() {
            @Override
            public void onJoinResult() {

            }
        }).joinConference(num, name, password);
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
