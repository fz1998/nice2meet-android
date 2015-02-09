package org.st;

public class Chat{
    private final long nativeChat;
    private long nativeChatListener;

    public static interface ChatListener {
        public void onReceivePublicMessage(int fromID, String message);
        public void onReceivePrivateMessage(int fromID, String message);
        public void onReceiveData(int fromID, String data);
    }
    
    public Chat(long nativeChat) {
        this.nativeChat = nativeChat;
    }
    
    public boolean setListener(ChatListener listener) {
        long nativeChatListener = nativeCreateChatListener(listener);
        this.nativeChatListener = nativeChatListener;
        return nativeSetListener(nativeChatListener);
    }
    private static native long nativeCreateChatListener(ChatListener listener);
    private native boolean nativeSetListener(long listener);
    
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
