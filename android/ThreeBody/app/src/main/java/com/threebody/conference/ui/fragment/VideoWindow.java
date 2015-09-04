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
public class VideoWindow extends FrameLayout {
    View llVideoWindow;

    TextView tvUserName;
    ImageView ivAudioStatus;
    ProgressBar progressBar;

    // biz objects
    VideoCommon videoCommon;

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
        glView = (GLSurfaceView) llVideoWindow.findViewById(R.id.v_gl_video_window);
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

//    public void setVideoDeviceAndShowVideoWindow(N2MVideo device) {
//
//        // save the device
//        video = device;
//
//        // to connect video render with the device, from this point, the video could be seen
//        if (device != null) {
//            attachRenderToVideo();
//        } else {
//            removeVideoRender();
//        }
//
//        // show video on the screen
//        if (llVideoWindow != null && video != null) {
//            show();
//        }
//    }
//
//    private void attachRenderToVideo() {
//        if (video.isScreen()) {
//            videoCommon.setScreenRender(video.getNodeId(), video.getDeviceId(), mRenderer);
//        } else {
//            videoCommon.attachRenderToVideo(video.getNodeId(), video.getDeviceId(), getRenderer());
//        }
//    }

    public void show() {

        if (video == null) {
            // could be detached or never attached.
            tvUserName.setText("");
            return;
        } else {

            // show the whole window

            llVideoWindow.setVisibility(View.VISIBLE);

            // show user name
            tvUserName.setText(video.getUser().getUserName());

            // show audio icon
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
                videoCommon.removeScreenRender(video.getNodeId(), video.getDeviceId(), mRenderer);
            } else {
                videoCommon.removeVideoRender(video.getNodeId(), video.getDeviceId(), mRenderer);
            }
            mRendererView.refresh();
        }
    }


    public void setVideoCommon(VideoCommon videoCommon) {
        this.videoCommon = videoCommon;
    }

    public boolean isVideoAttached() {
        return video != null;
    }

    public N2MVideo getVideo() {
        return video;
    }

    public void attachVideo(N2MVideo video) {
        this.video = video;
        videoCommon.attachRenderToVideo(video.getNodeId(), video.getDeviceId(), mRenderer);
    }

    public void detachVideo(N2MVideo video) {
        if (this.video == video) {
            videoCommon.removeVideoRender(video.getNodeId(), video.getDeviceId(), mRenderer);
            mRendererView.refresh();
            this.video = null;
        }
    }
}
