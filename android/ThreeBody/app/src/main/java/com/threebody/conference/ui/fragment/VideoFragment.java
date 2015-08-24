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

import org.webrtc.VideoRenderer;

import java.util.List;

/**
 * Created by xiaxin on 15-1-14.
 */
public class VideoFragment extends BaseFragment {
    VideoShowGLFrameLayout upperVideoFragment;
    VideoShowGLFrameLayout lowerVideoFragment;
    DeviceBean deviceUpper, deviceLower;
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
//        upperVideoFragment.onResume();
//        lowerVideoFragment.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
//        upperVideoFragment.onStop();
//        lowerVideoFragment.onStop();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(deviceUpper != null){
            upperVideoFragment.setDevice(deviceUpper);
        }
        if(deviceLower != null){
            lowerVideoFragment.setDevice(deviceLower);
        }

    }

    @Override
    protected void initView(final View view) {
        super.initView(view);
        upperVideoFragment = (VideoShowGLFrameLayout)view.findViewById(R.id.videoUp);
        lowerVideoFragment = (VideoShowGLFrameLayout)view.findViewById(R.id.videoDown);
        lowerVideoFragment.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ((MeetingActivity) getActivity()).changeToVideoSet();
                return true;
            }
        });
        upperVideoFragment.setOnLongClickListener(new View.OnLongClickListener() {
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
    public void receiVideoBean(VideoBean videoBean){
        if(isCanShow){
            if(deviceUpper != null){
                if(deviceUpper.getDeviceId().equals(videoBean.getDeviceId())){
                    upperVideoFragment.setVideoBean(videoBean);
                    return;
                }
            }
            if(deviceLower != null){
                if(deviceLower.getDeviceId().equals(videoBean.getDeviceId())){
                    lowerVideoFragment.setVideoBean(videoBean);
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
        if (lowerVideoFragment == null)
            return;
        lowerVideoFragment.removeVideoRender(videoCommon);
        lowerVideoFragment.setDevice(null);
        deviceLower = null;
    }
    void closeShowUp(){
        if (upperVideoFragment == null)
            return;
        upperVideoFragment.removeVideoRender(videoCommon);
        upperVideoFragment.setDevice(null);
        deviceUpper = null;
    }

    void changeShowUp(DeviceBean device){
        if (Eq(deviceUpper, device))
            return;
        closeShowUp();
        upperVideoFragment.setDevice(device);
        upperVideoFragment.setVideoRender(videoCommon);
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

        if  (videoCommon.switchVideoRender(upperVideoFragment.mRenderer, lowerVideoFragment.mRenderer))
        {
            DeviceBean temp = deviceUpper;
            deviceUpper = deviceLower;
            deviceLower = temp;

            VideoRenderer mRenderer  = upperVideoFragment.mRenderer;

            upperVideoFragment.mRenderer = lowerVideoFragment.mRenderer;
            lowerVideoFragment.mRenderer = mRenderer;
            upperVideoFragment.setDevice(deviceUpper);
            lowerVideoFragment.setDevice(deviceLower);
            return true;
        }
        return false;
    }
    public synchronized void  refresh(List<DeviceBean> devices) {
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
//               if(deviceBean.isVideoChecked() && !Eq(deviceBean, deviceUpper) && !Eq(deviceBean, deviceLower)){
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
//        if(deviceUpper != null){
//            upperVideoFragment.setDevice(deviceUpper);
//            upperVideoFragment.setVideoRender(videoCommon);
//        }
//        if(deviceLower != null){
//            lowerVideoFragment.setDevice(deviceLower);
//            lowerVideoFragment.setVideoRender(videoCommon);
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
            upperVideoFragment.removeVideoRender(videoCommon);
            lowerVideoFragment.removeVideoRender(videoCommon);
            return;
        }else if(device2 == null){
            checkOne();
        }else{
            checkTwo();
        }
    }
    private void checkOne(){
        if(deviceUpper != null && deviceUpper.getDeviceId().equals(device1.getDeviceId())){
            lowerVideoFragment.removeVideoRender(videoCommon);
            return;
        }else if(deviceLower != null && deviceLower.getDeviceId().equals(device1.getDeviceId())){
            upperVideoFragment.removeVideoRender(videoCommon);
            lowerVideoFragment.removeVideoRender(videoCommon);
            deviceLower = device1;
        }else{
            upperVideoFragment.removeVideoRender(videoCommon);
            lowerVideoFragment.removeVideoRender(videoCommon);
            deviceUpper = device1;
        }
    }
    private void checkTwo(){
        if(deviceUpper != null && deviceUpper.getDeviceId().equals(device1.getDeviceId())){
            if(deviceLower != null && deviceLower.getDeviceId().equals(device2.getDeviceId())){

                return;
            }else {
                lowerVideoFragment.removeVideoRender(videoCommon);
                deviceLower = device2;
            }
        }else{

            if(deviceLower != null && deviceLower.getDeviceId().equals(device1.getDeviceId())){
                if(deviceUpper != null && deviceUpper.getDeviceId().equals(device2.getDeviceId())){
                    return;
                }else {
                    upperVideoFragment.removeVideoRender(videoCommon);
                    deviceUpper = device2;
                }
            }else if(deviceLower != null && deviceLower.getDeviceId().equals(device2.getDeviceId())){
                upperVideoFragment.removeVideoRender(videoCommon);
                deviceUpper = device1;
            }else {
                if(deviceUpper != null && deviceUpper.getDeviceId().equals(device2.getDeviceId())){
                    lowerVideoFragment.removeVideoRender(videoCommon);
                    deviceLower = device1;
                    return;
                }
                lowerVideoFragment.removeVideoRender(videoCommon);
                upperVideoFragment.removeVideoRender(videoCommon);
                deviceUpper = device1;
                deviceLower = device2;
            }
        }
    }
    public void addDevice(DeviceBean deviceBean) {
        if(deviceBean != null){
            if(deviceUpper == null){
                deviceUpper = deviceBean;
                if(upperVideoFragment != null){
                    upperVideoFragment.setDevice(deviceUpper);
                }
            }else if(deviceLower == null){
                deviceLower = deviceBean;
                if(lowerVideoFragment != null){
                    lowerVideoFragment.setDevice(deviceBean);
                }
            }
        }
    }
    public void setVideoStataus(){
        

    }
    public void setAudioStatus(boolean isOpen, int nodeId){
        if(deviceUpper != null && deviceUpper.getUser() != null){
            if(deviceUpper.getUser().getNodeId() == nodeId){
                upperVideoFragment.setStatus(isOpen);
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
        upperVideoFragment.removeVideoRender(videoCommon);
        lowerVideoFragment.removeVideoRender(videoCommon);
    }
}
