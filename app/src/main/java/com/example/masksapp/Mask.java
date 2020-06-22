/*
# COMP 4521    # Tse Wing Hei    20446652    whtseae@connect.ust.hk
 */

package com.example.masksapp;

import android.util.Log;

import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Mask {
    private String id;
    private String name;
    private String brand;
    private String made_in;
    private String size;
    private double suggested_price;
    private double rating;
    private int rating_total;
    private int rating_count;
    private String description;
    private Standard standard;
    private ArrayList<Comment> comments = new ArrayList<Comment>();
    private ArrayList<Intelligence> intelligences = new ArrayList<Intelligence>();
    private String image;

    public Mask() {

    }

    public Mask(String name, @Nullable String brand, @Nullable String made_in, @Nullable String size, double suggested_price, @Nullable String description, @Nullable Standard standard) {
        this.name = name;
        this.brand = brand;
        this.made_in = made_in;
        this.size = size;
        this.suggested_price = suggested_price;
        this.rating = 0.0;
        this.rating_total = 0;
        this.rating_count = 0;
        this.description = description;
        this.standard = standard;
        this.image = null;
    }


    public Mask(String id, String name, @Nullable String brand, @Nullable String made_in, @Nullable String size, double suggested_price, double rating, int rating_total, int rating_count, @Nullable String description, @Nullable Standard standard, @Nullable String image, @Nullable ArrayList<Comment> comments, @Nullable ArrayList<Intelligence> intelligences) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.made_in = made_in;
        this.size = size;
        this.suggested_price = suggested_price;
        this.rating = rating;
        this.rating_total = rating_total;
        this.rating_count = rating_count;
        this.description = description;
        this.standard = standard;
        this.comments = comments;
        this.intelligences = intelligences;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getMade_in() {
        return made_in;
    }

    public void setMade_in(String made_in) {
        this.made_in = made_in;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public double getSuggested_price() {
        return suggested_price;
    }

    public void setSuggested_price(double suggested_price) {
        this.suggested_price = suggested_price;
    }

    public String stringPrice() {
        if (suggested_price > 9999 || suggested_price < 0) {
            return "N/A";
        } else {
            return String.valueOf(suggested_price);
        }
    }

    public double getRating() {
        return rating;
    }

    public void setRating() {
        if (rating_count == 0) {
            rating = 0.0;
        } else {
            rating = (double) rating_total / (double) rating_count;
        }
    }

    public int getRating_total() {
        return rating_total;
    }

    public void setRating_total(int rating_total) {
        this.rating_total = rating_total;
    }

    public int getRating_count() {
        return rating_count;
    }

    public void setRating_count(int rating_count) {
        this.rating_count = rating_count;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Standard getStandard() {
        return standard;
    }

    public void setStandard(Standard standard) {
        this.standard = standard;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String calculateRate() {
        int count = rating_count;
        int total = rating_total;
        double rate = (double) total / (double) count;
        double roundedRate = Math. round(rate * 10) / 10.0;
        return String.valueOf(roundedRate);
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }

    public void addComment(Comment comment) {
        comments.add(comment);
    }

    public void removeComment(Comment comment) {
        comments.remove(comment);
    }

    public ArrayList<Intelligence> getIntelligences() {
        return intelligences;
    }

    public void setIntelligences(ArrayList<Intelligence> intelligences) {
        this.intelligences = intelligences;
    }

    public void addIntelligence(Intelligence intelligence) {
        intelligences.add(intelligence);
    }
}
