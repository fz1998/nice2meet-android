package cn.tee3.n2m.biz.service;

import cn.tee3.n2m.biz.domain.MessageBean;

import org.st.Chat;
import org.st.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xiaxin on 15-2-4.
 */
public class ChatService {

    public static final int PUBLIC_MESSAGE = 0;
    Map<Integer, List<MessageBean>> messageMap;

    public static final int RECEIVE_PUBLIC_MESSAGE = 10000;

    protected Chat chatModule;
    ChatCallback callback;

    RoomService roomService;
    public ChatService(RoomService roomService, ChatCallback callback){
        this.callback = callback;
        this.roomService = roomService;
        chatModule = roomService.getRoomModule().getChat();
        roomService.setChatService(this);
        messageMap = new HashMap<>();
        messageMap.put(PUBLIC_MESSAGE, new ArrayList<MessageBean>());
        initListener();
    }

    public boolean sendPublicMessage(String message){
        User user = roomService.getMe();
        if(user != null){
            MessageBean msg = new MessageBean(message, user.getUserName(), user.getNodeId(), true, true);
            addPublicMessage(msg);
            if(chatModule != null){
                return chatModule.sendPublicMessage(message);
            }
        }
        return false;
    }

    private void initListener(){
        if (chatModule == null)
            return;
        Chat.ChatListener listener = new Chat.ChatListener() {
            @Override
            synchronized public void onReceivePublicMessage(int nodeId, String message) {
                User user = roomService.findUserById(nodeId);
                String name = "";
                if(user != null){
                    name = user.getUserName();
                }
                MessageBean msg = new MessageBean(message, name, nodeId, true, false);
                addPublicMessage(msg);
                if(checkCallback()){
                    callback.onReceivePublicMessage(nodeId, message);
                }
            }

            @Override
            synchronized public void onReceivePrivateMessage(int nodeId, String message) {
                MessageBean msg = new MessageBean(message,"",nodeId, false, false);
                addPrivateMessage(msg);
                if(checkCallback()){
                    callback.onReceivePrivateMessage(nodeId, message);
                }
            }

            @Override
            synchronized public void onReceiveData(int nodeId, String data) {
                if(checkCallback()){
                    callback.onReceiveData(nodeId, data);
                }
            }
        };
        chatModule.setListener(listener);
    }

    private boolean checkCallback(){
        return callback != null;
    }

    private void addPublicMessage(MessageBean messageBean){
        List<MessageBean> messageBeans = messageMap.get(PUBLIC_MESSAGE);
        messageBeans.add(messageBean);
    }

    private void addPrivateMessage(MessageBean messageBean){
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

    public List<MessageBean> getPublicMessgeList() {
        return messageMap.get(PUBLIC_MESSAGE);
    }


    public interface ChatCallback{
        void onReceivePublicMessage(int nodeId, java.lang.String message);

        void onReceivePrivateMessage(int nodeId, java.lang.String message);

        void onReceiveData(int nodeId, java.lang.String data);
    }

}
