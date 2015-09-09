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
import com.threebody.sdk.service.VideoService;
import com.threebody.sdk.domain.N2MVideo;

import org.st.VideoRendererView;
import org.webrtc.VideoRenderer;

/**
 * Created by xiaxin on 2015/3/28.
 */
public class VideoWindow extends FrameLayout {
    View llVideoWindow;

    TextView tvUserName;
    ImageView ivAudioStatus;
    ProgressBar progressBar;

    // biz objects
    VideoService videoService;

    public N2MVideo getVideo() {
        return video;
    }

    // when N2MVideo and VideoRenderer objects are connected, the video show up
    N2MVideo video;
    VideoRenderer mRenderer;
    GLSurfaceView glView;
    VideoRendererView mRendererView;

    public VideoWindow(Context context) {
        super(context);
        init();
    }

    public VideoWindow(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public VideoWindow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        // the container for all views inside llVideoFragment window
        llVideoWindow = LayoutInflater.from(getContext()).inflate(R.layout.video_window, null);

        tvUserName = (TextView) llVideoWindow.findViewById(R.id.tvUserName);
        ivAudioStatus = (ImageView) llVideoWindow.findViewById(R.id.ivAudioStatus);

        progressBar = (ProgressBar) llVideoWindow.findViewById(R.id.progressBar);
        glView = (GLSurfaceView) llVideoWindow.findViewById(R.id.gl_view);
        addView(llVideoWindow);
        if (video != null) {
            show();
        }
        initVideo();
    }

    private void initVideo() {
        if (mRendererView == null) {
            mRendererView = new VideoRendererView(glView, true);
            mRendererView.setScalingType(VideoRendererView.ScalingType.Scale_Aspect_Full);
            mRenderer = new VideoRenderer(mRendererView.getRendererCallback());
        }
    }


    public void show() {

        if (video == null) {
            // could be detached or never attached.
            tvUserName.setText("");
            ivAudioStatus.setVisibility(INVISIBLE);
            return;
        } else {

            // show the whole window

            llVideoWindow.setVisibility(View.VISIBLE);
            // show user name
            tvUserName.setText(video.getUser().getUserName());

            // show audio icon
            ivAudioStatus.setVisibility(VISIBLE);
            if (video.getUser().isAudioOn()) {
                ivAudioStatus.setImageResource(R.drawable.status_sound);
            } else {
                ivAudioStatus.setImageResource(R.drawable.status_soundoff);
            }
        }
    }

    public void setAudioStatusIcon(boolean isOpen) {
        if (isOpen) {
            ivAudioStatus.setImageResource(R.drawable.status_sound);
        } else {
            ivAudioStatus.setImageResource(R.drawable.status_soundoff);
        }
    }

    public VideoRenderer getRenderer() {
        return mRenderer;
    }

    //// TODO: 2015/8/29  why not on VideoCommon object ?
    public void removeVideoRender() {
        if (video != null) {
            if (video.isScreen()) {
                videoService.removeScreenRender(video.getNodeId(), video.getDeviceId(), mRenderer);
            } else {
                videoService.removeVideoRender(video.getNodeId(), video.getDeviceId(), mRenderer);
            }
            mRendererView.refresh();
        }
    }


    public void setVideoService(VideoService videoService) {
        this.videoService = videoService;
    }

    public boolean isVideoAttached() {
        return video != null;
    }

    public void attachVideo(N2MVideo video) {
        this.video = video;
        videoService.attachRenderToVideo(video.getNodeId(), video.getDeviceId(), mRenderer);
    }

    public void detachVideo(N2MVideo video) {
        if (this.video == video) {
            videoService.removeVideoRender(video.getNodeId(), video.getDeviceId(), mRenderer);
            mRendererView.refresh();
            this.video = null;
        }
    }
}
