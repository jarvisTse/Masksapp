/*
# COMP 4521    # Tse Wing Hei    20446652    whtseae@connect.ust.hk
 */

package com.example.masksapp;

import android.graphics.Path;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class FirebaseDatabaseHelper {
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReferenceMasks;
    private DatabaseReference mReferenceUsers;
    private DatabaseReference mReferenceBlogs;
    private DatabaseReference mReferenceExchanges;
    private List<Mask> masks = new ArrayList<>();
    private List<User> users = new ArrayList<>();
    private List<Blog> blogs = new ArrayList<>();
    private List<Exchange> exchanges = new ArrayList<>();

//    public interface DataStatus {
//        void DataIsLoaded(List<Mask> masks, List<String> keys);
//        void DataIsInserted();
//        void DataIsUpdated();
//        void DataIsDeleted();
//    }

    public FirebaseDatabaseHelper() {
        mDatabase = FirebaseDatabase.getInstance();
        mReferenceMasks = mDatabase.getReference().child("masks");
        mReferenceUsers = mDatabase.getReference().child("users");
        mReferenceBlogs = mDatabase.getReference().child("blogs");
        mReferenceExchanges = mDatabase.getReference().child("exchanges");
    }

    public void readMasks(String search, final int sort_index, final int filter_index) {
        Query queryRef = mReferenceMasks;
        switch (sort_index) {
            case 0:
                queryRef = mReferenceMasks;
                break;
            case 1:
                queryRef = mReferenceMasks.orderByChild("name");
                break;
            case 2:
                queryRef = mReferenceMasks.orderByChild("rating_count");
                break;
            case 3:
                queryRef = mReferenceMasks.orderByChild("rating");
                break;
            case 4:
                queryRef = mReferenceMasks.orderByChild("suggested_price");
                break;
        }
        if (!search.equals("")) {
            queryRef = mReferenceMasks.orderByChild("name").startAt(search).endAt(search + "\uf8ff");
        }
        queryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                masks.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    masks.add(ds_to_mask(ds));
                }
                if (sort_index == 2 || sort_index == 3) {
                    Collections.reverse(masks);
                }
                List<Mask> mask_list = new ArrayList<>();
                for (Mask mask : masks) {
                    switch (filter_index) {
                        case 0:
                            mask_list.add(mask);
                            break;
                        case 1:
                            if (mask.getSuggested_price() < 100) {
                                mask_list.add(mask);
                            }
                            break;
                        case 2:
                            if (mask.getStandard().isBfe()) {
                                mask_list.add(mask);
                            }
                            break;
                        case 3:
                            if (mask.getStandard().isPfe()) {
                                mask_list.add(mask);
                            }
                            break;
                        case 4:
                            if (mask.getStandard().isVfe()) {
                                mask_list.add(mask);
                            }
                            break;
                    }
                }
                masks = mask_list;
                HomeFragment.getInstance().updateList();
                HomeFragment.getInstance().search_item_count(masks.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void writeMask(Mask mask) {
        mReferenceMasks.push().setValue(mask);
    }

    public List<Mask> getMasks() {
        return masks;
    }

    public Mask ds_to_mask(DataSnapshot ds) {
        String id = ds.getKey();
        String mName = ds.child("name").getValue().toString();
        String mBrand = null, mMadeIn = null, mSize = null, mPrice = null, mRating = null, mRateTotal = null, mRateCount = null, mDescription = null, mImage = null;
        double d_price = 10000;
        double d_rating = 0.0;
        int rateTotal = 0;
        int rateCount = 0;

        if (ds.child("brand").getValue() != null) mBrand = ds.child("brand").getValue().toString();
        if (ds.child("made_in").getValue() != null) mMadeIn = ds.child("made_in").getValue().toString();
        if (ds.child("size").getValue() != null) mSize = ds.child("size").getValue().toString();
        if (ds.child("suggested_price").getValue() != null) {
            mPrice = ds.child("suggested_price").getValue().toString();
            d_price = Double.parseDouble(mPrice);
        }
        if (ds.child("rating").getValue() != null) {
            mRating = ds.child("rating").getValue().toString();
            d_rating = Double.parseDouble(mRating);
        }
        if (ds.child("rating_total").getValue() != null) {
            mRateTotal = ds.child("rating_total").getValue().toString();
            rateTotal = Integer.parseInt(mRateTotal);
        }
        if (ds.child("rating_count").getValue() != null) {
            mRateCount = ds.child("rating_count").getValue().toString();
            rateCount = Integer.parseInt(mRateCount);
        }
        if (ds.child("description").getValue() != null) mDescription = ds.child("description").getValue().toString();
        if (ds.child("image").getValue() != null) mImage = ds.child("image").getValue().toString();

        Boolean bfe = Boolean.parseBoolean(ds.child("standard").child("bfe").getValue().toString());
        Boolean pfe = Boolean.parseBoolean(ds.child("standard").child("pfe").getValue().toString());
        Boolean vfe = Boolean.parseBoolean(ds.child("standard").child("vfe").getValue().toString());
        String special_standard = ds.child("standard").child("special_standard").getValue().toString();
        Standard standard = new Standard(bfe, pfe, vfe, special_standard);

        ArrayList<Comment> comments = new ArrayList<Comment>();
        for (DataSnapshot ds_comment : ds.child("comments").getChildren()) {
            String comment_id = ds_comment.getKey();
            String uid = ds_comment.child("uid").getValue().toString();
            int star = Integer.parseInt(ds_comment.child("star").getValue().toString());
            String comment = ds_comment.child("comment").getValue().toString();
            String date = ds_comment.child("date").getValue().toString();
            Comment c = new Comment(comment_id, uid, star, comment, date);
            comments.add(c);
        }

        ArrayList<Intelligence> intelligences = new ArrayList<Intelligence>();
        for (DataSnapshot ds_intelligence : ds.child("intelligences").getChildren()) {
            String intelligence_id = ds_intelligence.getKey();
            String uid = ds_intelligence.child("uid").getValue().toString();
            String price = ds_intelligence.child("price").getValue().toString();
            String location = ds_intelligence.child("location").getValue().toString();
            String date = ds_intelligence.child("date").getValue().toString();
            Intelligence i = new Intelligence(intelligence_id, uid, price, location, date);
            intelligences.add(i);
        }

        return new Mask(id, mName, mBrand, mMadeIn, mSize, d_price, d_rating, rateTotal, rateCount, mDescription, standard, mImage, comments, intelligences);
    }

    public Mask getMaskById(@Nullable String id) {
        if (id == null)
            return null;
        for (Mask mask : masks) {
            if (mask.getId().equals(id)) {
               return mask;
            }
        }
        return null;
    }

    public Comment getCommentByUid(String uid, String mask_id) {
        Mask mask = getMaskById(mask_id);
        for (Comment comment : mask.getComments()) {
            if (comment.getUid().equals(uid)) {
                return comment;
            }
        }
        return null;
    }

    public void writeComment(String mask_id, Comment comment) {
        mReferenceMasks.child(mask_id).child("comments").push().setValue(comment);
        Mask mask = getMaskById(mask_id);
        mask.setRating_count(mask.getRating_count() + 1);
        mask.setRating_total(mask.getRating_total() + comment.getStar());
        mask.setRating();
        mask.addComment(comment);
        mReferenceMasks.child(mask_id).child("rating_count").setValue(mask.getRating_count());
        mReferenceMasks.child(mask_id).child("rating_total").setValue(mask.getRating_total());
        mReferenceMasks.child(mask_id).child("rating").setValue(mask.getRating());
    }

    public void removeComment(String mask_id, Comment comment) {
        mReferenceMasks.child(mask_id).child("comments").child(comment.getId()).removeValue();

        Mask mask = getMaskById(mask_id);
        mask.setRating_count(mask.getRating_count() - 1);
        mask.setRating_total(mask.getRating_total() - comment.getStar());
        mask.setRating();
        mask.removeComment(comment);
        mReferenceMasks.child(mask_id).child("rating_count").setValue(mask.getRating_count());
        mReferenceMasks.child(mask_id).child("rating_total").setValue(mask.getRating_total());
        mReferenceMasks.child(mask_id).child("rating").setValue(mask.getRating());
    }

    public ArrayList<Comment> getComments(String mask_id) {
        Mask mask = getMaskById(mask_id);
        return mask.getComments();
    }

    public void writeUser(User user) {
        mReferenceUsers.child(user.getUid()).setValue(user);
    }

    public void readUser() {
        mReferenceUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                users.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String uid = ds.child("uid").getValue().toString();
                    String name = ds.child("username").getValue().toString();
                    String email = ds.child("email").getValue().toString();
                    String icon;
                    if (!ds.hasChild("icon")) {
                        icon = "https://firebasestorage.googleapis.com/v0/b/masksapp-cb9fb.appspot.com/o/images%2F5fae0a7a-c54b-493a-903b-fad8a0316e5a?alt=media&token=bdf588b8-9236-49ad-897b-6d78b7a201d0";
                    } else {
                        icon = ds.child("icon").getValue().toString();
                    }
                    User user = new User(uid, name, email, icon);
                    users.add(user);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public User getUser(String uid) {
        for (User user : users) {
            if (user.getUid().equals(uid)) {
                return user;
            }
        }
        return null;
    }

    public void editUserName(String uid, String name) {
        mReferenceUsers.child(uid).child("username").setValue(name);
    }

    public void editUserEmail(String uid, String email) {
        mReferenceUsers.child(uid).child("email").setValue(email);
    }

    public void userUploadIcon(String uid, String icon) {
        mReferenceUsers.child(uid).child("icon").setValue(icon);
    }

    public void writeIntelligence(String mask_id, Intelligence intelligence) {
        mReferenceMasks.child(mask_id).child("intelligences").push().setValue(intelligence);
        Mask mask = getMaskById(mask_id);
        mask.addIntelligence(intelligence);
    }

    public void readBlog(String search, final int sort_index) {
        Log.d("sort_index", String.valueOf(sort_index));
        Query queryRef = mReferenceBlogs;
        switch (sort_index) {
            case 0:
                queryRef = mReferenceBlogs;
                break;
            case 1:
                queryRef = mReferenceBlogs.orderByChild("date");
                break;
            case 2:
                queryRef = mReferenceBlogs.orderByChild("title");
                break;
        }
        if (!search.equals("")) {
            queryRef = mReferenceBlogs.orderByChild("title").startAt(search).endAt(search + "\uf8ff");
        }
        queryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                blogs.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String id = ds.getKey();
//                    String uid, title, content, image, date;
                    String uid = ds.child("uid").getValue().toString();
                    String title = ds.child("title").getValue().toString();
                    String content = ds.child("content").getValue().toString();
                    String image = ds.child("image").getValue().toString();
                    String date = ds.child("date").getValue().toString();
                    ArrayList<Comment> comments = new ArrayList<Comment>();
                    for (DataSnapshot ds_comment : ds.child("comments").getChildren()) {
                        String comment_id = ds_comment.getKey();
                        String c_uid = ds_comment.child("uid").getValue().toString();
                        int star = Integer.parseInt(ds_comment.child("star").getValue().toString());
                        String comment = ds_comment.child("comment").getValue().toString();
                        String c_date = ds_comment.child("date").getValue().toString();
                        Comment c = new Comment(comment_id, c_uid, star, comment, c_date);
                        comments.add(c);
                    }

                    Blog blog = new Blog(id, uid, title, content, comments, image, date);
                    blogs.add(blog);
                }
                if (sort_index == 1) {
                    Collections.reverse(blogs);
                }
                BlogFragment.getInstance().updateList();
                BlogFragment.getInstance().search_item_count(blogs.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void writeBlog(Blog blog) {
        mReferenceBlogs.push().setValue(blog);
    }

    public List<Blog> getBlogs() {
        return blogs;
    }

    public Blog getBlogById(String id) {
        for (Blog blog : blogs) {
            if (blog.getId().equals(id)) {
                return blog;
            }
        }
        return null;
    }

    public void writeExchange(Exchange exchange) {
        mReferenceExchanges.push().setValue(exchange);
    }

    public void readExchange(String search, final int sort_index) {
        Log.d("sort_index", String.valueOf(sort_index));
        Query queryRef = mReferenceExchanges;
        switch (sort_index) {
            case 0:
                queryRef = mReferenceExchanges;
                break;
            case 1:
                queryRef = mReferenceExchanges.orderByChild("date");
                break;
            case 2:
                queryRef = mReferenceExchanges.orderByChild("title");
                break;
        }
        if (!search.equals("")) {
            queryRef = mReferenceExchanges.orderByChild("title").startAt(search).endAt(search + "\uf8ff");
        }
        queryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                exchanges.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String mask_id = null;
                    String image = null;
                    String id = ds.getKey();
                    String uid = ds.child("uid").getValue().toString();
                    String title = ds.child("title").getValue().toString();
                    String content = ds.child("content").getValue().toString();
                    String contact = ds.child("contact").getValue().toString();
                    if (ds.hasChild("mask_id"))
                        mask_id = ds.child("mask_id").getValue().toString();
                    if (ds.hasChild("image"))
                        image = ds.child("image").getValue().toString();
                    String date = ds.child("created_at").getValue().toString();
                    String finished = ds.child("finished").getValue().toString();
                    boolean isFinished = Boolean.parseBoolean(finished);

                    Exchange exchange = new Exchange(id, uid, title, content, contact, mask_id, image, isFinished, date);
                    exchanges.add(exchange);
                }
                if (sort_index == 1) {
                    Collections.reverse(exchanges);
                }
                ExchangeFragment.getInstance().updateList();
                ExchangeFragment.getInstance().search_item_count(exchanges.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public List<Exchange> getExchanges() {
        return exchanges;
    }

    public Exchange getExchangeById(String id) {
        for (Exchange exchange : exchanges) {
            if (exchange.getId().equals(id)) {
                return exchange;
            }
        }
        return null;
    }

    public void setExchangeFinished(String id) {
        mReferenceExchanges.child(id).child("finished").setValue(true);
    }
}
