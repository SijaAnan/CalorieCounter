package com.example.nadii.caloriecounter;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
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

public class MealsActivity extends AppCompatActivity {

    private TextView mTitletxt, mEditFoodTxt;
    private Button mDonebtn, mEditFoodBtn;
    private EditText mEditFoodEditTxt;
    private ListView mFoodList;

    private AlertDialog dialog;

    private ArrayList<String> mUserFood = new ArrayList<>();
    private ArrayList<String> mUserFood2edelete = new ArrayList<>();
    private ArrayAdapter<String> home_arrayAdapter;


    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;

    private String meal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meals);

        //firebase
        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        //Toolbar set
        getSupportActionBar().setTitle("Meals Edit");
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //design fields
        mTitletxt = findViewById(R.id.meals_headtxt);
        mDonebtn = findViewById(R.id.meals_btn);
        mFoodList = findViewById(R.id.meals_list);

        Intent intent = getIntent();
        meal= intent.getStringExtra("meal");

        mTitletxt.setText(meal);

        home_arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mUserFood);
        mFoodList.setAdapter(home_arrayAdapter);

        get_food();

        mDonebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent startIntent = new Intent(MealsActivity.this , FoodActivity.class);
                //startIntent.putExtra("meal",mUserFood.get(position).toString());
                startActivity(startIntent);
                finish();
            }
        });

        mFoodList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {


                if (!mUserFood.get(position).toString().equals("Breakfast") && !mUserFood.get(position).toString().equals("Lunch") && !mUserFood.get(position).toString().equals("Dinner") ) {
                    //Toast.makeText(FoodActivity.this, "Long press for edit or delete. ", Toast.LENGTH_LONG).show();

                    PopupMenu popupMenu = new PopupMenu(MealsActivity.this , mFoodList.getChildAt(position));
                    popupMenu.getMenuInflater().inflate(R.menu.food_popup_menu,popupMenu.getMenu());

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {

                            //Toast.makeText(FoodActivity.this, "" + item.getTitle() , Toast.LENGTH_LONG).show();

                            if(item.getTitle().equals("delete")) {

                                String current_uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                                Calendar c = Calendar.getInstance();
                                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                                String formattedDate = df.format(c.getTime());

                                databaseReference = FirebaseDatabase.getInstance().getReference();
                                databaseReference = databaseReference.child("users").child(current_uid);
                                databaseReference = databaseReference.child("food").child(formattedDate).child(meal);


                                databaseReference.child(mUserFood2edelete.get(position).toString()).setValue(null);

                                //databaseReference.removeValue();
                                Toast.makeText(MealsActivity.this, "" + mUserFood.get(position).toString(), Toast.LENGTH_LONG).show();
                                //mUserFood.remove(position);
                                //mUserFood2edelete.remove(position);
                                mFoodList.getChildAt(position).setBackgroundColor(Color.RED);

                            }
                            else if(item.getTitle().equals("edit")){

                                Toast.makeText(MealsActivity.this, "" + mUserFood2edelete.get(position).toString(), Toast.LENGTH_LONG).show();
                                handleEditFood(position);
                                mFoodList.getChildAt(position).setBackgroundColor(Color.GRAY);
                            }

                            return true;
                        }
                    });

                    popupMenu.show();
                }
            }
        });

    }

    private void handleEditFood(final int position ){

        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(MealsActivity.this);

        View edit_view = getLayoutInflater().inflate(R.layout.edit_food, null);

        mEditFoodTxt = (TextView) edit_view.findViewById(R.id.editfood_textView);
        mEditFoodEditTxt = (EditText) edit_view.findViewById(R.id.editfood_editText);
        mEditFoodBtn = (Button) edit_view.findViewById(R.id.editfood_btn);

        String myFood;
        myFood = mUserFood2edelete.get(position).toString();

        mEditFoodTxt.setText(myFood);


        mEditFoodBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //HashMap<String , String> userMeal = new HashMap<>();

                String  myCalories;

                //Toast.makeText(MealsActivity.this, " " + myFood, Toast.LENGTH_LONG).show();
                myCalories = mEditFoodEditTxt.getText().toString();

                String current_uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                String formattedDate = df.format(c.getTime());


                if(!myCalories.isEmpty()) {

                    databaseReference = FirebaseDatabase.getInstance().getReference();
                    databaseReference = databaseReference.child("users").child(current_uid);
                    databaseReference = databaseReference.child("food").child(formattedDate).child(meal);


                    databaseReference.child(mUserFood2edelete.get(position).toString()).setValue(myCalories);

                    //mEditFoodEditTxt.getText().clear();
                    //search_edit_text.getText().clear();
                }
                else {

                    Toast.makeText(MealsActivity.this, "Can't be added - Weight in grams is Empty.", Toast.LENGTH_LONG).show();
                }

                dialog.cancel();
            }
        });

        mBuilder.setView(edit_view);
        dialog = mBuilder.create();
        dialog.show();
    }

    private void get_food() {

        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
        //final String current_uid = current_user.getUid();

        //mUserFood.clear();

        if (current_user != null) {

            //String current_uid = current_user.getUid();
            String current_uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = df.format(c.getTime());

            databaseReference = FirebaseDatabase.getInstance().getReference();
            databaseReference = databaseReference.child("users").child(current_uid);
            databaseReference = databaseReference.child("food").child(formattedDate);

            ValueEventListener eventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    //mUserFood.add(meal);
                    //mUserFood2edelete.add(meal);

                    //List <String> list = new ArrayList<>();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String name = ds.getKey().toString();
                        String value = ds.getValue(String.class);

                        mUserFood.add("    " + name + "  " + value);
                        mUserFood2edelete.add(name);

                    }

                    home_arrayAdapter.notifyDataSetChanged();

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }

            };
            databaseReference.child(meal).addListenerForSingleValueEvent(eventListener);
        }
    }

}
