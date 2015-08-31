package com.threebody.conference.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.threebody.conference.R;
import com.threebody.conference.ui.MeetingActivity;
import com.threebody.sdk.common.VideoCommon;
import com.threebody.sdk.domain.N2MVideo;

import org.webrtc.VideoRenderer;

import java.util.List;

/**
 * Created by xiaxin on 15-1-14.
 */
public class VideoFragment extends BaseFragment {
    VideoShowGLFrameLayout upperVideoLayout;
    VideoShowGLFrameLayout lowerVideoLayout;
    N2MVideo deviceUpper, deviceLower;
    N2MVideo device1, device2;
    VideoCommon videoCommon;
    boolean isCanShow = true;
    View view ;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view == null){
             view = inflater.inflate(R.layout.fragment_video, null);
            initView(view);
        }else{
            ViewGroup vg = (ViewGroup)view.getParent();
            if(vg != null){
                vg.removeAllViewsInLayout();
            }
        }
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(deviceUpper != null){
            upperVideoLayout.setDevice(deviceUpper);
        }
        if(deviceLower != null){
            lowerVideoLayout.setDevice(deviceLower);
        }

    }

    @Override
    protected void initView(final View view) {
        super.initView(view);
        upperVideoLayout = (VideoShowGLFrameLayout)view.findViewById(R.id.videoUp);
        lowerVideoLayout = (VideoShowGLFrameLayout)view.findViewById(R.id.videoDown);
        lowerVideoLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ((MeetingActivity) getActivity()).changeToVideoSet();
                return true;
            }
        });
        upperVideoLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                switchVideo();
                return true;
            }
        });
        videoCommon = ((MeetingActivity)getActivity()).getVideoCommon();
    }
    @Override
    public void onStart() {
        super.onStart();
        isCanShow = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        isCanShow = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isCanShow = true;
    }

    void closeShowDown(){
        if (lowerVideoLayout == null)
            return;
        lowerVideoLayout.removeVideoRender(videoCommon);
        lowerVideoLayout.setDevice(null);
        deviceLower = null;
    }
    void closeShowUp(){
        if (upperVideoLayout == null)
            return;
        upperVideoLayout.removeVideoRender(videoCommon);
        upperVideoLayout.setDevice(null);
        deviceUpper = null;
    }

    void changeShowUp(N2MVideo device){
        if (deviceUpper == device)
            return;
        closeShowUp();
        upperVideoLayout.setDevice(device);
        upperVideoLayout.setVideoRender(videoCommon);
        deviceUpper = device;
    }

    void changeShowDown(N2MVideo device){
        if (deviceUpper == device)
            return;
        closeShowDown();
        lowerVideoLayout.setDevice(device);
        lowerVideoLayout.setVideoRender(videoCommon);
        deviceLower = device;
    }
    boolean switchVideo(){

        if  (videoCommon.switchVideoRender(upperVideoLayout.mRenderer, lowerVideoLayout.mRenderer))
        {
            N2MVideo temp = deviceUpper;
            deviceUpper = deviceLower;
            deviceLower = temp;

            VideoRenderer mRenderer  = upperVideoLayout.mRenderer;

            upperVideoLayout.mRenderer = lowerVideoLayout.mRenderer;
            lowerVideoLayout.mRenderer = mRenderer;
            upperVideoLayout.setDevice(deviceUpper);
            lowerVideoLayout.setDevice(deviceLower);
            return true;
        }
        return false;
    }

    public synchronized void  refresh(List<N2MVideo> devices) {
        int i = 0;
        if(devices != null && !devices.isEmpty()){
            for (N2MVideo n2MVideo : devices){
                if(n2MVideo.isVideoChecked() ){
                    if(i == 0){
                        device1 = n2MVideo;
                        i+=1;
                    }else if(i == 1){
                        i+=1;
                        device2 = n2MVideo;
                    }
                }
            }
        }
        if (i == 0){
            closeShowUp();
            closeShowDown();
        }else if (i == 1){
            closeShowDown();
            changeShowUp(device1);
        }else if (i  == 2){
            changeShowUp(device1);
            changeShowDown(device2);
        }
    }


    public void setAudioStatus(boolean isOpen, int nodeId){
        if(deviceUpper != null && deviceUpper.getUser() != null){
            if(deviceUpper.getUser().getNodeId() == nodeId){
                upperVideoLayout.setAudioStatusIcon(isOpen);
                return;
            }
        }
        if(deviceLower != null && deviceLower.getUser() != null){
            if(deviceLower.getUser().getNodeId() == nodeId){
                lowerVideoLayout.setAudioStatusIcon(isOpen);
            }
        }

    }

    public void closeAll() {
        upperVideoLayout.removeVideoRender(videoCommon);
        lowerVideoLayout.removeVideoRender(videoCommon);
    }
}
