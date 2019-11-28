package com.mphra.projekmaple;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class VehicleSticker extends AppCompatActivity {

    private TextView tittle,category;
    private ImageView image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_sticker);
        tittle=(TextView) findViewById(R.id.vehicleStickerTittle);
        category=(TextView) findViewById(R.id.vehicleStickerCategory);
        image=(ImageView) findViewById(R.id.vehicleStickerThumbnail);

        Intent intent = getIntent();
        String getTittle = intent.getExtras().getString("Tittle");
        int getImage = intent.getExtras().getInt("Thumbnail");

        tittle.setText(getTittle);
        image.setImageResource(getImage);
    }
}
