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

public class ExchangeAdapter extends ArrayAdapter<Exchange> {

    public ExchangeAdapter(Context context, int resource, List<Exchange> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Exchange exchange = getItem(position);
        @SuppressLint("ViewHolder") View oneView = LayoutInflater.from(getContext()).inflate(R.layout.exchange_item, parent, false);

        ImageView iv_icon = (ImageView) oneView.findViewById(R.id.iv_icon);
        TextView tv_name = (TextView) oneView.findViewById(R.id.tv_name);
        TextView tv_date = (TextView) oneView.findViewById(R.id.tv_date);
        TextView tv_title = (TextView) oneView.findViewById(R.id.tv_title);

        Picasso.get().load(MainActivity.getHelper().getUser(exchange.getUid()).getIcon()).transform(new CircleTransform()).into(iv_icon);
        tv_name.setText(MainActivity.getHelper().getUser(exchange.getUid()).getUsername());
        tv_date.setText(exchange.getCreated_at());
        if (exchange.isFinished()) {
            String s = "[Sold Out] " + exchange.getTitle();
            tv_title.setText(s);
        } else {
            tv_title.setText(exchange.getTitle());
        }

        return oneView;
    }

}
