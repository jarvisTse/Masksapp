/*
# COMP 4521    # Tse Wing Hei    20446652    whtseae@connect.ust.hk
 */

package com.example.masksapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import java.util.List;

public class CommentsActivity extends AppCompatActivity {

    String mask_id;
    Mask mask;
    ListView list_comment;
    Boolean view_comment;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mask_comments);

        Intent i = getIntent();
        mask_id = i.getStringExtra("mask_id");

        MainActivity.getHelper().readUser();
        mask = MainActivity.getHelper().getMaskById(mask_id);
        list_comment = (ListView) findViewById(R.id.list_comment);

        view_comment = true;
        List<Comment> comments = MainActivity.getHelper().getComments(mask_id);
        CommentAdapter commentAdapter = new CommentAdapter(CommentsActivity.this, R.layout.comment_item, comments);
        list_comment.setAdapter(commentAdapter);

        final Button btn_intelligence = (Button) findViewById(R.id.btn_intelligence);
        final Button btn_comment = (Button) findViewById(R.id.btn_comment);
        final TextView tv_no_record = (TextView) findViewById(R.id.tv_no_record);
        TextView tv_header = (TextView) findViewById(R.id.tv_header);
        tv_header.setText(mask.getName());

        if (comments.isEmpty()) {
            tv_no_record.setVisibility(View.VISIBLE);
        } else {
            tv_no_record.setVisibility(View.GONE);
        }

        btn_intelligence.performClick();


        btn_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!view_comment) {
                    List<Comment> comments = mask.getComments();
                    CommentAdapter commentAdapter = new CommentAdapter(CommentsActivity.this, R.layout.comment_item, comments);
                    list_comment.setAdapter(commentAdapter);

                    btn_comment.setBackgroundResource(R.drawable.btn_round_main);
                    btn_comment.setTextColor(getResources().getColor(R.color.colorWhite));
                    btn_intelligence.setBackgroundResource(R.drawable.btn_round_white_border);
                    btn_intelligence.setTextColor(getResources().getColor(R.color.colorMain));

                    if (comments.isEmpty()) {
                        tv_no_record.setVisibility(View.VISIBLE);
                    } else {
                        tv_no_record.setVisibility(View.GONE);
                    }

                    view_comment = true;
                }
            }
        });

        btn_intelligence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (view_comment) {
                    final List<Intelligence> intelligences = mask.getIntelligences();
                    final IntelligenceAdapter intelligenceAdapter = new IntelligenceAdapter(CommentsActivity.this, R.layout.intelligence_item, intelligences);
                    list_comment.setAdapter(intelligenceAdapter);
//                    list_comment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                        @Override
//                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                            if (list_comment.getAdapter() == intelligenceAdapter) {
//                                Log.d("item", "click");
//                                Intent intent = new Intent(CommentsActivity.this, MapsActivity.class);
//                                intent.putExtra("location", intelligences.get(position).getLocation());
//                                startActivity(intent);
//                            }
//                            Log.d("comment", "click");
//                        }
//                    });


                    btn_intelligence.setBackgroundResource(R.drawable.btn_round_main);
                    btn_intelligence.setTextColor(getResources().getColor(R.color.colorWhite));
                    btn_comment.setBackgroundResource(R.drawable.btn_round_white_border);
                    btn_comment.setTextColor(getResources().getColor(R.color.colorMain));

                    if (intelligences.isEmpty()) {
                        tv_no_record.setVisibility(View.VISIBLE);
                    } else {
                        tv_no_record.setVisibility(View.GONE);
                    }

                    view_comment = false;
                }
            }
        });

        ImageView btn_back = (ImageView) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });
    }
}
