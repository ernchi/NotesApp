package com.example.notesapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final int NOTE_ACTIVITY_REQUEST_CODE = 0;

    private NotesDataRepo notesDataRepo;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> notesList;
    private String returnData = null;
    private int indexOfEdit = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // open database and retrieve list of notes
        notesDataRepo = new NotesDataRepo(this);
        notesList = notesDataRepo.getNotesAsList();

        /**
         * ListView displays all notes
         */
        ListView listView = findViewById(R.id.listViewNotes);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, notesList);
        listView.setAdapter(adapter);

        /**
         * Start new activity when user clicks on fabCreateNote
         */
        FloatingActionButton fabCreateNote = findViewById(R.id.fabCreateNote);
        fabCreateNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NoteActivity.class);
                intent.putExtra(Constants.MAIN_ACTIVITY_EDIT, false);
                startActivityForResult(intent, NOTE_ACTIVITY_REQUEST_CODE);
            }
        });

        /**
         * Click on a note to edit it
         */
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String note = notesList.get(position);
                editNote(note, position);
            }
        });


        /**
         * Display option to delete note on long click
         */
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String note = notesList.get(position);
                deleteNote(note, position);
                return false;
            }
        });
    }

    /**
     * Removes note from database and updates ListView
     * @param note the note to be deleted
     * @param index index of note in list
     */
    private void deleteNote(String note, int index) {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Do you want to delete this note?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity.this, "Note deleted", Toast.LENGTH_SHORT).show();
                        notesList.remove(index);
                        // remove recipe from database
                        notesDataRepo.delete(note);
                        adapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    /**
     * Brings user to NoteActivity to edit their note
     * @param note note to be edited
     * @param index index of note in list
     */
    private void editNote(String note, int index) {
        indexOfEdit = index;
        Intent intent = new Intent(getApplicationContext(), NoteActivity.class);
        intent.putExtra(Constants.MAIN_ACTIVITY_EDIT, true);
        intent.putExtra(Constants.MAIN_ACTIVITY_NOTE, note);
        startActivityForResult(intent, NOTE_ACTIVITY_REQUEST_CODE);
    }

    /**
     * Update ListView when return to MainActivity from NoteActivity
     * @param requestCode NOTE_ACTIVITY_REQUEST_CODE
     * @param resultCode informs MainActivity whether user adds a note or canceled their action
     * @param data intent received from NoteActivity
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NOTE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                returnData = data.getStringExtra(Constants.NOTE_ACTIVITY_NEW_NOTE);
                int resultInt = data.getIntExtra(Constants.NOTE_ACTIVITY_RESULT, 0);
                switch (resultInt) {
                    case Constants.RESULT_DELETE:
                        // remove edited note from list
                        notesList.remove(indexOfEdit);
                        indexOfEdit = -1;
                        break;
                    case Constants.RESULT_UPDATE:
                        // replace existing note with new note
                        notesList.set(indexOfEdit, returnData);
                        indexOfEdit = -1;
                        break;
                    case Constants.RESULT_INSERT:
                        // add new note into list
                        notesList.add(returnData);
                        break;
                    default: break;
                }
            }
        }
        adapter.notifyDataSetChanged();
    }
}