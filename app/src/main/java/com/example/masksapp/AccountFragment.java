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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.UUID;

public class AccountFragment extends Fragment {

    View v;
    TextView tv_name;
    TextView tv_email;

    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;
    private ImageView iv_profile;
    private ImageView iv_preview;
    private Button btn_choose;
    private Button btn_upload;
    private TextView tv_upload_image;

    FirebaseStorage storage;
    StorageReference storageReference;

    public AccountFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account, container, false);
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

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        tv_name = (TextView) v.findViewById(R.id.tv_name);
        tv_email = (TextView) v.findViewById(R.id.tv_email);
        iv_profile = (ImageView) v.findViewById(R.id.iv_icon);
        Button btn_logout = (Button) v.findViewById(R.id.btn_logout);
//            tv_name.setText(user.getDisplayName());

        String name = "";
        String email = user.getEmail();
        String icon = MainActivity.getHelper().getUser(user.getUid()).getIcon();
        if (user.getDisplayName() != null) {
            name = user.getDisplayName();
        }
        tv_name.setText(name);
        tv_email.setText(email);
        Picasso.get().load(icon).transform(new CircleTransform()).into(iv_profile);

        LinearLayout layout_name = (LinearLayout) v.findViewById(R.id.layout_name);
        layout_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editUserName();
            }
        });

        LinearLayout layout_email = (LinearLayout) v.findViewById(R.id.layout_email);
        layout_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editUserEmail();
            }
        });

        LinearLayout layout_upload_icon = (LinearLayout) v.findViewById(R.id.layout_upload_icon);
        layout_upload_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadIcon();
            }
        });

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                MainActivity.getInstance().changeFrame(5);
            }
        });
    }

    public void editUserName() {
        LayoutInflater factory = LayoutInflater.from(getActivity());
        final View dialogView = factory.inflate(R.layout.dialog_edit_name, null);
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setView(dialogView);

        final EditText et_edit_username = (EditText) dialogView.findViewById(R.id.et_edit_username);
        if (FirebaseAuth.getInstance().getCurrentUser().getDisplayName() != null) {
            et_edit_username.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        }
        final TextView tv_edit_username = (TextView) dialogView.findViewById(R.id.tv_edit_username);
        et_edit_username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    tv_edit_username.setVisibility(View.VISIBLE);
                    et_edit_username.setHint("");
                } else {
                    tv_edit_username.setVisibility(View.GONE);
                    et_edit_username.setHint(R.string.username);
                }
            }
        });

        final AlertDialog ad = alertDialog.show();

        Button btn_submit = (Button) dialogView.findViewById(R.id.btn_submit);
        Button btn_cancel = (Button) dialogView.findViewById(R.id.btn_cnacel);
        final TextView tv_username_error = (TextView) dialogView.findViewById(R.id.tv_username_error);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = et_edit_username.getText().toString();
                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(name)
                        .build();
                FirebaseAuth.getInstance().getCurrentUser().updateProfile(profileUpdates);
                MainActivity.getHelper().editUserName(FirebaseAuth.getInstance().getUid(), name);
                tv_name.setText(name);
                tv_edit_username.setText(name);
                Toast.makeText(getActivity(), "Username update successfully.",Toast.LENGTH_SHORT).show();
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

    public void editUserEmail() {
        LayoutInflater factory = LayoutInflater.from(getActivity());
        final View dialogView = factory.inflate(R.layout.dialog_edit_email, null);
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setView(dialogView);

        final EditText et_edit_email = (EditText) dialogView.findViewById(R.id.et_edit_email);
        final TextView tv_edit_email = (TextView) dialogView.findViewById(R.id.tv_edit_email);
        et_edit_email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    tv_edit_email.setVisibility(View.VISIBLE);
                    et_edit_email.setHint("");
                } else {
                    tv_edit_email.setVisibility(View.GONE);
                    et_edit_email.setHint(R.string.currEmail);
                }
            }
        });
        final EditText et_email = (EditText) dialogView.findViewById(R.id.et_email);
        final TextView tv_email = (TextView) dialogView.findViewById(R.id.tv_email);
        et_email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    tv_email.setVisibility(View.VISIBLE);
                    et_email.setHint("");
                } else {
                    tv_email.setVisibility(View.GONE);
                    et_email.setHint(R.string.newEmail);
                }
            }
        });
        final EditText et_password = (EditText) dialogView.findViewById(R.id.et_password);
        final TextView tv_password = (TextView) dialogView.findViewById(R.id.tv_password);
        et_password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    tv_password.setVisibility(View.VISIBLE);
                    et_password.setHint("");
                } else {
                    tv_password.setVisibility(View.GONE);
                    et_password.setHint(R.string.password);
                }
            }
        });

        final AlertDialog ad = alertDialog.show();

        Button btn_submit = (Button) dialogView.findViewById(R.id.btn_submit);
        Button btn_cancel = (Button) dialogView.findViewById(R.id.btn_cnacel);
        final TextView tv_email_error = (TextView) dialogView.findViewById(R.id.tv_email_error);
        final TextView tv_new_email_error = (TextView) dialogView.findViewById(R.id.tv_new_email_error);
        final TextView tv_password_error = (TextView) dialogView.findViewById(R.id.tv_password_error);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_email_error.setVisibility(View.GONE);
                tv_new_email_error.setVisibility(View.GONE);
                tv_password_error.setVisibility(View.GONE);
                String currEmail = et_email.getText().toString();
                final String newEmail = et_edit_email.getText().toString();
                String password = et_password.getText().toString();
                if (currEmail.matches("")) {
                    tv_email_error.setText(R.string.emptyEmail);
                    tv_email_error.setVisibility(View.VISIBLE);
                } else if (newEmail.matches("")) {
                    tv_new_email_error.setText(R.string.emptyEmail);
                    tv_new_email_error.setVisibility(View.VISIBLE);
                } else if (password.matches("")) {
                    tv_password_error.setText(R.string.emptyPassord);
                    tv_password_error.setVisibility(View.VISIBLE);
                } else {
                    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    AuthCredential credential = EmailAuthProvider.getCredential(currEmail, password);
                    user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                user.updateEmail(newEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            MainActivity.getHelper().editUserEmail(user.getUid(), newEmail);
                                            tv_email.setText(newEmail);
                                            Toast.makeText(getActivity(), "Email update successfully.",Toast.LENGTH_SHORT).show();
                                            ad.dismiss();
                                        } else {
                                            if (task.getException() != null && task.getException().getMessage() != null) {
                                                if (task.getException().getMessage().equals("The email address is badly formatted.")) {
                                                    tv_new_email_error.setText(R.string.emailBad);
                                                    tv_new_email_error.setVisibility(View.VISIBLE);
                                                } else if (task.getException().getMessage().equals("The email address is already in use by another account.")) {
                                                    tv_new_email_error.setText(R.string.emailUsed);
                                                    tv_new_email_error.setVisibility(View.VISIBLE);
                                                } else {
                                                    Toast.makeText(getActivity(), task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                                }
                                                Log.d("editEmail", task.getException().getMessage());
                                            }
                                        }
                                    }
                                });
                            } else {
                                if (task.getException() != null && task.getException().getMessage() != null) {
                                    if (task.getException().getMessage().equals("The email address is badly formatted.")) {
                                        tv_email_error.setText(R.string.emailBad);
                                        tv_email_error.setVisibility(View.VISIBLE);
                                    } else if (task.getException().getMessage().equals("There is no user record corresponding to this identifier. The user may have been deleted.")) {
                                        tv_email_error.setText(R.string.emailNoUser);
                                        tv_email_error.setVisibility(View.VISIBLE);
                                    } else if (task.getException().getMessage().equals("The password is invalid or the user does not have a password.")) {
                                        tv_password_error.setText(R.string.passwordWrong);
                                        tv_password_error.setVisibility(View.VISIBLE);
                                    } else {
                                        Toast.makeText(getActivity(), task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                    }
                                    Log.d("editEmail", task.getException().getMessage());
                                }
                            }
                        }
                    });
                }
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ad.dismiss();
            }
        });
    }

    public void uploadIcon() {
        LayoutInflater factory = LayoutInflater.from(getActivity());
        final View dialogView = factory.inflate(R.layout.dialog_upload_icon, null);
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setView(dialogView);

        iv_preview = (ImageView) dialogView.findViewById(R.id.iv_upload);
        Picasso.get().load(MainActivity.getHelper().getUser(FirebaseAuth.getInstance().getUid()).getIcon()).transform(new CircleTransform()).into(iv_preview);
        Button btn_cancel = (Button) dialogView.findViewById(R.id.btn_cnacel);
        btn_choose = (Button) dialogView.findViewById(R.id.btn_choose);
        btn_upload = (Button) dialogView.findViewById(R.id.btn_upload);

        final AlertDialog ad = alertDialog.show();

        btn_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String img_url = "images/"+ UUID.randomUUID().toString();
                uploadImage(img_url);
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
                iv_preview.setImageBitmap(bitmap);
                btn_choose.setVisibility(View.GONE);
                btn_upload.setVisibility(View.VISIBLE);
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
                                    FirebaseDatabaseHelper helper = MainActivity.getHelper();
                                    helper.userUploadIcon(FirebaseAuth.getInstance().getCurrentUser().getUid(), uri.toString());
                                    Picasso.get().load(uri.toString()).transform(new CircleTransform()).into(iv_profile);
                                    Toast.makeText(getActivity(), "Upload icon successfully.",Toast.LENGTH_SHORT).show();
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

}
