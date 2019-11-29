package com.mphra.projekmaple;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class UserManagement extends AppCompatActivity {

    private TextView tittle,category;
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
    DbManager db= new DbManager(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_management);
        tittle=(TextView) findViewById(R.id.userManagementTittle);
        category=(TextView) findViewById(R.id.userManagementCategory);
        image=(ImageView) findViewById(R.id.userManagementThumbnail);

        Intent intent = getIntent();
        String getTittle = intent.getExtras().getString("Tittle");
        int getImage = intent.getExtras().getInt("Thumbnail");
        final String getUsername = intent.getExtras().getString("username");

        tittle.setText(getTittle);
        image.setImageResource(getImage);
        fullnameEditText = (EditText) findViewById(R.id.fullNameEditText);
        addressEditText = (EditText) findViewById(R.id.addressEditText);
        emailEditText = (EditText) findViewById(R.id.emailEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        updateButton = (Button) findViewById(R.id.updateButton);

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if ( updateUserInfo(getUsername)== true) {
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

