package com.mphra.projekmaple;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class ReportIssue extends AppCompatActivity {

    private TextView tittle,category;
    private ImageView image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_issue);
        tittle=(TextView) findViewById(R.id.reportIssueTittle);
        category=(TextView) findViewById(R.id.reportIssueCategory);
        image=(ImageView) findViewById(R.id.reportIssueThumbnail);

        Intent intent = getIntent();
        String getTittle = intent.getExtras().getString("Tittle");
        int getImage = intent.getExtras().getInt("Thumbnail");

        tittle.setText(getTittle);
        image.setImageResource(getImage);
    }
}
