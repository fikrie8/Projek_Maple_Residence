package com.mphra.projekmaple;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class SecurityPayment extends AppCompatActivity {

    private TextView tittle,category;
    private ImageView image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_payment);
        tittle=(TextView) findViewById(R.id.securityPaymentTittle);
        category=(TextView) findViewById(R.id.securityPaymentCategory);
        image=(ImageView) findViewById(R.id.securityPaymentThumbnail);

        Intent intent = getIntent();
        String getTittle = intent.getExtras().getString("Tittle");
        int getImage = intent.getExtras().getInt("Thumbnail");

        tittle.setText(getTittle);
        image.setImageResource(getImage);
    }
}
