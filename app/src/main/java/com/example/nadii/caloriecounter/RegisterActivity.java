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
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    //create firebase instase.
    private FirebaseAuth mAuth;

    private Button mVerifyBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //initialize the FirebaseAuth instance.
        mAuth = FirebaseAuth.getInstance();

        //Toolbar set
        getSupportActionBar().setTitle("Email Verification");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //design fields.
        mVerifyBtn = (Button) findViewById(R.id.reg_verify_btn);

        mVerifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                send_verify_email();
                }

        });


    }


    private void send_verify_email() {

        mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){
                    Toast.makeText(RegisterActivity.this , "Verification Email Sent.", Toast.LENGTH_LONG).show();

                    //after the mail is sent , logout the user and finish the activity
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(RegisterActivity.this , StartActivity.class));
                    finish();
                }
                else{

                    //email not sent. display message , and restart the activity.
                    Toast.makeText(RegisterActivity.this , "ERROR in sending verification email.", Toast.LENGTH_LONG).show();
                    finish();
                    startActivity(getIntent());
                }
            }
        });
    }
}
