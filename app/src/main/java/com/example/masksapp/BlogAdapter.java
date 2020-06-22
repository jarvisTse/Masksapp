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

import com.squareup.picasso.Picasso;

import java.util.List;

public class BlogAdapter extends ArrayAdapter<Blog> {

    public BlogAdapter(Context context, int resource, List<Blog> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Blog blog = getItem(position);
        @SuppressLint("ViewHolder") View oneView = LayoutInflater.from(getContext()).inflate(R.layout.blog_item, parent, false);

        TextView tv_title = (TextView) oneView.findViewById(R.id.tv_title);
        ImageView iv_image = (ImageView) oneView.findViewById(R.id.iv_image);

        tv_title.setText(blog.getTitle());
        Picasso.get().load(blog.getImage()).into(iv_image);

        return oneView;
    }
}
