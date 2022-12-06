package com.example.jeyabookcentre.Models;

import java.io.Serializable;

public class CartModel implements Serializable {

    private String key,bookisbn,bookname,bookauthor,bookimg,bookprice,quantity;
    private int total_price,discount,total_quantity;

    public CartModel(){}

    public CartModel(String key, String bookname, String bookimg, String bookprice, String quantity, int total_price, String bookisbn, String bookauthor
            , int total_quantity, int discount) {
        this.key = key;
        this.bookname = bookname;
        this.bookimg = bookimg;
        this.bookprice = bookprice;
        this.quantity = quantity;
        this.total_price = total_price;
        this.bookisbn = bookisbn;
        this.bookauthor = bookauthor;
        this.total_quantity = total_quantity;
        this.discount = discount;
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

    public String getBookname() {
        return bookname;
    }

    public void setBookname(String bookname) {
        this.bookname = bookname;
    }

    public String getBookauthor() {
        return bookauthor;
    }

    public void setBookauthor(String bookauthor) {
        this.bookauthor = bookauthor;
    }

    public String getBookimg() {
        return bookimg;
    }

    public void setBookimg(String bookimg) {
        this.bookimg = bookimg;
    }

    public String getBookprice() {
        return bookprice;
    }

    public void setBookprice(String bookprice) {
        this.bookprice = bookprice;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public int getTotal_quantity() {
        return total_quantity;
    }

    public void setTotal_quantity(int total_quantity) {
        this.total_quantity = total_quantity;
    }

    public int getTotal_price() {
        return total_price;
    }

    public void setTotal_price(int total_price) {
        this.total_price = total_price;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }
}
