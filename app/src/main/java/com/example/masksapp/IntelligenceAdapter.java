/*
# COMP 4521    # Tse Wing Hei    20446652    whtseae@connect.ust.hk
 */

package com.example.masksapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class IntelligenceAdapter extends ArrayAdapter<Intelligence> {

    public IntelligenceAdapter(Context context, int resource, List<Intelligence> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Intelligence intelligence = getItem(position);
        @SuppressLint("ViewHolder") View oneView = LayoutInflater.from(getContext()).inflate(R.layout.intelligence_item, parent, false);

        ImageView iv_icon = (ImageView) oneView.findViewById(R.id.iv_icon);
        TextView tv_name = (TextView) oneView.findViewById(R.id.tv_name);
        TextView tv_date = (TextView) oneView.findViewById(R.id.tv_date);
        TextView tv_price = (TextView) oneView.findViewById(R.id.tv_price);
        TextView tv_location = (TextView) oneView.findViewById(R.id.tv_location);
        Button btn_location = (Button) oneView.findViewById(R.id.btn_location);

        Picasso.get().load(MainActivity.getHelper().getUser(intelligence.getUid()).getIcon()).transform(new CircleTransform()).into(iv_icon);
        tv_name.setText(MainActivity.getHelper().getUser(intelligence.getUid()).getUsername());
        tv_date.setText(intelligence.getDate());
        tv_price.setText(intelligence.getPrice());
        tv_location.setText(intelligence.getLocation());

        btn_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MapsActivity.class);
                intent.putExtra("location", intelligence.getLocation());
                getContext().startActivity(intent);
            }
        });

        return oneView;
    }

}
