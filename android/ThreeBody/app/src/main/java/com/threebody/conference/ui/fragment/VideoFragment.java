package com.threebody.conference.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.threebody.conference.R;
import com.threebody.conference.ui.MeetingActivity;
import com.threebody.sdk.common.VideoCommon;
import com.threebody.sdk.domain.DeviceBean;

import org.webrtc.VideoRenderer;

import java.util.List;

/**
 * Created by xiaxin on 15-1-14.
 */
public class VideoFragment extends BaseFragment {
    VideoShowGLFrameLayout upperVideoLayout;
    VideoShowGLFrameLayout lowerVideoFragment;
    DeviceBean deviceUpper, deviceLower;
    DeviceBean device1, device2;
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
            lowerVideoFragment.setDevice(deviceLower);
        }

    }

    @Override
    protected void initView(final View view) {
        super.initView(view);
        upperVideoLayout = (VideoShowGLFrameLayout)view.findViewById(R.id.videoUp);
        lowerVideoFragment = (VideoShowGLFrameLayout)view.findViewById(R.id.videoDown);
        lowerVideoFragment.setOnLongClickListener(new View.OnLongClickListener() {
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

    private boolean Eq(DeviceBean device1, DeviceBean device2){
        if (device1 == null && device2 != null){
            return false;
        }else if ((device1 != null && device2 == null)){
            return  false;
        } else if (device2 != null
                && null != device1
                && device1.getDeviceId() == device2.getDeviceId()
                && device1.getNodeId() == device2.getNodeId()){
            return true;
        }
        else {
            return false;
        }
    }

    void closeShowDown(){
        if (lowerVideoFragment == null)
            return;
        lowerVideoFragment.removeVideoRender(videoCommon);
        lowerVideoFragment.setDevice(null);
        deviceLower = null;
    }
    void closeShowUp(){
        if (upperVideoLayout == null)
            return;
        upperVideoLayout.removeVideoRender(videoCommon);
        upperVideoLayout.setDevice(null);
        deviceUpper = null;
    }

    void changeShowUp(DeviceBean device){
        if (Eq(deviceUpper, device))
            return;
        closeShowUp();
        upperVideoLayout.setDevice(device);
        upperVideoLayout.setVideoRender(videoCommon);
        deviceUpper = device;
    }

    void changeShowDown(DeviceBean device){
        if (Eq(deviceUpper, device))
            return;
        closeShowDown();
        lowerVideoFragment.setDevice(device);
        lowerVideoFragment.setVideoRender(videoCommon);
        deviceLower = device;
    }
    boolean switchVideo(){

        if  (videoCommon.switchVideoRender(upperVideoLayout.mRenderer, lowerVideoFragment.mRenderer))
        {
            DeviceBean temp = deviceUpper;
            deviceUpper = deviceLower;
            deviceLower = temp;

            VideoRenderer mRenderer  = upperVideoLayout.mRenderer;

            upperVideoLayout.mRenderer = lowerVideoFragment.mRenderer;
            lowerVideoFragment.mRenderer = mRenderer;
            upperVideoLayout.setDevice(deviceUpper);
            lowerVideoFragment.setDevice(deviceLower);
            return true;
        }
        return false;
    }

    public synchronized void  refresh(List<DeviceBean> devices) {
        int i = 0;
        if(devices != null && !devices.isEmpty()){
            for (DeviceBean deviceBean : devices){
                if(deviceBean.isVideoChecked() ){
                    if(i == 0){
                        device1 = deviceBean;
                        i+=1;
                    }else if(i == 1){
                        i+=1;
                        device2 = deviceBean;
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
                upperVideoLayout.setStatus(isOpen);
                return;
            }
        }
        if(deviceLower != null && deviceLower.getUser() != null){
            if(deviceLower.getUser().getNodeId() == nodeId){
                lowerVideoFragment.setStatus(isOpen);
            }
        }

    }

    public void closeAll() {
        upperVideoLayout.removeVideoRender(videoCommon);
        lowerVideoFragment.removeVideoRender(videoCommon);
    }
}
