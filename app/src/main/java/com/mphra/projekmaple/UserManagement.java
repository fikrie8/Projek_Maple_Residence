package com.mphra.projekmaple;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class UserManagement extends AppCompatActivity {

    private TextView tittle;
    private ImageView image;
    private EditText fullnameEditText;
    private EditText addressEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button updateButton;
    String fullnameContent;
    String addressContent;
    String emailContent;
    String passwordContent;
    DbUserManager db= new DbUserManager(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_management);

        Intent intent;
        String getTittle;
        int getImage;
        final String getUsername;

        intent = getIntent();
        getTittle = intent.getExtras().getString("Tittle");
        getImage = intent.getExtras().getInt("Thumbnail");
        getUsername = intent.getExtras().getString("username");

        tittle = findViewById(R.id.userManagementTittle);
        image = findViewById(R.id.userManagementThumbnail);
        tittle.setText(getTittle);
        image.setImageResource(getImage);
        fullnameEditText = findViewById(R.id.userManagementFullNameEditText);
        addressEditText = findViewById(R.id.userManagementAddressEditText);
        emailEditText = findViewById(R.id.userManagementEmailEditText);
        passwordEditText = findViewById(R.id.userManagementPasswordEditText);
        updateButton = findViewById(R.id.userManagementUpdateButton);

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if ( updateUserInfo(getUsername)) {
                    Toast.makeText(getApplicationContext(),"User info has been updated",Toast.LENGTH_LONG).show();
                    String genderContent;
                    Intent intent;

                    genderContent = db.fetchData(getUsername,"gender");
                    intent = new Intent(getBaseContext(), UserDashboard.class);
                    intent.putExtra("username",getUsername);
                    intent.putExtra("gender", genderContent);
                    startActivity(intent);
                }
            }
        });
    }

    public boolean updateUserInfo(String username){
        boolean result = false;

        if (fullnameEditText.getText().toString().isEmpty()) {
            fullnameContent = db.fetchData(username,"username");
        } else {
            fullnameContent = fullnameEditText.getText().toString();
        }

        if (addressEditText.getText().toString().isEmpty()) {
            addressContent = db.fetchData(username,"address");
        } else {
            addressContent = addressEditText.getText().toString();
        }

        if (emailEditText.getText().toString().isEmpty()) {
           emailContent = db.fetchData(username,"email");
        } else {
            emailContent = emailEditText.getText().toString();
        }

        if (passwordEditText.getText().toString().isEmpty()) {
            passwordContent = db.fetchData(username,"password");
        } else {
            passwordContent = passwordEditText.getText().toString();
        }

        if (    !fullnameContent.isEmpty()&& !addressContent.isEmpty() &&
                !emailContent.isEmpty()&& !passwordContent.isEmpty())
        {
            result = db.updateData(fullnameContent, addressContent, emailContent, username, passwordContent);
        }
        return result;
    }
}

/**code explanation

 Line 38-41 : Catching the variable passed from previous activity.

 Line 51-67 : Set the on click listener for update button. I'm sending back the gender and username because the UserDashboard activity will need
 it to display the layout. Eg. Displaying the picture according to gender.

 Line 69-102 : get the value for every EditText. Unlike register, I cannot report error if the field is empty because the user might only want to
 update certain field and use the previous data for others. So I just fetch the data that has a blank field. It is a bit expensive in term of
 memory usage.

 In future : Will add a function to retrieve a lot of data in 1 query to solve the problem for making a repetitive query.

 **/

