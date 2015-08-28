package com.threebody.conference.ui.fragment;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.threebody.conference.R;
import com.threebody.sdk.common.VideoCommon;
import com.threebody.sdk.domain.DeviceBean;

import org.st.VideoRendererView;
import org.webrtc.VideoRenderer;

/**
 * Created by xiaxin on 2015/3/28.
 */
public class VideoShowGLFrameLayout extends FrameLayout {
    TextView tvUserName;
    LinearLayout llFlFragment;
    ImageView ivAudioStatus;
    FrameLayout flVideo;
    ProgressBar progressBar;
    GLSurfaceView glView;
    DeviceBean deviceBean;
    VideoRendererView mRendererView;
    VideoRenderer mRenderer;
    View view;

    public VideoShowGLFrameLayout(Context context) {
        super(context);
        init();
    }

    public VideoShowGLFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public VideoShowGLFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    private void init(){
        view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_video_view, null);
        tvUserName = (TextView)view.findViewById(R.id.tvUserName);
        llFlFragment = (LinearLayout)view.findViewById(R.id.flVideoFragment);
        ivAudioStatus = (ImageView)view.findViewById(R.id.ivAudioStatus);
        flVideo = (FrameLayout)view.findViewById(R.id.flVideo);
        progressBar = (ProgressBar)view.findViewById(R.id.progressBar);
        glView = (GLSurfaceView)view.findViewById(R.id.videoView);
        addView(view);
        if(deviceBean != null){
            showVideoLayout();
        }
         initVideo();
    }

    private  void initVideo(){
       if(mRendererView == null){
           mRendererView = new VideoRendererView(glView, true);
           mRendererView.setScalingType(VideoRendererView.ScalingType.Scale_Aspect_Full);
           mRenderer = new VideoRenderer(mRendererView.getRendererCallback());
       }
    }

    public void setDevice(DeviceBean device){

        deviceBean = device;
        if(llFlFragment != null && deviceBean != null){
            showVideoLayout();
        }
    }

    public void setVideoRender(VideoCommon videoCommon){
        if (deviceBean.isScreen()){
            videoCommon.setScreenRender(deviceBean.getNodeId(),deviceBean.getDeviceId(), mRenderer);
        }
        else {
            videoCommon.setVideoRender(deviceBean.getNodeId(), deviceBean.getDeviceId(), getRenderer());
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

    public VideoRenderer getRenderer() {
        return mRenderer;
    }

    public void removeVideoRender(VideoCommon videoCommon){
        if(deviceBean != null){
            if (deviceBean.isScreen()){
                videoCommon.removeSreenRender(deviceBean.getNodeId(),deviceBean.getDeviceId(), mRenderer);
            }
            else {
                videoCommon.removeVideoRender(deviceBean.getNodeId(), deviceBean.getDeviceId(),mRenderer);
            }
            mRendererView.refresh();
        }
    }
}
