package cn.tee3.n2m;

//import com.threebody.conference.ui.fragment.VideoWindow;

import com.threebody.conference.ui.fragment.VideoWindow;
import com.threebody.sdk.common.VideoCommon;
import com.threebody.sdk.domain.N2MVideo;

import org.webrtc.VideoRenderer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/9/3.
 */
public class VideoDisplayController {

    private VideoWindow upperVideoWindow;
    private VideoWindow lowerVideoWindow;

    List<N2MVideo> videoList = new ArrayList<N2MVideo>();
    private VideoCommon videoCommon;

    public void addVideo(N2MVideo video) {
        videoList.add(video);
        handleUpperWindow(video);
        handleLowerWindow(video);

    }

    private void handleLowerWindow(N2MVideo video) {
//        // get video render
//        if(!lowerVideoWindow.isBoundVideo()){
//            return;
//        }
//        VideoRenderer render = lowerVideoWindow.getRenderer();
//        // connect render with
//        videoCommon.setVideoRender(video.getNodeId(), video.getDeviceId(), render);
    }

    private void handleUpperWindow(N2MVideo video) {
        // get video render
        if(upperVideoWindow.isBoundVideo()){
            return;
        }
        VideoRenderer render = upperVideoWindow.getRenderer();
        // connect render with video device
        videoCommon.setVideoRender(video.getNodeId(), video.getDeviceId(), render);

        // save video for VideoWindow
        upperVideoWindow.setVideo(video);
    }


    public void setUpperVideoWindow(VideoWindow upperVideoWindow) {
        this.upperVideoWindow = upperVideoWindow;
    }

    public VideoWindow getUpperVideoWindow() {
        return upperVideoWindow;
    }

    public void setLowerVideoWindow(VideoWindow lowerVideoWindow) {
        this.lowerVideoWindow = lowerVideoWindow;
    }

    public VideoWindow getLowerVideoWindow() {
        return lowerVideoWindow;
    }

    public void setVideoCommon(VideoCommon videoCommon) {
        this.videoCommon = videoCommon;
    }

    public VideoCommon getVideoCommon() {
        return videoCommon;
    }
}
