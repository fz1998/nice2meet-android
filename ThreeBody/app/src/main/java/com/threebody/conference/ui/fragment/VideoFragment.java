package com.threebody.conference.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.threebody.conference.R;
import com.threebody.conference.ui.MeetingActivity;
import com.threebody.sdk.common.impl.VideoCommonImpl;
import com.threebody.sdk.domain.DeviceBean;
import com.threebody.sdk.domain.VideoBean;

import java.util.List;

/**
 * Created by xiaxin on 15-1-14.
 */
public class VideoFragment extends BaseFragment {
    VideoShowGLFragment videoUp;
    VideoShowGLFragment videoDown;
    DeviceBean deviceUp, deviceDown;
    DeviceBean device1, device2;
    VideoCommonImpl videoCommon;
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
    public void onResume() {
        super.onResume();
//        videoUp.onResume();
//        videoDown.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
//        videoUp.onStop();
//        videoDown.onStop();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(deviceUp != null){
            videoUp.setDevice(deviceUp);
        }
        if(deviceDown != null){
            videoDown.setDevice(deviceDown);
        }

    }

    @Override
    protected void initView(final View view) {
        super.initView(view);
        videoUp = (VideoShowGLFragment)view.findViewById(R.id.videoUp);
        videoDown = (VideoShowGLFragment)view.findViewById(R.id.videoDown);
        videoDown.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ((MeetingActivity)getActivity()).changeToVideoSet();
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
    public void receiVideoBean(VideoBean videoBean){
        if(isCanShow){
            if(deviceUp != null){
                if(deviceUp.getDeviceId().equals(videoBean.getDeviceId())){
                    videoUp.setVideoBean(videoBean);
                    return;
                }
            }
            if(deviceDown != null){
                if(deviceDown.getDeviceId().equals(videoBean.getDeviceId())){
                    videoDown.setVideoBean(videoBean);
                }
            }
        }

    }

    public void refresh(List<DeviceBean> devices) {
         int i = 0;

        if(devices != null && !devices.isEmpty()){
            for (DeviceBean deviceBean : devices){
               if(deviceBean.isVideoChecked()){
                 if(i == 0){
                     device1 = deviceBean;
                     i++;
                 }else if(i == 1){
                     device2 = deviceBean;
                 }
               }
            }
        }
        checkDevice(device1, device2);
        if(deviceUp != null){
            videoUp.setDevice(deviceUp);
            videoUp.openVideo(videoCommon);
        }
        if(deviceDown != null){
            videoDown.setDevice(deviceDown);
            videoDown.openVideo(videoCommon);
        }
    }
    private void checkDevice(DeviceBean device1, DeviceBean device2){
        if(device1 == null){
            videoUp.closeVideo(videoCommon);
            videoDown.closeVideo(videoCommon);
            return;
        }else if(device2 == null){
            checkOne();
        }else{
            checkTwo();
        }
    }
    private void checkOne(){
        if(deviceUp != null && deviceUp.getDeviceId().equals(device1.getDeviceId())){
            videoDown.closeVideo(videoCommon);
            return;
        }else if(deviceDown != null && deviceDown.getDeviceId().equals(device1.getDeviceId())){
            videoUp.closeVideo(videoCommon);
            videoDown.closeVideo(videoCommon);
            deviceDown = device1;
        }else{
            videoUp.closeVideo(videoCommon);
            videoDown.closeVideo(videoCommon);
            deviceUp = device1;
        }
    }
    private void checkTwo(){
        if(deviceUp != null && deviceUp.getDeviceId().equals(device1.getDeviceId())){
            if(deviceDown != null && deviceDown.getDeviceId().equals(device2.getDeviceId())){

                return;
            }else {
                videoDown.closeVideo(videoCommon);
                deviceDown = device2;
            }
        }else{

            if(deviceDown != null && deviceDown.getDeviceId().equals(device1.getDeviceId())){
                if(deviceUp != null && deviceUp.getDeviceId().equals(device2.getDeviceId())){
                    return;
                }else {
                    videoUp.closeVideo(videoCommon);
                    deviceUp = device2;
                }
            }else if(deviceDown != null && deviceDown.getDeviceId().equals(device2.getDeviceId())){
                videoUp.closeVideo(videoCommon);
                deviceUp = device1;
            }else {
                if(deviceUp != null && deviceUp.getDeviceId().equals(device2.getDeviceId())){
                    videoDown.closeVideo(videoCommon);
                    deviceDown = device1;
                    return;
                }
                videoDown.closeVideo(videoCommon);
                videoUp.closeVideo(videoCommon);
                deviceUp = device1;
                deviceDown = device2;
            }
        }
    }
    public void addDevice(DeviceBean deviceBean) {
        if(deviceBean != null){
            if(deviceUp == null){
                deviceUp = deviceBean;
                if(videoUp != null){
                    videoUp.setDevice(deviceUp);
                }
            }else if(deviceDown == null){
                deviceDown = deviceBean;
                if(videoDown != null){
                    videoDown.setDevice(deviceBean);
                }
            }
        }
    }
    public void setVideoStataus(){
        

    }
    public void setAudioStatus(boolean isOpen, int nodeId){
        if(deviceUp != null && deviceUp.getUser() != null){
            if(deviceUp.getUser().getNodeId() == nodeId){
                videoUp.setStatus(isOpen);
                return;
            }
        }
        if(deviceDown != null && deviceDown.getUser() != null){
            if(deviceDown.getUser().getNodeId() == nodeId){
                videoDown.setStatus(isOpen);
            }
        }

    }

    public void closeAll() {
        videoUp.closeVideo(videoCommon);
        videoDown.closeVideo(videoCommon);
    }
}
