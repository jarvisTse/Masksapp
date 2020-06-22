/*
# COMP 4521    # Tse Wing Hei    20446652    whtseae@connect.ust.hk
 */

package com.example.masksapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MaskAdapter extends ArrayAdapter<Mask> {
    public MaskAdapter(Context context, int resource, List<Mask> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Mask mask = getItem(position);
        @SuppressLint("ViewHolder") View oneMaskView = LayoutInflater.from(getContext()).inflate(R.layout.mask_item, parent, false);

        TextView mName = (TextView) oneMaskView.findViewById(R.id.tv_name);
        TextView mBrand = (TextView) oneMaskView.findViewById(R.id.tv_brand);
        TextView mMadeIn = (TextView) oneMaskView.findViewById(R.id.tv_made_in);
        TextView mPrice = (TextView) oneMaskView.findViewById(R.id.tv_suggested_price);
        TextView mRate = (TextView) oneMaskView.findViewById(R.id.tv_rate);
        ImageView iv_image = (ImageView) oneMaskView.findViewById(R.id.iv_image);

        mName.setText(mask.getName());
        mBrand.setText(mask.getBrand());
        mMadeIn.setText(mask.getMade_in());
        mPrice.setText(mask.stringPrice());
        mRate.setText(mask.calculateRate());

        Picasso.get().load(mask.getImage()).into(iv_image);

        return oneMaskView;
    }

}
