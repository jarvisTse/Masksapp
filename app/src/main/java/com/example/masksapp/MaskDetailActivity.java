/*
# COMP 4521    # Tse Wing Hei    20446652    whtseae@connect.ust.hk
 */

package com.example.masksapp;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MaskDetailActivity extends AppCompatActivity {

    private Mask mask;
    TextView tv_rating;
    RatingBar rb_rating;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mask_detail);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        Intent i = getIntent();
//        mask = (Mask) i.getExtras().getSerializable("Mask");
        final String mask_id = i.getStringExtra("mask_id");
        mask = MainActivity.getHelper().getMaskById(mask_id);

        ImageView iv_image = (ImageView) findViewById(R.id.iv_image);
        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        TextView tv_name = (TextView) findViewById(R.id.tv_name);
        TextView tv_brand = (TextView) findViewById(R.id.tv_brand);
        TextView tv_size = (TextView) findViewById(R.id.tv_size);
        TextView tv_suggested_price = (TextView) findViewById(R.id.tv_suggested_price);
        TextView tv_made_in = (TextView) findViewById(R.id.tv_made_in);
        TextView tv_special_standard = (TextView) findViewById(R.id.tv_special_standard);
        TextView tv_efficiency = (TextView) findViewById(R.id.tv_efficiency);
        TextView tv_description = (TextView) findViewById(R.id.tv_description);
        TextView tv_mask_id = (TextView) findViewById(R.id.tv_mask_id);
        tv_rating = (TextView) findViewById(R.id.tv_rating);
        rb_rating = (RatingBar) findViewById(R.id.rb_rating);

        Picasso.get().load(mask.getImage()).into(iv_image);
        tv_title.setText(mask.getName());
        tv_name.setText(mask.getName());
        tv_brand.setText(mask.getBrand());
        tv_made_in.setText(mask.getMade_in());
        tv_size.setText(mask.getSize());
        tv_suggested_price.setText("HK$ " + mask.stringPrice());
        tv_special_standard.setText(mask.getStandard().getSpecial_standard());
        tv_description.setText(mask.getDescription());
        tv_mask_id.setText(mask.getId());

        tv_rating.setText(mask.calculateRate());
        rb_rating.setRating(Float.parseFloat(mask.calculateRate()));

        boolean hasOne = false;
        tv_efficiency.setText("None");
        if (mask.getStandard().isBfe()) {
            tv_efficiency.setText("BFE > 95%");
            hasOne = true;
        }
        if (mask.getStandard().isPfe()) {
            if (hasOne) {
                tv_efficiency.setText(tv_efficiency.getText() + ", PFE > 95%");
            } else {
                tv_efficiency.setText("PFE > 95%");
                hasOne = true;
            }
        }
        if (mask.getStandard().isVfe()) {
            if (hasOne) {
                tv_efficiency.setText(tv_efficiency.getText() + ", VFE > 95%");
            } else {
                tv_efficiency.setText("VFE > 95%");
            }
        }

        ImageView btn_back = (ImageView) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        TextView tv_viewComment = (TextView) findViewById(R.id.tv_viewComment);
        tv_viewComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MaskDetailActivity.this, CommentsActivity.class);
                intent.putExtra("mask_id", mask_id);
                startActivity(intent);
            }
        });

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        LinearLayout layout_comment = (LinearLayout) findViewById(R.id.layout_comment);
        layout_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user != null) {
                    showCommentDialog();
                } else {
                    showLoginFirstDialog(0);
                }
            }
        });

        LinearLayout layout_intelligence = (LinearLayout) findViewById(R.id.layout_intelligence);
        layout_intelligence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user != null) {
                    showIntelligenceDialog();
                } else {
                    showLoginFirstDialog(1);
                }
            }
        });

        TextView btn_info_fe = (TextView) findViewById(R.id.btn_info_fe);
        btn_info_fe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTipsDialog(0);
            }
        });

        TextView btn_info_ss = (TextView) findViewById(R.id.btn_info_ss);
        btn_info_ss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTipsDialog(1);
            }
        });

        TextView btn_info_id = (TextView) findViewById(R.id.btn_info_id);
        btn_info_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTipsDialog(2);
            }
        });

        Button btn_copy = (Button) findViewById(R.id.btn_copy);
        btn_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("mask ID", mask.getId());
                clipboard.setPrimaryClip(clip);

                Toast.makeText(MaskDetailActivity.this, "Mask ID has copied to clipboard.",Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void showLoginFirstDialog(int i) {
        LayoutInflater factory = LayoutInflater.from(this);
        final View dialogView = factory.inflate(R.layout.dialog_login_first, null);
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(MaskDetailActivity.this);

        TextView tv_dialog_header = (TextView) dialogView.findViewById(R.id.tv_dialog_header);
        switch (i) {
            case 0:
                tv_dialog_header.setText(R.string.rateComment);
                break;
            case 1:
                tv_dialog_header.setText(R.string.provideIntelligence);
                break;
        }
        alertDialog.setView(dialogView);

        final AlertDialog ad = alertDialog.show();

        Button btn_login = (Button) dialogView.findViewById(R.id.btn_login);
        Button btn_cancel = (Button) dialogView.findViewById(R.id.btn_cnacel);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MaskDetailActivity.this, MainActivity.class);
                intent.putExtra("frame_id", 4);
                startActivity(intent);
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ad.dismiss();
            }
        });
    }

    public void showCommentDialog() {
        LayoutInflater factory = LayoutInflater.from(this);
        final View dialogView = factory.inflate(R.layout.dialog_comment, null);
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(MaskDetailActivity.this);

        final RatingBar ratingBar = (RatingBar) dialogView.findViewById(R.id.rb_rating);
        final TextView tv_rate = (TextView) dialogView.findViewById(R.id.tv_rate);
        final EditText et_comment = (EditText) dialogView.findViewById(R.id.et_comment);

        final Comment past_comment = MainActivity.getHelper().getCommentByUid(FirebaseAuth.getInstance().getCurrentUser().getUid(), mask.getId());
        if (past_comment != null) {
            ratingBar.setRating(past_comment.getStar());
            et_comment.setText(past_comment.getComment());
        }
        tv_rate.setText(String.valueOf((int)ratingBar.getRating()));

        TextView tv_name = (TextView) dialogView.findViewById(R.id.tv_name);
        tv_name.setText(mask.getName());
        alertDialog.setView(dialogView);

        final AlertDialog ad = alertDialog.show();

        Button btn_submit = (Button) dialogView.findViewById(R.id.btn_submit);
        Button btn_cancel = (Button) dialogView.findViewById(R.id.btn_cnacel);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if ( fromUser ) {
                    ratingBar.setRating((float) Math.ceil(rating));
                    tv_rate.setText(String.valueOf((int)ratingBar.getRating()));
                }
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                int rate = (int) ratingBar.getRating();
                String content = "";
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date date = new Date();
                String strDate = dateFormat.format(date);
                if (et_comment.getText() != null) {
                    content = et_comment.getText().toString();
                }
                Comment comment = new Comment(uid, rate, content, strDate);
                MainActivity.getHelper().writeComment(mask.getId(), comment);

                if (past_comment != null) {
                    MainActivity.getHelper().removeComment(mask.getId(), past_comment);
                }

                tv_rating.setText(mask.calculateRate());
                rb_rating.setRating(Float.parseFloat(mask.calculateRate()));
                Toast.makeText(MaskDetailActivity.this, "Comment has been created.",Toast.LENGTH_SHORT).show();
                ad.dismiss();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ad.dismiss();
            }
        });
    }

    public void showIntelligenceDialog() {
        LayoutInflater factory = LayoutInflater.from(this);
        final View dialogView = factory.inflate(R.layout.dialog_intelligence, null);
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(MaskDetailActivity.this);

        final EditText et_price = (EditText) dialogView.findViewById(R.id.et_price);
        final EditText et_location = (EditText) dialogView.findViewById(R.id.et_location);
        TextView tv_name = (TextView) dialogView.findViewById(R.id.tv_name);
        tv_name.setText(mask.getName());

        alertDialog.setView(dialogView);

        final AlertDialog ad = alertDialog.show();

        Button btn_submit = (Button) dialogView.findViewById(R.id.btn_submit);
        Button btn_cancel = (Button) dialogView.findViewById(R.id.btn_cnacel);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                String price = "";
                String location = "";
                if (et_price.getText() != null) {
                    price = et_price.getText().toString();
                }
                if (et_location.getText() != null) {
                    location = et_location.getText().toString();
                }
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date date = new Date();
                String strDate = dateFormat.format(date);
                Intelligence intelligence = new Intelligence(uid, price, location, strDate);
                MainActivity.getHelper().writeIntelligence(mask.getId(), intelligence);
                Toast.makeText(MaskDetailActivity.this, "Intelligence has been created.",Toast.LENGTH_SHORT).show();
                ad.dismiss();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ad.dismiss();
            }
        });
    }

    public void showTipsDialog (int i) {
        LayoutInflater factory = LayoutInflater.from(this);
        final View dialogView = factory.inflate(R.layout.dialog_tips, null);
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(MaskDetailActivity.this);

        TextView tv_header = (TextView) dialogView.findViewById(R.id.tv_header);
        TextView tv_detail = (TextView) dialogView.findViewById(R.id.tv_detail);
        switch (i) {
            case 0:
                tv_header.setText(R.string.tips_fe);
                tv_detail.setText(R.string.tips_detail_fe);
                break;
            case 1:
                tv_header.setText(R.string.tips_special);
                tv_detail.setText(R.string.tips_detail_special);
                break;
            case 2:
                tv_header.setText(R.string.tips_maskid);
                tv_detail.setText(R.string.tips_detail_maskid);
                break;
        }

        alertDialog.setView(dialogView);
        final AlertDialog ad = alertDialog.show();

        ImageView btn_close = (ImageView) dialogView.findViewById(R.id.btn_close);

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ad.dismiss();
            }
        });

    }

}
