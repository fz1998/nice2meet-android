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
import com.threebody.sdk.domain.N2MVideo;

import org.st.VideoRendererView;
import org.webrtc.VideoRenderer;

/**
 * Created by xiaxin on 2015/3/28.
 */
public class VideoShowGLFrameLayout extends FrameLayout {
    TextView tvUserName;
    LinearLayout view_window_ll;
    ImageView ivAudioStatus;
//    FrameLayout flVideo;
    ProgressBar progressBar;

    N2MVideo n2MVideo;

    GLSurfaceView glView;
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
        view = LayoutInflater.from(getContext()).inflate(R.layout.video_window, null);
        tvUserName = (TextView)view.findViewById(R.id.tvUserName);
        view_window_ll = (LinearLayout)view.findViewById(R.id.Video_window_ll);
        ivAudioStatus = (ImageView)view.findViewById(R.id.ivAudioStatus);
//        flVideo = (FrameLayout)view.findViewById(R.id.flVideo_btn);

        progressBar = (ProgressBar)view.findViewById(R.id.progressBar);
        glView = (GLSurfaceView)view.findViewById(R.id.video_window_gl_view);
        addView(view);
        if(n2MVideo != null){
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

    public void setDevice(N2MVideo device){

        n2MVideo = device;
        if(view_window_ll != null && n2MVideo != null){
            showVideoLayout();
        }
    }

    public void setVideoRender(VideoCommon videoCommon){
        if (n2MVideo.isScreen()){
            videoCommon.setScreenRender(n2MVideo.getNodeId(), n2MVideo.getDeviceId(), mRenderer);
        }
        else {
            videoCommon.setVideoRender(n2MVideo.getNodeId(), n2MVideo.getDeviceId(), getRenderer());
        }
    }
    private void showVideoLayout(){
        // show video
        view_window_ll.setVisibility(View.VISIBLE);
        // show user name
        tvUserName.setText(n2MVideo.getUser().getUserName());
        // show audio icon
        if(n2MVideo.getUser().getRole() != null){
            if(n2MVideo.getUser().isAudioOn()){
                ivAudioStatus.setImageResource(R.drawable.status_sound);
            }else{
                ivAudioStatus.setImageResource(R.drawable.status_soundoff);
            }
        }
    }

    public void setAudioStatusIcon(boolean isOpen) {
        if(isOpen){
            ivAudioStatus.setImageResource(R.drawable.status_sound);
        }else{
            ivAudioStatus.setImageResource(R.drawable.status_soundoff);
        }
    }

    public VideoRenderer getRenderer() {
        return mRenderer;
    }

    //// TODO: 2015/8/29  why not on VideoCommon object ?
    public void removeVideoRender(VideoCommon videoCommon){
        if(n2MVideo != null){
            if (n2MVideo.isScreen()){
                videoCommon.removeScreenRender(n2MVideo.getNodeId(), n2MVideo.getDeviceId(), mRenderer);
            }
            else {
                videoCommon.removeVideoRender(n2MVideo.getNodeId(), n2MVideo.getDeviceId(),mRenderer);
            }
            mRendererView.refresh();
        }
    }
}
