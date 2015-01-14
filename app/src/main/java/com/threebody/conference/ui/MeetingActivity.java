package com.threebody.conference.ui;

import android.os.Bundle;

import com.threebody.conference.R;

/**
 * Created by xiaxin on 15-1-13.
 */
public class MeetingActivity extends BaseActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initUI() {
        setContentView(R.layout.activity_meeting);
        super.initUI();
    }
}
