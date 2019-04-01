package com.example.nadii.caloriecounter;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {


    //create firebase instase.
    private FirebaseAuth mAuth;

    private DatabaseReference mDataBase;
    private DatabaseReference mCurrUserRef;

    private ListView mUserList;

    private ArrayList<String> mUserData = new ArrayList<>();
    private ArrayAdapter<String > arrayAdapter;
    private Button mEditBtn;

/*
    private EditText mName;
    private EditText mHeight;
    private EditText mWeight;
    private EditText mDateBirth;
    private EditText mGender;
    private EditText mBMI;*/

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((MainActivity)getActivity()).setActionBarTitle("Profile");

        final View view = inflater.inflate(R.layout.fragment_profile, container, false);

        //initialize the FirebaseAuth instance.
        mAuth = FirebaseAuth.getInstance();

        //design fields.
        mUserList = (ListView) view.findViewById(R.id.profilefragment_userlist);
        mEditBtn = (Button) view.findViewById(R.id.profilefrag_edit_btn);

        //ArrayAdapter<String > arrayAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,mUserData);
        //mUserList.setAdapter(arrayAdapter);

     /*   mName = (EditText) view.findViewById(R.id.profilefragment_name_value);
        mHeight = (EditText) view.findViewById(R.id.profilefragment_height_value_txt);
        mWeight = (EditText) view.findViewById(R.id.profilefragment_weight_value_txt);
        mDateBirth = (EditText) view.findViewById(R.id.profilefragment_birth_value);
        mGender = (EditText) view.findViewById(R.id.profilefragment_gender_value);
        mBMI = (EditText) view.findViewById(R.id.profilefragment_BMI_value_txt); */

        //mUserData.add("FUCK THIS");

        //arrayAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,mUserData);
        //mUserList.setAdapter(arrayAdapter);

        get_profile();

        mEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent reg_intent = new Intent(getActivity() , RegisterActivity.class);
                reg_intent.putExtra("flag", "Edit");
                startActivity(reg_intent);
                getActivity().finish();

            }

        });

        // Inflate the layout for this fragment
        return view;
    }

    private void get_profile() {

        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
        //final String current_uid = current_user.getUid();

        arrayAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,mUserData);

        mUserData.clear();

        if(current_user != null) {

            //String current_uid = current_user.getUid();
            String current_uid = FirebaseAuth.getInstance().getCurrentUser().getUid();


            //reference to the realtime database. pointing to our root.
            mDataBase = FirebaseDatabase.getInstance().getReference();

            //mDataBase = mDataBase.child("users");//.child(current_uid);
            mCurrUserRef = mDataBase.child("users").child(current_uid);

            ValueEventListener eventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    String name = dataSnapshot.child("name").getValue(String.class);
                    String birthdate = dataSnapshot.child("birth date").getValue(String.class);
                    String gender = dataSnapshot.child("gender").getValue(String.class);
                    String height = dataSnapshot.child("height").getValue(String.class);
                    String weight = dataSnapshot.child("weight").getValue(String.class);

                    mUserData.add("name: " + name);
                    mUserData.add("birth date: " + birthdate);
                    mUserData.add("gender: " + gender);
                    mUserData.add("height: " + height);
                    mUserData.add("weight: " + weight);
                    mUserData.add("BMI : " + calcualteBMI(height,weight));

                    arrayAdapter.notifyDataSetChanged();

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            mCurrUserRef.addListenerForSingleValueEvent(eventListener);

            mUserList.setAdapter(arrayAdapter);
        }


    }

    double calcualteBMI(String height_str , String weight_str) {

        double height = Double.parseDouble(height_str);
        double weight = Double.parseDouble(weight_str);

        height = height * 1/100;
        height = height*height;


        // Calculate result
        double result = Math.round(weight/height);



        // If or else for values
        /*
        if(result < 18.5){
            TextViewResult.setText("Your BMI is " + result + "\nYou are categorized as underweight.");
        }
        else{
            if(result < 24.9){
                TextViewResult.setText("Your BMI is " + result + "\nYou are categorized as normal weight.");
            }
            else {
                if (result < 29.9) {
                    TextViewResult.setText("Your BMI is " + result + "\nYou are categorized as overweight.");
                }
                else{
                    if(result > 30 && result < 34.9){
                        TextViewResult.setText("Your BMI is " + result + "\nYou are categorized as obese class I (Moderately obese).");
                    }
                    else {
                        if(result < 39.9){
                            TextViewResult.setText("Your BMI is " + result + "\nYou are categorized as obese class II (Severely obese).");
                        }
                        else{
                            if(result < 40){
                                TextViewResult.setText("Your BMI is " + result + "\nYou are categorized as obese class III (Very severely obese).");
                            }
                            else{
                                TextViewResult.setText("");
                            }
                        }
                    }
                }
            }
        }*/

        return result;
    }

}
