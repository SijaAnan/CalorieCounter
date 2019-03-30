package com.example.nadii.caloriecounter;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    //create firebase instase.
    private FirebaseAuth mAuth;

    private DatabaseReference mDataBase;

    private Button mVerifyBtn;
    private EditText mName;
    private EditText mHeight;
    private EditText mWeight;
    private RadioGroup radioGgender;
    private RadioButton radioBgender;
    Spinner day_spinner;
    Spinner month_spinner;
    Spinner year_spinner;

    //Variables
    private String[] day_array = new String[31];
    private String[] year_array = new String[100];

    HashMap<String , String> userMap = new HashMap<>();

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
        mName = (EditText) findViewById(R.id.reg_name_edittxt);
        mHeight = (EditText) findViewById(R.id.reg_height_edittxt);
        mWeight = (EditText) findViewById(R.id.reg_weight_edittxt);
        radioGgender = (RadioGroup) findViewById(R.id.reg_radiogroup);

        //fill day array
        int counter = 0;
        for(int i = 0 ; i < 31 ; i++){

            counter = i+1;
            this.day_array[i] = "" + counter;
        }

        //day spinner
        day_spinner = (Spinner) findViewById(R.id.reg_day_spinner);
        month_spinner = (Spinner) findViewById(R.id.reg_month_spinner);

        ArrayAdapter<String> day_adapter = new ArrayAdapter(this , android.R.layout.simple_spinner_item , day_array);
        day_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        day_spinner.setAdapter(day_adapter);
        //day_spinner.setOnItemSelectedListener(this);

        //fill year array
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);

        int year_sel = 0;
        for(int j = 0 ; j < (100) ; j++){

            year_sel = year - j;
            this.year_array[j] = "" + year_sel;
        }

        //year spinner
        year_spinner = (Spinner) findViewById(R.id.reg_year_spinner);
        ArrayAdapter<String> year_adapter = new ArrayAdapter(this , android.R.layout.simple_spinner_item , year_array);
        year_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        year_spinner.setAdapter(year_adapter);

        //sends the spinners items selected to the database
        spinners_item_select();

        mVerifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //send_verify_email();
                Intent intent = getIntent();
                String checkFlag= intent.getStringExtra("flag");

                if(checkFlag.equals("FirstTime")){
                    //if we came here from start activity - registration process.
                    set_profile();
                    FirebaseAuth.getInstance().signOut();
                    finish();
                }
                else{
                // It MainActivity - editing profile.
                    set_profile();
                    Intent main_intent = new Intent(RegisterActivity.this , MainActivity.class);
                    startActivity(main_intent);
                    finish();

                }
            }

        });


    }

    public void checkButton(View view){

        int radioId = radioGgender.getCheckedRadioButtonId();
        radioBgender = findViewById(radioId);

    }
/*
    private void send_verify_email() {

        mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){

                    set_profile();

                    Toast.makeText(RegisterActivity.this , "Profile Set ,Verification Email Sent.", Toast.LENGTH_LONG).show();

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
    }*/

    private void set_profile() {


        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
        String current_uid = current_user.getUid();

        if(current_user != null) {

            //String current_uid = current_user.getUid();
        }

        //reference to the realtime database. pointing to our root.
        mDataBase = FirebaseDatabase.getInstance().getReference();

        mDataBase = mDataBase.child("users").child(current_uid);

        //HashMap<String , String> userMap = new HashMap<>();

        userMap.put("name", mName.getText().toString());
        userMap.put("weight" , mWeight.getText().toString());
        userMap.put("height" , mHeight.getText().toString());

        int radioId = radioGgender.getCheckedRadioButtonId();
        radioBgender = findViewById(radioId);
        userMap.put("gender" , radioBgender.getText().toString());


        //userMap.put("birth date" , "dd/mm/yyyy");

        mDataBase.setValue(userMap);

        mDataBase = mDataBase.child("food");

        HashMap<String , String> userBreakfast = new HashMap<>();
        userBreakfast.put("egg" , "63");
        mDataBase.child("breakfast").setValue(userBreakfast);

        HashMap<String , String> userLunch = new HashMap<>();
        userLunch.put("burger" , "540");
        mDataBase.child("lunch").setValue(userLunch);

        HashMap<String , String> userDinner = new HashMap<>();
        userDinner.put("spaghetti" , "340");
        mDataBase.child("dinner").setValue(userDinner);

        HashMap<String , String> userSnacks = new HashMap<>();
        userSnacks.put("apple" , "40");
        mDataBase.child("snacks").setValue(userSnacks);


    }


    public void spinners_item_select(){

        final String[] text = new String[1];

        day_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                text[0] = parent.getItemAtPosition(position).toString();
                //userMap.put("birth date" , text[0]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        month_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                text[0] = text[0] + parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        year_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                text[0] = text[0] + parent.getItemAtPosition(position).toString();
                userMap.put("birth date" , text[0]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
