package com.threebody.conference.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.threebody.conference.R;
import com.threebody.conference.ui.adapter.MessageAdapter;
import com.threebody.sdk.domain.Message;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

/**
 * Created by xiaxin on 15-1-14.
 */
public class ChatFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener{
    @InjectView(R.id.swipe)SwipeRefreshLayout srl;
    @InjectView(R.id.lvChat)ListView lvChat;
    MessageAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, null);
        initView(view);
        return view;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        srl.setOnRefreshListener(this);
        srl.setColorScheme(android.R.color.holo_green_dark, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        List<Message> messages = new ArrayList<>();
        for(int i = 0; i < 25; i++){
            Message message = new Message();
            message.setName("user"+i%2);
            message.setMe(i%2 == 0);
            message.setMessage("Hello World!");
            messages.add(message);
        }
        adapter = new MessageAdapter(getActivity(), messages);
        lvChat.setAdapter(adapter);
    }

    @Override
    public void onRefresh() {
        srl.setRefreshing(true);
    }
}
