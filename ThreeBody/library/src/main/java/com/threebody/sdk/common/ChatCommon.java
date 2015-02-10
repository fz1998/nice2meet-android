package com.threebody.sdk.common;

import com.threebody.sdk.domain.Message;

import org.st.Chat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xiaxin on 15-2-4.
 */
public abstract class ChatCommon {
    protected Chat chat;
    private Chat.ChatListener listener;
    ChatCallback callback;
    Map<Integer, List<Message>> messageMap;
    RoomCommon roomCommon;
    protected ChatCommon(RoomCommon roomCommon, ChatCallback callback){
        this.callback = callback;
        this.roomCommon = roomCommon;
        chat = roomCommon.getChat();
        roomCommon.setChatCommon(this);
        messageMap = new HashMap<>();
        initListener();

    }



    public boolean sendPublicMessage(String message){
        Message msg = new Message(message, roomCommon.getMe().getUserName(), roomCommon.getMe().getNodeId(), true, true);
        addMessage(msg);
        if(chat != null){
           return chat.sendPublicMessage(message);
        }
        return false;
    }
    public boolean sendPrivateMessage(int nodeId, String message){
        Message msg = new Message(message, roomCommon.getMe().getUserName(), roomCommon.getMe().getNodeId(), false, true);
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
                Message msg = new Message(message, "", nodeId, true, false);
                addMessage(msg);
                if(checkCallback()){
                    callback.onReceivePublicMessage(nodeId, message);
                }
            }

            @Override
            public void onReceivePrivateMessage(int nodeId, String message) {
                Message msg = new Message(message,"",nodeId, false, false);
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
    }
    private boolean checkCallback(){
        if(callback == null){
            return false;
        }
        return true;
    }
    private void addMessage(Message message){
        if(!checkUser(message.getNodeId())){
            messageMap.put(message.getNodeId(), new ArrayList<Message>());
        }
        List<Message> messages = messageMap.get(message.getNodeId());
        messages.add(message);
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
