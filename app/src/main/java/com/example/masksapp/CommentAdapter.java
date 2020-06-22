/*
# COMP 4521    # Tse Wing Hei    20446652    whtseae@connect.ust.hk
 */

package com.example.masksapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CommentAdapter extends ArrayAdapter<Comment> {

    public CommentAdapter(Context context, int resource, List<Comment> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Comment comment = getItem(position);
        @SuppressLint("ViewHolder") View oneView = LayoutInflater.from(getContext()).inflate(R.layout.comment_item, parent, false);

        ImageView iv_icon = (ImageView) oneView.findViewById(R.id.iv_icon);
        TextView tv_name = (TextView) oneView.findViewById(R.id.tv_name);
        TextView tv_date = (TextView) oneView.findViewById(R.id.tv_date);
        TextView tv_comment = (TextView) oneView.findViewById(R.id.tv_comment);
        RatingBar rb_rating = (RatingBar) oneView.findViewById(R.id.rb_rating);

        Picasso.get().load(MainActivity.getHelper().getUser(comment.getUid()).getIcon()).transform(new CircleTransform()).into(iv_icon);
        tv_name.setText(MainActivity.getHelper().getUser(comment.getUid()).getUsername());
        tv_date.setText(comment.getDate());
        tv_comment.setText(comment.getComment());
        rb_rating.setRating(comment.getStar());

        return oneView;
    }

}
