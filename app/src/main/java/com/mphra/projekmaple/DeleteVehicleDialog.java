package com.mphra.projekmaple;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;

import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;

public class DeleteVehicleDialog extends DialogFragment {
    private int getId;
    private ArrayList<String> searchBy = new ArrayList<>();
    private ArrayList<String> list = new ArrayList<>();
    private ArrayList<Button> listOfButton = new ArrayList<>();

    public static DeleteVehicleDialog getInstanceFor(ArrayList<String> list) {
        DeleteVehicleDialog deleteVehicleDialog = new DeleteVehicleDialog();
        deleteVehicleDialog.list=list;
        return deleteVehicleDialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final DbStickerManager db = new DbStickerManager(getContext());
        super.onCreate(savedInstanceState);
        Log.d("DeleteVehicleDialog","getContext = " + getContext());

        if (getArguments() != null) {
            getId = getArguments().getInt("id",0);
            searchBy = getArguments().getStringArrayList("searchBy");
        }

        int index;
        int sizeIn20Dp = 20;
        int sizeIn10Dp = 10;
        float scale = getResources().getDisplayMetrics().density;
        int twentyDp = (int) (sizeIn20Dp*scale + 0.5f);
        int tenDp = (int) (sizeIn10Dp*scale + 0.5f);

        TableLayout layout = new TableLayout(getContext());
        layout.setOrientation(TableLayout.VERTICAL);
        String buttonDisplay = "DELETE";
        for(int i=0; i<list.size(); i++){
            TableRow tableRow = new TableRow(getContext());
            TextView textView = new TextView(getContext());
            Button deleteButton = new Button(getContext());
            StringBuilder stringBuilder = new StringBuilder();

            tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            tableRow.setPadding(0,0,tenDp,tenDp);

            textView.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            textView.setGravity(Gravity.CENTER_VERTICAL);

            stringBuilder.append(i);
            stringBuilder.append(" : ");
            stringBuilder.append(list.get(i));
            textView.setText(stringBuilder);
            textView.setPadding(twentyDp,twentyDp,twentyDp,tenDp);
            Log.d("DeleteVehicleDialog","textView = " + textView.getText().toString());

            deleteButton.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            deleteButton.setGravity((Gravity.CENTER));
            deleteButton.setText(buttonDisplay);
            deleteButton.setBackgroundColor(Color.RED);
            deleteButton.setPadding(tenDp,tenDp,tenDp,tenDp);
            listOfButton.add(deleteButton);

            tableRow.addView(textView, 0);
            tableRow.addView(deleteButton, 1);
            layout.addView(tableRow);
        }

        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(layout);
        dialog.setTitle("deleteVehicle");
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));

        Log.d("DeleteVehicleDialog","listOfButton size = " + listOfButton.size());
        Log.d("DeleteVehicleDialog","list size = " + list.size());

        for (index = 0; index != listOfButton.size(); index++) {
            final int getIndex = index;
            listOfButton.get(index).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (searchBy.size() > 1) {
                        Log.d("DeleteVehicleDialog","id = " + getId + ", searchBy = " + searchBy.get(0) + ",list = " + list.get(getIndex));
                        db.deleteData(getId, searchBy.get(0),list.get(getIndex));
                        Log.d("DeleteVehicleDialog","id = " + getId + ", searchBy = " + searchBy.get(1) + ",list = " + list.get(getIndex));
                        db.deleteData(getId, searchBy.get(1),list.get(getIndex));
                    } else {
                        Log.d("DeleteVehicleDialog","id = " + getId + ", searchBy = " + searchBy.get(0) + ",list = " + list.get(getIndex));
                        db.deleteData(getId, searchBy.get(0),list.get(getIndex));
                    }
                    dialog.cancel();
                }
            });
        }

        dialog.show();
        return dialog;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
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

 Line 25-28 : Variable declaration.

 Line 30-34 : Pass the list of vehicle that will be used inside this dialog. I cannot simply send it using arguments. Like string or integer.
 Source: https://stackoverflow.com/questions/47812649/how-to-pass-array-list-data-from-one-dialog-fragment-to-another-dialog-fragment

 Line 48-53 : Padding calculation. Since I'm doing the layout programmatically, I need to do some calculation for the padding.

 Line 55-81 : Creating the layout that has unknown amount of textField and Button. Since the list of vehicle that will be displayed in the dialog
 is dynamic, it is impossible to do the layout inside XML file. So it needs to be done programmatically.
 Source: https://stackoverflow.com/questions/16344485/cant-change-a-custom-dialogfragment-layout-dynamically
 https://stackoverflow.com/questions/5918320/dynamically-add-textviews-to-a-linearlayout

 Line 83-89 : Creating the dialog and setting its attribute. I only create the dialog after the layout is finished because the dialog is dependent
 to the layout. It will need that layout during the 'setContentView()' method. Its better to finish everything related to layout first. Then only
 continue with dialog.

 Line 94-111 : Setting the setOnClickListener for every button that has been created in the layout. This is an exception where I cannot set it
 while creating the layout. I can only do this after the dialog has been created,not while creating the layout. The reason for that is because this
 listener have a dialog method 'dialog.cancel()'. Setting this while creating the layout will not work because the dialog were not created when
 creating the layout. That's why I set it after the dialog has been created.

 Line 123-130 : I want to inform the calling activity that this dialog is dismiss. So this is how to do so.
 Source : https://stackoverflow.com/questions/23786033/dialogfragment-and-ondismiss

 **/
