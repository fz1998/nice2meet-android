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
import com.threebody.sdk.common.ChatCommon;
import com.threebody.sdk.common.impl.ChatCommonImpl;
import com.threebody.sdk.common.impl.RoomCommonImpl;
import com.threebody.sdk.domain.MessageBean;

import java.util.List;

import butterknife.InjectView;

/**
 * Created by xiaxin on 15-1-14.
 */
public class ChatFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener{
    @InjectView(R.id.swipe)SwipeRefreshLayout srl;
    @InjectView(R.id.lvChat)ListView lvChat;
    @InjectView(R.id.btnSend)Button btnSend;
    @InjectView(R.id.etSend)EditText etSend;
    MessageAdapter adapter;
    List<MessageBean> messageBeans;
    ChatCommonImpl chatCommon;
    RoomCommonImpl roomCommon;
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
        chatCommon = ((MeetingActivity)getActivity()).getChatCommon();
        roomCommon = ((MeetingActivity)getActivity()).getRoomCommon();
        btnSend.setOnClickListener(this);
        messageBeans = chatCommon.getMessageList(ChatCommon.PUBLIC);
        adapter = new MessageAdapter(getActivity(), messageBeans);
        lvChat.setAdapter(adapter);
    }

    @Override
    public void onRefresh() {
        srl.setRefreshing(true);
    }
    private void sendMessage(){
        if(!TextViewUtil.isNullOrEmpty(etSend)){
            ToastUtil.showToast(getActivity(), R.string.noSendMessage);
            return;
        }
        MessageBean messageBean = new MessageBean();
        messageBean.setMe(true);
        messageBean.setMessage(etSend.getText().toString());
        messageBean.setPublic(true);
        messageBean.setName(roomCommon.getMe().getUserName());
        messageBean.setNodeId(ChatCommon.PUBLIC);
        messageBeans.add(messageBean);
        adapter.refresh(messageBeans);
        chatCommon.sendMessage(messageBean);

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
        msg.what = ChatCommon.RECEIVE_PUBLIC_MESSAGE;
        handler.sendMessage(msg);
    }
    public Handler handler = new Handler(){
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case ChatCommon.RECEIVE_PUBLIC_MESSAGE:
                    if(messageBeans != null){
                       adapter.refresh(messageBeans);
                    }
                    break;
            }
        }
    };
}
