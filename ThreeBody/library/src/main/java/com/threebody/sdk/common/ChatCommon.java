package com.threebody.sdk.common;

import org.st.Chat;

/**
 * Created by xiaxin on 15-2-4.
 */
public abstract class ChatCommon implements Chat.ChatListener{
    protected Chat chat;
    protected ChatCommon(){
    }
    @Override
    public void onReceivePublicMessage(int i, String s) {
        receivePublicMessage(i, s);
    }

    @Override
    public void onReceivePrivateMessage(int i, String s) {
        receivePrivateMessage(i, s);
    }

    @Override
    public void onReceiveData(int i, String s) {
        receiveData(i, s);
    }
    protected abstract void receivePublicMessage(int id, String message);
    protected abstract void receivePrivateMessage(int id, String message);
    protected abstract void receiveData(int id, String message);

    protected boolean sendPublicMessage(String message){
        if(chat != null){
           return chat.sendPublicMessage(message);
        }
        return false;
    }
    protected boolean sendPrivateMessage(int id, String message){
        if(chat != null){
//            return chat.sendPrivateMessage(id, message);
        }
        return false;
    }
    protected boolean sendData(int id, String message){
        if(chat != null){
            return chat.sendData(id, message);
        }
        return false;
    }

}
