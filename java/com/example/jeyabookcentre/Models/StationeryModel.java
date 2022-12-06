package com.example.jeyabookcentre.Models;

import java.io.Serializable;

public class StationeryModel implements Serializable {

    String key,item_code, itemname, itemauthor, itempublisher,itemprice, itemdesctript, quantity, itemcategory,
            Itemimage, discprice, discnote,itemStatus;


    public StationeryModel() {
    }

    public StationeryModel(String key, String item_code, String itemname, String itemauthor, String itempublisher, String itemprice,
                           String itemdesctript, String quantity, String itemcategory, String itemimage, String discprice,
                           String discnote,String itemStatus) {
        this.key = key;
        this.item_code = item_code;
        this.itemname = itemname;
        this.itemauthor = itemauthor;
        this.itempublisher = itempublisher;
        this.itemprice = itemprice;
        this.itemdesctript = itemdesctript;
        this.quantity = quantity;
        this.itemcategory = itemcategory;
        this.Itemimage = itemimage;
        this.discprice = discprice;
        this.discnote = discnote;
        this.itemStatus = itemStatus;
    }

    public String getItemStatus() {
        return itemStatus;
    }

    public void setItemStatus(String itemStatus) {
        this.itemStatus = itemStatus;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getItem_code() {
        return item_code;
    }

    public void setItem_code(String item_code) {
        this.item_code = item_code;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public String getItemauthor() {
        return itemauthor;
    }

    public void setItemauthor(String itemauthor) {
        this.itemauthor = itemauthor;
    }

    public String getItempublisher() {
        return itempublisher;
    }

    public void setItempublisher(String itempublisher) {
        this.itempublisher = itempublisher;
    }

    public String getItemprice() {
        return itemprice;
    }

    public void setItemprice(String itemprice) {
        this.itemprice = itemprice;
    }

    public String getItemdesctript() {
        return itemdesctript;
    }

    public void setItemdesctript(String itemdesctript) {
        this.itemdesctript = itemdesctript;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getItemcategory() {
        return itemcategory;
    }

    public void setItemcategory(String itemcategory) {
        this.itemcategory = itemcategory;
    }

    public String getItemimage() {
        return Itemimage;
    }

    public void setItemimage(String itemimage) {
        Itemimage = itemimage;
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
}
