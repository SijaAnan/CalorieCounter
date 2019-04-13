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
import android.view.Menu;
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

public class FoodDataBaseActivity extends AppCompatActivity implements View.OnClickListener, Database_SearchAdapter.SearchListener {

    //create firebase instase.
    private FirebaseAuth mAuth;

    private DatabaseReference mDataBase;

    private Button mVerifyBtn;
    private EditText mName;

    private AlertDialog dialog;

    EditText   search_edit_text;
    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;
    ArrayList<String> fullNameList;
    Database_SearchAdapter searchAdapter;

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

                searchAdapter = new Database_SearchAdapter(FoodDataBaseActivity.this, fullNameList, FoodDataBaseActivity.this);
                recyclerView.setAdapter(searchAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.fooddatabase_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == R.id.add_food_database){
            /*android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flMain , new FoodFragment());
            ft.commit();*/

            handleAddFood();

        }

        return super.onOptionsItemSelected(item);
    }


    private void handleAddFood(){

        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(FoodDataBaseActivity.this);

        View edit_view = getLayoutInflater().inflate(R.layout.edit_database, null);

        final EditText mEditFoodEditTxt,mEditFoodCalEditTxt;
        final Button mEditFoodBtn;

        mEditFoodEditTxt = (EditText) edit_view.findViewById(R.id.editdatabase_food_name);
        mEditFoodCalEditTxt = (EditText) edit_view.findViewById(R.id.editdatabase_food_cal);
        mEditFoodBtn = (Button) edit_view.findViewById(R.id.editdatabase_btn);





        mEditFoodBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //HashMap<String , String> userMeal = new HashMap<>();

                String  myCalories,myFood;

                //Toast.makeText(MealsActivity.this, " " + myFood, Toast.LENGTH_LONG).show();
                myFood = mEditFoodEditTxt.getText().toString();
                myCalories = mEditFoodCalEditTxt.getText().toString();

                String current_uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                String formattedDate = df.format(c.getTime());


                if(!myCalories.isEmpty()) {

                    databaseReference = FirebaseDatabase.getInstance().getReference();
                    databaseReference = databaseReference.child("food");
                    databaseReference.child(myFood).setValue(myCalories);

                }
                else {

                    Toast.makeText(FoodDataBaseActivity.this, "Can't be added - Weight in grams is Empty.", Toast.LENGTH_LONG).show();
                }

                dialog.cancel();
            }
        });

        mBuilder.setView(edit_view);
        dialog = mBuilder.create();
        dialog.show();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemSelect(String text) {

    }

    public void search_init(){
        search_edit_text.getText().clear();
        fullNameList.clear();
        recyclerView.removeAllViews();
    }
}
