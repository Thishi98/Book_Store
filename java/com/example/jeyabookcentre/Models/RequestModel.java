package com.example.jeyabookcentre.Models;

import java.io.Serializable;

public class RequestModel implements Serializable {

    private String key;
    private String requestBook;
    private String author;
    private String suggestprice;
    private String requestUser;
    private String requestUserPhn;
    private String Progress;
    private String requestUID;

    public RequestModel() {
    }

    public RequestModel(String key, String requestBook, String author, String suggestprice, String requestUser,
                        String requestUserPhn, String progress, String requestUID) {
        this.key = key;
        this.requestBook = requestBook;
        this.author = author;
        this.suggestprice = suggestprice;
        this.requestUser = requestUser;
        this.requestUserPhn = requestUserPhn;
        Progress = progress;
        this.requestUID = requestUID;
    }

    public String getRequestUID() {
        return requestUID;
    }

    public void setRequestUID(String requestUID) {
        this.requestUID = requestUID;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getRequestBook() {
        return requestBook;
    }

    public void setRequestBook(String requestBook) {
        this.requestBook = requestBook;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getSuggestprice() {
        return suggestprice;
    }

    public void setSuggestprice(String suggestprice) {
        this.suggestprice = suggestprice;
    }

    public String getRequestUser() {
        return requestUser;
    }

    public void setRequestUser(String requestUser) {
        this.requestUser = requestUser;
    }

    public String getRequestUserPhn() {
        return requestUserPhn;
    }

    public void setRequestUserPhn(String requestUserPhn) {
        this.requestUserPhn = requestUserPhn;
    }

    public String getProgress() {
        return Progress;
    }

    public void setProgress(String progress) {
        Progress = progress;
    }
}
