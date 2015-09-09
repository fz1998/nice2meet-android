package cn.tee3.n2m.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import java.util.List;
import butterknife.InjectView;
import cn.tee3.n2m.R;
import cn.tee3.n2m.biz.domain.N2MVideo;
import cn.tee3.n2m.biz.service.N2MRoomSystem;
import cn.tee3.n2m.ui.activity.MeetingActivity;
import cn.tee3.n2m.ui.adapter.VideoSelectAdapter;

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
