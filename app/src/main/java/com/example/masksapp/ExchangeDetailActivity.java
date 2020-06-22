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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

public class ExchangeDetailActivity extends AppCompatActivity {

    private Exchange exchange;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_detail);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        Intent i = getIntent();
        final String exchange_id = i.getStringExtra("exchange_id");
        exchange = MainActivity.getHelper().getExchangeById(exchange_id);

        ImageView iv_image = (ImageView) findViewById(R.id.iv_image);
        ImageView iv_icon = (ImageView) findViewById(R.id.iv_icon);
        TextView tv_header = (TextView) findViewById(R.id.tv_header);
        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        TextView tv_date = (TextView) findViewById(R.id.tv_date);
        TextView tv_author = (TextView) findViewById(R.id.tv_author);
        TextView tv_content = (TextView) findViewById(R.id.tv_content);
        TextView tv_contact = (TextView) findViewById(R.id.tv_contact);

        if (exchange.getImage() != null) {
            Picasso.get().load(exchange.getImage()).into(iv_image);
        } else {
            iv_image.setVisibility(View.GONE);
        }
        Picasso.get().load(MainActivity.getHelper().getUser(exchange.getUid()).getIcon()).transform(new CircleTransform()).into(iv_icon);
        tv_header.setText(exchange.getTitle());
        tv_title.setText(exchange.getTitle());
        tv_date.setText(exchange.getCreated_at());
        tv_author.setText(MainActivity.getHelper().getUser(exchange.getUid()).getUsername());
        tv_content.setText(exchange.getContent());
        tv_contact.setText(exchange.getContact());

        ImageView btn_back = (ImageView) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        TextView tv_viewMaskDetail = (TextView) findViewById(R.id.tv_viewMaskDetail);
        if (exchange.getMask_id() != null) {
            tv_viewMaskDetail.setVisibility(View.VISIBLE);
            tv_viewMaskDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ExchangeDetailActivity.this, MaskDetailActivity.class);
                    intent.putExtra("mask_id", exchange.getMask_id());
                    startActivity(intent);
                }
            });
        }

        final LinearLayout layout_sold_out = (LinearLayout) findViewById(R.id.layout_sold_out);
        final Button btn_sold_out = (Button) findViewById(R.id.btn_sold_out);
        if (exchange.isFinished()) {
            layout_sold_out.setVisibility(View.VISIBLE);
        } else {
            layout_sold_out.setVisibility(View.GONE);
        }

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            if (user.getUid().equals(exchange.getUid()) && !exchange.isFinished()) {
                btn_sold_out.setVisibility(View.VISIBLE);
                btn_sold_out.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MainActivity.getHelper().setExchangeFinished(exchange.getId());
                        btn_sold_out.setVisibility(View.GONE);
                        layout_sold_out.setVisibility(View.VISIBLE);
                        Toast.makeText(ExchangeDetailActivity.this, "This selling post status has declared as sold out.",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }
}
