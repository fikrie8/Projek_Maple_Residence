package com.mphra.projekmaple;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbAdapter extends SQLiteOpenHelper {
    private static final String dbName = "userInfo.db";
    private static final String stickerTableName = "tbl_stickerInfo";
    private static final String userTableName = "tbl_userInfo";

    public DbAdapter(Context context) {
        super(context, dbName, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query;
        Log.e("DbAdapter", "Creating userInfo table");

        query = "create table " + userTableName + " " +
                "(user_id INTEGER PRIMARY KEY," +
                "fullname TEXT NOT NULL, " +
                "address TEXT NOT NULL, " +
                "email TEXT NOT NULL, " +
                "gender TEXT NOT NULL, " +
                "username TEXT NOT NULL UNIQUE, " +
                "password TEXT NOT NULL)";
        db.execSQL(query);

        Log.e("DbAdapter", "Creating stickerInfo table");

        query = "create table " + stickerTableName + " " +
                "(sticker_id INTEGER PRIMARY KEY," +
                "fullname TEXT NOT NULL, " +
                "address TEXT NOT NULL, " +
                "plate_number TEXT NOT NULL UNIQUE, " +
                "user_id INTEGER, " +
                "FOREIGN KEY(user_id) REFERENCES tbl_userInfo(user_id))";
        db.execSQL(query);

        Log.e("DbAdapter", "done Creating Table");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query;

        query = "DROP TABLE IF EXISTS " + userTableName;
        db.execSQL(query);
        query = "DROP TABLE IF EXISTS " + stickerTableName;
        db.execSQL(query);
        onCreate(db);
    }

    public boolean createDb() {
        String getDbName;
        boolean result = false;

        Log.d("DbAdapter", "createDb");
        this.getWritableDatabase();
        getDbName = this.getDatabaseName();
        Log.d("DbAdapter", "getDbName = " + getDbName);
        if (getDbName.equals(dbName)) {
            result = true;
        }
        return result;
    }
}

/**code explanation
 The purpose I'm creating this Adapter is to make sure all the table are created before the user trying to access/use the tables.
 Method getWritableDatabase()/getReadableDatabase() will call onCreate() once for every db. If I don't do this, the problem will occur when trying
 to create the next table in the same db. The onCreate() will not be called since it has been called once. Of course I can called it manually by
 calling onCreate() method, but I don't want it to done that way. It should be called automatically. The explanation for this situation can be
 read in this link : https://stackoverflow.com/questions/5024223/sqliteopenhelper-failing-to-call-oncreate

 Line 34-40 : The tbl_userInfo has a relation of one to many with tbl_stickerInfo. 1 user can have many owner inside tbl_stickerInfo. That is
 why I use the user_id as a reference for the tbl_stickerInfo.

 Line 64-68 : To make sure the db is created. I need to get the databaseName that was created and compare it with the dbName that I have
 specifically assign in the variable.

 **/