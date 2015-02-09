package com.threebody.conference.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.threebody.conference.R;
import com.threebody.conference.ui.adapter.VideoSetAdapter;
import com.threebody.sdk.common.STSystem;

import butterknife.InjectView;

/**
 * Created by xiaxin on 15-1-17.
 */
public class VideoSetFragment extends BaseFragment{
    @InjectView(R.id.userList)ListView userList;
    VideoSetAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video_set, null);
        initView(view);
        return view;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
//        List<User> users = new ArrayList<>();
//        for(int i = 0; i < 25; i++){
//            User user = new User();
//            user.setName("user"+i);
//            users.add(user);
//        }
        adapter = new VideoSetAdapter(getActivity(), STSystem.getInstance().getRoomCommons().get(0).getUsers());
        userList.setAdapter(adapter);
    }

}
