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

import java.util.concurrent.Executor;

public class LoginFragment extends Fragment {

    private View v;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    EditText et_email;
    EditText et_password;
    TextView tv_email_error;
    TextView tv_password_error;


    public LoginFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setSettings();
    }

    public void setSettings() {
        mAuth = FirebaseAuth.getInstance();
        v = getView();
        Button btn_login = v.findViewById(R.id.btn_login);
        et_email = v.findViewById(R.id.et_email);
        et_password = v.findViewById(R.id.et_password);
        final TextView tv_email = v.findViewById(R.id.tv_email);
        final TextView tv_password = v.findViewById(R.id.tv_password);
        final TextView tv_noAccount = v.findViewById(R.id.tv_noAccount);
        tv_email_error = v.findViewById(R.id.tv_email_error);
        tv_password_error = v.findViewById(R.id.tv_password_error);

        btn_login.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                tv_email_error.setVisibility(View.GONE);
                tv_password_error.setVisibility(View.GONE);
                login();
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

        tv_noAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.getInstance().changeFrame(6);
            }
        });
    }

    public void login() {
        if (et_email.getText().toString().matches("")) {
            tv_email_error.setText(R.string.emptyEmail);
            tv_email_error.setVisibility(View.VISIBLE);
        } else if (et_password.getText().toString().matches("")) {
            tv_password_error.setText(R.string.emptyPassord);
            tv_password_error.setVisibility(View.VISIBLE);
        } else {
            String email = et_email.getText().toString();
            String password = et_password.getText().toString();
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("sign", "signInWithEmail:success");
                        user = mAuth.getCurrentUser();
                        MainActivity.getInstance().changeFrame(2);
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
                        }
                    }
                }
            });
        }
    }
}
