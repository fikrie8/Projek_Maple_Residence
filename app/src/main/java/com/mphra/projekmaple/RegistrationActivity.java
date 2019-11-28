package com.mphra.projekmaple;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class RegistrationActivity extends AppCompatActivity {

    EditText fullnameEditText;
    EditText addressEditText;
    EditText emailEditText;
    RadioGroup genderGroup;
    RadioButton genderButton;
    EditText usernameEditText;
    EditText passwordEditText;
    EditText confirmPasswordEditText;
    Button registerButton;
    String fullnameContent;
    String addressContent;
    String emailContent;
    String genderContent;
    String usernameContent;
    String passwordContent;
    String confirmPasswordContent;
    DbManager db = new DbManager(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        fullnameEditText = (EditText)findViewById(R.id.fullNameEditText);
        addressEditText = (EditText)findViewById(R.id.addressEditText);
        emailEditText = (EditText)findViewById(R.id.emailEditText);
        genderGroup = (RadioGroup)findViewById(R.id.radioGroup);
        usernameEditText = (EditText)findViewById(R.id.usernameEditText);
        passwordEditText = (EditText)findViewById(R.id.passwordEditText);
        confirmPasswordEditText = (EditText)findViewById(R.id.confirmPasswordEditText);
        registerButton = (Button)findViewById(R.id.userRegisterButton);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (CheckAndGetContent()== 0) {
                    storeUserInfo();
                    Intent intent = new Intent(getBaseContext(), UserDashboard.class);
                    intent.putExtra("username",usernameContent);
                    intent.putExtra("gender", genderContent);
                    startActivity(intent);
                }
            }
        });
    }

    public int CheckAndGetContent(){

        fullnameContent = fullnameEditText.getText().toString();
        addressContent = addressEditText.getText().toString();
        emailContent = emailEditText.getText().toString();
        genderButton = (RadioButton)findViewById(genderGroup.getCheckedRadioButtonId());
        genderContent = genderButton.getText().toString();
        usernameContent = usernameEditText.getText().toString();
        passwordContent = passwordEditText.getText().toString();
        confirmPasswordContent = confirmPasswordEditText.getText().toString();

        if (    !fullnameContent.isEmpty()&& !addressContent.isEmpty() &&
                !emailContent.isEmpty()&& !usernameContent.isEmpty() &&
                !passwordContent.isEmpty()&& !confirmPasswordContent.isEmpty() )
        {
            if (db.checkUsername(usernameContent) == true) {
                return 0;
            }
            else {
                Toast.makeText(getApplicationContext(),"Username already used",Toast.LENGTH_SHORT).show();
                return -1;
            }
        } else {
            Toast.makeText(getApplicationContext(),"Fields cannot be empty",Toast.LENGTH_SHORT).show();
            return -1;
        }
    }

     public void storeUserInfo() {

        Boolean result;
        result = db.addUser(fullnameContent, addressContent, emailContent, genderContent, usernameContent, passwordContent);
        if (result == true) {
            Toast.makeText(this,"User successfully registered",Toast.LENGTH_LONG).show();
        }
    }
}

/** code explanation

 Line 39-46 : Just set the widget that will be modified/used. Eg. TextView doesn't need to be set/define because we are not manipulating anything
 from it. Just let it display as defaulted in XML file.

 Line 48 - 58 : Set register button listener. Just check the field entered before going to UserDashboard activity.

 Line 69 : I set the genderButton inside onClickListener() method because the value inside the listener is valid. Where the button is
 already selected. Compared to set it inside onCreate() method where it will take the default value.

 Line 90-97 : Since this is a register method, we need to store the user information inside the db. This function does that purpose. Make sure to
 store the info inside the db before going to the next activity.
 **/