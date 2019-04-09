package com.example.nadii.caloriecounter;


import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import static java.security.AccessController.getContext;

public class FoodDataBaseActivity extends AppCompatActivity implements View.OnClickListener, SearchAdapter.SearchListener {

    //create firebase instase.
    private FirebaseAuth mAuth;

    private DatabaseReference mDataBase;

    private Button mVerifyBtn;
    private EditText mName;

    EditText   search_edit_text;
    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;
    ArrayList<String> fullNameList;
    SearchAdapter searchAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_data_base);

        //Toolbar set
        getSupportActionBar().setTitle("Database Edit");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        search_edit_text = (EditText) findViewById(R.id.food_database_search_edit_text);
        recyclerView = (RecyclerView) findViewById(R.id.food_database_recyclerView);


        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));


        /*
         * Create a array list for each node you want to use
         * */
        fullNameList = new ArrayList<>();

        search_edit_text.addTextChangedListener(watcher);

    }

    private TextWatcher watcher = new TextWatcher()
    {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after)
        {


        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count)
        {


            fullNameList.clear();
            recyclerView.removeAllViews();
        }

        @Override
        public void afterTextChanged(Editable s)
        {

            if (!s.toString().isEmpty())
            {



                setAdapter(s.toString());

            }
            else
            {
                /*
                 * Clear the list when editText is empty
                 * */
                fullNameList.clear();
                recyclerView.removeAllViews();
            }
        }
    };

    private void setAdapter(final String searchedString)
    {
        recyclerView.setVisibility(View.VISIBLE);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("food").addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                /*
                 * Clear the list for every new search
                 * */

                recyclerView.removeAllViews();

                int counter = 0;

                /*
                 * Search all users for matching searched string
                 * */
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {

                    String food = snapshot.getKey(); // Food string
                    String Calories = snapshot.child("Calories").getValue(String.class); // calories amount (string)


                    if (food.toLowerCase().contains(searchedString.toLowerCase()))
                    {
                        fullNameList.add(food);
                        counter++;

                    }

                    /*
                     * Get maximum of 3 searched results only
                     * */
                    if (counter == 5)
                        break;
                }

                searchAdapter = new SearchAdapter(FoodDataBaseActivity.this, fullNameList, FoodDataBaseActivity.this);
                recyclerView.setAdapter(searchAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemSelect(String text) {

    }
}
