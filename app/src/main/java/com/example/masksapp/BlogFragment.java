/*
# COMP 4521    # Tse Wing Hei    20446652    whtseae@connect.ust.hk
 */

package com.example.masksapp;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;


import androidx.appcompat.app.AlertDialog;

import java.util.List;

public class BlogFragment extends Fragment {

    private static BlogFragment instance;
    private View v;
    private int sort_index;
    private String search;
    EditText et_search;

    public BlogFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blog, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setSettings();
    }

    public void setSettings() {
        instance = this;
        v = getView();
        sort_index = 0;
        search = "";
        MainActivity.getHelper().readBlog(search, sort_index);

        Button btn_cancel = (Button) getView().findViewById(R.id.btn_cancel);
        et_search = (EditText) getView().findViewById(R.id.et_search);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_search.setText("");
                search = "";
                MainActivity.getHelper().readBlog(search, sort_index);
            }
        });

        et_search.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    switch (keyCode)
                    {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            sort_index = 0;
                            search = String.valueOf(et_search.getText());
                            MainActivity.getHelper().readBlog(search, sort_index);
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });

        LinearLayout layout_sort = (LinearLayout) v.findViewById(R.id.layout_sort);
        layout_sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSortDialog();
            }
        });
    }

    public void updateList() {
        final List<Blog> blogs = MainActivity.getHelper().getBlogs();
        BlogAdapter blogAdapter = new BlogAdapter(MainActivity.getInstance(), R.layout.blog_item, blogs);
        ListView list_search = (ListView) v.findViewById(R.id.list_search);
        list_search.setAdapter(blogAdapter);

        list_search.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), BlogDetailActivity.class);
                intent.putExtra("blog_id", blogs.get(position).getId());
                startActivity(intent);
            }
        });
    }

    public void search_item_count(int count) {
        TextView tv_searchNum = (TextView) v.findViewById(R.id.tv_searchNum);
        tv_searchNum.setText("Searching Result: " + count);
    }

    public static BlogFragment getInstance() {
        return instance;
    }

    public void showSortDialog() {
        LayoutInflater factory = LayoutInflater.from(getActivity());
        final View dialogView = factory.inflate(R.layout.dialog_sort_blog, null);
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

        final RadioGroup rg_sort = (RadioGroup) dialogView.findViewById(R.id.rg_sort);
        int radioButtonID = rg_sort.getChildAt(sort_index).getId();
        rg_sort.check(radioButtonID);
        alertDialog.setView(dialogView);

        final AlertDialog ad = alertDialog.show();

        Button btn_sort = (Button) dialogView.findViewById(R.id.btn_sort);
        Button btn_cancel = (Button) dialogView.findViewById(R.id.btn_cnacel);

        btn_sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sort_index = rg_sort.indexOfChild(dialogView.findViewById(rg_sort.getCheckedRadioButtonId()));
                search = "";
                et_search.setText(search);
                MainActivity.getHelper().readBlog(search, sort_index);
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
}
