/*
# COMP 4521    # Tse Wing Hei    20446652    whtseae@connect.ust.hk
 */

package com.example.masksapp;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class SignupFragment extends Fragment {

    private View v;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    EditText et_username;
    EditText et_email;
    EditText et_password;
    EditText et_password_confirm;
    TextView tv_name_error;
    TextView tv_email_error;
    TextView tv_password_error;
    TextView tv_password_confirm_error;


    public SignupFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signup, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setSettings();
    }

    public void setSettings() {
        mAuth = FirebaseAuth.getInstance();
        v = getView();
        Button btn_signup = v.findViewById(R.id.btn_signup);
        et_username = v.findViewById(R.id.et_username);
        et_email = v.findViewById(R.id.et_email);
        et_password = v.findViewById(R.id.et_password);
        et_password_confirm = v.findViewById(R.id.et_password_confirm);
        final TextView tv_username = v.findViewById(R.id.tv_username);
        final TextView tv_email = v.findViewById(R.id.tv_email);
        final TextView tv_password = v.findViewById(R.id.tv_password);
        final TextView tv_password_confirm = v.findViewById(R.id.tv_password_confirm);
        final TextView tv_hasAccount = v.findViewById(R.id.tv_hasAccount);
        tv_name_error = v.findViewById(R.id.tv_name_error);
        tv_email_error = v.findViewById(R.id.tv_email_error);
        tv_password_error = v.findViewById(R.id.tv_password_error);
        tv_password_confirm_error = v.findViewById(R.id.tv_password_confirm_error);

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_name_error.setVisibility(View.GONE);
                tv_email_error.setVisibility(View.GONE);
                tv_password_error.setVisibility(View.GONE);
                tv_password_confirm_error.setVisibility(View.GONE);
                signup();
            }
        });

        et_username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    tv_username.setVisibility(View.VISIBLE);
                    et_username.setHint("");
                } else {
                    tv_username.setVisibility(View.GONE);
                    et_username.setHint("Username");
                }
            }
        });

        et_email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    tv_email.setVisibility(View.VISIBLE);
                    et_email.setHint("");
                } else {
                    tv_email.setVisibility(View.GONE);
                    et_email.setHint("Email");
                }
            }
        });

        et_password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    tv_password.setVisibility(View.VISIBLE);
                    et_password.setHint("");
                } else {
                    tv_password.setVisibility(View.GONE);
                    et_password.setHint("Password");
                }
            }
        });

        et_password_confirm.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    tv_password_confirm.setVisibility(View.VISIBLE);
                    et_password_confirm.setHint("");
                } else {
                    tv_password_confirm.setVisibility(View.GONE);
                    et_password_confirm.setHint("Password Confirmation");
                }
            }
        });

        tv_hasAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.getInstance().changeFrame(5);
            }
        });
    }

    public void signup() {
        final String name = et_username.getText().toString();
        final String email = et_email.getText().toString();
        String password = et_password.getText().toString();
        String password_confirm = et_password_confirm.getText().toString();
        if (name.equals("")) {
            tv_name_error.setText(R.string.emptyUsername);
            tv_name_error.setVisibility(View.VISIBLE);
        } else if (email.equals("")) {
            tv_email_error.setText(R.string.emptyEmail);
            tv_email_error.setVisibility(View.VISIBLE);
        } else if (password.equals("")) {
            tv_password_error.setText(R.string.emptyPassord);
            tv_password_error.setVisibility(View.VISIBLE);
        } else if (password_confirm.equals("")) {
            tv_password_confirm_error.setText(R.string.emptyPasswordConfirm);
            tv_password_confirm_error.setVisibility(View.VISIBLE);
        } else if (!password.equals(password_confirm)) {
            tv_password_confirm_error.setText(R.string.unMatchPassword);
            tv_password_confirm_error.setVisibility(View.VISIBLE);
        } else {
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("sign", "signInWithEmail:success");
                        user = mAuth.getCurrentUser();
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(name)
                                .build();
                        user.updateProfile(profileUpdates)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d("profile", "User profile updated.");
                                        }
                                    }
                                });
                        User u = new User(user.getUid(), name, email);
                        MainActivity.getHelper().writeUser(u);
                        MainActivity.getInstance().changeFrame(2);
                    } else {
                        if (task.getException() != null && task.getException().getMessage() != null) {
                            if (task.getException().getMessage().equals("The email address is badly formatted.")) {
                                tv_email_error.setText(R.string.emailBad);
                                tv_email_error.setVisibility(View.VISIBLE);
                            } else if (task.getException().getMessage().equals("The email address is already in use by another account.")) {
                                tv_email_error.setText(R.string.emailUsed);
                                tv_email_error.setVisibility(View.VISIBLE);
                            } else {
                                Toast.makeText(getActivity(), task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            }
                            Log.d("singup", task.getException().getMessage());
                        }
                    }
                }
            });
        }
    }
}