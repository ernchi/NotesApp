package com.example.notesapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    // version number to upgrade database version
    private static final int DATABASE_VERSION = 2;

    // database name
    private static final String DATABASE_NAME = "crud.db";

    // table name
    public static final String DATABASE_TABLE_NAME = "NotesData";

    // column names
    private static final String TABLE_COLUMN_ID = "Id";
    public static final String TABLE_COLUMN_NOTES = "Notes";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //All necessary tables you like to create will create here
        String CREATE_TABLE_NOTESDATA= "CREATE TABLE " + DATABASE_TABLE_NAME + " ("
                + TABLE_COLUMN_ID  + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + TABLE_COLUMN_NOTES + " TEXT NOT NULL )";

        db.execSQL(CREATE_TABLE_NOTESDATA);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // drop older table if existed, all data will be removed
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_NAME);

        // create tables again
        onCreate(db);
    }
}
