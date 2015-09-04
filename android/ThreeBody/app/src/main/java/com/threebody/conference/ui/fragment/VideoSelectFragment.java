package com.threebody.conference.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.threebody.conference.R;
import com.threebody.conference.ui.MeetingActivity;
import com.threebody.conference.ui.adapter.VideoSetAdapter;
import com.threebody.sdk.common.STSystem;
import com.threebody.sdk.domain.N2MVideo;

import java.util.List;

import butterknife.InjectView;

/**
 * Created by xiaxin on 15-1-17.
 */
public class VideoSelectFragment extends BaseFragment{
    @InjectView(R.id.userList)ListView userList;
    @InjectView(R.id.btnAddIn)Button btnAddIn;
    VideoSetAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video_select, null);
        initView(view);
        return view;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        if(adapter == null){
            adapter = new VideoSetAdapter(getActivity(), STSystem.getInstance().getRoomCommons().get(0).getVideoCommon().getDevices());
        }
        userList.setAdapter(adapter);
        btnAddIn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.btnAddIn:
//                List<N2MVideo> checkedVideoList = adapter.getSelectedVideo();
                List<N2MVideo> videoList = adapter.getVideoList();
                ((MeetingActivity) getActivity()).getVideoFragment().getVideoDisplayController().handleVideoSelection(videoList);

                ((MeetingActivity)getActivity()).refreshVideoFragmentUI();
                break;
            default:
                break;
        }
    }
}
