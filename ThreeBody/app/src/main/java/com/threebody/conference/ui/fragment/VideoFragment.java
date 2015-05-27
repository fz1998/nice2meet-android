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

import org.apache.http.auth.NTUserPrincipal;

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
        if (videoDown == null)
            return;
        videoDown.removeVideoRender(videoCommon);
        videoDown.setDevice(null);
        deviceDown = null;
    }
    void closeShowUp(){
        if (videoUp == null)
            return;
        videoUp.removeVideoRender(videoCommon);
        videoUp.setDevice(null);
        deviceUp = null;
    }

    void changeShowUp(DeviceBean device){
        if (Eq(deviceUp, device))
            return;
        closeShowUp();
        videoUp.setDevice(device);
        videoUp.setVideoRender(videoCommon);
        deviceUp = device;
    }

    void changeShowDown(DeviceBean device){
        if (Eq(deviceUp, device))
            return;
        closeShowDown();
        videoDown.setDevice(device);
        videoDown.setVideoRender(videoCommon);
        deviceDown = device;
    }
    public void refresh(List<DeviceBean> devices) {
        int i = 0;
        DeviceBean nextShowDevice1 = null, nextShowDevice2 = null;
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
//    public void refresh(List<DeviceBean> devices) {
//         int i = 0;
//        DeviceBean nextShowDevice1 = null, nextShowDevice2 = null;
//        if(devices != null && !devices.isEmpty()){
//            for (DeviceBean deviceBean : devices){
//               if(deviceBean.isVideoChecked() && !Eq(deviceBean, deviceUp) && !Eq(deviceBean, deviceDown)){
//                 if(i == 0){
//                     device1 = deviceBean;
//                     i++;
//                 }else if(i == 1){
//                     device2 = deviceBean;
//                 }
//               }
//            }
//        }
//        checkDevice(device1,nextShowDevice1 , device2, nextShowDevice2 );
//
//        if(deviceUp != null){
//            videoUp.setDevice(deviceUp);
//            videoUp.setVideoRender(videoCommon);
//        }
//        if(deviceDown != null){
//            videoDown.setDevice(deviceDown);
//            videoDown.setVideoRender(videoCommon);
//        }
//    }
    private void checkDevice(DeviceBean device1, DeviceBean nextShowDevice1, DeviceBean device2, DeviceBean nextShowDevice2){

        if (device1 != null ){
            if (nextShowDevice1 == null){
                //取消device1的显示

            }else {
                //更新device1显示
            }
        }else {
            if (nextShowDevice1 == null){
                //device1没有显示

            }else {
                //更新device1显示
            }
        }


        if(device1 == null){
            videoUp.removeVideoRender(videoCommon);
            videoDown.removeVideoRender(videoCommon);
            return;
        }else if(device2 == null){
            checkOne();
        }else{
            checkTwo();
        }
    }
    private void checkOne(){
        if(deviceUp != null && deviceUp.getDeviceId().equals(device1.getDeviceId())){
            videoDown.removeVideoRender(videoCommon);
            return;
        }else if(deviceDown != null && deviceDown.getDeviceId().equals(device1.getDeviceId())){
            videoUp.removeVideoRender(videoCommon);
            videoDown.removeVideoRender(videoCommon);
            deviceDown = device1;
        }else{
            videoUp.removeVideoRender(videoCommon);
            videoDown.removeVideoRender(videoCommon);
            deviceUp = device1;
        }
    }
    private void checkTwo(){
        if(deviceUp != null && deviceUp.getDeviceId().equals(device1.getDeviceId())){
            if(deviceDown != null && deviceDown.getDeviceId().equals(device2.getDeviceId())){

                return;
            }else {
                videoDown.removeVideoRender(videoCommon);
                deviceDown = device2;
            }
        }else{

            if(deviceDown != null && deviceDown.getDeviceId().equals(device1.getDeviceId())){
                if(deviceUp != null && deviceUp.getDeviceId().equals(device2.getDeviceId())){
                    return;
                }else {
                    videoUp.removeVideoRender(videoCommon);
                    deviceUp = device2;
                }
            }else if(deviceDown != null && deviceDown.getDeviceId().equals(device2.getDeviceId())){
                videoUp.removeVideoRender(videoCommon);
                deviceUp = device1;
            }else {
                if(deviceUp != null && deviceUp.getDeviceId().equals(device2.getDeviceId())){
                    videoDown.removeVideoRender(videoCommon);
                    deviceDown = device1;
                    return;
                }
                videoDown.removeVideoRender(videoCommon);
                videoUp.removeVideoRender(videoCommon);
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
        videoUp.removeVideoRender(videoCommon);
        videoDown.removeVideoRender(videoCommon);
    }
}
