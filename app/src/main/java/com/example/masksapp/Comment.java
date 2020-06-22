/*
# COMP 4521    # Tse Wing Hei    20446652    whtseae@connect.ust.hk
 */

package com.example.masksapp;

public class Comment {
    private String id;
    private String uid;
    private int star;
    private String comment;
    private String date;

    public Comment(String uid, int star, String comment, String date) {
        this.uid = uid;
        this.star = star;
        this.comment = comment;
        this.date = date;
    }

    public Comment(String id, String uid, int star, String comment, String date) {
        this.id = id;
        this.uid = uid;
        this.star = star;
        this.comment = comment;
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

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
