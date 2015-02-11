package com.threebody.conference.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.threebody.sdk.common.VideoCommon;
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
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video_view, null);
        initView(view);
        return view;
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
        Message msg = new Message();
        msg.what = VideoCommon.NEW_DEVICE;
        msg.obj = device;
        handler.sendMessage(msg);
    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case VideoCommon.NEW_DEVICE:
                    llFlFragment.setVisibility(View.VISIBLE);
                    DeviceBean device = (DeviceBean)msg.obj;
                    tvUserName.setText(device.getUser().getUserName());
                    if(device.getUser().isAudioOn()){
                        ivAudioStatus.setImageResource(R.drawable.status_sound);
                    }else{
                        ivAudioStatus.setImageResource(R.drawable.status_soundoff);
                    }
                    break;
            }
        }
    };
}
