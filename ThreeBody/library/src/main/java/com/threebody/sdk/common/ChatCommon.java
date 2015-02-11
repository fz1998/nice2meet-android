package com.threebody.sdk.common;

import com.threebody.sdk.domain.MessageBean;

import org.st.Chat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xiaxin on 15-2-4.
 */
public abstract class ChatCommon {
    public static final int PUBLIC = 0;
    public static final int RECEIVE_PUBLIC_MESSAGE = 10000;
    public static final int RECEIVI_PRIVATE_MESSAG = 10001;
    protected Chat chat;
    private Chat.ChatListener listener;
    ChatCallback callback;
    Map<Integer, List<MessageBean>> messageMap;
    RoomCommon roomCommon;
    protected ChatCommon(RoomCommon roomCommon, ChatCallback callback){
        this.callback = callback;
        this.roomCommon = roomCommon;
        chat = roomCommon.getChat();
        roomCommon.setChatCommon(this);
        messageMap = new HashMap<>();
        initListener();

    }

    public Map<Integer, List<MessageBean>> getMessageMap() {
        return messageMap;
    }

    public boolean sendPublicMessage(String message){
        MessageBean msg = new MessageBean(message, "", 0, true, true);
        addMessage(msg);
        if(chat != null){
           return chat.sendPublicMessage(message);
        }
        return false;
    }
    public boolean sendPrivateMessage(int nodeId, String message){
        MessageBean msg = new MessageBean(message, roomCommon.getMe().getUserName(), roomCommon.getMe().getNodeId(), false, true);
        msg.setToNodeId(nodeId);
        addMessage(msg);
        if(chat != null){
//            return chat.sendPrivateMessage(id, message);
        }
        return false;
    }
    public boolean sendData(int id, String message){
        if(chat != null){
            return chat.sendData(id, message);
        }
        return false;
    }
    private void initListener(){
        listener = new Chat.ChatListener() {
            @Override
            public void onReceivePublicMessage(int nodeId, String message) {
                MessageBean msg = new MessageBean(message, "", nodeId, true, false);
                addMessage(msg);
                if(checkCallback()){
                    callback.onReceivePublicMessage(nodeId, message);
                }
            }

            @Override
            public void onReceivePrivateMessage(int nodeId, String message) {
                MessageBean msg = new MessageBean(message,"",nodeId, false, false);
                addMessage(msg);
                if(checkCallback()){
                    callback.onReceivePrivateMessage(nodeId, message);
                }
            }

            @Override
            public void onReceiveData(int nodeId, String data) {
                if(checkCallback()){
                    callback.onReceiveData(nodeId, data);
                }
            }
        };
        chat.setListener(listener);
    }
    private boolean checkCallback(){
        if(callback == null){
            return false;
        }
        return true;
    }
    private void addMessage(MessageBean messageBean){
        if(!checkUser(messageBean.getNodeId())){
            messageMap.put(messageBean.getNodeId(), new ArrayList<MessageBean>());
        }
        List<MessageBean> messageBeans = messageMap.get(messageBean.getNodeId());
        messageBeans.add(messageBean);
    }
    private boolean checkUser(int nodeId){
        for (Integer key : messageMap.keySet()){
            if(nodeId == key){
                return true;
            }
        }
        return false;
    }
    public interface ChatCallback{
        void onReceivePublicMessage(int nodeId, java.lang.String message);

        void onReceivePrivateMessage(int nodeId, java.lang.String message);

        void onReceiveData(int nodeId, java.lang.String data);
    }
}
