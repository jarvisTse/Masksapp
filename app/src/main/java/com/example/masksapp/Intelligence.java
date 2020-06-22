/*
# COMP 4521    # Tse Wing Hei    20446652    whtseae@connect.ust.hk
 */

package com.example.masksapp;

public class Intelligence {
    private String id;
    private String uid;
    private String price;
    private String location;
    private String date;

    public Intelligence(String uid, String price, String location, String date) {
        this.uid = uid;
        this.price = price;
        this.location = location;
        this.date = date;
    }

    public Intelligence(String id, String uid, String price, String location, String date) {
        this.id = id;
        this.uid = uid;
        this.price = price;
        this.location = location;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
