package com.threebody.conference.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.threebody.conference.R;
import com.threebody.conference.ui.MeetingActivity;
import com.threebody.conference.ui.adapter.MessageAdapter;
import com.threebody.conference.ui.util.TextViewUtil;
import com.threebody.conference.ui.util.ToastUtil;
import com.threebody.sdk.service.ChatService;
import com.threebody.sdk.service.RoomService;

import java.io.UnsupportedEncodingException;

import butterknife.InjectView;

/**
 * Created by xiaxin on 15-1-14.
 */
public class ChatFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener{
    @InjectView(R.id.lvChat)ListView lvChat;
    @InjectView(R.id.btnSend)Button btnSend;
    @InjectView(R.id.etSend)EditText etSend;
    MessageAdapter adapter;
    ChatService chatService;
    RoomService roomService;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, null);
        initView(view);
        return view;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        chatService = ((MeetingActivity)getActivity()).getChatService();
        roomService = ((MeetingActivity)getActivity()).getRoomService();
        btnSend.setOnClickListener(this);
        adapter = new MessageAdapter(getActivity(), chatService.getPublicMessgeList());
        lvChat.setAdapter(adapter);
    }

    //// TODO: 2015/9/5 What's this for ? should be handled somehow ?
    @Override
    public void onRefresh() {
    }

    private void sendMessage(){
        if(!TextViewUtil.isNullOrEmpty(etSend)){
            ToastUtil.showToast(getActivity(), R.string.noSendMessage);
            return;
        }

        String message = "";
        try {
             message = new String(etSend.getText().toString().getBytes(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        chatService
                .sendPublicMessage(message);
        adapter.refresh(chatService.getPublicMessgeList());
        etSend.setText("");
        lvChat.smoothScrollToPosition(adapter.getCount() - 1);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.btnSend:
                sendMessage();
                break;
            default:
                break;
        }
    }
    public void receivePublicMessage(){
        android.os.Message msg = new android.os.Message();
        msg.what = ChatService.RECEIVE_PUBLIC_MESSAGE;
        handler.sendMessage(msg);
    }
    public Handler handler = new Handler(){
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case ChatService.RECEIVE_PUBLIC_MESSAGE:
                    if(chatService != null){
                        if(chatService.getPublicMessgeList() != null){
                            adapter.refresh(chatService.getPublicMessgeList());
                            lvChat.smoothScrollToPosition(adapter.getCount() - 1);
                        }
                    }
                    break;
            }
        }
    };
}
