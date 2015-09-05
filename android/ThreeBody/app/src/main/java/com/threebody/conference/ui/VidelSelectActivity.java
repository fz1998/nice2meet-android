package com.threebody.conference.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.threebody.conference.R;
import com.threebody.conference.ui.fragment.VideoSelectFragment;

import cn.tee3.n2m.Constants;

/**
 * Created by xiaxin on 15-1-17.
 */
public class VidelSelectActivity extends BaseActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
    }

    @Override
    protected void initUI() {
        setContentView(R.layout.activity_video_select);
        super.initUI();
        getSupportFragmentManager().beginTransaction().add(R.id.videoSetContainer, new VideoSelectFragment()).commit();
    }

    @Override
    public void onBackPressed() {
        Intent data = new Intent();
        data.putExtra(Constants.DATA, true);
        setResult(Activity.RESULT_OK, data);
        super.onBackPressed();
    }
}
