package com.threebody.conference.ui.fragment;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
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
    View llVideoWindow;

    TextView tvUserName;
    ImageView ivAudioStatus;
    ProgressBar progressBar;

    // biz objects
    VideoCommon videoCommon;

    // when N2MVideo and VideoRenderer objects are connected, the video show up
    N2MVideo n2MVideo;
    VideoRenderer mRenderer;
    GLSurfaceView glView;
    VideoRendererView mRendererView;

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
        // the container for all views inside llVideoFragment window
        llVideoWindow = LayoutInflater.from(getContext()).inflate(R.layout.video_window, null);

        tvUserName = (TextView) llVideoWindow.findViewById(R.id.tvUserName);
        ivAudioStatus = (ImageView) llVideoWindow.findViewById(R.id.ivAudioStatus);

        progressBar = (ProgressBar) llVideoWindow.findViewById(R.id.progressBar);
        glView = (GLSurfaceView) llVideoWindow.findViewById(R.id.v_gl_video_window);
        addView(llVideoWindow);
        if(n2MVideo != null){
            showVideoWindow();
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

    public void setVideoDeviceAndShowVideoWindow(N2MVideo device){

        // save the device
        n2MVideo = device;

        // to connect video render with the device, from this point, the video could be seen
        if (device != null) {
            setVideoRender();
        } else {
            removeVideoRender();
        }

        // show video on the screen
        if(llVideoWindow != null && n2MVideo != null){
            showVideoWindow();
        }
    }

    private void setVideoRender(){
        if (n2MVideo.isScreen()){
            videoCommon.setScreenRender(n2MVideo.getNodeId(), n2MVideo.getDeviceId(), mRenderer);
        }
        else {
            videoCommon.setVideoRender(n2MVideo.getNodeId(), n2MVideo.getDeviceId(), getRenderer());
        }
    }

    public void showVideoWindow(){
//    private void showVideoWindow(){
        // show video
        llVideoWindow.setVisibility(View.VISIBLE);
        // show user name
        tvUserName.setText(n2MVideo.getUser().getUserName());
        // show audio icon
//        if(n2MVideo.getUser().getRole() != null){
            if(n2MVideo.getUser().isAudioOn()){
                ivAudioStatus.setImageResource(R.drawable.status_sound);
            }else{
                ivAudioStatus.setImageResource(R.drawable.status_soundoff);
            }
//        }
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
    public void removeVideoRender(){
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


    public void setVideoCommon(VideoCommon videoCommon) {
        this.videoCommon = videoCommon;
    }

}
