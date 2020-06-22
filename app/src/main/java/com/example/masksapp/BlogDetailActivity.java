/*
# COMP 4521    # Tse Wing Hei    20446652    whtseae@connect.ust.hk
 */

package com.example.masksapp;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

public class BlogDetailActivity extends AppCompatActivity {

    private Blog blog;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_detail);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        Intent i = getIntent();
        final String blog_id = i.getStringExtra("blog_id");
        blog = MainActivity.getHelper().getBlogById(blog_id);

        ImageView iv_image = (ImageView) findViewById(R.id.iv_image);
        ImageView iv_icon = (ImageView) findViewById(R.id.iv_icon);
        TextView tv_header = (TextView) findViewById(R.id.tv_header);
        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        TextView tv_date = (TextView) findViewById(R.id.tv_date);
        TextView tv_author = (TextView) findViewById(R.id.tv_author);
        TextView tv_content = (TextView) findViewById(R.id.tv_content);

        Picasso.get().load(blog.getImage()).into(iv_image);
        Picasso.get().load(MainActivity.getHelper().getUser(blog.getUid()).getIcon()).transform(new CircleTransform()).into(iv_icon);
        tv_header.setText(blog.getTitle());
        tv_title.setText(blog.getTitle());
        tv_date.setText(blog.getDate());
        tv_author.setText(MainActivity.getHelper().getUser(blog.getUid()).getUsername());
        tv_content.setText(blog.getContent());

        ImageView btn_back = (ImageView) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
