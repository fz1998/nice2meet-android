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
            return true;
        }
    }

    private boolean attachToUpperWindowIfAvailable(N2MVideo video) {
        if(upperVideoWindow.isVideoAttached()){
            return false;
        } else {
            upperVideoWindow.attachVideo(video);
            return true;
        }
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

    public void switchWindowPosition() {
        VideoWindow temp = upperVideoWindow;
        upperVideoWindow = lowerVideoWindow;
        lowerVideoWindow = temp;
    }
}
