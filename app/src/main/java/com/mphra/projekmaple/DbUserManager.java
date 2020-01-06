package com.mphra.projekmaple;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.content.Context;
import android.util.Base64;
import android.util.Log;

public class DbUserManager extends SQLiteOpenHelper {

    private static final String dbName = "userInfo.db";
    private static final String userTableName = "tbl_userInfo";

    public DbUserManager(Context context) {
        super(context, dbName, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query;
        Log.e("DbUserManager", "Creating table");
        query = "create table " + userTableName + " " +
                "(user_id INTEGER PRIMARY KEY," +
                "fullname TEXT NOT NULL, " +
                "address TEXT NOT NULL, " +
                "email TEXT NOT NULL, " +
                "gender TEXT NOT NULL, " +
                "username TEXT NOT NULL UNIQUE, " +
                "password TEXT NOT NULL)";
    db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query;

        query = "DROP TABLE IF EXISTS " + userTableName;
        db.execSQL(query);
        onCreate(db);
    }

    public boolean addUser(String fullname, String address, String email, String gender, String username, String password) {
        long result = -1;
        SQLiteDatabase db;
        ContentValues cv = new ContentValues();
        String passwordEncode;

        passwordEncode = Base64.encodeToString(password.getBytes(),Base64.DEFAULT);
        db = this.getWritableDatabase();
        cv.put("fullname", fullname);
        cv.put("address", address);
        cv.put("email", email);
        cv.put("gender", gender);
        cv.put("username", username);
        cv.put("password", passwordEncode);

        try {
            result = db.insertOrThrow(userTableName, null, cv);
        } catch (SQLiteException e) {
            Log.e("DbUserManager","result = " + result);
            Log.e("DbUserManager","SQLException = " + e);
        }
        if (result == -1) {
            return false;
        }
        return true;
    }

    boolean checkUsername(String username) {
        SQLiteDatabase db;
        Cursor cursor;

        db = this.getReadableDatabase();
        cursor = db.rawQuery("Select * from " + userTableName + " where username =?", new String[]{username});
        if (cursor.getCount() > 0) {
            cursor.close();
            return false;
        } else {
            cursor.close();
            return true;
        }
    }

    boolean checkUsernameAndPassword(String username, String password) {
        SQLiteDatabase db;
        Cursor cursor;
        String passwordEncode;
        String query;

        passwordEncode = Base64.encodeToString(password.getBytes(),Base64.DEFAULT);
        query = "Select * from " + userTableName + " where username = '" + username + "' AND password = '" + passwordEncode + "'";
        db = this.getReadableDatabase();
        cursor = db.rawQuery(query,null);
        if (cursor.getCount() > 0) {
            cursor.close();
            return true;
        } else {
            cursor.close();
            return false;
        }
    }

    String fetchData(String username, String columnName) {
        SQLiteDatabase db;
        String returnQuery = "";
        Cursor cursor;

        db = this.getReadableDatabase();
        cursor = db.rawQuery("Select * from tbl_userInfo where username = '" + username + "';", null);
        if(cursor.moveToFirst()){
            do{
                returnQuery = cursor.getString(cursor.getColumnIndex(columnName));

            }while (cursor.moveToNext());
        }
        cursor.close();

        return returnQuery;
    }

    Boolean updateData(String fullname, String address, String email, String username, String password) {
        long result;
        SQLiteDatabase db;

        String passwordEncode = Base64.encodeToString(password.getBytes(),Base64.DEFAULT);
        db= this.getWritableDatabase();
        ContentValues data = new ContentValues();
        data.put("fullname", fullname);
        data.put("address", address);
        data.put("email", email);
        data.put("password", passwordEncode);
        result = db.update(userTableName, data, "username =?", new String[]{username});
        if (result == -1) {
            return false;
        }
        return true;
    }
}

/**code explanation

 Line 12-13 : I hardcode the db name and tablename in just one static variable to ease the usage and implementation. If the
 db/table name need to be changed, just changed it at the variables instead of looking everywhere in the code.

 Line 20-31 : Creating table for user. If you want to add more information related to user. Remember to add in here as well.

 Line 40-59 : Add user into db. Instead of writing a long query. I can use contentvalue which is more easier to maintain.

 Line 44 : sqlite3 store password in plain text. So I need to encode it first. I use android.util.Base64 because it is compatible from API 8

 Line 61-74 : Checking if the username has already been used.

 Line 76-91 : Checking if the username and password match. That's why i wrote a query that is requesting a specific username and password. Take
 note that the password stored in db is encoded. So the password that will be used to query must almost be encoded.
 Line 93-107 : I can use this to fetch any data from user as long as I know the column name. To used this function, refer to onCreate() method to
 know the correct column name.

 Line 61-91 : Theoretically, I should be calling cursor.close() then only call cursor.getCount(). But after reading through SO,
 I use cursor.close() after cursor.getCount(). Eventhough getcount() method just return the number of rows
 in the cursor and we are not retrieving any data from the cursor. Close() method Closes the Cursor, releasing all of its
 resources and making it completely invalid .The behavior is inconsistent and it's not always noticeable but some people
 have already talked about some of the side effects.
 src : https://stackoverflow.com/questions/47735910/what-is-the-significance-of-closing-the-cursor-in-android-sqlite

 Line 117-133 : Method to update the user information. Since the username is not allowed to change, we use this variable as the clause to look into
 the db. Plus the username is always passed around between activities. So I just reuse what is available instead of making a new query to get the
 ID first.
 **/
