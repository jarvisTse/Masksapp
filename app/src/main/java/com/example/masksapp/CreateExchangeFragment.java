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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SwitchCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class CreateExchangeFragment extends Fragment {

    FirebaseStorage storage;
    StorageReference storageReference;

    private Uri filePath;
    private Exchange exchange;
    private final int PICK_IMAGE_REQUEST = 71;
    private ImageView imageView;
    private TextView tv_upload_image;
    private boolean hasMaskId;

    private View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_exchange, container, false);
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

        imageView = (ImageView) v.findViewById(R.id.imgView);
        tv_upload_image = (TextView) v.findViewById(R.id.tv_upload_image);
        hasMaskId = false;

        LinearLayout layout_create = (LinearLayout) v.findViewById(R.id.layout_create);
        layout_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChooseCreateDialog();
            }
        });

        SwitchCompat sw_hasInfo = (SwitchCompat) v.findViewById(R.id.sw_hasInfo);
        final EditText et_mask_id = (EditText) v.findViewById(R.id.et_mask_id);
        sw_hasInfo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    hasMaskId = true;
                    et_mask_id.setVisibility(View.VISIBLE);
                } else {
                    hasMaskId = false;
                    et_mask_id.setVisibility(View.GONE);
                }
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
                EditText et_title = (EditText) getView().findViewById(R.id.et_title);
                EditText et_content = (EditText) getView().findViewById(R.id.et_content);
                EditText et_contact = (EditText) getView().findViewById(R.id.et_contact);

                String uid = FirebaseAuth.getInstance().getUid();
                String title = et_title.getText().toString();
                String content = et_content.getText().toString();
                String contact = et_contact.getText().toString();
                String mask_id;
                if (hasMaskId) {
                    mask_id = et_mask_id.getText().toString();
                } else {
                    mask_id = null;
                }
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date curr_date = new Date();
                String date = dateFormat.format(curr_date);

                if (title.equals("")) {
                    Toast.makeText(getActivity(), "Title should not empty.",Toast.LENGTH_SHORT).show();
                } else if (mask_id != null && MainActivity.getHelper().getMaskById(mask_id) == null) {
                    Toast.makeText(getActivity(), "Cannot find the mask by the mask id.",Toast.LENGTH_SHORT).show();
                } else {
                    exchange = new Exchange(uid, title, content, contact, mask_id, date);
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
                                    exchange.setImage(uri.toString());

                                    FirebaseDatabaseHelper helper = MainActivity.getHelper();
                                    helper.writeExchange(exchange);

                                    Toast.makeText(getActivity(), "Insert exchange post successfully.",Toast.LENGTH_SHORT).show();
                                    MainActivity.getInstance().changeFrame(3);
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
        } else {
            FirebaseDatabaseHelper helper = MainActivity.getHelper();
            helper.writeExchange(exchange);

            Toast.makeText(getActivity(), "Insert exchange post successfully.",Toast.LENGTH_SHORT).show();
            MainActivity.getInstance().changeFrame(3);
        }
    }

    public void showChooseCreateDialog() {
        LayoutInflater factory = LayoutInflater.from(getActivity());
        final View dialogView = factory.inflate(R.layout.dialog_create, null);
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

        final RadioGroup rg_create = (RadioGroup) dialogView.findViewById(R.id.rg_create);
        rg_create.check(rg_create.getChildAt(2).getId());

        alertDialog.setView(dialogView);
        final AlertDialog ad = alertDialog.show();

        Button btn_choose = (Button) dialogView.findViewById(R.id.btn_choose);
        Button btn_cancel = (Button) dialogView.findViewById(R.id.btn_cnacel);

        btn_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (rg_create.indexOfChild(dialogView.findViewById(rg_create.getCheckedRadioButtonId()))) {
                    case 0:
                        MainActivity.getInstance().changeFrame(1);
                        break;
                    case 1:
                        MainActivity.getInstance().changeFrame(7);
                        break;
                    case 2:
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
