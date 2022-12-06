package com.example.jeyabookcentre.Models;

import java.io.Serializable;

public class Promotions implements Serializable {

    String promotion_id,promotion_note,promotion_img;

    public Promotions() {

    }

    public Promotions(String promotion_id, String promotion_note, String promotion_img) {
        this.promotion_id = promotion_id;
        this.promotion_note = promotion_note;
        this.promotion_img = promotion_img;
    }

    public String getPromotion_id() {
        return promotion_id;
    }

    public void setPromotion_id(String promotion_id) {
        this.promotion_id = promotion_id;
    }

    public String getPromotion_note() {
        return promotion_note;
    }

    public void setPromotion_note(String promotion_note) {
        this.promotion_note = promotion_note;
    }

    public String getPromotion_img() {
        return promotion_img;
    }

    public void setPromotion_img(String promotion_img) {
        this.promotion_img = promotion_img;
    }
}
