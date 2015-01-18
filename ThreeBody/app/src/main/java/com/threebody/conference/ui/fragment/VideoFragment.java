package com.threebody.conference.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.threebody.conference.R;
import com.threebody.conference.ui.BaseActivity;
import com.threebody.conference.ui.util.FragmentUtil;
import com.threebody.conference.ui.view.video.LocalVideoView;
import com.threebody.sdk.domain.VideoBean;

/**
 * Created by xiaxin on 15-1-14.
 */
public class VideoFragment extends BaseFragment {
    VideoShowFragmenet videoUp;
    VideoShowFragmenet videoDown;
    LocalVideoView localVideo;
    LocalVideoView videoView;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video, null);
        initView(view);
        return view;
    }

    @Override
    protected void initView(final View view) {
        super.initView(view);
        videoUp = (VideoShowFragmenet)getChildFragmentManager().findFragmentById(R.id.videoUp);
        videoDown = (VideoShowFragmenet)getChildFragmentManager().findFragmentById(R.id.videoDown);

//        videoView = new RemoteVideoView(getActivity());
        videoView = new LocalVideoView(getActivity());
        videoView.resetSize(LocalVideoView.WIDTH, LocalVideoView.HEIGHT);
        videoDown.getFlVideo().addView(videoView);

//        localVideo = new LocalVideoView(getActivity());
//        videoDown.getFlVideo().addView(localVideo);
//        localVideo.startCamera();
        videoDown.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentUtil.moveToRightFragment((BaseActivity)getActivity(), R.id.llContainer, new VideoSetFragment());
            }
        });
    }
    public void setLocalData(VideoBean videoBean){
        videoView.setVideoBean(videoBean);
    }
    public void setLocalSize(int width, int height){
        videoView.resetSize(width, height);
    }
}
