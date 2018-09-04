package com.example.nadii.caloriecounter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StartActivity extends AppCompatActivity {


    private Button mSignUpBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        mSignUpBtn = (Button) findViewById(R.id.start_signup_button);

        mSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent reg_intent = new Intent(StartActivity.this , RegisterActivity.class);
                startActivity(reg_intent);
            }
        });
    }
}
