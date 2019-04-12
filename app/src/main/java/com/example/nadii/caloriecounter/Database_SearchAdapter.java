package com.example.nadii.caloriecounter;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;


public class Database_SearchAdapter extends RecyclerView.Adapter<Database_SearchAdapter.SearchViewHolder>
{

    private TextView  mEditFoodTxt;
    private Button  mEditFoodBtn;
    private EditText mEditFoodEditTxt;

    private AlertDialog dialog;

    DatabaseReference databaseReference;
    private int selectedPos = RecyclerView.NO_POSITION;
    Context           context;
    ArrayList<String> fullNameList;


    private SearchListener _searchListener;


    class SearchViewHolder extends RecyclerView.ViewHolder
    {
        TextView full_name;

        public SearchViewHolder(View itemView)
        {
            super(itemView);
            full_name = (TextView) itemView.findViewById(R.id.full_name);
        }
    }


    public Database_SearchAdapter(Context context, ArrayList<String> fullNameList, SearchListener searchListener)
    {
        this.context = context;
        this.fullNameList = fullNameList;
        _searchListener = searchListener;

    }


    @Override
    public Database_SearchAdapter.SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.search_list_items, parent, false);
        return new Database_SearchAdapter.SearchViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final SearchViewHolder holder, int position)
    {

        final String text = fullNameList.get(position);
        holder.full_name.setText(text);
        //holder.full_name.setBackgroundColor(selectedPos == position ? Color.GREEN : Color.LTGRAY);

        holder.full_name.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(final View v)
            {

                //creating a popup menu
                PopupMenu popup = new PopupMenu(context, holder.full_name);
                //inflating menu from xml resource
                popup.inflate(R.menu.food_popup_menu);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        if(item.getTitle().equals("delete")) {

                           /* String current_uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                            Calendar c = Calendar.getInstance();
                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                            String formattedDate = df.format(c.getTime());*/

                            databaseReference = FirebaseDatabase.getInstance().getReference();
                            databaseReference = databaseReference.child("food");

                            databaseReference.child(text).setValue(null);
/*
                            //databaseReference.removeValue();
                            Toast.makeText(MealsActivity.this, "" + mUserFood.get(position).toString(), Toast.LENGTH_LONG).show();
                            //mUserFood.remove(position);
                            //mUserFood2edelete.remove(position);
                            mFoodList.getChildAt(position).setBackgroundColor(Color.RED);*/

                        }
                        else if(item.getTitle().equals("edit")){

                            handleEditFood(text);
                            //mFoodList.getChildAt(position).setBackgroundColor(Color.GRAY);
                        }

                        //fullNameList.clear();
                        //recyclerView.removeAllViews();
                        ((FoodDataBaseActivity)context).search_init();
                        return true;

                    }
                });
                //displaying the popup
                popup.show();



                if (holder.getAdapterPosition() == RecyclerView.NO_POSITION)
                    return;

                notifyItemChanged(selectedPos);
                selectedPos = holder.getLayoutPosition();
                notifyItemChanged(selectedPos);
                //Toast.makeText(context, ((TextView) v).getText(), Toast.LENGTH_SHORT).show(); //Here I get the text string


                // close keyboard on click

                v.clearFocus();
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null)
                {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }


                // Here I add the food + calories


                if (_searchListener != null)
                {
                    _searchListener.onItemSelect(text);
                }
//                fullNameList.clear();
//                notifyDataSetChanged();

            }
        });
    }

    private void handleEditFood(final String food ){

        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);

        View edit_view = ((FoodDataBaseActivity)context).getLayoutInflater().inflate(R.layout.edit_food, null);

        mEditFoodTxt = (TextView) edit_view.findViewById(R.id.editfood_textView);
        mEditFoodEditTxt = (EditText) edit_view.findViewById(R.id.editfood_editText);
        mEditFoodBtn = (Button) edit_view.findViewById(R.id.editfood_btn);

        mEditFoodEditTxt.setHint("calories in 100 grams");

        final String myFood;
        //myFood = mUserFood2edelete.get(position).toString();
        myFood = food;

        mEditFoodTxt.setText(myFood);


        mEditFoodBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //HashMap<String , String> userMeal = new HashMap<>();

                String  myCalories;

                //Toast.makeText(MealsActivity.this, " " + myFood, Toast.LENGTH_LONG).show();
                myCalories = mEditFoodEditTxt.getText().toString();

                /*String current_uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                String formattedDate = df.format(c.getTime());*/


                if(!myCalories.isEmpty()) {

                    /*databaseReference = FirebaseDatabase.getInstance().getReference();
                    databaseReference = databaseReference.child("users").child(current_uid);
                    databaseReference = databaseReference.child("food").child(formattedDate).child(meal);*/

                    databaseReference = FirebaseDatabase.getInstance().getReference();
                    databaseReference = databaseReference.child("food");


                    databaseReference.child(myFood).setValue(myCalories);

                    //mEditFoodEditTxt.getText().clear();
                    //search_edit_text.getText().clear();
                }
                else {

                    Toast.makeText(context , "Can't be added - Weight in grams is Empty.", Toast.LENGTH_LONG).show();
                }

                dialog.cancel();
            }
        });

        mBuilder.setView(edit_view);
        dialog = mBuilder.create();
        dialog.show();
    }


    @Override
    public int getItemCount()
    {
        return fullNameList.size();
    }


    public interface SearchListener
    {
        void onItemSelect(String text);
    }
}