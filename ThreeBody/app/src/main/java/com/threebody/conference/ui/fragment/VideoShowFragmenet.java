package com.threebody.conference.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.threebody.conference.R;
import com.threebody.conference.ui.view.video.RemoteVideoView;
import com.threebody.sdk.domain.DeviceBean;
import com.threebody.sdk.domain.VideoBean;

import butterknife.InjectView;

/**
 * Created by xiaxin on 15-1-14.
 */
public class VideoShowFragmenet extends BaseFragment {
    @InjectView(R.id.tvUserName)TextView tvUserName;
    @InjectView(R.id.flVideoFragment)LinearLayout llFlFragment;
    @InjectView(R.id.ivVideoStatus)ImageView ivVideoStatus;
    @InjectView(R.id.ivAudioStatus)ImageView ivAudioStatus;
    @InjectView(R.id.flVideoView)FrameLayout flVideo;
    @InjectView(R.id.videoView)RemoteVideoView videoView;
    @InjectView(R.id.progressBar)ProgressBar progressBar;
    DeviceBean deviceBean;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video_view, null);
        initView(view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(deviceBean != null){
            showVideoLayout();
        }
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        initVideo();
    }
    private  void initVideo(){
        ViewTreeObserver vto = videoView.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                if(videoView.getWidth() != 0){
                    videoView.setLayoutParam(videoView.getWidth(), videoView.getHeight());
                    videoView.getViewTreeObserver().removeOnPreDrawListener(this);
                    return true;
                }
                return false;
            }
        });
    }

    private void initUser(){

    }
    public FrameLayout getFlVideo(){
        return flVideo;
    }
    public void open(){
//        videoView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }
    public void setVideoBean(VideoBean videoBean){
        videoView.setVideoBean(videoBean);
    }
    public void setDevice(DeviceBean device){
//        Message msg = new Message();
//        msg.what = VideoCommon.NEW_DEVICE;
            deviceBean = device;
//        handler.sendMessage(msg);

        if(llFlFragment != null){
            showVideoLayout();
        }
    }

    private void showVideoLayout(){
        llFlFragment.setVisibility(View.VISIBLE);
        tvUserName.setText(deviceBean.getUser().getUserName());
        if(deviceBean.getUser().getRole() != null){
            if(deviceBean.getUser().isAudioOn()){
                ivAudioStatus.setImageResource(R.drawable.status_sound);
            }else{
                ivAudioStatus.setImageResource(R.drawable.status_soundoff);
            }
        }
    }

    public void setStatus(boolean isOpen) {
        if(isOpen){
            ivAudioStatus.setImageResource(R.drawable.status_sound);
        }else{
            ivAudioStatus.setImageResource(R.drawable.status_soundoff);
        }
    }
}
