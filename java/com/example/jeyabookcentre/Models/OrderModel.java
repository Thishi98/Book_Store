package com.example.jeyabookcentre.Models;

public class OrderModel {

    private String key,bookisbn,bookName,bookAuthor,bookImg,bookprice;
    private int total_price,total_quantity;

    public OrderModel() {
    }

    public OrderModel(String key, String bookisbn, String bookName, String bookAuthor, String bookImg, String bookprice, int total_price, int total_quantity) {
        this.key = key;
        this.bookisbn = bookisbn;
        this.bookName = bookName;
        this.bookAuthor = bookAuthor;
        this.bookImg = bookImg;
        this.bookprice = bookprice;
        this.total_price = total_price;
        this.total_quantity = total_quantity;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getBookisbn() {
        return bookisbn;
    }

    public void setBookisbn(String bookisbn) {
        this.bookisbn = bookisbn;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }

    public String getBookImg() {
        return bookImg;
    }

    public void setBookImg(String bookImg) {
        this.bookImg = bookImg;
    }

    public String getBookprice() {
        return bookprice;
    }

    public void setBookprice(String bookprice) {
        this.bookprice = bookprice;
    }

    public int getTotal_price() {
        return total_price;
    }

    public void setTotal_price(int total_price) {
        this.total_price = total_price;
    }

    public int getTotal_quantity() {
        return total_quantity;
    }

    public void setTotal_quantity(int total_quantity) {
        this.total_quantity = total_quantity;
    }
}
