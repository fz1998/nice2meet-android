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
import com.threebody.conference.ui.adapter.VideoSelectAdapter;
import com.threebody.sdk.service.N2MRoomSystem;
import com.threebody.sdk.domain.N2MVideo;

import java.util.List;

import butterknife.InjectView;

/**
 * Created by xiaxin on 15-1-17.
 */
public class VideoSelectFragment extends BaseFragment{
    @InjectView(R.id.userList)ListView userList;
    @InjectView(R.id.btnJoin)Button btnAddIn;
    VideoSelectAdapter adapter;
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
            //// TODO: 2015/9/5 get(0) should be cleaned
            adapter = new VideoSelectAdapter(getActivity(), N2MRoomSystem.instance().getRoomService().getVideoService().getDevices());
        }
        userList.setAdapter(adapter);
        btnAddIn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.btnJoin:
                List<N2MVideo> videoList = adapter.getVideoList();
                ((MeetingActivity) getActivity()).getVideoFragment().getVideoDisplayController().handleVideoSelection(videoList);
                ((MeetingActivity)getActivity()).refreshVideoFragmentUI();
                break;
            default:
                break;
        }
    }
}
