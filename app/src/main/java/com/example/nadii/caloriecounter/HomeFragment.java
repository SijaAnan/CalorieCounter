package com.example.nadii.caloriecounter;


import android.os.Bundle;
//import android.app.Fragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    //create firebase instase.
    private FirebaseAuth mAuth;

    private DatabaseReference mDataBase;
    private DatabaseReference mCurrUserFoodRef;

    private ListView mFoodList;

    private ArrayList<String> mUserFood = new ArrayList<>();
    private ArrayAdapter<String> home_arrayAdapter;

    private TextView mSumCalorie;
    private double calorie_sum=0;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((MainActivity) getActivity()).setActionBarTitle("Home");

        final View home_view = inflater.inflate(R.layout.fragment_home, container, false);

        //initialize the FirebaseAuth instance.
        mAuth = FirebaseAuth.getInstance();

        //design fields
        mFoodList = (ListView) home_view.findViewById(R.id.homefragment_list);
        mSumCalorie = (TextView) home_view.findViewById(R.id.homefrag_txt_view);

        //for toolbar.
        //setHasOptionsMenu(true);

        get_food();

        // Inflate the layout for this fragment
        return home_view;
    }


    private void get_food() {

        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
        //final String current_uid = current_user.getUid();

        home_arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, mUserFood);

        //mUserFood.clear();

        if (current_user != null) {

            //String current_uid = current_user.getUid();
            String current_uid = FirebaseAuth.getInstance().getCurrentUser().getUid();


            //reference to the realtime database. pointing to our root.
            mDataBase = FirebaseDatabase.getInstance().getReference();

            //mDataBase = mDataBase.child("users");//.child(current_uid);
            mCurrUserFoodRef = mDataBase.child("users").child(current_uid).child("food");

            ValueEventListener breakfast_eventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    mUserFood.add("Breakfast");
                    //List <String> list = new ArrayList<>();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String name = ds.getKey().toString();
                        String value = ds.getValue(String.class);

                        calorie_sum = calorie_sum + Double.parseDouble(value);

                        mUserFood.add("    " + name + "  " + value);

                    }

                    home_arrayAdapter.notifyDataSetChanged();

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            mCurrUserFoodRef.child("breakfast").addListenerForSingleValueEvent(breakfast_eventListener);

            ValueEventListener lunch_eventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    mUserFood.add("Lunch");
                    //List <String> list = new ArrayList<>();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String name = ds.getKey().toString();
                        String value = ds.getValue(String.class);

                        calorie_sum = calorie_sum + Double.parseDouble(value);

                        mUserFood.add("    " + name + "  " + value);

                    }

                    home_arrayAdapter.notifyDataSetChanged();

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            mCurrUserFoodRef.child("lunch").addListenerForSingleValueEvent(lunch_eventListener);

            ValueEventListener dinner_eventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    mUserFood.add("Dinner");
                    //List <String> list = new ArrayList<>();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String name = ds.getKey().toString();
                        String value = ds.getValue(String.class);

                        calorie_sum = calorie_sum + Double.parseDouble(value);

                        mUserFood.add("    " + name + "  " + value);

                    }

                    home_arrayAdapter.notifyDataSetChanged();

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            mCurrUserFoodRef.child("dinner").addListenerForSingleValueEvent(dinner_eventListener);

            ValueEventListener snacks_eventListner = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    mUserFood.add("Snacks");
                    //List <String> list = new ArrayList<>();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String name = ds.getKey().toString();
                        String value = ds.getValue(String.class);

                        calorie_sum = calorie_sum + Double.parseDouble(value);

                        mUserFood.add("    " + name + "  " + value);

                    }

                    home_arrayAdapter.notifyDataSetChanged();

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            mCurrUserFoodRef.child("snacks").addListenerForSingleValueEvent(snacks_eventListner);
        }
        mFoodList.setAdapter(home_arrayAdapter);
    }

}
