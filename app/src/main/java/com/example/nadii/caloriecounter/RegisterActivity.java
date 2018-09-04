package com.example.nadii.caloriecounter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

    private EditText mEmail;
    private EditText mPassword;
    private Button mCreateBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //initialize the FirebaseAuth instance.
        mAuth = FirebaseAuth.getInstance();

        mEmail = (EditText) findViewById(R.id.reg_email_txt);
        mPassword = (EditText) findViewById(R.id.reg_pass_txt);
        mCreateBtn = (Button) findViewById(R.id.reg_signup_btn);

        mCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = mEmail.getText().toString();
                String password = mPassword.getText().toString();

                register_user(email,password);

            }
        });
    }

    private void register_user(String email, String password) {

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){

                    Intent main_intent = new Intent(RegisterActivity.this , MainActivity.class);
                    startActivity(main_intent);
                    finish();

                }else{
                    Toast.makeText(RegisterActivity.this , "Error...", Toast.LENGTH_LONG).show();
                }

            }
        });
    }
}
