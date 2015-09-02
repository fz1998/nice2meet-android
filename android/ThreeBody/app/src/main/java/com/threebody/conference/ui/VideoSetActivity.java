package com.threebody.conference.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.threebody.conference.R;
import com.threebody.conference.ui.fragment.VideoSelectFragment;
import com.threebody.conference.ui.util.Constans;

/**
 * Created by xiaxin on 15-1-17.
 */
public class VideoSetActivity extends BaseActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
    }

    @Override
    protected void initUI() {
        setContentView(R.layout.activity_video_set);
        super.initUI();
        getSupportFragmentManager().beginTransaction().add(R.id.videoSetContainer, new VideoSelectFragment()).commit();
    }

    @Override
    public void onBackPressed() {
        Intent data = new Intent();
        data.putExtra(Constans.DATA, true);
        setResult(Activity.RESULT_OK, data);
        super.onBackPressed();
    }
}
