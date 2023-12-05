package com.app.fleemarket;


public class ChatRoomItem {
    String id;
    String boardId;
    String title;
    String lastMessage;
    String sender;
    String receiver;
    long lastMessageTime;

    public ChatRoomItem(String id, String boardId, String title, String lastMessage, String sender, long lastMessageTime) {
        this.id = id;
        this.boardId = boardId;
        this.title = title;
        this.lastMessage = lastMessage;
        this.sender = sender;
        this.lastMessageTime = lastMessageTime;
    }

    public ChatRoomItem() {}

    public String getBoardId() {
        return boardId;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public void setBoardId(String boardId) {
        this.boardId = boardId;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public long getLastMessageTime() {
        return lastMessageTime;
    }

    public void setLastMessageTime(long lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
    }

    @Override
    public String toString() {
        return "ChatRoomItem{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", lastMessage='" + lastMessage + '\'' +
                ", sender='" + sender + '\'' +
                ", lastMessageTime=" + lastMessageTime +
                '}';
    }
}
