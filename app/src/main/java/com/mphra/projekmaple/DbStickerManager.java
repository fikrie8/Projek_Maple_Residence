package com.mphra.projekmaple;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DbStickerManager extends SQLiteOpenHelper {

    private static final String dbName = "userInfo.db";
    private static final String stickerTableName = "tbl_stickerInfo";

    public DbStickerManager(Context context) {
        super(context, dbName, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query;
        Log.d("DbStickerManager", "Creating table");
        query = "create table " + stickerTableName + " " +
                "(sticker_id INTEGER PRIMARY KEY," +
                "fullname TEXT NOT NULL, " +
                "address TEXT NOT NULL, " +
                "plate_number TEXT NOT NULL UNIQUE, " +
                "user_id INTEGER, " +
                "FOREIGN KEY(user_id) REFERENCES tbl_userInfo(user_id))";
    db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query;

        query = "DROP TABLE IF EXISTS " + stickerTableName;
        db.execSQL(query);
        onCreate(db);
    }

    public boolean addSticker(String fullname, String address, String plateNumber, int userId) {
        long result = -1;
        SQLiteDatabase db;
        ContentValues cv = new ContentValues();

        db = this.getWritableDatabase();
        Log.d("DbStickerManager","fullname = " + fullname);
        Log.d("DbStickerManager","address = " + address);
        Log.d("DbStickerManager","plateNumber = " + plateNumber);
        Log.d("DbStickerManager","user_id = " + userId);
        cv.put("fullname", fullname);
        cv.put("address", address);
        cv.put("plate_number", plateNumber);
        cv.put("user_id", userId);

        try {
            result = db.insertOrThrow(stickerTableName, null, cv);
        } catch (SQLException e) {
            Log.e("DbStickerManager", "SQLException = " + e);
        }

        if (result == -1) {
            return false;
        }
        return true;
    }

    public String fetchData(String userId, String columnName) {
        SQLiteDatabase db;
        String returnQuery = "";
        Cursor cursor;

        db = this.getReadableDatabase();
        cursor = db.rawQuery("Select * from tbl_stickerInfo where user_id = '" + userId + "';", null);
        if(cursor.moveToFirst()){
            do{
                returnQuery = cursor.getString(cursor.getColumnIndex(columnName));

            }while (cursor.moveToNext());
        }
        cursor.close();

        return returnQuery;
    }

    public ArrayList<String> fetchDatas(String userId, String columnName) {
        SQLiteDatabase db;
        Cursor cursor;
        ArrayList<String> arrayList = new ArrayList<>();

        db = this.getReadableDatabase();
        cursor = db.rawQuery("Select * from tbl_stickerInfo where user_id = '" + userId + "';", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            arrayList.add(cursor.getString(cursor.getColumnIndex(columnName)));
            cursor.moveToNext();
        }
        cursor.close();

        return arrayList;
    }

    public boolean deleteData (int userId, String columnName, String value) {
        SQLiteDatabase db;
        int deletedRows;
        boolean result = false;
        String selection = "user_id =? and " + columnName + " =? ";
        String[] selectionArgs = {Integer.toString(userId),value};
        Log.d("DbStickermanager","selection = " + selection);
        Log.d("DbStickermanager","selectionArgs 0 = " + selectionArgs[0]);
        Log.d("DbStickermanager","selectionArgs 1 = " + selectionArgs[1]);
        Log.d("DbStickermanager","columnName = " + columnName);
        Log.d("DbStickermanager","value = " + value);

        db = this.getWritableDatabase();
        deletedRows = db.delete(stickerTableName,selection,selectionArgs);
        Log.d("DbStickerManager","deletedRows = " + deletedRows);
        if (deletedRows > 0) {
            result = true;
        } else {
            Log.d("DbStickerManager","selection = " + selection);
            Log.d("DbStickerManager","selectionArgs = " + selectionArgs);
            Log.e("DbStickerManager","There might be an error when deleting vehicle");
            Log.e("DbStickerManager","deletedRows = " + deletedRows);
        }
        return result;
    }

    public int countData (String userId) {
        SQLiteDatabase db;
        Cursor cursor;
        String query;
        int countData;

        Log.d("DbStickerManager", "countData");
        db = this.getReadableDatabase();
        query = "Select * from " + stickerTableName + " where user_id = '" + userId + "';";
        cursor = db.rawQuery(query,null);
        countData = cursor.getCount();
        cursor.close();
        return countData;
    }
}

/**code explanation

 Line 45-70 : When adding a sticker, there might be an exception if the plate_number is already used because I have set the plate_number as unique.
 To catch that exception, I use 'insertOrThrow()' method instead of 'insert()'. That way I can catch the exception and print in to the log.

 Line 72-88 : Different from DbUserManager. The table doesn't have a username. So to look for a user information, I need to use user_id instead.
 Since user_id is also bind to a specific user, it can be used as an argument to look for a specific user.

 Line 90-105 : 1 user can have a lot of vehicle. So it's possible to get more than 1 row during a query. So I just return an array instead of a
 single String.
 
 **/
