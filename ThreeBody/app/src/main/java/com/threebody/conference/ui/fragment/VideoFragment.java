package com.threebody.conference.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.threebody.conference.R;
import com.threebody.conference.ui.view.video.LocalVideoView;

/**
 * Created by xiaxin on 15-1-14.
 */
public class VideoFragment extends BaseFragment {
    VideoShowFragmenet videoUp;
    VideoShowFragmenet videoDown;
    LocalVideoView videoView;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video, null);
        initView(view);
        return view;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        videoUp = new VideoShowFragmenet();
        videoDown = new VideoShowFragmenet();
        videoView = new LocalVideoView(getActivity());
        videoUp.getFlVideo().addView(videoView);

        videoView.startCamera();
    }
}
