package com.threebody.conference.ui.view.video;

import com.threebody.sdk.domain.VideoBean;

/**
 * Created by xiaxin on 15-1-17.
 */
public interface OnFramenLister {
    void OnPreviewFrame(VideoBean videoBean);
    void OnResize(int width, int height);
}
