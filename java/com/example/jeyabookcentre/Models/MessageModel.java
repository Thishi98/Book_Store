package com.example.jeyabookcentre.Models;

public class MessageModel {

    private String msgID;
    private String senderID;
    private String RecieverID;
    private String message;
    private long timestamp;
    private int totalChats;

    public MessageModel() {
    }

    public MessageModel(String msgID, String senderID, String recieverID, String message, long timestamp, int totalChats) {
        this.msgID = msgID;
        this.senderID = senderID;
        this.RecieverID = recieverID;
        this.message = message;
        this.timestamp = timestamp;
        this.totalChats = totalChats;
    }

    public int getTotalChats() {
        return totalChats;
    }

    public void setTotalChats(int totalChats) {
        this.totalChats = totalChats;
    }

    public String getRecieverID() {
        return RecieverID;
    }

    public void setRecieverID(String recieverID) {
        RecieverID = recieverID;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getMsgID() {
        return msgID;
    }

    public void setMsgID(String msgID) {
        this.msgID = msgID;
    }

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
