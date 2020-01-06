package com.mphra.projekmaple;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    ImageView logo;
    Button signIn;
    Button register;
    DbAdapter dbAdapter = new DbAdapter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signIn = findViewById(R.id.mainSignInButton);
        register = findViewById(R.id.mainRegisterButton);

        logo = findViewById(R.id.mainLogoImageView);
        Bitmap bmp = BitmapFactory.decodeResource(this.getResources(), R.drawable.maple_logo);
        RoundedBitmapDrawable dr = RoundedBitmapDrawableFactory.create(this.getResources(), bmp);
        dr.setCircular(true);
        logo.setImageDrawable(dr);
        if (!dbAdapter.createDb()) {
            Log.e("MainActivity","Failed to create table");
        }

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = new Intent(MainActivity.this, SignInActivity.class);
                Log.e("MainActivity", "calling signInActivity");
                startActivity(signInIntent);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registrationIntent = new Intent(MainActivity.this, RegistrationActivity.class);
                Log.e("MainActivity", "Calling RegistrationActivity");
                startActivity(registrationIntent);
            }
        });
    }
}

/**code explanation

 Line 33-36 : To make the ImageView circular. I'm not sure how to do it in XML files. So I did it in code instead.

 Line 38-43 : Set the listener. Whenever the signIn button is pressed, it will redirect to SignIn activities.

 Line 46-51 : Set the listener. Whenever the register button is pressed, it will redirect to Registration activities.
 **/