package com.example.nadii.caloriecounter;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class HistoryActivity extends AppCompatActivity {

    private TextView mTitletxt, mEditFoodTxt;
    private Button mDonebtn, mEditFoodBtn;
    private EditText mEditFoodEditTxt;
    private ListView mFoodList;

    private AlertDialog dialog;

    private ArrayList<String> mUserFood = new ArrayList<>();
    private ArrayList<String> tmp_list = new ArrayList<>();
    private ArrayList<String> mUserFood2edelete = new ArrayList<>();
    private ArrayAdapter<String> home_arrayAdapter;


    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;

    private String meal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        //firebase
        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        //Toolbar set
        getSupportActionBar().setTitle("Progress");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //design fields
        mTitletxt = findViewById(R.id.history_headtxt);
        mFoodList = findViewById(R.id.history_list);


        mTitletxt.setText("last 7 days");

        home_arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mUserFood);
        mFoodList.setAdapter(home_arrayAdapter);

        get_food_history();

        //list_trim();


        mFoodList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {


                PopupMenu popupMenu = new PopupMenu(HistoryActivity.this , mFoodList.getChildAt(position));
                popupMenu.getMenuInflater().inflate(R.menu.history_popup_menu,popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        //Toast.makeText(FoodActivity.this, "" + item.getTitle() , Toast.LENGTH_LONG).show();

                        Intent startIntent = new Intent(HistoryActivity.this , EditPrevDaysActivity.class);
                        startIntent.putExtra("date",mUserFood.get(position));
                        startIntent.putExtra("meal",item.getTitle().toString());

                        startActivity(startIntent);

                        return true;
                    }
                });

                popupMenu.show();
            }

        });


    }

    private void list_trim(){

        Integer list_size = mUserFood.size();

        Log.d("the list SIZE     " , list_size.toString());

        if(list_size > 3){

            tmp_list.addAll(mUserFood.subList(list_size-3,list_size));
            mUserFood.clear();
            mUserFood.addAll(tmp_list);
        }
        else{

        }
    }
    private void get_food_history() {

        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
        //final String current_uid = current_user.getUid();

        mUserFood.clear();

        if (current_user != null) {

            //String current_uid = current_user.getUid();
            String current_uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = df.format(c.getTime());

            databaseReference = FirebaseDatabase.getInstance().getReference();
            databaseReference = databaseReference.child("users").child(current_uid);

            ValueEventListener eventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    //mUserFood.add(meal);
                    //mUserFood2edelete.add(meal);

                    //List <String> list = new ArrayList<>();

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {

                        String date = ds.getKey();

                        mUserFood.add(date);

                    }

                    list_trim();
                    home_arrayAdapter.notifyDataSetChanged();

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }

            };
            databaseReference.child("food").addListenerForSingleValueEvent(eventListener);
        }
    }
}
