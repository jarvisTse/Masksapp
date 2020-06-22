/*
# COMP 4521    # Tse Wing Hei    20446652    whtseae@connect.ust.hk
 */

package com.example.masksapp;

import androidx.annotation.Nullable;

public class Exchange {
    private String id;
    private String uid;
    private String title;
    private String content;
    private String contact;
    private String mask_id;
    private String image;
    private boolean isFinished;
    private String created_at;

    public Exchange(String uid, String title, String content, String contact, @Nullable String mask_id, String created_at) {
        this.uid = uid;
        this.title = title;
        this.content = content;
        this.contact = contact;
        this.mask_id = mask_id;
        this.created_at = created_at;
        this.isFinished = false;
    }

    public Exchange(String id, String uid, String title, String content, String contact, @Nullable String mask_id, @Nullable String image, boolean isFinished, String created_at) {
        this.id = id;
        this.uid = uid;
        this.title = title;
        this.content = content;
        this.contact = contact;
        this.mask_id = mask_id;
        this.image = image;
        this.isFinished = isFinished;
        this.created_at = created_at;
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

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getMask_id() {
        return mask_id;
    }

    public void setMask_id(String mask_id) {
        this.mask_id = mask_id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
