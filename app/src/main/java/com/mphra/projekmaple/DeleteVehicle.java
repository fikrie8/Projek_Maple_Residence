package com.mphra.projekmaple;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class DeleteVehicle extends AppCompatActivity implements DialogInterface.OnDismissListener{

    String getId;
    DbStickerManager db = new DbStickerManager(this);
    EditText vehicleOwnerEditView, vehiclePlateNumberEditView;
    Button deleteVehicleButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_vehicle);

        Intent PreviousIntent;
        PreviousIntent = getIntent();
        getId = PreviousIntent.getExtras().getString("id");

        vehicleOwnerEditView = findViewById(R.id.deleteVehicleOwnerNameEditText);
        vehiclePlateNumberEditView = findViewById(R.id.deleteVehiclePlateNumberEditText);
        deleteVehicleButton = findViewById(R.id.deleteVehicleBySearchButton);

        vehicleOwnerEditView.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    searchForNameOrPlateNumber();
                    return true;
                }
                return false;
            }
        });

        vehiclePlateNumberEditView.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    searchForNameOrPlateNumber();
                    return true;
                }
                return false;
            }
        });

        deleteVehicleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchForNameOrPlateNumber();
            }
        });
    }

    public void searchForNameOrPlateNumber() {
        Pattern pattern = Pattern.compile("");
        Matcher matcher;
        int searchIndex = 0;
        boolean result;
        boolean searchByRegEx = true;
        ArrayList<String> arrayListOfName = new ArrayList<>();
        ArrayList<String> arrayListOfPlateNumber = new ArrayList<>();
        ArrayList<String> arrayListOfResult = new ArrayList<>();
        ArrayList<String> searchBy = new ArrayList<>();
        ArrayList<String> arrayListTmp = new ArrayList<>();

        if (vehicleOwnerEditView.getText().toString().isEmpty() && vehiclePlateNumberEditView.getText().toString().isEmpty()) {
            Log.e("DeleteVehicle","Both EditView is empty");
            Toast.makeText(this,"Both fields cannot be empty",Toast.LENGTH_SHORT).show();
        } else {
            if (!vehicleOwnerEditView.getText().toString().isEmpty()) {
                try {
                    pattern = Pattern.compile(vehicleOwnerEditView.getText().toString());
                } catch (PatternSyntaxException e) {
                    Toast.makeText(DeleteVehicle.this, "Incorrect regular expression syntax", Toast.LENGTH_SHORT).show();
                    Log.e("DeleteVehicle", "pattern : " + pattern);
                    Log.e("DeleteVehicle", "pattern error : " + e);
                }
                if (pattern.toString().isEmpty()) {
                    Log.e("DeleteVehicle", "pattern is empty");
                } else {
                    arrayListOfName = db.fetchDatas(getId, "fullname");
                }
            }
            if (!vehiclePlateNumberEditView.getText().toString().isEmpty()) {
                try {
                    pattern = Pattern.compile(vehiclePlateNumberEditView.getText().toString());
                } catch (PatternSyntaxException e) {
                    Toast.makeText(DeleteVehicle.this, "Incorrect regular expression syntax", Toast.LENGTH_SHORT).show();
                    Log.e("DeleteVehicle", "pattern : " + pattern);
                    Log.e("DeleteVehicle", "pattern error : " + e);
                }
                if (pattern.toString().isEmpty()) {
                    Log.e("DeleteVehicle", "pattern is empty");
                } else {
                    arrayListOfPlateNumber = db.fetchDatas(getId, "plate_number");
                }

            }
            Log.d("DeleteVehicle", "arrayListOfName size = " + arrayListOfName.size());
            Log.d("DeleteVehicle", "arrayOfName = " + arrayListOfName);
            Log.d("DeleteVehicle", "arrayListOfPlateNumber size = " + arrayListOfPlateNumber.size());
            Log.d("DeleteVehicle", "arrayOfPlateNumber = " + arrayListOfPlateNumber);

            if (arrayListOfName.isEmpty() && arrayListOfPlateNumber.isEmpty()) {
                Log.e("DeleteVehicle", "List of names & plate numbers is 0");
                Toast.makeText(DeleteVehicle.this, "Your search doesn't have any result", Toast.LENGTH_SHORT).show();
            } else {
                arrayListTmp.addAll(arrayListOfName);
                Log.d("DeleteVehicle", "Checking for names using contains");
                while (searchIndex != arrayListTmp.size()) {
                    if (arrayListTmp.get(searchIndex).toLowerCase().contains(vehicleOwnerEditView.getText().toString().toLowerCase()) == true) {
                        searchIndex++;
                    } else {
                        arrayListTmp.remove(searchIndex);
                        Log.d("DeleteVehicle", "size after remove = " + arrayListTmp.size());
                        if (arrayListTmp.size() == 0) {
                            break;
                        }
                    }
                }
                if (arrayListTmp.size() > 0){
                    arrayListOfResult.addAll(arrayListTmp);
                    searchByRegEx = false;
                }

                arrayListTmp.removeAll(arrayListOfName);
                arrayListTmp.addAll(arrayListOfPlateNumber);
                searchIndex = 0;
                Log.d("DeleteVehicle", "Checking for plate numbers using contains");
                while (searchIndex != arrayListTmp.size()) {
                    if (arrayListTmp.get(searchIndex).toLowerCase().contains(vehiclePlateNumberEditView.getText().toString().toLowerCase()) == true) {
                        searchIndex++;
                    } else {
                        arrayListTmp.remove(searchIndex);
                        Log.d("DeleteVehicle", "size after remove = " + arrayListTmp.size());
                        if (arrayListTmp.size() == 0) {
                            break;
                        }
                    }
                }
                if (arrayListTmp.size() > 0){
                    arrayListOfResult.addAll(arrayListTmp);
                    searchByRegEx = false;
                }

                if (searchByRegEx) {
                    arrayListOfResult.addAll(arrayListOfName);
                    arrayListOfResult.addAll(arrayListOfPlateNumber);
                    searchIndex = 0;
                    Log.d("DeleteVehicle", "Checking for names & plate numbers using regex");
                    while (searchIndex != arrayListOfResult.size()) {
                        matcher = pattern.matcher(arrayListOfResult.get(searchIndex));
                        try {
                            result = matcher.find();
                        } catch (PatternSyntaxException e) {
                            Log.e("DeleteVehicle","error Description= " + e.getDescription());
                            Toast.makeText(DeleteVehicle.this, "Incorrect regular expression syntax", Toast.LENGTH_SHORT).show();
                            break;
                        }
                        if (result) {
                            Log.d("DeleteVehicle", "result true, searchIndex = " + arrayListOfResult.get(searchIndex));
                            searchIndex++;
                        } else {
                            Log.d("DeleteVehicle", "result false, searchIndex = " + arrayListOfResult.get(searchIndex));
                            arrayListOfResult.remove(searchIndex);
                            if (arrayListOfResult.size() == 0) {
                                Log.d("DeleteVehicle", "size after remove = " + arrayListOfResult.size());
                                Toast.makeText(DeleteVehicle.this, "Your search doesn't have any result", Toast.LENGTH_SHORT).show();
                                break;
                            }
                        }
                        Log.d("DeleteVehicle", "searchIndex = " + searchIndex);
                    }
                }

                if (arrayListOfResult.size() > 0) {
                    DeleteVehicleDialog deleteVehicleDialog = DeleteVehicleDialog.getInstanceFor(arrayListOfResult);
                    Bundle bundle = new Bundle();
                    bundle.putInt("id",Integer.parseInt(getId));

                    if (arrayListOfName.size() > 0) {
                        searchBy.add("fullname");
                    }
                    if (arrayListOfPlateNumber.size() > 0) {
                        searchBy.add("plate_number");
                    }
                    bundle.putStringArrayList("searchBy",searchBy);
                    deleteVehicleDialog.setArguments(bundle);
                    deleteVehicleDialog.show(getSupportFragmentManager(), "deleteVehicle");
                }
            }
        }
    }

    @Override
    public void onDismiss(final DialogInterface dialog) {
        vehicleOwnerEditView.setText("");
        vehiclePlateNumberEditView.setText("");
        vehicleOwnerEditView.requestFocus();
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("result","done");
        setResult(Activity.RESULT_CANCELED,returnIntent);
        finish();
    }
}

/**code explanation
 Line 23-26 : Variable declaration. For future implementation. Its better not to declare EditText & Button inside onCreate() because we might want
 to use it inside another function for manipulation or just resetting the value.

 Line 33-35 : The vehicle that has be registered are bound to user_id. So I get the user_id from the calling activity.

 Line 41-49 & 52-60 : Both these editText are handling the enter button. Sometimes the user will press Enter after typing the keyword to search.
 So I decided to handle it.

 Line 71-210 : This is the function that will do the search. It will be called in 3 places. 2 EditText & 1 Button.

 Logic explanation for the function :
        Line 72-81 : Variable declaration. Why I use 5 ArrayList.
        A: 1 to store a list of VehicleOwner. 1 to store a list of PlateNumber.
        Both these 2 list are used as a place to store the whole data. I'm trying to prevent from using 'like' queries in the db for situation where
        the user doesn't type in the full information. eg. just typing 'A' inside the plate_number. So I took all the data and just store it inside
        the arrayList. Then I can just do a 'contain()' method to check if its inside that arrayList or not.
        B: 1 to used as a temporary storage.
        This arrayList will be used to hold the whole data. Then during the search iteration, it will remove anything that doesn't match the finding.
        So at the end of the iteration, this temporary storage will contain only the ownerName/plateNumber that match the finding.
        C: 1 to store as a final value that will be sent to the dialog.
        The dialog will display all the data that match the finding. The data will have a possibility of having more than 1, so that's why I used
        an arrayList to pass to the dialog.
        D: 1 to store the column name that will be used for searching in the db.
        Line 87-113 : The search can be 2 ways. Search by using a normal string or regular expression. That is why I compile the user input to
        make sure whatever that is typed is a correct syntax. If the syntax is incorrect, the pattern will become an empty string. So there's no
        need to fetch any data when that happen.
        Line 125-141 : Search using String's 'contains()' method for VehicleOwner. The logic is to copy all the name inside the B arrayList.
        then remove whichever that doesn't contain the value. I don't use directly arrayList from arrayList A because I might need to use it later
        when searching using regular expression. What ever the remaining name will be stored inside arrayList C. If arrayList is not empty, means it
        doesn't need to search using regular expression. That's why I set 'searchByRegEx=false'.
        Line 143-161 : Search using String's 'contains()' method for PlateNumber. The logic is exactly the same as above. The difference is that I
        remove any data inside the B arrayList first and then store all the plateNumber from A arrayList.
        Line 163-191 : Search using regular expression. The logic in finding the searched data is the same as above. It will store all the value
        from A & B arrayList. Then remove any data that doesn't match the regular expression. Leaving only data that match the regular expression.
        Line 193-207 : If the C arrayList has a data, then I will create the DeleteDialog. Since the delete will require column name, so I need to
        make sure which column name will be used by checking the amount of data.

 Line 212-217 : To display a maximum of 3 registered vehicle at the lower part of the layout. Else, it will not show anything.

 Line 212-217 : To handle when the deleteVehicle dialog is dismiss. Whenever the deleteVehicle dialog is closed, the editText need to be empty
 so that the search doesn't display as before calling the deleteVehicle dialog.
 Source : https://stackoverflow.com/questions/23786033/dialogfragment-and-ondismiss


 Line 219-225 : To inform the calling activity that back button is pressed. I want to refresh the previous activity layout whenever that happens.
 Source : https://stackoverflow.com/questions/5312334/how-to-handle-back-button-in-activity

 **/