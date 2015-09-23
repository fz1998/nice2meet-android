package cn.tee3.n2m.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import cn.tee3.n2m.R;
import cn.tee3.n2m.biz.service.AudioService;
import cn.tee3.n2m.biz.service.VideoService;
import cn.tee3.n2m.ui.activity.MeetingActivity;
import cn.tee3.n2m.ui.VideoDisplayController;

/**
 * Created by xiaxin on 15-1-14.
 */
public class VideoFragment extends BaseFragment {

    LinearLayout llVideoFragment;

    VideoWindow upperVideoWindow;
    VideoWindow lowerVideoWindow;

    VideoService videoService;
    AudioService audioService;

    public VideoDisplayController getVideoDisplayController() {
        return videoDisplayController;
    }

    VideoDisplayController videoDisplayController;

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
    }

    @Override
    protected void initView(final View view) {
        super.initView(view);
        upperVideoWindow = (VideoWindow)view.findViewById(R.id.videoUp);
        lowerVideoWindow = (VideoWindow)view.findViewById(R.id.videoDown);
        // upper video: longClick will switch videos
        upperVideoWindow.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                switchVideo();
                return true;
            }
        });

        // lower video: longClick will show VideoSetFragment
        lowerVideoWindow.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ((MeetingActivity) getActivity()).changeToVideoSet();
                return true;
            }
        });


        // init VideoService
        videoService = ((MeetingActivity)getActivity()).getVideoService();
        upperVideoWindow.setVideoService(videoService);
        lowerVideoWindow.setVideoService(videoService);

        // TODO: 2015/9/9 new : AudioService pass to videoDisplayController
        //init AudioService
        audioService = ((MeetingActivity)getActivity()).getAudioService();

        // init VideoDisplayController
        videoDisplayController = new VideoDisplayController();
        videoDisplayController.setLowerVideoWindow(lowerVideoWindow);
        videoDisplayController.setUpperVideoWindow(upperVideoWindow);
        videoDisplayController.setVideoService(videoService);
        videoDisplayController.setAudioService(audioService);

        videoService.setVideoDisplayController(videoDisplayController);
        audioService.setVideoDisplayController(videoDisplayController);
    }

    // TODO: 2015/8/31 To use singleton instance for click handler.
    void switchVideo(){

        //// FIXME: 2015/9/2 look at this new solution later.
        switchWindowsInsideFragment();
        videoDisplayController.switchWindowPosition();
    }

    private void switchWindowsInsideFragment() {

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
        upperVideoWindow.show();
        lowerVideoWindow.show();
    }


    public void setAudioStatus(boolean isOpen, int nodeId){

        // TODO: 2015/9/23 getVideo() is null, need to protect here.
        if(upperVideoWindow.getVideo() == null) {
            return;
        }

        if (upperVideoWindow.getVideo().getNodeId() == nodeId){
            //upperwindow
            upperVideoWindow.setAudioStatusIcon(isOpen);
        }else {
            lowerVideoWindow.setAudioStatusIcon(isOpen);
        }
    }

    public void closeAll() {
        upperVideoWindow.removeVideoRender();
        lowerVideoWindow.removeVideoRender();
    }
}
