package cn.tee3.n2m.ui;

//import com.threebody.conference.ui.fragment.VideoWindow;

import cn.tee3.n2m.biz.service.AudioService;
import cn.tee3.n2m.ui.fragment.VideoWindow;
import cn.tee3.n2m.biz.service.VideoService;
import cn.tee3.n2m.biz.domain.N2MVideo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/9/3.
 */
public class VideoDisplayController {

    public VideoWindow getUpperVideoWindow() {
        return upperVideoWindow;
    }

    private VideoWindow upperVideoWindow;
    private VideoWindow lowerVideoWindow;

    List<N2MVideo> videoList = new ArrayList<N2MVideo>();
    List<N2MVideo> displayedVideoList = new ArrayList<N2MVideo>();
    private VideoService videoService;
    private AudioService audioService;

    public void addVideo(N2MVideo video) {
        videoList.add(video);
        attachVideo(video);
//        video.setVideoChecked(true);
    }

    private void attachVideo(N2MVideo video) {
        if (attachToUpperWindowIfAvailable(video)){
            return;
        }
        attachToLowerWindowIfAvailable(video);
    }

    private boolean attachToLowerWindowIfAvailable(N2MVideo video) {
        if(lowerVideoWindow.isVideoAttached()){
            return false;
        } else {
            lowerVideoWindow.attachVideo(video);
            displayedVideoList.add(video);

            video.setVideoChecked(true);
            return true;
        }
    }

    private boolean attachToUpperWindowIfAvailable(N2MVideo video) {
        if(upperVideoWindow.isVideoAttached()){
            return false;
        } else {
            upperVideoWindow.attachVideo(video);
            displayedVideoList.add(video);

            video.setVideoChecked(true);
            return true;
        }
    }

    public void setUpperVideoWindow(VideoWindow upperVideoWindow) {
        this.upperVideoWindow = upperVideoWindow;
    }

    public void setLowerVideoWindow(VideoWindow lowerVideoWindow) {
        this.lowerVideoWindow = lowerVideoWindow;
    }

    public void setVideoService(VideoService videoService) {
        this.videoService = videoService;
    }

    public void setAudioService(AudioService audioService) {
        this.audioService = audioService;
    }

    public void switchWindowPosition() {
        VideoWindow temp = upperVideoWindow;
        upperVideoWindow = lowerVideoWindow;
        lowerVideoWindow = temp;
    }

    public void handleVideoSelection(List<N2MVideo> videoList) {

        // detach unselected videos
        List<N2MVideo> unselectedVideoList = getUnselectedVideo(videoList);
        for (N2MVideo video: unselectedVideoList){
            detachVideo(video);
            if (displayedVideoList.contains(video)) {
                displayedVideoList.remove(video);
            }
        }

        //attach selected videos, unless it has already been attached.
        List<N2MVideo> selectedVideoList = getSelectedVideo(videoList);
        removeDisplayedVideosFromSelectedList(selectedVideoList);

        for (N2MVideo selectedVideo: selectedVideoList){
            attachVideo(selectedVideo);
        }
    }

    private void detachVideo(N2MVideo video) {
        upperVideoWindow.detachVideo(video);
        lowerVideoWindow.detachVideo(video);
    }

    private void removeDisplayedVideosFromSelectedList(List<N2MVideo> selectedVideoList) {
        List<N2MVideo> list = new ArrayList<N2MVideo>();
        for (N2MVideo selectedVideo: selectedVideoList){
            list.add(selectedVideo);
        }

        for (N2MVideo selectedVideo: list){
            for (N2MVideo shownVideo: displayedVideoList) {
                if (selectedVideo == shownVideo) {
                    selectedVideoList.remove(selectedVideo);
                }
            }
        }
    }

    private List<N2MVideo> getSelectedVideo(List<N2MVideo> videoList) {
        List<N2MVideo> list = new ArrayList<N2MVideo>();
        for (N2MVideo video : videoList) {
            if (video.isVideoChecked()){
                list.add(video);
            }
        }
        return list;
    }

    private List<N2MVideo> getUnselectedVideo(List<N2MVideo> videoList) {
        List<N2MVideo> list = new ArrayList<N2MVideo>();
        for (N2MVideo video : videoList) {
            if (!video.isVideoChecked()){
                list.add(video);
            }
        }
        return list;
    }

    public List<N2MVideo> getVideoList() {
        return videoList;
    }

    public N2MVideo getVideoById(int nodeId) {
        for (N2MVideo video: getVideoList()) {
            if (video.getNodeId() == nodeId){
                return video;
            }
        }
        return null;
    }

    public List<N2MVideo> getVideoListById(int nodeId) {
        List<N2MVideo> list = new ArrayList<N2MVideo>();
        for (N2MVideo video: getVideoList()) {
            if (video.getNodeId() == nodeId){
                list.add(video);
            }
        }
        return list;
    }

    public void deleteVideo(N2MVideo video) {
        videoList.remove(video);
        //// FIXME: 2015/9/5 delete from displayList?
        displayedVideoList.remove(video);

        video.setVideoChecked(false);
        detachVideo(video);
    }
}