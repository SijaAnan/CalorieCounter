package com.example.nadii.caloriecounter;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {


    //create firebase instase.
    private FirebaseAuth mAuth;

    //progress dialog
    private ProgressDialog mLoginProgress;

    private EditText mLoginEmail;
    private EditText mLoginPassword;
    private Button mSignInBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //initialize the FirebaseAuth instance.
        mAuth = FirebaseAuth.getInstance();

        //Toolbar set
        getSupportActionBar().setTitle("Sign in to your account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //progress
        mLoginProgress = new ProgressDialog(this);

        mLoginEmail = (EditText) findViewById(R.id.login_email_txt);
        mLoginPassword = (EditText) findViewById(R.id.login_password_txt);
        mSignInBtn = (Button) findViewById(R.id.login_signin_btn);

        mSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = mLoginEmail.getText().toString();
                String password = mLoginPassword.getText().toString();

                if(!TextUtils.isEmpty(email) || !TextUtils.isEmpty(password)){

                    mLoginProgress.setTitle("Logging in");
                    mLoginProgress.setMessage("Please wait while we check your details.");
                    mLoginProgress.setCanceledOnTouchOutside(false);
                    mLoginProgress.show();

                    login_user(email,password);
                }
            }
        });
    }

    private void login_user(String email, String password) {

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){

                    mLoginProgress.dismiss();

                    Intent main_intent = new Intent(LoginActivity.this , MainActivity.class);
                    startActivity(main_intent);
                    finish();
                }else{

                    mLoginProgress.hide();
                    Toast.makeText(LoginActivity.this , "Cannot Sign in. ERROR", Toast.LENGTH_LONG).show();
                }

            }
        });
    }
}
