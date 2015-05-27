package com.threebody.conference.ui.fragment;

import android.content.Context;
import android.graphics.Point;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.threebody.conference.R;
import com.threebody.sdk.common.VideoCommon;
import com.threebody.sdk.domain.DeviceBean;
import com.threebody.sdk.domain.VideoBean;

import org.st.VideoRendererView;
import org.webrtc.VideoRenderer;

import butterknife.InjectView;

/**
 * Created by xiaxin on 2015/3/28.
 */
public class VideoShowGLFragment extends FrameLayout{
    TextView tvUserName;
    LinearLayout llFlFragment;
    ImageView ivVideoStatus;
    ImageView ivAudioStatus;
    FrameLayout flVideo;
    ProgressBar progressBar;
    GLSurfaceView glView;
    DeviceBean deviceBean;
    VideoRendererView mRendererView;
    VideoRenderer mRenderer;
    View view;

    public VideoShowGLFragment(Context context) {
        super(context);
        init();
    }

    public VideoShowGLFragment(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public VideoShowGLFragment(Context context, AttributeSet attrs, int defStyleAttr) {
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
    //    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        if(view == null){
//            view = inflater.inflate(R.layout.fragment_video_view, container, false);
//            initView(view);
//            initVideo();
//        }else{
//            ViewGroup vg = (ViewGroup)view.getParent();
//            if(vg != null){
//                vg.removeAllViewsInLayout();
//            }
//        }
//
//
//        return view;
//    }
//
//    @Override
//    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        if(deviceBean != null){
//            showVideoLayout();
//        }
//    }
//
//    @Override
//    protected void initView(View view) {
//        super.initView(view);
//    }
    private  void initVideo(){
       if(mRendererView == null){
           mRendererView = new VideoRendererView(glView, true);
           mRenderer = new VideoRenderer(mRendererView.getRendererCallback());
       }
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
//        videoView.setVideoBean(videoBean);
    }
    public void setDevice(DeviceBean device){
//        Message msg = new Message();
//        msg.what = VideoCommon.NEW_DEVICE;
        deviceBean = device;
//        handler.sendMessage(msg);

        if(llFlFragment != null && deviceBean != null){
            showVideoLayout();
        }
    }
    public void openVideo(DeviceBean device){

    }
    public void setVideoRender(VideoCommon videoCommon){
        videoCommon.setVideoRender(deviceBean.getNodeId(), getRenderer());
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
            videoCommon.removeVideoRender(deviceBean.getNodeId(), mRenderer);
            //mRenderer.dispose();
        }
    }

    public VideoRendererView getmRendererView() {
        return mRendererView;
    }
    public void onStop(){
        mRendererView.onStop();
    }
    public void onResume(){
        mRendererView.onRsume();
    }
}
