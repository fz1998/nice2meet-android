package com.threebody.conference.ui;

import android.os.Bundle;

import com.threebody.conference.R;
import com.threebody.conference.ui.fragment.VideoSetFragment;

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
        getSupportFragmentManager().beginTransaction().add(R.id.videoSetContainer, new VideoSetFragment()).commit();
    }
}
