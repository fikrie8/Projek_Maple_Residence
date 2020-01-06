package com.mphra.projekmaple;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Toast;

public class SignInActivity extends AppCompatActivity {

    EditText username;
    EditText password;
    Button logIn;
    String usernameContent;
    String passwordContent;
    DbUserManager db = new DbUserManager(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        username = findViewById(R.id.signInUsernameEditText);
        password = findViewById(R.id.signInPasswordEditText);
        logIn = findViewById(R.id.signInLogInButton);

        username.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener(){
                    @Override
                    public void onGlobalLayout() {
                        Drawable img = SignInActivity.this.getResources().getDrawable(R.drawable.user);
                        img.setBounds(0, 0, img.getIntrinsicWidth() * username.getLineHeight() / img.getIntrinsicHeight(), username.getLineHeight());
                        username.setCompoundDrawables(img, null, null, null);
                        if (Build.VERSION.SDK_INT <16 ) {
                            username.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        } else {
                            username.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        }
                    }
                });

        password.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener(){
            @Override
            public void onGlobalLayout() {
                Drawable img = SignInActivity.this.getResources().getDrawable(R.drawable.password);
                img.setBounds(0, 0, img.getIntrinsicWidth() * password.getLineHeight() / img.getIntrinsicHeight(), password.getLineHeight());
                password.setCompoundDrawables(img, null, null, null);

                if (Build.VERSION.SDK_INT <16 ) {
                    password.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    password.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });

        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                boolean userExist;
                boolean access;
                String gender;
                if (CheckUsernameAndPasswordFields()) {
                    userExist = db.checkUsername(usernameContent);
                    if (!userExist) {
                        access = db.checkUsernameAndPassword(usernameContent, passwordContent);
                        if (access) {
                            gender = db.fetchData(usernameContent,"gender");
                            Intent intent = new Intent(getBaseContext(), UserDashboard.class);
                            intent.putExtra("username",usernameContent);
                            intent.putExtra("gender",gender);
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(getApplicationContext(),"Incorrect password",Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"User does not exist",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public boolean CheckUsernameAndPasswordFields(){
        usernameContent = username.getText().toString();
        passwordContent = password.getText().toString();

        if (!usernameContent.isEmpty()&& !passwordContent.isEmpty())
        {
            return true;
        } else {
            Toast.makeText(getApplicationContext(),"Fields cannot be empty",Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}

/**code explanation

 Line 32-58 : Not sure how to make the edge for EditText a bit circular in XML like button(is it possible?) So I just wrote it in code.

 Line 61 : Set Login button listener. Just check the field entered before going to UserDashboard activity.

 Line 75 : I'm passing gender variable so that the image used for UserManagement inside UserDashboard activity will be displayed according to gender.

 **/