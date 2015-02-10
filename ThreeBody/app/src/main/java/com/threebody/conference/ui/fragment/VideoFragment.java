package com.threebody.conference.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.threebody.conference.R;
import com.threebody.conference.ui.BaseActivity;
import com.threebody.conference.ui.MeetingActivity;
import com.threebody.conference.ui.util.FragmentUtil;
import com.threebody.sdk.common.impl.VideoCommonImpl;
import com.threebody.sdk.domain.DeviceBean;
import com.threebody.sdk.domain.VideoBean;

import java.util.List;

/**
 * Created by xiaxin on 15-1-14.
 */
public class VideoFragment extends BaseFragment {
    VideoShowFragmenet videoUp;
    VideoShowFragmenet videoDown;
    DeviceBean deviceUp, deviceDown;
    VideoCommonImpl videoCommon;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video, null);
        initView(view);
        return view;
    }

    @Override
    protected void initView(final View view) {
        super.initView(view);
        videoUp = (VideoShowFragmenet)getChildFragmentManager().findFragmentById(R.id.videoUp);
        videoDown = (VideoShowFragmenet)getChildFragmentManager().findFragmentById(R.id.videoDown);

//        videoView = new RemoteVideoView(getActivity());
//        videoView = new LocalVideoView(getActivity());
//        videoView.resetSize(LocalVideoView.WIDTH, LocalVideoView.HEIGHT);
//        videoDown.getFlVideo().addView(videoView);

//        localVideo = new LocalVideoView(getActivity());
//        videoDown.getFlVideo().addView(localVideo);
//        localVideo.startCamera();
        videoDown.getView().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                FragmentUtil.moveToRightFragment((BaseActivity)getActivity(), R.id.llContainer, new VideoSetFragment());
                return true;
            }
        });
        videoCommon = ((MeetingActivity)getActivity()).getVideoCommon();
    }
    public void receiVideoBean(VideoBean videoBean){
//        if(deviceUp != null && deviceUp.getDeviceId().equals(videoBean.getDeviceId())){
//            videoUp.setVideoBean(videoBean);
//        }else if(deviceDown != null && deviceDown.getDeviceId().equals(videoBean.getDeviceId())){
//            videoDown.setVideoBean(videoBean);
//        }
        videoUp.setVideoBean(videoBean);
    }

    public void refresh(List<DeviceBean> devices) {
         int i = 0;
        DeviceBean device1 = null;
        DeviceBean device2 = null;
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
    }
    private void checkDevice(DeviceBean device1, DeviceBean device2){

        if(deviceUp != null){
           if(deviceUp.isVideoChecked()){
               videoCommon.closeVideo(deviceUp.getNodeId(), deviceUp.getDeviceId());
           }
        }
        if(deviceDown != null){
            if(deviceDown.isVideoChecked()){
                videoCommon.closeVideo(deviceDown.getNodeId(), deviceDown.getDeviceId());
            }
        }
        if(device1 != null){
            deviceUp = device1;
            videoCommon.openVideo(deviceUp.getNodeId(), deviceUp.getDeviceId());
            videoUp.setDevice(deviceUp);
        }
        if(device2 != null){
            deviceDown = device2;
            videoCommon.openVideo(deviceDown.getNodeId(), deviceDown.getDeviceId());
            videoDown.setDevice(deviceDown);
        }

    }


}
