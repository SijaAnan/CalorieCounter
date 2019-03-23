package com.example.nadii.caloriecounter;


import android.os.Bundle;
import android.support.v4.app.Fragment;
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

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        //initialize the FirebaseAuth instance.
        mAuth = FirebaseAuth.getInstance();

        //design fields.
        mUserList = (ListView) view.findViewById(R.id.profilefragment_userlist);

        //ArrayAdapter<String > arrayAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,mUserData);
        //mUserList.setAdapter(arrayAdapter);

        mEditBtn = (Button) view.findViewById(R.id.reg_verify_btn);
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
/*
        mEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }

        });*/

        // Inflate the layout for this fragment
        return view;
    }

    private void get_profile() {

        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
        String current_uid = current_user.getUid();

        arrayAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,mUserData);


        if(current_user != null) {

            //String current_uid = current_user.getUid();
        }

        //reference to the realtime database. pointing to our root.
        mDataBase = FirebaseDatabase.getInstance().getReference();

        mDataBase = mDataBase.child("users").child(current_uid);

        mDataBase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //problem here!!!
                Toast.makeText(getActivity() , "CanFUCKKSKSKSKSistered already.", Toast.LENGTH_LONG).show();
                String value = dataSnapshot.child("name").getValue(String.class);
                mUserData.add(value);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mUserList.setAdapter(arrayAdapter);
        /*
        mDataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Toast.makeText(getActivity() , "CanFUCKKSKSKSKSistered already.", Toast.LENGTH_LONG).show();
                String value = dataSnapshot.getValue(String.class);
                mUserData.add(value);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
*/
/*
        mDataBase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Toast.makeText(getActivity() , "CanFUCKKSKSKSKSistered already.", Toast.LENGTH_LONG).show();
                String value = dataSnapshot.getValue(String.class);
                mUserData.add(value);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                String value = dataSnapshot.getValue(String.class);
                mUserData.add(value);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/




        //userMap.put("birth date" , "dd/mm/yyyy");

       // mDataBase.setValue(userMap);
/*
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
        mDataBase.child("snacks").setValue(userSnacks);*/
    }

}
