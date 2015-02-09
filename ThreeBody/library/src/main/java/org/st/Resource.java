package org.st;

public class Resource{
    public enum ResourceOpt {PUB,UNPUB,SUB,UNSUB,MUTE,UNMUTE,FORBID,UNFORBID};
    public enum ResourceState { INIT, PUBLISHED,MUTED,FORBIDED,SUBED };
    String id;
    int owner_id;
    MediaStream stream;
    public boolean open(){
	return true;
    }
    public boolean close(){
	return true;
    }

}
