package com.threebody.conference.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.threebody.conference.R;
import com.threebody.conference.ui.MeetingActivity;
import com.threebody.sdk.common.VideoCommon;

import cn.tee3.n2m.VideoDisplayController;

/**
 * Created by xiaxin on 15-1-14.
 */
public class VideoFragment extends BaseFragment {
    // LinearLayout for the whole VideoFragment
    LinearLayout llVideoFragment;

    VideoWindow upperVideoWindow;
    VideoWindow lowerVideoWindow;

//    N2MVideo deviceUpper, deviceLower;
//    N2MVideo device1, device2;

    VideoCommon videoCommon;
    VideoDisplayController videoDisplayController;

    boolean isCanShow = true;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(llVideoFragment == null){
             llVideoFragment = (LinearLayout) inflater.inflate(R.layout.fragment_video, null);
            initView(llVideoFragment);
        }else{
            ViewGroup vg = (ViewGroup) llVideoFragment.getParent();
            if(vg != null){
                vg.removeAllViewsInLayout();
            }
        }
        return llVideoFragment;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        if(deviceUpper != null){
//            upperVideoWindow.setVideoDeviceAndShowVideoWindow(deviceUpper);
//        }
//        if(deviceLower != null){
//            lowerVideoWindow.setVideoDeviceAndShowVideoWindow(deviceLower);
//        }
    }

    @Override
    protected void initView(final View view) {
        super.initView(view);
        upperVideoWindow = (VideoWindow)view.findViewById(R.id.videoUp);
        lowerVideoWindow = (VideoWindow)view.findViewById(R.id.videoDown);
        // upper video: longClick will switch videos
//        upperVideoWindow.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                switchVideo();
//                return true;
//            }
//        });
//
//        // lower video: longClick will show VideoSetFragment
//        lowerVideoWindow.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                ((MeetingActivity) getActivity()).changeToVideoSet();
//                return true;
//            }
//        });


        // init VideoCommon
        videoCommon = ((MeetingActivity)getActivity()).getVideoCommon();
        upperVideoWindow.setVideoCommon(videoCommon);
        lowerVideoWindow.setVideoCommon(videoCommon);

        // init VideoDisplayController
        videoDisplayController = new VideoDisplayController();
        videoDisplayController.setLowerVideoWindow(lowerVideoWindow);
        videoDisplayController.setUpperVideoWindow(upperVideoWindow);
        videoDisplayController.setVideoCommon(videoCommon);

        videoCommon.setVideoDisplayController(videoDisplayController);
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

//    void closeShowDown(){
//        if (lowerVideoWindow == null)
//            return;
////        lowerVideoWindow.removeVideoRender();
//        lowerVideoWindow.setVideoDeviceAndShowVideoWindow(null);
//        deviceLower = null;
//    }
//    void closeShowUp(){
//        if (upperVideoWindow == null)
//            return;
////        upperVideoWindow.removeVideoRender();
//        upperVideoWindow.setVideoDeviceAndShowVideoWindow(null);
//        deviceUpper = null;
//    }
//
//    void changeShowUp(N2MVideo device){
//        if (deviceUpper == device)
//            return;
//        closeShowUp();
//        upperVideoWindow.setVideoDeviceAndShowVideoWindow(device);
//        deviceUpper = device;
//    }
//
//    void changeShowDown(N2MVideo device){
//        if (deviceUpper == device)
//            return;
//        closeShowDown();
//        lowerVideoWindow.setVideoDeviceAndShowVideoWindow(device);
//        deviceLower = device;
//    }

    // TODO: 2015/8/31 To use singleton instance for click handler.
    void switchVideo(){
        //// FIXME: 2015/9/2 look at this new solution later.
//        VideoShowGLFrameLayout temp = upperVideoWindow;
//        upperVideoWindow = lowerVideoWindow;
//        lowerVideoWindow = temp;

        View v0 = llVideoFragment.getChildAt(0);
        View v1 = llVideoFragment.getChildAt(1);
        View vTemp = v0;
        v0 = v1;
        v1 = vTemp;

        // upper video:switch
        v0.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                switchVideo();
                return true;
            }
        });

        // lower video:VideSetFragment
        v1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ((MeetingActivity) getActivity()).changeToVideoSet();
                return true;
            }
        });

        llVideoFragment.removeAllViews();
        llVideoFragment.addView(v0);
        llVideoFragment.addView(v1);
    }

    public synchronized void refreshVideoWindows() {
//    public synchronized void refreshVideoWindows() {
        upperVideoWindow.show();
        lowerVideoWindow.show();
//
//        int i = 0;
//
//        // get devices
//        List<N2MVideo> devices = videoCommon.getDevices();
//
//        // determinate device1 and device2
//        if(devices != null && !devices.isEmpty()){
//            for (N2MVideo n2MVideo : devices){
//                if(n2MVideo.isVideoChecked() ){
//                    if(i == 0){
//                        device1 = n2MVideo;
//                        i+=1;
//                    }else if(i == 1){
//                        i+=1;
//                        device2 = n2MVideo;
//                    }
//                }
//            }
//        }
//
//        // shnow device1 and device2 on the screen
//        if (i == 0){
//            closeShowUp();
//            closeShowDown();
//        }else if (i == 1){
//            closeShowDown();
//            changeShowUp(device1);
//        }else if (i  == 2){
//            changeShowUp(device1);
//            changeShowDown(device2);
//        }
    }


    public void setAudioStatus(boolean isOpen, int nodeId){
//        if(deviceUpper != null && deviceUpper.getUser() != null){
//            if(deviceUpper.getUser().getNodeId() == nodeId){
                upperVideoWindow.setAudioStatusIcon(isOpen);
//                return;
//            }
//        }
//        if(deviceLower != null && deviceLower.getUser() != null){
//            if(deviceLower.getUser().getNodeId() == nodeId){
                lowerVideoWindow.setAudioStatusIcon(isOpen);
//            }
//        }

    }

    public void closeAll() {
        upperVideoWindow.removeVideoRender();
        lowerVideoWindow.removeVideoRender();
    }
}
