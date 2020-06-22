/*
# COMP 4521    # Tse Wing Hei    20446652    whtseae@connect.ust.hk
 */

package com.example.masksapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.InputType;
import android.text.Layout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static MainActivity instance;
    private static FirebaseDatabaseHelper helper;
    private BlogFragment blogFragment;
    private CreateFragment createFragment;
    private CreateBlogFragment createBlogFragment;
    private HomeFragment homeFragment;
    private ExchangeFragment exchangeFragment;
    private LoginFragment loginFragment;
    private SignupFragment signupFragment;
    private AccountFragment accountFragment;
    private BottomNavigationBar bottomNavigationBar;
    private int current_bar_frame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        Intent i = getIntent();
        int frame_id = i.getIntExtra("frame_id", 2);

        instance = this;
        helper = new FirebaseDatabaseHelper();
        helper.readUser();

        blogFragment = new BlogFragment();
        createFragment = new CreateFragment();
        homeFragment = new HomeFragment();
        exchangeFragment = new ExchangeFragment();
        accountFragment = new AccountFragment();
//        loginFragment = new LoginFragment();
//        signupFragment = new SignupFragment();

        getFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();

        bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.layout_bottom_bar);
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        bottomNavigationBar.setBarBackgroundColor(R.color.colorMain);
        bottomNavigationBar.setActiveColor(R.color.colorWhite);
        bottomNavigationBar.setInActiveColor(R.color.colorGray);
        bottomNavigationBar.addItem(new BottomNavigationItem(R.drawable.ic_blog_gray, "Blog"))
                .addItem(new BottomNavigationItem(R.drawable.ic_add_gray, "Create"))
                .addItem(new BottomNavigationItem(R.drawable.ic_home_gray, "Home"))
                .addItem(new BottomNavigationItem(R.drawable.ic_exchange_gray, "Trade"))
                .addItem(new BottomNavigationItem(R.drawable.ic_person_gray, "Account"))
                .setFirstSelectedPosition(2)
                .initialise();

        current_bar_frame = 2;

        bottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                switch (position) {
                    case 0:
                        current_bar_frame = 0;
                        getFragmentManager().beginTransaction().replace(R.id.container, blogFragment).commit();
                        break;
                    case 1:
                        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                            getFragmentManager().beginTransaction().replace(R.id.container, createFragment).commit();
                            current_bar_frame = 1;
                        } else {
                            showLoginFirstDialog();
                        }
                        break;
                    case 2:
                        current_bar_frame = 2;
                        getFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();
                        break;
                    case 3:
                        current_bar_frame = 3;
                        getFragmentManager().beginTransaction().replace(R.id.container, exchangeFragment).commit();
                        break;
                    case 4:
                        current_bar_frame = 4;
                        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                            getFragmentManager().beginTransaction().replace(R.id.container, accountFragment).commit();
                        } else {
                            loginFragment = new LoginFragment();
                            getFragmentManager().beginTransaction().replace(R.id.container, loginFragment).commit();
                        }
                        break;
                }
            }
            @Override
            public void onTabUnselected(int position) {
            }
            @Override
            public void onTabReselected(int position) {
            }
        });

        bottomNavigationBar.selectTab(frame_id);
    }

    public static MainActivity getInstance() {
        return instance;
    }

    public static FirebaseDatabaseHelper getHelper() {
        return helper;
    }

    public void changeFrame(int position) {
        switch(position) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
                bottomNavigationBar.selectTab(position);
                break;
            case 5:
                loginFragment = new LoginFragment();
                getFragmentManager().beginTransaction().replace(R.id.container, loginFragment).commit();
                break;
            case 6:
                signupFragment = new SignupFragment();
                getFragmentManager().beginTransaction().replace(R.id.container, signupFragment).commit();
                break;
            case 7:
                createBlogFragment = new CreateBlogFragment();
                getFragmentManager().beginTransaction().replace(R.id.container, createBlogFragment).commit();
                break;
            case 8:
                CreateExchangeFragment createExchangeFragment = new CreateExchangeFragment();
                getFragmentManager().beginTransaction().replace(R.id.container, createExchangeFragment).commit();
                break;
        }
    }

    public void showLoginFirstDialog() {
        LayoutInflater factory = LayoutInflater.from(this);
        final View dialogView = factory.inflate(R.layout.dialog_login_first, null);
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);

        TextView tv_dialog_header = (TextView) dialogView.findViewById(R.id.tv_dialog_header);
        tv_dialog_header.setText(R.string.create);
        alertDialog.setView(dialogView);

        final AlertDialog ad = alertDialog.show();

        Button btn_login = (Button) dialogView.findViewById(R.id.btn_login);
        Button btn_cancel = (Button) dialogView.findViewById(R.id.btn_cnacel);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFrame(4);
                ad.dismiss();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomNavigationBar.selectTab(current_bar_frame);
                ad.dismiss();
            }
        });
    }

    @Override
    public void onBackPressed(){
        LayoutInflater factory = LayoutInflater.from(this);
        final View dialogView = factory.inflate(R.layout.dialog_quit, null);
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);

        alertDialog.setView(dialogView);

        final AlertDialog ad = alertDialog.show();

        Button btn_quit = (Button) dialogView.findViewById(R.id.btn_quit);
        Button btn_cancel = (Button) dialogView.findViewById(R.id.btn_cnacel);

        btn_quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                System.exit(0);
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ad.dismiss();
            }
        });
    }

}
