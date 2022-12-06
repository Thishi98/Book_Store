package com.example.jeyabookcentre.Models;

import java.io.Serializable;

public class SearchModel implements Serializable {

    private String key,bookname,bookauthor,subbookcategory,bookprice;
    private String isbn,bookpublisher,bookdesctript, bookcategory, bookimage,discprice,discnote,quantity,bookStatus;

    public SearchModel() {
    }

    public SearchModel(String key, String bookname, String bookauthor, String subbookcategory, String bookprice,
                       String isbn, String bookpublisher, String bookdesctript, String bookcategory, String bookimage,
                       String discprice, String discnote, String quantity,String bookStatus) {
        this.key = key;
        this.bookname = bookname;
        this.bookauthor = bookauthor;
        this.subbookcategory = subbookcategory;
        this.bookprice = bookprice;
        this.isbn = isbn;
        this.bookpublisher = bookpublisher;
        this.bookdesctript = bookdesctript;
        this.bookcategory = bookcategory;
        this.bookimage = bookimage;
        this.discprice = discprice;
        this.discnote = discnote;
        this.quantity = quantity;
        this.bookStatus = bookStatus;
    }

    public String getBookStatus() {
        return bookStatus;
    }

    public void setBookStatus(String bookStatus) {
        this.bookStatus = bookStatus;
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

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
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

    public String getBookimage() {
        return bookimage;
    }

    public void setBookimage(String bookimage) {
        this.bookimage = bookimage;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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

    public String getSubbookcategory() {
        return subbookcategory;
    }

    public void setSubbookcategory(String subbookcategory) {
        this.subbookcategory = subbookcategory;
    }

    public String getBookprice() {
        return bookprice;
    }

    public void setBookprice(String bookprice) {
        this.bookprice = bookprice;
    }

}
