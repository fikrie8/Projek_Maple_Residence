package com.mphra.projekmaple;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class VehicleSticker extends AppCompatActivity implements DialogInterface.OnDismissListener{

    private TextView tittle;
    private ImageView image;
    TextView firstVehicleOwnerTextView,firstPlateNumberTextView;
    TextView secondVehicleOwnerTextView,secondPlateNumberTextView;
    TextView thirdVehicleOwnerTextView,thirdPlateNumberTextView;
    Button registerVehicleButton, deleteVehicleButton;
    String getId;
    String getTittle, getUsername;
    DbUserManager dbUser = new DbUserManager(this);
    DbStickerManager dbVehicle = new DbStickerManager(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_sticker);

        Intent PreviousIntent;
        int getImage;
        PreviousIntent = getIntent();
        getTittle = PreviousIntent.getExtras().getString("Tittle");
        getImage = PreviousIntent.getExtras().getInt("Thumbnail");
        getUsername = PreviousIntent.getExtras().getString("username");

        tittle = findViewById(R.id.vehicleStickerTittle);
        image = findViewById(R.id.vehicleStickerThumbnail);
        tittle.setText(getTittle);
        image.setImageResource(getImage);

        registerVehicleButton = findViewById(R.id.registerVehicleButton);
        deleteVehicleButton = findViewById(R.id.deleteVehicleButton);
        getId = dbUser.fetchData(getUsername,"user_id");
        firstVehicleOwnerTextView = findViewById(R.id.firstFullnameVehicleRegistered);
        firstPlateNumberTextView = findViewById(R.id.firstPlateNumberVehicleRegistered);
        secondVehicleOwnerTextView = findViewById(R.id.secondFullnameVehicleRegistered);
        secondPlateNumberTextView = findViewById(R.id.secondPlateNumberVehicleRegistered);
        thirdVehicleOwnerTextView = findViewById(R.id.thirdFullnameVehicleRegistered);
        thirdPlateNumberTextView = findViewById(R.id.thirdPlateNumberVehicleRegistered);

        registerVehicleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterVehicleDialog dialog = new RegisterVehicleDialog();
                Bundle bundle = new Bundle();
                Log.d("VehicleSticker","id = " + Integer.parseInt(getId));
                bundle.putInt("id",Integer.parseInt(getId));
                dialog.setArguments(bundle);
                dialog.show(getSupportFragmentManager(),"Vehicle");
            }
        });

        deleteVehicleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent deleteVehicleIntent = new Intent(getBaseContext(), DeleteVehicle.class);
                deleteVehicleIntent.putExtra("id",getId);
                startActivityForResult(deleteVehicleIntent, 1);
            }
        });

        if (dbVehicle.countData(getId) > 0) {
            if (dbVehicle.countData(getId) == 1)
            {
                firstVehicleOwnerTextView.setText(dbVehicle.fetchData(getId,"fullname"));
                firstPlateNumberTextView.setText(dbVehicle.fetchData(getId,"plate_number"));
            }
            else if (dbVehicle.countData(getId) == 2) {
                ArrayList<String> listOfFullname;
                ArrayList<String> listOfPlateNumber;
                String firstName, secondName;
                String firstPlateNumber, secondPlateNumber;

                listOfFullname = dbVehicle.fetchDatas(getId,"fullname");
                listOfPlateNumber = dbVehicle.fetchDatas(getId,"plate_number");

                firstName = listOfFullname.get(0);
                secondName = listOfFullname.get(1);
                firstPlateNumber = listOfPlateNumber.get(0);
                secondPlateNumber = listOfPlateNumber.get(1);

                firstVehicleOwnerTextView.setText(firstName);
                firstPlateNumberTextView.setText(firstPlateNumber);
                secondVehicleOwnerTextView.setText(secondName);
                secondPlateNumberTextView.setText(secondPlateNumber);
            }
            else if (dbVehicle.countData(getId) >= 3) {
                ArrayList<String> listOfFullname;
                ArrayList<String> listOfPlateNumber;
                String firstName, secondName, thirdName;
                String firstPlateNumber, secondPlateNumber, thirdPlateNumber;

                listOfFullname = dbVehicle.fetchDatas(getId,"fullname");
                listOfPlateNumber = dbVehicle.fetchDatas(getId,"plate_number");

                firstName = listOfFullname.get(0);
                secondName = listOfFullname.get(1);
                thirdName = listOfFullname.get(2);
                firstPlateNumber = listOfPlateNumber.get(0);
                secondPlateNumber = listOfPlateNumber.get(1);
                thirdPlateNumber = listOfPlateNumber.get(2);

                firstVehicleOwnerTextView.setText(firstName);
                firstPlateNumberTextView.setText(firstPlateNumber);
                secondVehicleOwnerTextView.setText(secondName);
                secondPlateNumberTextView.setText(secondPlateNumber);
                thirdVehicleOwnerTextView.setText(thirdName);
                thirdPlateNumberTextView.setText(thirdPlateNumber);
            }
        } else {
            firstVehicleOwnerTextView.setText("");
            firstPlateNumberTextView.setText("");
            secondVehicleOwnerTextView.setText("");
            secondPlateNumberTextView.setText("");
            thirdVehicleOwnerTextView.setText("");
            thirdPlateNumberTextView.setText("");
        }
    }

    @Override
    public void onDismiss(final DialogInterface dialog) {
        recreateActivityCompat(VehicleSticker.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_CANCELED) {
                recreateActivityCompat(VehicleSticker.this);
            }
        }
    }

    @SuppressLint("NewApi")
    public static final void recreateActivityCompat(final Activity a) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            a.recreate();
        } else {
            final Intent intent = a.getIntent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            a.finish();
            a.overridePendingTransition(0, 0);
            a.startActivity(intent);
            a.overridePendingTransition(0, 0);
        }
    }
}

/**code explanation
 Line 21-30 : Variable declaration. For future implementation. Its better not to declare EditText & Button inside onCreate() because we might want
 to use it inside another function for manipulation or just resetting the value.

 Line 51 : The vehicle that will be registered are bound to user_id. Since I only have the username from the user dashboard, I need to get the
 user_id here.

 Line 59-68 : Vehicle registration doesn't need to call a new activity. I decided to just use a dialog because it just use 1 layout.

 Line 71-77 : Delete vehicle will need to call activity because the user will need to search which vehicle to delete first. So it's proper to call
 an activity then that activity will call the delete dialog instead off making a search dialog calling a delete dialog.
 Take note here that I'm using a 'startActivityForResult()' method instead of normal 'startActivity()'. It's to make sure this activity know when
 the delete activity is finish. That way, I can refresh the list of the registered vehicle after the user finish deleting the vehicle.
 If not, the list will not be updated and the deleted vehicle might still be displayed.
 Source : https://stackoverflow.com/questions/10407159/how-to-manage-startactivityforresult-on-android
 Source : https://developer.android.com/training/basics/intents/result.html

 Line 80-136 : To display a maximum of 3 registered vehicle at the lower part of the layout. Else, it will not show anything.

 Line 138-141 : To handle when the register dialog is dismiss. Whenever the register dialog is closed, the list of registered vehicle might be added
 or not. So I need to refresh the layout. This handler is to know when the register dialog is closed.
 Source : https://stackoverflow.com/questions/23786033/dialogfragment-and-ondismiss


 Line 143-151 : Does the same as 'OnDismiss()'. The difference is that this handle when the delete activity finish/when the user press back.
 I want to refresh the layout whenever that happens. This handler is to know when the delete activity is finish.
 Source : https://stackoverflow.com/questions/5312334/how-to-handle-back-button-in-activity

 Line 153-165 : This function is to refresh/redraw the layout. Source : https://appglobe.com/restart-an-activity-android/

 **/