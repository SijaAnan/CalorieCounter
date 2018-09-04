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

public class RegisterActivity extends AppCompatActivity {

    //create firebase instase.
    private FirebaseAuth mAuth;

    //ProgressDialog
    private ProgressDialog mRegProgress;

    private EditText mEmail;
    private EditText mPassword;
    private Button mCreateBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //initialize the FirebaseAuth instance.
        mAuth = FirebaseAuth.getInstance();

        //Toolbar set
        getSupportActionBar().setTitle("Create an Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //progress dialog
        mRegProgress = new ProgressDialog(this);

        //design fiedls
        mEmail = (EditText) findViewById(R.id.reg_email_txt);
        mPassword = (EditText) findViewById(R.id.reg_pass_txt);
        mCreateBtn = (Button) findViewById(R.id.reg_signup_btn);

        mCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = mEmail.getText().toString();
                String password = mPassword.getText().toString();

                if(!TextUtils.isEmpty(email) || !TextUtils.isEmpty(password)) {

                    mRegProgress.setTitle("Registering User");
                    mRegProgress.setMessage("Please wait while we create your account");
                    mRegProgress.setCanceledOnTouchOutside(false);
                    mRegProgress.show();

                    register_user(email, password);

                }
            }
        });
    }

    private void register_user(String email, String password) {

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){

                    mRegProgress.dismiss();

                    Intent main_intent = new Intent(RegisterActivity.this , MainActivity.class);
                    startActivity(main_intent);
                    finish();

                }else{

                    mRegProgress.hide();
                    Toast.makeText(RegisterActivity.this , "Cannot Sign up. ERROR", Toast.LENGTH_LONG).show();
                }

            }
        });
    }
}
