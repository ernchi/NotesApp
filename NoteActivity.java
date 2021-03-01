package com.example.notesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

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

        // check if user wants to edit existing note
        isEdit = getIntent().getBooleanExtra("isEdit", false);
        if (isEdit) {
            Log.i("isEdit", "true");
            // set the existing note in the text input field
            existingNote = getIntent().getStringExtra("note");
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
                Log.i("newNote length ", "is " + newNote.length());
                if (newNote.length() == 0) {
                    NoteActivity.this.finish();
                }
                if (isEdit) {
                    // update note in database
                    notesDataRepo.update(existingNote, newNote);
                } else {
                    try {
                        // add note to database
                        notesDataRepo.insert(newNote);
                    } catch (Exception e) {
                        NoteActivity.this.finish();
                    }
                }
                // put the String to pass back into an Intent and close this activity
                Log.i("Note newNote", "note is " + newNote);
                Intent intent = new Intent();
                intent.putExtra("newNote", newNote);
                setResult(Activity.RESULT_OK, intent);
                NoteActivity.this.finish();
            }
        });

        /**
         * Return to main activity
         */
        FloatingActionButton fabCancelNewNote = findViewById(R.id.fabCancelNewNote);
        fabCancelNewNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { NoteActivity.this.finish(); }
        });
    }
}