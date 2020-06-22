/*
# COMP 4521    # Tse Wing Hei    20446652    whtseae@connect.ust.hk
 */

package com.example.masksapp;

import java.util.ArrayList;

public class Blog {
    private String id;
    private String uid;
    private String title;
    private String content;
    private ArrayList<Comment> comments = new ArrayList<Comment>();
    private String image;
    private String date;

    public Blog(String uid, String title, String content, String date) {
        this.uid = uid;
        this.title = title;
        this.content = content;
        this.image = image;
        this.date = date;
    }

    public Blog(String id, String uid, String title, String content, ArrayList<Comment> comments, String image, String date) {
        this.id = id;
        this.uid = uid;
        this.title = title;
        this.content = content;
        this.comments = comments;
        this.image = image;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
