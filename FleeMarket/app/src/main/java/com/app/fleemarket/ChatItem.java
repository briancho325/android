package com.app.fleemarket;


public class ChatItem {
    String id;
    String message;
    String sender;
    long time;
    boolean isMe;

    public ChatItem(String id, String message, String sender, long time, boolean isMe) {
        this.id = id;
        this.message = message;
        this.sender = sender;
        this.time = time;
        this.isMe = isMe;
    }

    public ChatItem() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean isMe() {
        return isMe;
    }

    public void setMe(boolean me) {
        isMe = me;
    }
}
