/*
# COMP 4521    # Tse Wing Hei    20446652    whtseae@connect.ust.hk
 */

package com.example.masksapp;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

public class CreateFragment extends Fragment {

    FirebaseStorage storage;
    StorageReference storageReference;

    private Uri filePath;
    private Mask mask;
    private final int PICK_IMAGE_REQUEST = 71;
    private ImageView imageView;
    private TextView tv_upload_image;

    private View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setSettings();
    }

    public void setSettings() {
        v = getView();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        String[] spin_standard = { "No ASTM/KF standard", "ASTM Level 1", "ASTM Level 2", "ASTM Level 3", "KF-80", "KF-94" };
        Spinner spin = (Spinner) v.findViewById(R.id.spinner_standard);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, spin_standard);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);

        imageView = (ImageView) v.findViewById(R.id.imgView);
        tv_upload_image = (TextView) v.findViewById(R.id.tv_upload_image);

        LinearLayout layout_create = (LinearLayout) v.findViewById(R.id.layout_create);
        layout_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChooseCreateDialog();
            }
        });

        LinearLayout btn_upload_image = (LinearLayout) v.findViewById(R.id.btn_upload_image);
        btn_upload_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        Button btn_send = (Button) getView().findViewById(R.id.btn_send);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et_name = (EditText) getView().findViewById(R.id.et_name);
                EditText et_brand = (EditText) getView().findViewById(R.id.et_brand);
                EditText et_madeIn = (EditText) getView().findViewById(R.id.et_madeIn);
                EditText et_size = (EditText) getView().findViewById(R.id.et_size);
                EditText et_price = (EditText) getView().findViewById(R.id.et_price);
                EditText et_description = (EditText) getView().findViewById(R.id.et_description);
                CheckBox cb_bfe = (CheckBox) getView().findViewById(R.id.cb_bfe);
                CheckBox cb_pfe = (CheckBox) getView().findViewById(R.id.cb_pfe);
                CheckBox cb_vfe = (CheckBox) getView().findViewById(R.id.cb_vfe);
                Spinner spin = (Spinner) getView().findViewById(R.id.spinner_standard);

                double price;
                if (et_price.getText().toString().equals("")) {
                    price = 10000;
                } else {
                    price = Double.parseDouble(et_price.getText().toString());
                }
                Standard standard = new Standard(cb_bfe.isChecked(), cb_pfe.isChecked(), cb_vfe.isChecked(), spin.getSelectedItem().toString());

                if (et_name.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Mask name should not empty.",Toast.LENGTH_SHORT).show();
                } else if (filePath == null) {
                    Toast.makeText(getActivity(), "Please upload a image.",Toast.LENGTH_SHORT).show();
                } else {
                    mask = new Mask(et_name.getText().toString(), et_brand.getText().toString(), et_madeIn.getText().toString(),
                            et_size.getText().toString(), price, et_description.getText().toString(), standard);

                    String img_url = "images/"+ UUID.randomUUID().toString();
                    uploadImage(img_url);
                }
            }
        });
    }

    public void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK
                && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
                imageView.setVisibility(View.VISIBLE);
//                LinearLayout layout_img_box = (LinearLayout) findViewById(R.id.layout_img_box);
//                layout_img_box.setVisibility(View.VISIBLE);
                tv_upload_image.setText("Re-Upload Image");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage(final String url)
    {
        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            final StorageReference ref = storageReference.child(url);
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    mask.setImage(uri.toString());

                                    FirebaseDatabaseHelper helper = MainActivity.getHelper();
                                    helper.writeMask(mask);

                                    Toast.makeText(getActivity(), "Insert data successfully.",Toast.LENGTH_SHORT).show();
                                    MainActivity.getInstance().changeFrame(2);
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
    }

    public void showChooseCreateDialog() {
        LayoutInflater factory = LayoutInflater.from(getActivity());
        final View dialogView = factory.inflate(R.layout.dialog_create, null);
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

        final RadioGroup rg_create = (RadioGroup) dialogView.findViewById(R.id.rg_create);
        rg_create.check(rg_create.getChildAt(0).getId());

        alertDialog.setView(dialogView);
        final AlertDialog ad = alertDialog.show();

        Button btn_choose = (Button) dialogView.findViewById(R.id.btn_choose);
        Button btn_cancel = (Button) dialogView.findViewById(R.id.btn_cnacel);

        btn_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (rg_create.indexOfChild(dialogView.findViewById(rg_create.getCheckedRadioButtonId()))) {
                    case 0:
                        break;
                    case 1:
                        MainActivity.getInstance().changeFrame(7);
                        break;
                    case 2:
                        MainActivity.getInstance().changeFrame(8);
                        break;
                }
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
