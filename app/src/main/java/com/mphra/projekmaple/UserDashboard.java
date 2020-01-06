package com.mphra.projekmaple;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class UserDashboard extends AppCompatActivity {

    TextView username;
    ImageView imageProfile;
    List<ItemList> listItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);

        Intent intent = getIntent();
        String getUsername = intent.getExtras().getString("username");
        String gender = intent.getExtras().getString("gender");
        username = findViewById(R.id.userDashboardProfileTextView);
        imageProfile = findViewById(R.id.userDashboardProfileImageView);
        Bitmap bmp = BitmapFactory.decodeResource(this.getResources(), R.drawable.profile_picture);
        RoundedBitmapDrawable dr = RoundedBitmapDrawableFactory.create(this.getResources(), bmp);
        dr.setCircular(true);
        username.setText(getUsername);
        imageProfile.setImageDrawable(dr);

        listItem = new ArrayList<>();
        if (gender.equals("Male")) {
            listItem.add(new ItemList("User Management","User",R.drawable.user_setting_male));
        } else {
            listItem.add(new ItemList("User Management","User",R.drawable.user_setting_female));
        }
        listItem.add(new ItemList("Vehicle Sticker","User",R.drawable.car_sticker_image));
        listItem.add(new ItemList("Security Payment","User",R.drawable.payment));
        listItem.add(new ItemList("Report Issues","User",R.drawable.report));
        listItem.add(new ItemList("Community Item Booking","User",R.drawable.booking));
        listItem.add(new ItemList("Setting","User",R.drawable.setting));

        RecyclerView newRecycleView = findViewById(R.id.recyclerview);
        RecyclerViewAdapter myAdapter = new RecyclerViewAdapter(this, listItem, getUsername);
        newRecycleView.setLayoutManager(new GridLayoutManager(this, 2));
        newRecycleView.setAdapter(myAdapter);
    }
}

/**code explanation

 Line 31-32 : Catching the variable passed from previous activity.

 Line 35-39 : Make the image for profile picture circular.

 Line 41-51 : Setting the ItemList for the User to be displayed on DashBoard.

 Line 53-56 : Creating ReecycleView and sending the ItemList that has been defined to adapter. The Adapter will determine how the itemList
 will be displayed and how it will react. Eg. go to certain activity when pressed one of the itemList.

 **/
