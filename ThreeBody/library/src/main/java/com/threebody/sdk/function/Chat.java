package com.threebody.sdk.function;

public class Chat{
    private final long nativeChat;
    private long nativeChatObserver;

    public static interface ChatObserver {
        public void onReceivePublicMessage(int fromID, String message);
        public void onReceivePrivateMessage(int fromID, String message);
        public void onReceiveData(int fromID, String data);
    }
    
    public Chat(long nativeChat) {
        this.nativeChat = nativeChat;
    }
    
    public boolean setObserver(ChatObserver observer) {
        long nativeChatObserver = nativeCreateChatObserver(observer);
        this.nativeChatObserver = nativeChatObserver;
        return nativeSetObserver(nativeChatObserver);
    }
    private static native long nativeCreateChatObserver(ChatObserver observer);
    private native boolean nativeSetObserver(long observer_p);
    
    public boolean sendPublicMessage(String message) {
        return nativeSendPublicMessage(message);
    }
    private native boolean nativeSendPublicMessage(String message);
    
    public boolean sendPrivateMessage(int toID, int message) {
        return nativeSendPrivateMessage(toID, message);
    }
    private native boolean nativeSendPrivateMessage(int toID, int message);
    
    public boolean sendData(int toID, String data) {
        return nativeSendData(toID, data);
    }
    private native boolean nativeSendData(int toID, String data);
}
