package com.threebody.conference.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.threebody.conference.R;
import com.threebody.sdk.domain.User;

import butterknife.InjectView;

/**
 * Created by xiaxin on 15-1-14.
 */
public class VideoShowFragmenet extends BaseFragment {
    @InjectView(R.id.tvUserName)TextView tvUserName;
    @InjectView(R.id.ivVideoStatus)ImageView ivVideoStatus;
    @InjectView(R.id.ivAudioStatus)ImageView ivAudioStatus;
    @InjectView(R.id.flVideoView)FrameLayout flVideo;
    User user;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video_view, null);
        initView(view);
        return view;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
    }
    public void setUser(User user){
        this.user = user;
        initUser();
    }
    private void initUser(){

    }
    public FrameLayout getFlVideo(){
        return flVideo;
    }
}
