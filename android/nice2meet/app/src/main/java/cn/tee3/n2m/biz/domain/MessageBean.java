package cn.tee3.n2m.biz.domain;

/**
 * Created by xiaxin on 15-1-17.
 */
public class MessageBean {
    String uid;
    String message;
    String name;
    int nodeId;
    int toNodeId;
    boolean isPublic;
    boolean isMe;

    public MessageBean() {
    }

    public MessageBean(String message, String name, int nodeId, boolean isPublic, boolean isMe) {
        this.message = message;
        this.name = name;
        this.nodeId = nodeId;
        this.isPublic = isPublic;
        this.isMe = isMe;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isMe() {
        return isMe;
    }

    public void setMe(boolean isMe) {
        this.isMe = isMe;
    }

    public int getNodeId() {
        return nodeId;
    }

    public void setNodeId(int nodeId) {
        this.nodeId = nodeId;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    public int getToNodeId() {
        return toNodeId;
    }

    public void setToNodeId(int toNodeId) {
        this.toNodeId = toNodeId;
    }
}
