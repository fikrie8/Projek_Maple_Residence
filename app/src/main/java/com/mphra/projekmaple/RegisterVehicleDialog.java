package com.mphra.projekmaple;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

public class RegisterVehicleDialog extends DialogFragment {
    int getId;
    private EditText fullnameEditText, addressEditText, plateNumberEditText;
    private Button registerVehicleButton;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final DbStickerManager db = new DbStickerManager(getContext());
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            getId = getArguments().getInt("id",0);
        }
        Log.d("RegisterVehicleDialog","getContext = " + getContext());
        Log.d("RegisterVehicleDialog","this = " + this);

        final int id = getId;
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.register_vehicle_dialog);
        dialog.setTitle("registerVehicle");
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        fullnameEditText = dialog.findViewById(R.id.registerVehicleOwnerNameEditText);
        addressEditText = dialog.findViewById(R.id.registerVehicleAddressEditText);
        plateNumberEditText = dialog.findViewById(R.id.registerVehiclePlateNumberEditText);
        registerVehicleButton = dialog.findViewById(R.id.registerVehicleRegisterButton);
        fullnameEditText.setFocusable(true);
        fullnameEditText.requestFocus();
        fullnameEditText.requestFocusFromTouch();

        Log.d("RegisterVehicleDialog","hasWindowFocused? = " + fullnameEditText.hasWindowFocus());
        Log.d("RegisterVehicleDialog","isFocused? = " + fullnameEditText.isFocused());
        Log.d("RegisterVehicleDialog","currentFocus = " + dialog.getWindow().getCurrentFocus());

        registerVehicleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckAndGetContent(v) == 0) {
                    if (db.addSticker(fullnameEditText.getText().toString(),
                            addressEditText.getText().toString(),
                            plateNumberEditText.getText().toString(),
                            id) == true) {
                        Toast.makeText(v.getContext(),"Vehicle Successfully Registered" , Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(v.getContext(),"Unable to register vehicle, please check log" , Toast.LENGTH_SHORT).show();
                    }
                    dialog.cancel();
                }
            }
        });
        dialog.show();
        return dialog;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    private int CheckAndGetContent(View v){

        Log.d("RegistrationActivity", "Inside CheckAndGetContent");
        if (!fullnameEditText.getText().toString().isEmpty() &&
            !addressEditText.getText().toString().isEmpty() &&
            !plateNumberEditText.getText().toString().isEmpty()) {
            return 0;
        } else {
            Toast.makeText(v.getContext(),"Fields cannot be empty",Toast.LENGTH_SHORT).show();
            Log.e("RegisterVehicleDialog","Fields cannot be empty");
            return -1;
        }
    }

    @Override
    public void onDismiss(final DialogInterface dialog) {
        super.onDismiss(dialog);
        final Activity activity = getActivity();
        if (activity instanceof DialogInterface.OnDismissListener) {
            ((DialogInterface.OnDismissListener) activity).onDismiss(dialog);
        }
    }
}

/**code explanation
 Explanation/example is used in creating a custom Dialog : https://stackoverflow.com/questions/13341560/how-to-create-a-custom-dialog-box-in-android
 & https://androidx.de/androidx/fragment/app/DialogFragment.html

 Why Google recommends that we use DialogFragment instead of a simple Alert Dialog builder in the activity :
 https://www.journaldev.com/23096/android-dialogfragment

 Line 29-31 : This his how to get the variables that has been passed from the activity creating the dialog.

 Line 38&39 : Set it to true because sometimes the user might want to dismiss the dialog. Since I didn't create a cancel button. I can just allow the
 user to use the back key.

 Line 56-70 : I have made plate_number as unique. I think it's impossible for 1 vehicle to have the same plate_number. So the user shouldn't be
 able to add the vehicle. By doing this way, I need to handle to display the error. Instead of putting a whole bunch of exception caught to the
 user interface. I just decided to ask them to check the log file.

 Line 77 : I use  getContext() to get the context in this dialog instead of using 'this'. By trying to print the value in logcat, 'this' will
 return null, making the db initialization error. To solve this problem, I need to use onAttach() and set super.onAttach() for the context.
 This way, the context from the previous activity is available in this dialog. I can get the context by using getContext(). Refer this link in SO
 to understand more : https://stackoverflow.com/questions/6215239/getactivity-returns-null-in-fragment-function
 https://stackoverflow.com/questions/15464263/passing-context-as-argument-of-dialogfragment

 Line 81-95 : Check the editTextField to make sure it doesn't have any missing field.

 Line 97-104 : I want to inform the calling activity that this dialog is dismiss. So this is how to do so.
 Source : https://stackoverflow.com/questions/23786033/dialogfragment-and-ondismiss

 **/
