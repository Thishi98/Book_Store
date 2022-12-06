package com.example.jeyabookcentre.Models;

import java.io.Serializable;

public class Model implements Serializable {

    String key,isbn, bookname, bookauthor, bookpublisher, bookdesctript, bookcategory, subbookcategory,
            discprice, discnote, bookimage,quantity;
    // private ArrayList<Category_sub_section> arrayList;

    int bookprice;

    public Model(){

    }

    public Model(String key, String isbn, String bookname, String bookauthor, String bookpublisher, String bookdesctript, String bookcategory, String subbookcategory, String discprice, String discnote, String bookimage, String quantity, int bookprice) {
        this.key = key;
        this.isbn = isbn;
        this.bookname = bookname;
        this.bookauthor = bookauthor;
        this.bookpublisher = bookpublisher;
        this.bookdesctript = bookdesctript;
        this.bookcategory = bookcategory;
        this.subbookcategory = subbookcategory;
        this.discprice = discprice;
        this.discnote = discnote;
        this.bookimage = bookimage;
        this.quantity = quantity;
        this.bookprice = bookprice;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
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

    public String getBookpublisher() {
        return bookpublisher;
    }

    public void setBookpublisher(String bookpublisher) {
        this.bookpublisher = bookpublisher;
    }

    public String getBookdesctript() {
        return bookdesctript;
    }

    public void setBookdesctript(String bookdesctript) {
        this.bookdesctript = bookdesctript;
    }

    public String getBookcategory() {
        return bookcategory;
    }

    public void setBookcategory(String bookcategory) {
        this.bookcategory = bookcategory;
    }

    public String getSubbookcategory() {
        return subbookcategory;
    }

    public void setSubbookcategory(String subbookcategory) {
        this.subbookcategory = subbookcategory;
    }

    public String getDiscprice() {
        return discprice;
    }

    public void setDiscprice(String discprice) {
        this.discprice = discprice;
    }

    public String getDiscnote() {
        return discnote;
    }

    public void setDiscnote(String discnote) {
        this.discnote = discnote;
    }

    public String getBookimage() {
        return bookimage;
    }

    public void setBookimage(String bookimage) {
        this.bookimage = bookimage;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public int getBookprice() {
        return bookprice;
    }

    public void setBookprice(int bookprice) {
        this.bookprice = bookprice;
    }
}
