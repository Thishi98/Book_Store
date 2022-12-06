package com.example.jeyabookcentre.Models;

import android.widget.TextView;

import java.io.Serializable;
import java.util.List;

public class OrdHistory implements Serializable {

    private String key,order_id,order_date,order_status,Total_price,ordered_by,cust_name,cust_phone;

    public OrdHistory() {
    }

    public OrdHistory(String key, String order_id, String order_date, String order_status, String total_price, String ordered_by,
                      String cust_name, String cust_phone) {
        this.key = key;
        this.order_id = order_id;
        this.order_date = order_date;
        this.order_status = order_status;
        this.Total_price = total_price;
        this.ordered_by = ordered_by;
        this.cust_name = cust_name;
        this.cust_phone = cust_phone;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getTotal_price() {
        return Total_price;
    }

    public void setTotal_price(String total_price) {
        Total_price = total_price;
    }

    public String getOrdered_by() {
        return ordered_by;
    }

    public void setOrdered_by(String ordered_by) {
        this.ordered_by = ordered_by;
    }

    public String getCust_name() {
        return cust_name;
    }

    public void setCust_name(String cust_name) {
        this.cust_name = cust_name;
    }

    public String getCust_phone() {
        return cust_phone;
    }

    public void setCust_phone(String cust_phone) {
        this.cust_phone = cust_phone;
    }

}