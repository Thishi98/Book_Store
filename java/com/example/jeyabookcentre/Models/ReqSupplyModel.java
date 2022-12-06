package com.example.jeyabookcentre.Models;

import java.io.Serializable;

public class ReqSupplyModel implements Serializable {

    private String requestBook;
    private String author;
    private String suggestprice;
    private String requestUser;
    private String requestUserPhn;
    private String requestUID;
    private String publishername;
    private String type;
    private String quantity;
    private String price;

    public ReqSupplyModel() {
    }

    public ReqSupplyModel(String requestBook, String author, String suggestprice, String requestUser, String requestUserPhn, String requestUID, String publishername, String type, String quantity, String price) {
        this.requestBook = requestBook;
        this.author = author;
        this.suggestprice = suggestprice;
        this.requestUser = requestUser;
        this.requestUserPhn = requestUserPhn;
        this.requestUID = requestUID;
        this.publishername = publishername;
        this.type = type;
        this.quantity = quantity;
        this.price = price;
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

    public String getRequestUID() {
        return requestUID;
    }

    public void setRequestUID(String requestUID) {
        this.requestUID = requestUID;
    }

    public String getPublishername() {
        return publishername;
    }

    public void setPublishername(String publishername) {
        this.publishername = publishername;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
