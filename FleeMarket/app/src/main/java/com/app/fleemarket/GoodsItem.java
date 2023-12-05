package com.app.fleemarket;

import java.io.Serializable;
import java.util.Map;

public class GoodsItem implements Serializable {
    private String id;
    private String title;
    private long created;
    private String authorId;
    private String author;
    private String content;
    private int price;
    private boolean isSoldout;
    private Map<String, ChatRoomItem> rooms;


    public GoodsItem(String id, String title, String content, long created, String author, int price, boolean isSoldout) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.created = created;
        this.author = author;
        this.price = price;
        this.isSoldout = isSoldout;
    }

    public GoodsItem() {

    }

    public Map<String, ChatRoomItem> getRooms() {
        return rooms;
    }

    public void setRooms(Map<String, ChatRoomItem> rooms) {
        this.rooms = rooms;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public boolean isSoldout() {
        return isSoldout;
    }

    public void setSoldout(boolean soldout) {
        isSoldout = soldout;
    }

    @Override
    public String toString() {
        return "BoardItem{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", created=" + created +
                ", authorId='" + authorId + '\'' +
                ", author='" + author + '\'' +
                ", content='" + content + '\'' +
                ", price=" + price +
                ", isSoldout=" + isSoldout +
                '}';
    }
}
