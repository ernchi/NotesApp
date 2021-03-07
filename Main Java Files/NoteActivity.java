package com.example.notesapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class NoteActivity extends AppCompatActivity {

    private EditText textInput;
    private String newNote;
    private NotesDataRepo notesDataRepo;
    private boolean isEdit;
    private String existingNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        notesDataRepo = new NotesDataRepo(this);
        textInput = (EditText) findViewById(R.id.textInput);

        /**
         * Check if user wants to edit existing note
         */
        isEdit = getIntent().getBooleanExtra(Constants.MAIN_ACTIVITY_EDIT, false);
        if (isEdit) {
            // set the existing note in the text input field
            existingNote = getIntent().getStringExtra(Constants.MAIN_ACTIVITY_NOTE);
            textInput.setText(existingNote);
        }

        /**
         * Add or update new note in database and return to main activity
         */
        FloatingActionButton fabAddNewNote = findViewById(R.id.fabAddNewNote);
        fabAddNewNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newNote = textInput.getText().toString();
                // put the String to pass back into an Intent
                Intent intent = new Intent();
                intent.putExtra(Constants.NOTE_ACTIVITY_NEW_NOTE, newNote);

                // check if user is creating a new note or updating an existing one
                if (isEdit) {
                    /**
                     * If user removes all text from existing note, delete note from database,
                     * otherwise update note in database
                     */
                    if (newNote.trim().length() == 0) {
                        // remove note from database
                        notesDataRepo.delete(existingNote);
                        intent.putExtra(Constants.NOTE_ACTIVITY_RESULT, Constants.RESULT_DELETE);
                    } else {
                        // update note in database
                        notesDataRepo.update(existingNote, newNote);
                        intent.putExtra(Constants.NOTE_ACTIVITY_RESULT, Constants.RESULT_UPDATE);
                    }
                    setResult(Activity.RESULT_OK, intent);
                } else {
                    // add new note to database if it is not empty
                    if (newNote.trim().length() > 0) {
                        notesDataRepo.insert(newNote);
                        intent.putExtra(Constants.NOTE_ACTIVITY_RESULT, Constants.RESULT_INSERT);
                        setResult(Activity.RESULT_OK, intent);
                    } else {
                        setResult(Activity.RESULT_CANCELED);
                    }
                }
                NoteActivity.this.finish();
            }
        });

        /**
         * Return to main activity
         */
        FloatingActionButton fabCancelNewNote = findViewById(R.id.fabCancelNewNote);
        fabCancelNewNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(Activity.RESULT_CANCELED);
                NoteActivity.this.finish();
            }
        });
    }
}