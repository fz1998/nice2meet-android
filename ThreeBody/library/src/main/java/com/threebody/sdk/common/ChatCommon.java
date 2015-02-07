package com.threebody.sdk.common;

import org.st.Chat;

/**
 * Created by xiaxin on 15-2-4.
 */
public abstract class ChatCommon {
    protected Chat chat;
    private Chat.ChatListener listener;
    ChatCallback callback;
    protected ChatCommon(RoomCommon roomCommon, ChatCallback callback){
        this.callback = callback;
        chat = roomCommon.getChat();
        initListener();
    }



    public boolean sendPublicMessage(String message){
        if(chat != null){
           return chat.sendPublicMessage(message);
        }
        return false;
    }
    public boolean sendPrivateMessage(int id, String message){
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
                if(checkCallback()){
                    callback.onReceivePublicMessage(nodeId, message);
                }
            }

            @Override
            public void onReceivePrivateMessage(int nodeId, String message) {
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
    public interface ChatCallback{
        void onReceivePublicMessage(int nodeId, java.lang.String message);

        void onReceivePrivateMessage(int nodeId, java.lang.String message);

        void onReceiveData(int nodeId, java.lang.String data);
    }
}
