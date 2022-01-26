package com.perspectivev.entities;

public class Message {
    private int id;
    private String messageText;

    public Message(int id, String messageText) {
        this.id = id;
        this.messageText = messageText;
    }

    public Message() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }
}
