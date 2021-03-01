package com.example.notesapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

public class NotesDataRepo {
    private DBHelper dbHelper;

    public NotesDataRepo(Context context) {
        dbHelper = new DBHelper(context);
    }

    public int insert(String notes) {
        // Open connection to write data
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(dbHelper.TABLE_COLUMN_NOTES, notes);

        // Inserting Row
        long NotesData_Id = db.insert(dbHelper.DATABASE_TABLE_NAME, null, values);
        db.close(); // Closing database connection
        return (int) NotesData_Id;
    }

    public void delete(String notes) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(dbHelper.DATABASE_TABLE_NAME, dbHelper.TABLE_COLUMN_NOTES + "= ?", new String[] { notes });
        db.close(); // Closing database connection
    }

    public ArrayList<String> getNotesAsList() {
        // Open connection to read only
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT  " +
                dbHelper.TABLE_COLUMN_NOTES +
                " FROM " + dbHelper.DATABASE_TABLE_NAME;

        ArrayList<String> notesList = new ArrayList<>();

        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        int notesIndex = cursor.getColumnIndex(dbHelper.TABLE_COLUMN_NOTES);

        if (cursor.moveToFirst()) {
            do {
                String notes = cursor.getString(notesIndex);
                Log.i("NotesDataRepo get list", notes);
                notesList.add(notes);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return notesList;
    }

    public void update(String existingNote, String updatedNote) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(dbHelper.TABLE_COLUMN_NOTES, updatedNote);

        // It's a good practice to use parameter ?, instead of concatenate string
        db.update(dbHelper.DATABASE_TABLE_NAME, values, dbHelper.TABLE_COLUMN_NOTES + "= ?", new String[] { existingNote });
        db.close(); // Closing database connection
    }



}
