package com.example.jeyabookcentre.Models;

import java.io.Serializable;

public class FavouriteModel implements Serializable {

    private String key,Fisbn, Fbname, Fauthor, Fpublish,Fprice, Fsubcat, Fimg;

    public FavouriteModel() {}

    public FavouriteModel(String key, String fisbn, String fbname, String fauthor, String fpublish, String fprice, String fsubcat,
                          String fimg) {
        this.key = key;
        this.Fisbn = fisbn;
        this.Fbname = fbname;
        this.Fauthor = fauthor;
        this.Fpublish = fpublish;
        this.Fprice = fprice;
        this.Fsubcat = fsubcat;
        this.Fimg = fimg;
    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getFisbn() {
        return Fisbn;
    }

    public void setFisbn(String fisbn) {
        Fisbn = fisbn;
    }

    public String getFbname() {
        return Fbname;
    }

    public void setFbname(String fbname) {
        Fbname = fbname;
    }

    public String getFauthor() {
        return Fauthor;
    }

    public void setFauthor(String fauthor) {
        Fauthor = fauthor;
    }

    public String getFpublish() {
        return Fpublish;
    }

    public void setFpublish(String fpublish) {
        Fpublish = fpublish;
    }

    public String getFprice() {
        return Fprice;
    }

    public void setFprice(String fprice) {
        Fprice = fprice;
    }

    public String getFsubcat() {
        return Fsubcat;
    }

    public void setFsubcat(String fsubcat) {
        Fsubcat = fsubcat;
    }

    public String getFimg() {
        return Fimg;
    }

    public void setFimg(String fimg) {
        Fimg = fimg;
    }
}
